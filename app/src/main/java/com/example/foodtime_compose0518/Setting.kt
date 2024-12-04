import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.displayFontFamily



@Composable
fun setting(navController: NavController) {
    Column (modifier = Modifier
        .background(MaterialTheme.colorScheme.background),


    ) {
        ListItem(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .clickable { navController.navigate("Signal_Notification") },
            leadingContent = {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Localized description",
                )
            },
            headlineContent = {
                Text(
                    text = "到期日提醒設定",
                    fontSize = 20.sp,
                    fontFamily = displayFontFamily,
                )
            },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Localized description",
                )
            }
        )
        HorizontalDivider()

        ListItem(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .clickable { navController.navigate("Foodexpiration_setting") },

            leadingContent = {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "Localized description",
                )
            },
            headlineContent = {
                Text(
                    text = "食材到期設定",
                    fontSize = 20.sp,
                    fontFamily = displayFontFamily,
                )
            },
            trailingContent = {
                Icon(
                    Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Localized description",
                )
            }
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun hihipreview() {
    Foodtime0518_Theme{
        var navController = rememberNavController()
        setting(navController)
    }
}