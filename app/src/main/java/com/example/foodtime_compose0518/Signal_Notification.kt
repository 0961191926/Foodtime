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
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
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
fun Signal_Notification(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val notificationEnabled = remember { mutableStateOf(true) }
        NotificationSetting(
            isEnabled = notificationEnabled.value,
            onToggle = { notificationEnabled.value = it }
        )

        val redLightDays = remember { mutableStateOf(3) }
        val redLightNotification = remember { mutableStateOf(true) }
        ListItem2(
            imageResId = R.drawable.redlight,
            name = "紅燈",
            expirationDays = redLightDays,
            isNotificationEnabled = redLightNotification.value,
            onNotificationToggle = { redLightNotification.value = it }
        )
            Divider()

        val yellowLightDays = remember { mutableStateOf(5) }
        val yellowLightNotification = remember { mutableStateOf(true) }
        ListItem2(
            imageResId = R.drawable.yellowlight,
            name = "黃燈",
            expirationDays = yellowLightDays,
            isNotificationEnabled = yellowLightNotification.value,
            onNotificationToggle = { yellowLightNotification.value = it }
        )

        Divider()


        val skullDays = remember { mutableStateOf(7) }
        val skullNotification = remember { mutableStateOf(true) }
        ListItem2(
            imageResId = R.drawable.skull,
            name = "骷髏頭",
            expirationDays = skullDays,
            isNotificationEnabled = skullNotification.value,
            onNotificationToggle = { skullNotification.value = it }
        )

        Divider()

    }
}

@Composable
fun ListItem(
    imageResId: Int,
    name: String,
    expirationDays: MutableState<Int>,
    isNotificationEnabled: Boolean,
    onNotificationToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                fontSize = 18.sp,
                fontFamily = displayFontFamily
            )
            Text(
                text = "${expirationDays.value} 天",
                modifier = Modifier.padding(horizontal = 4.dp),
                fontSize = 20.sp,
                fontFamily = displayFontFamily
            )
            Switch(
                checked = isNotificationEnabled,
                onCheckedChange = onNotificationToggle
            )
        }
        Slider(
            value = expirationDays.value.toFloat(),
            onValueChange = { expirationDays.value = it.toInt() },
            valueRange = 0f..30f,
            modifier = Modifier.padding(horizontal = 16.dp)
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
            if (expirationDays.value > 0) {
                expirationDays.value--
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
            expirationDays.value++
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加天数")
        }
        Switch(
            checked = isNotificationEnabled,
            onCheckedChange = onNotificationToggle
        )
    }
}





@Preview
@Composable
private fun notificationpreview() {
    Foodtime0518_Theme {
        var navController = rememberNavController()
        Signal_Notification(navController)
    }
}



