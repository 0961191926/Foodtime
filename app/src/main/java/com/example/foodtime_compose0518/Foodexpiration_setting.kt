import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
@Composable
fun FoodExpirationScreen(navController: NavController) {
    val items = remember {
        mutableStateListOf(
            ListItem(R.drawable.apple, "蘋果", 3),
            ListItem(R.drawable.broccoli_1, "花椰菜", 5),
            ListItem(R.drawable.meat, "肉", 7),
            ListItem(R.drawable.salmon,"鮭魚", 10),
            ListItem(R.drawable.carrot,"紅蘿蔔", 10),
            ListItem(R.drawable.tofu,"豆腐", 5),
            ListItem(R.drawable.cabbage,"高麗菜", 5),
            ListItem(R.drawable.radish,"蘿蔔", 7),
            ListItem(R.drawable.eggplant,"茄子", 7),
            ListItem(R.drawable.tomato,"番茄", 8),
            ListItem(R.drawable.fish,"魚", 5),
            ListItem(R.drawable.sprout,"豆芽菜", 5),
            ListItem(R.drawable.shellfish,"蛤利", 5),
            ListItem(R.drawable.egg,"蛋", 5),
            ListItem(R.drawable.sausage,"豬肉", 4),


            )
    }

    LazyColumn {
        items(items) { item ->
            ItemRow(item) { newDays ->
                val index = items.indexOf(item)
                if (index != -1) {
                    items[index] = item.copy(days = newDays)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRow(item: ListItem, onDaysChange: (Int) -> Unit) {
    var days by remember { mutableStateOf(item.days) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
            value = days.toString(),
            onValueChange = { newValue ->
                days = newValue.toIntOrNull() ?: days
                onDaysChange(days)
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


data class ListItem(
    val imageResId: Int,
    val name: String,
    val days: Int
)


@Preview
@Composable
private fun hihihi() {
    Foodtime0518_Theme {
        var navController = rememberNavController()
        FoodExpirationScreen(navController)
    }
}