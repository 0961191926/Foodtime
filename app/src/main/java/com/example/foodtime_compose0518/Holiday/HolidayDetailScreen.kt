package com.example.foodtime_compose0518

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.primaryContainerLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material.Divider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem3(
    note: HolidayDetailTable,
    holidayViewModel: HolidayViewModel,
    onClick: (HolidayDetailTable) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove()
                    Toast.makeText(context, "項目已刪除", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        },
        positionalThreshold = { it * .25f }
    )

    val itemName by holidayViewModel.getItemNameById(note.itemId).collectAsState("")
    val cover1 = ImageMapper.getImageResourceByName(itemName)

    SwipeToDismissBox(
        state = dismissState,
        content = {
            NoteContent(note, cover1, onClick, holidayViewModel)
        },
        backgroundContent = { DismissBackground(dismissState) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun NoteContent(
    note: HolidayDetailTable,
    cover1: Int,
    onClick: (HolidayDetailTable) -> Unit,
    holidayViewModel: HolidayViewModel
) {
    var quantity by remember { mutableStateOf(note.quantity.toString()) }

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
        val itemName by holidayViewModel.getItemNameById(note.itemId).collectAsState("")

        Text(
            text = itemName,
            fontFamily = displayFontFamily,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val number = quantity.toIntOrNull()
                if (number != null && number > 0) {
                    val newQuantity = number - 1
                    quantity = newQuantity.toString()
                    holidayViewModel.updateHolidayDetail(
                        note.copy(quantity = newQuantity)
                    )
                }
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "减少数量")
            }

            TextField(
                value = quantity,
                onValueChange = { newValue ->
                    val number = newValue.toIntOrNull()
                    if (number != null && number >= 0) {
                        note.quantity = number
                        holidayViewModel.updateHolidayDetail(note)
                        quantity = newValue
                    } else if (newValue.isEmpty()) {
                        quantity = ""
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .width(50.dp)
                    .background(MaterialTheme.colorScheme.background),
                textStyle = TextStyle(
                    fontFamily = bodyFontFamily,
                    fontSize = 20.sp
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
                val number = quantity.toIntOrNull()
                if (number != null) {
                    val newQuantity = number + 1
                    quantity = newQuantity.toString()
                    holidayViewModel.viewModelScope.launch {
                        holidayViewModel.updateHolidayDetail(
                            note.copy(quantity = newQuantity)
                        )
                    }
                }
            }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加数量")
            }
        }
    }
}


@Composable
fun HolidayDetailScreen(navController: NavController, holidayId: Int, holidayViewModel: HolidayViewModel) {
    val holidayDetailList by holidayViewModel.getHolidayDetailsByHolidayId(holidayId).collectAsState(emptyList())
    //屬於這個節日的食材

    LazyColumn {
        items(holidayDetailList, key = { it.detailId }) { note ->
            NoteItem3(
                note = note,
                holidayViewModel = holidayViewModel,
                onClick = { /* 处理点击事件 */ },
                onRemove = {
                    holidayViewModel.viewModelScope.launch {
                        holidayViewModel.deleteHolidayDetail(note)
                    }
                }
            )
            Divider()
        }
    }
    Padding16dp {
        androidx.compose.material3.ExtendedFloatingActionButton(
            onClick = {
                navController.navigate("HolidayAddFragment/$holidayId")
            },
            icon = {
                Icon(
                    Icons.Filled.Add,
                    "Extended floating action button."
                )
            },
            text = { Text(text = "新增食材") },
        )
    }
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

