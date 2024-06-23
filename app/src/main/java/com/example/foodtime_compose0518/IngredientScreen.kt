package com.example.foodtime_compose0518

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodtime_compose0518.ui.theme.Foodtime_compose0518Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily

data class Note(val id: Int, val productname: String, val valid_date: String)


@Composable
fun NoteItem(
    note: Note,
    cover1: Int,
    cover2: Int,
    onClick: (Note) -> Unit // 移除 navController 參數
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(cover1),
            contentDescription = "Note cover 1",
            modifier = Modifier
                .size(40.dp)
                .clickable { onClick(note) } // 修改点击事件的调用方式

        )

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = note.productname, fontFamily = displayFontFamily)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.valid_date,   fontFamily = bodyFontFamily,)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(cover2),
            contentDescription = "Note cover 2",
            modifier = Modifier
                .size(40.dp)
                .clickable { onClick(note) } // 修改点击事件的调用方式

        )
    }
}






@Composable
fun NoteList(navController: NavController) {
    Column {
        NoteItem(
            note = Note(id = 1, productname = "蘋果", valid_date = "2024/05/03"),
            cover1 = R.drawable.apple,
            cover2 = R.drawable.yellowlight,
            onClick = {navController.navigate("FoodDetail") }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
        NoteItem(
            note = Note(id = 1, productname = "花椰菜", valid_date = "2024/05/03"),
            cover1 = R.drawable.broccoli,
            cover2 = R.drawable.yellowlight,
            onClick = { /* Remove the navigation operation */ }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
        NoteItem(
            note = Note(id = 1, productname = "牛肉", valid_date = "2024/05/03"),
            cover1 = R.drawable.meat,
            cover2 = R.drawable.skull,
            onClick = { /* Remove the navigation operation */ }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
        NoteItem(
            note = Note(id = 1, productname = "蘋果", valid_date = "2024/05/03"),
            cover1 = R.drawable.apple,
            cover2 = R.drawable.redlight,
            onClick = { /* Remove the navigation operation */ }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        Padding16dp {
            MyFloatingActionButton(
                navController = navController, // 传递 NavController
                onClick = {
                    navController.navigate("addFragment")

                }

            )
        }
    }
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

@Composable
fun MyFloatingActionButton(
    navController: NavController, // 添加 NavController 参数
    onClick: () -> Unit
) {
//    FloatingActionButton(
//        onClick = onClick,
//        backgroundColor = MaterialTheme.colors.onPrimary,
//        contentColor = MaterialTheme.colors.onPrimary,
//        elevation = FloatingActionButtonDefaults.elevation(),
//        shape = CircleShape,
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Icon(
//            imageVector = Icons.Default.Add,
//            contentDescription = "Add"
//        )
//    }
    Foodtime_compose0518Theme {
        ExtendedFloatingActionButton(onClick = { navController.navigate("addFragment") }) {
            Text(text = "新增食材")
        }
    }
}

@Composable
fun IngredientsScreen(navController: NavController) {
    TemplateScreen(
        title = "食材庫"
    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(text = "這是食材庫頁面")
//            Button(onClick = { navController.navigate("addFragment") }) {
//                Text("添加食材")
//            }
//        }
        Column {
            // 将 NavController 传递给 NoteList 函数
            NoteList(navController = navController)
        }
    }
}
