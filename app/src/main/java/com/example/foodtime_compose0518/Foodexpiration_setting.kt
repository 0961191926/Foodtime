import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.DismissBackground
import com.example.foodtime_compose0518.NormalTable
import com.example.foodtime_compose0518.NormalViewModel
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.SettingTable
import com.example.foodtime_compose0518.SettingViewModel
import com.example.foodtime_compose0518.imageMapping
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem3(
    note: SettingTable,
    cover1: Int,
    settingViewModel: SettingViewModel,
    onClick: (SettingTable) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onRemove()
                Toast.makeText(context, "項目已刪除", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        content = {
            NoteContent(note, cover1, onClick, settingViewModel)
        },
        backgroundContent = { DismissBackground(dismissState) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContent(
    note: SettingTable,
    cover1: Int,
    onClick: (SettingTable) -> Unit,
    settingViewModel: SettingViewModel
) {
    var days by remember { mutableStateOf(note.settingDay.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(cover1),
            contentDescription = "Note cover 1",
            modifier = Modifier
                .size(50.dp)
                .padding(start = 16.dp)
                .clickable { onClick(note) }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = note.settingName,
            fontFamily = displayFontFamily,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                val updatedDays = note.settingDay + 1
                days = updatedDays.toString()
                settingViewModel.updateSettingItem(note.copy(settingDay = updatedDays))
            }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加天数")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(105.dp) // 控制整体宽度
            ) {
                TextField(
                    value = days,
                    onValueChange = { newValue ->
                        if (newValue.isBlank()) {
                            days = "" // 允许空输入
                        } else {
                            val newDays = newValue.toIntOrNull()
                            if (newDays != null && newDays >= 0) {
                                days = newDays.toString()
                                settingViewModel.updateSettingItem(note.copy(settingDay = newDays))
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(fontFamily = bodyFontFamily, fontSize = 20.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.width(70.dp) // 限制输入框宽度
                )

                Text(
                    text = "天",
                    fontFamily = bodyFontFamily,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 4.dp) // 添加间距
                )
            }

            IconButton(onClick = {
                if (note.settingDay > 0) {
                    val updatedDays = note.settingDay - 1
                    days = updatedDays.toString()
                    settingViewModel.updateSettingItem(note.copy(settingDay = updatedDays))
                }
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "减少天数")
            }
        }

    }
}


@Composable
fun Foodexpiration_SettingScreen(navController: NavController, settingViewModel: SettingViewModel) {
    val settingList = settingViewModel.settingList.collectAsState()

    LazyColumn {
        items(settingList.value, key = { it.settingId }) { settingItem ->
            val cover1 = imageMapping[settingItem.settingName] ?: R.drawable.kang
            NoteItem3(
                note = settingItem,
                cover1 = cover1,
                settingViewModel = settingViewModel,
                onClick = { },
                onRemove = { settingViewModel.deleteSettingItem(settingItem) }
            )
            Divider()
        }
    }

}