import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults.containerColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import com.example.foodtime_compose0518.MyDatePickerComponent
import com.example.foodtime_compose0518.TemplateScreen
import com.example.foodtime_compose0518.convertLongToDateString

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("SuspiciousIndentation")
@Composable
fun HolidayScreen(navController: NavController, viewModel: HolidayViewModel) {
    val holist = viewModel.holidayList.collectAsState(arrayListOf())

    LazyColumn {
        items(holist.value, key = { it.holidayId }) { holiday ->
            val dismissState = rememberDismissState()

            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                LaunchedEffect(key1 = holiday) {
                    viewModel.deleteHoliday(holiday)
                }
            }

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val color = if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                        Color.Red
                    } else {
                        Color.Transparent
                    }
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    }
                },
                dismissContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ListItem(
                            headlineContent = { Text(text = holiday.holidayName) },
                            supportingContent = { Text(convertLongToDateString(holiday.Date)) },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description"
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate("HolidayDetail/${holiday.holidayId}")
                                }
                        )
                        // 在列表右侧添加日期选择 Icon
                        MyDatePickerIcon(
                            initialDate = holiday.Date,
                            onDateSelected = { newDate ->
                                // 处理日期更新的逻辑
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(60.dp))
                    HorizontalDivider()
                }
            )
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
                    contentDescription = "Extended floating action button."
                )
            },
            text = { Text(text = "新增節日") }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun MyDatePickerIcon(initialDate: Long, onDateSelected: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date(initialDate)) }
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd", Locale.US) }

    IconButton(onClick = { showDatePicker = true }) {
        Icon(
            Icons.Default.DateRange,
            contentDescription = "Select Date"
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time
        )
        AlertDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Date(millis)
                            val formattedDate = dateFormat.format(selectedDate)
                            onDateSelected(formattedDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text(
                                "Select date",
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                            )
                        },
                        showModeToggle = false
                    )
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
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
            modifier = Modifier
                .clickable {
                    navController.navigate("HolidayDetail")
                }
                .padding(vertical = 20.dp)
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}




