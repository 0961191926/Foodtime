import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.foodtime_compose0518.Padding16dp
import com.example.foodtime_compose0518.TemplateScreen


@Composable
fun HolidayScreen(navController: NavController) {

        Column {
            ListItem(
                headlineContent = { Text("端午節") },
                supportingContent = { Text("2024/06/10") },
                leadingContent = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                },
                modifier = Modifier.clickable {
                    navController.navigate("HolidayDetail")
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("中元節") },
                supportingContent = { Text("2024/08/18") },
                leadingContent = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                },
                modifier = Modifier.clickable {
                    navController.navigate("HolidayDetail")
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("中秋節") },
                supportingContent = { Text("2024/09/17") },
                leadingContent = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                },
                modifier = Modifier.clickable {
                    navController.navigate("HolidayDetail")
                }
            )
            HorizontalDivider()
            Padding16dp {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate("Addholiday")
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Create,
                            "Extended floating action button."
                        )
                    },
                    text = { Text(text = "新增節日") },
                )
            }
        }
}
