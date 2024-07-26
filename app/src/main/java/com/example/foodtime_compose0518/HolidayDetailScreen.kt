package com.example.foodtime_compose0518

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.primaryContainerLight

//class HolidayDetailScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Foodtime_compose0518Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting2("Android")
//                }
//            }
//        }
//    }
//}



@Composable
fun HolidayDetailScreen(navController: NavController) {
    var quantity by remember { mutableStateOf(1) }
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFFECF5FF))
            .padding(horizontal = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp)
        ) {
            // TextView
//            Text(
//                text = "              中秋節購物清單",
//                fontSize = 28.sp,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(vertical = 25.dp)
//            )

            // Horizontal line
//            Divider(
//                color = myprimary1,
//                thickness = 5.dp,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                // Image and Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = "Navigation Menu",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(12.dp)
                    )
                    Text(
                        text = "蘋果",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                // Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = {
                        if (quantity > 0) quantity--// 确保数量不会变成负数
                    }) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "增加數量"
                        )
                    }

                    androidx.compose.material3.OutlinedTextField(
                        value = quantity.toString(), // 显示当前数量
                        onValueChange = {
                            quantity = it.toIntOrNull() ?: quantity // 确保输入的是数字
                        },
                        label = { },
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    IconButton(onClick = {
                        quantity++ // 确保数量不会变成负数
                    }) {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = "減少數量"
                        )
                    }
                }
            }
        }

        Foodtime0518_Theme {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("HolidayAddFragment") },
                text = { Text("新增食材") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                backgroundColor = primaryContainerLight, // 背景颜色
            )
        }
    }
}

@Preview
@Composable
fun HolidayDetailScreenPreview() {
    val navController = rememberNavController()
    HolidayDetailScreen(navController)
}



//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PartialBottomSheet(navController: NavController) {
//    var showBottomSheet by remember { mutableStateOf(false) }
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = false,
//    )
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        Button(
//            onClick = { showBottomSheet = true }
//        ) {
//            Text("顯示底部彈出視窗")
//        }
//
//        if (showBottomSheet) {
//            ModalBottomSheet(
//                modifier = Modifier.fillMaxHeight(),
//                sheetState = sheetState,
//                onDismissRequest = { showBottomSheet = false }
//            ) {
//                AddFragmentContent(navController = navController)
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun PreviewPartialBottomSheet() {
//    val navController = rememberNavController() // 創建一個 NavController 的實例
//    PartialBottomSheet(navController = navController)
//}

//@Composable
//fun DialogExamples() {
//    val openAlertDialog = remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Button(onClick = { openAlertDialog.value = true }) {
//            Text("顯示 Alert Dialog")
//        }
//
//        if (openAlertDialog.value) {
//            AlertDialogExample(
//                onDismissRequest = { openAlertDialog.value = false },
//                onConfirmation = {
//                    openAlertDialog.value = false
//                    println("Confirmation registered")
//                },
//                dialogTitle = "Alert dialog example",
//                dialogText = "This is an example of an alert dialog with buttons.",
//                icon = Icons.Default.Info
//            )
//        }
//    }
//}
//
//@Composable
//fun AlertDialogExample(
//    onDismissRequest: () -> Unit,
//    onConfirmation: () -> Unit,
//    dialogTitle: String,
//    dialogText: String,
//    icon: ImageVector
//) {
//    AlertDialog(
//        onDismissRequest = onDismissRequest,
//        confirmButton = {
//            TextButton(onClick = onConfirmation) {
//                Text("確認")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismissRequest) {
//                Text("取消")
//            }
//        },
//        title = {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(icon, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = dialogTitle)
//            }
//        },
//        text = {
//            Text(dialogText)
//        }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewDialogExamples() {
//    DialogExamples()
//}


