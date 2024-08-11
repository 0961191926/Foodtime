package com.example.foodtime_compose0518

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight
import com.example.foodtime_compose0518.ui.theme.primaryLight
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale


@Composable
fun AddFragmentScreen(navController: NavController, stockViewModel: StockViewModel) {
    TemplateScreen(
        title = "新增食材"
    ) {
        AddFragmentContent(navController = navController, stockViewModel=stockViewModel)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFragmentContent(navController: NavController, stockViewModel: StockViewModel) { // 接收 navController
    var ingredientName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) } // 修改为 Int 类型
    var loginDate by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }

    Foodtime0518_Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 食材名稱输入框
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "食材名稱",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )
                OutlinedTextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    label = { Text(text = "輸入食材名稱") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(70.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "數量",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )
                Spacer(modifier = Modifier.width(50.dp))
                IconButton(onClick = {
                    if (quantity > 0) quantity-- // 确保数量不会变成负数
                }) {
                    Icon(
                        Icons.Outlined.KeyboardArrowDown,
                        contentDescription = "增加數量"
                    )
                }

                OutlinedTextField(
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

            Spacer(modifier = Modifier.height(60.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "登入日期",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )
                MyDatePickerComponent()

            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "有效期限",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )

                MyDatePickerComponent()
            }

            Spacer(modifier = Modifier.height(65.dp))

            Row {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        primaryLight // 使用您定义的颜色
                    ),
                    onClick = {
                        stockViewModel.setStockName(ingredientName)
                        stockViewModel.setNumber(quantity)
                        stockViewModel.setLoginDate(loginDate)
                        stockViewModel.setExpiryDate(expirationDate)
                        stockViewModel.addStockItem()
                        navController.navigate("ingredients")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(35.dp) // 设置按钮的弧度
                ) {
                    Text("增加食材",
                        fontSize = 16.sp,
                        fontFamily = bodyFontFamily,)
                }

                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        onPrimaryLight // 使用您定义的颜色
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(35.dp) // 设置按钮的弧度
                ) {
                    Text(text = "取消",
                        fontSize = 16.sp,
                        fontFamily = bodyFontFamily,
                        style = TextStyle(color = primaryLight)
                    )
                }
            }
        }
    }
}




@Composable
fun DateTextField (
    modifier: Modifier = Modifier,
    text: String,
    trailingIcon: @Composable (() -> Unit)? = null, //要讓datepicker的IconButton放在這
    onChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text, // 修改為 Text
    keyBoardActions: KeyboardActions = KeyboardActions(), //設定軟鍵盤
    isEnabled: Boolean = true,
    label: String,
    supportingText: String = "yyyy/mm/dd",
    isIllegalInput: Boolean         //error state
) {
    OutlinedTextField(
        value = text,
        onValueChange = onChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 18.sp),
        keyboardActions = keyBoardActions,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        enabled = isEnabled,
        trailingIcon = trailingIcon,
        label = {
            Text(text = "Date/Time", style = TextStyle(fontSize = 18.sp))
        },
        singleLine = true,
//        supportingText = {
//            Text(text = supportingText, style = TextStyle(fontSize = 18.sp))
//        },
        isError = isIllegalInput
    )
}

@Preview
@Composable
fun DateTextFieldPreview() {
    // 这里可以调用 DateTextField 函数，并传入所需的参数进行预览
    DateTextField(
        text = "2024/05/12",
        onChange = {},
        label = "Select Date",
        isIllegalInput = false
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerComponent() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) }
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd", Locale.US) }
    var textFieldValue by remember { mutableStateOf(dateFormat.format(selectedDate)) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("日期") },
                placeholder = { Text("YYYY/MM/DD") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Date")
                    }
                },
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
            )
        }

        if (showDatePicker) {
            Dialog(onDismissRequest = { showDatePicker = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.time)
                        DatePicker(state = datePickerState, modifier = Modifier.padding(16.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                selectedDate = Date(datePickerState.selectedDateMillis ?: 0)
                                textFieldValue = dateFormat.format(selectedDate)
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}



@Preview
@Composable
private fun MadatePickerpreview() {
    Foodtime0518_Theme {
        MyDatePickerComponent()
    }
}





