import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.SettingTable
import com.example.foodtime_compose0518.SettingViewModel
import com.example.foodtime_compose0518.ui.theme.displayFontFamily

@Composable
fun FoodExpirationScreen(
    navController: NavController,
    settingViewModel: SettingViewModel // 新增 ViewModel 參數
) {
    val items = remember {
        mutableStateListOf(
            ListItem(R.drawable.apple, "蘋果", 3),
            ListItem(R.drawable.broccoli_1, "花椰菜", 5),
            ListItem(R.drawable.meat, "肉", 7),
            ListItem(R.drawable.salmon,"鮭魚", 10),
            ListItem(R.drawable.carrot,"紅蘿蔔", 10),
            ListItem(R.drawable.garbage,"豆腐", 5),
            ListItem(R.drawable.cabbage,"高麗菜", 5),
            ListItem(R.drawable.radish,"蘿蔔", 7),
            ListItem(R.drawable.eggplant,"茄子", 7),
            ListItem(R.drawable.tomato,"番茄", 8),
            ListItem(R.drawable.fish,"魚", 5),
            ListItem(R.drawable.sprout,"豆芽菜", 5),
            ListItem(R.drawable.shellfish,"蛤利", 5),
            ListItem(R.drawable.egg,"蛋", 5),
            ListItem(R.drawable.sausage,"豬肉", 4)
        )
    }

    // 初始化數據到資料庫
    items.forEach { item ->
        // 構建 SettingTable 實例，並將其插入資料庫
        val settingItem = SettingTable(
            settingName = item.name,
            settingDay = item.days,
            settingNotify = true // 你可以根據需求修改通知的預設值
        )
        settingViewModel.insertSetting(settingItem)
    }

    LazyColumn {
        items(items) { item ->
            ItemRow(item, settingViewModel) { newDays ->
                val index = items.indexOf(item)
                if (index != -1) {
                    newDays?.let {
                        items[index] = item.copy(days = it)
                        // 將更新的天數保存到資料庫
                        settingViewModel.updateFoodExpiration(item.name, it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRow(
    item: ListItem,
    settingViewModel: SettingViewModel, // 傳遞 ViewModel
    onDaysChange: (Int?) -> Unit
) {
    var daysText by remember { mutableStateOf(item.days.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Min),  // 使用 IntrinsicSize.Min 來適應內容
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .padding(end = 10.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = item.name,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 10.dp),
            fontFamily = displayFontFamily
        )
        Spacer(modifier = Modifier.weight(1f))
        TextField(
            value = daysText,
            onValueChange = { newValue ->
                daysText = newValue
                val newDays = newValue.toIntOrNull()
                onDaysChange(newDays)
            },
            textStyle = TextStyle(fontSize = 20.sp, fontFamily = displayFontFamily),
            modifier = Modifier
                .width(60.dp)
                .background(MaterialTheme.colorScheme.background),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(text = "天", fontSize = 20.sp, fontFamily = displayFontFamily)
    }
}

// ListItem 的數據類
data class ListItem(
    val imageResId: Int,
    val name: String,
    val days: Int
)


