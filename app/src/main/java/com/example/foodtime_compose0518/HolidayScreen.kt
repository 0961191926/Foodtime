import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults.containerColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodtime_compose0518.FoodDatabase
import com.example.foodtime_compose0518.HolidayTable
import com.example.foodtime_compose0518.HolidayViewModel
import com.example.foodtime_compose0518.TemplateScreen


@SuppressLint("SuspiciousIndentation")
@Composable
fun HolidayScreen(navController: NavController,viewModel: HolidayViewModel) {
     val holist =viewModel.holidayList.collectAsState(arrayListOf())

            LazyColumn(modifier = Modifier.clickable {
                navController.navigate("HolidayDetail")
            }){
                items(holist.value) {
                    ListItem(
                        headlineContent = { Text(text = " ${it.holidayName}")},
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
                }

            }
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


@Composable
fun UserItem(navController: NavController,user: List<HolidayTable>) {
    for(holidayList in user)
        ListItem(
            headlineContent = { Text(text = "Name: ${holidayList.holidayName}")},
            supportingContent = { Text("2024/09/17") },
            leadingContent = {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                )
            },
            modifier = Modifier.clickable {
                navController.navigate("HolidayDetail")
            } .padding(vertical = 20.dp)
        )

}
@Composable
fun Padding16dp(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(), // 使用 fillMaxSize 将 Box 填充整个父容器
        contentAlignment = Alignment.BottomEnd // 设置内容在底部和右侧对齐
    ) {
        content()
    }
}