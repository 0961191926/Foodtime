import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.SettingTable
import com.example.foodtime_compose0518.SettingViewModel
import com.example.foodtime_compose0518.ui.theme.displayFontFamily

@Composable
fun NotificationSetting(isEnabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row {
        Text(
            text = "開啟通知",
            fontSize = 24.sp,
            fontFamily = displayFontFamily,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
        )
    }
}

@Composable
fun Signal_Notification(navController: NavController, settingViewModel: SettingViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val notificationEnabled = remember { mutableStateOf(true) }
        val redLightNotification = remember { mutableStateOf(true) }
        val yellowLightNotification = remember { mutableStateOf(true) }
        val redLightDays = remember { mutableStateOf(3) }
        val yellowLightDays = remember { mutableStateOf(5) }

        NotificationSetting(
            isEnabled = notificationEnabled.value,
            onToggle = {
                notificationEnabled.value = it
                if (!it) {
                    redLightNotification.value = false
                    yellowLightNotification.value = false

                    settingViewModel.updateSetting("NotificationEnabled", it, 0)
                    settingViewModel.updateSetting("RedLightEnabled", redLightNotification.value, 0)
                    settingViewModel.updateSetting("YellowLightEnabled", yellowLightNotification.value, 0)
                } else {
                    settingViewModel.updateSetting("NotificationEnabled", it, 0)
                }
            }
        )

        ListItem2(
            imageResId = R.drawable.redlight,
            name = "紅燈",
            expirationDays = redLightDays,
            isNotificationEnabled = notificationEnabled.value && redLightNotification.value,
            onNotificationToggle = {
                if (notificationEnabled.value) {
                    redLightNotification.value = it
                    settingViewModel.updateSetting("RedLightEnabled", it, redLightDays.value)
                }
            },
            settingViewModel = settingViewModel,
            onDayChange = { newDay ->  // 當使用者修改天數時調用
                redLightDays.value = newDay
                settingViewModel.updateSettingDay("RedLightEnabled", newDay)
            }
        )
        Divider()

        ListItem2(
            imageResId = R.drawable.yellowlight,
            name = "黃燈",
            expirationDays = yellowLightDays,
            isNotificationEnabled = notificationEnabled.value && yellowLightNotification.value,
            onNotificationToggle = {
                if (notificationEnabled.value) {
                    yellowLightNotification.value = it
                    settingViewModel.updateSetting("YellowLightEnabled", it, yellowLightDays.value)
                }
            },
            settingViewModel = settingViewModel,
            onDayChange = { newDay ->  // 當使用者修改天數時調用
                yellowLightDays.value = newDay
                settingViewModel.updateSettingDay("YellowLightEnabled", newDay)
            }
        )

        Divider()

        val skullDays = remember { mutableStateOf(0) }
        ListItem(
            imageResId = R.drawable.skull,
            name = "骷髏頭",
            expirationDays = skullDays,
            onNotificationToggle = { }
        )
        Divider()
    }
}

// 骷髏頭
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    imageResId: Int,
    name: String,
    expirationDays: MutableState<Int>,
    onNotificationToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontFamily = displayFontFamily
        )
        IconButton(onClick = {
        },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "减少天數",
                modifier = Modifier.size(24.dp)
            )
        }
        TextField(
            value = "${expirationDays.value}天",
            onValueChange = { newValue ->
                val number = newValue.replace("天", "").toIntOrNull()
                if (number != null && number >= 0) {
                    expirationDays.value = number
                } else if (newValue.isEmpty()) {
                    expirationDays.value = 0
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(70.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 3.dp),
            textStyle = TextStyle(
                fontFamily = displayFontFamily,
                fontSize = 18.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        IconButton(onClick = {
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加天數",
                modifier = Modifier.size(24.dp))
        }
        Switch(
            checked = false,
            onCheckedChange = {},
            enabled = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem2(
    imageResId: Int,
    name: String,
    expirationDays: MutableState<Int>,
    isNotificationEnabled: Boolean,
    onNotificationToggle: (Boolean) -> Unit,
    settingViewModel: SettingViewModel,
    onDayChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        val settingList=settingViewModel.settingList.collectAsState(emptyList())
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            fontSize = 20.sp,
            fontFamily = displayFontFamily
        )
        IconButton(onClick = {
            if (settingList.value[1].settingDay > 0) {
                expirationDays.value--
                onDayChange(expirationDays.value)
                //val notificationSetting = SettingTable(settingId = , settingName = "NotificationEnabled", settingNotify = true, settingDay =0)
                //settingViewModel.insertSetting(notificationSetting)
            }
        },
            modifier = Modifier.size(40.dp)  // 調整 IconButton 的大小
            ) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "减少天數",
                modifier = Modifier.size(24.dp)
            )// 調整 Icon 的大小)
        }
        TextField(
            value = "${expirationDays.value}天",
            onValueChange = { newValue ->
                val number = newValue.replace("天", "").toIntOrNull()
                if (number != null && number >= 0) {
                    expirationDays.value = number
                    onDayChange(number)
                } else if (newValue.isEmpty()) {
                    expirationDays.value = 0
                    onDayChange(0)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
            modifier = Modifier
                .width(70.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 3.dp),
            textStyle = TextStyle(
                fontFamily = displayFontFamily,
                fontSize = 18.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        IconButton(onClick = {
            expirationDays.value++
            onDayChange(expirationDays.value)
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加天数")
        }
        Switch(
            checked = isNotificationEnabled,
            onCheckedChange = onNotificationToggle
        )
    }
}









