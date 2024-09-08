package com.example.foodtime_compose0518

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    val today = dateFormat.format(Date())
    var loginDate by remember { mutableStateOf(today) }
    var expirationDate by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val initialDate = System.currentTimeMillis()
    var expanded by remember { mutableStateOf(false) }
    val suggestions=stockViewModel.UnexpiredList.collectAsState(emptyList())
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
 Foodtime0518_Theme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
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
                    onValueChange = { ingredientName = it
                        expanded = it.isNotEmpty() },
                    label = { Text(text = "輸入食材名稱") },
                    modifier = Modifier.weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                keyboardController?.show()
                            }
                        },

                    trailingIcon = {
                        if (expanded) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
                        } else {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                        .focusable(false),



                ) {

                    val filteredSuggestions = suggestions.value.filter { it.stockitemName.contains(ingredientName, ignoreCase = true) }
                    filteredSuggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            text ={ Text(text =suggestion.stockitemName ) },

                            onClick = {
                            ingredientName = suggestion.stockitemName
                            expanded = false
                                focusRequester.requestFocus()


                            })

                    }
                }
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
                MyDatePickerComponent(initialDate = initialDate){ selectedDate ->
                    loginDate = selectedDate
                }


            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "有效期限",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )

                MyDatePickerComponent(initialDate){ selectedDate ->
                    expirationDate = selectedDate
                }
            }

            Spacer(modifier = Modifier.height(65.dp))
            if (showError) {
                Text(
                    text = "有效期限不可為空值",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Row {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        primaryLight // 使用您定义的颜色
                    ),
                    onClick = {
                        if (expirationDate.isEmpty()) {
                            showError = true
                        } else {
                            showError = false
                            stockViewModel.setStockName(ingredientName)
                            stockViewModel.setNumber(quantity)
                            stockViewModel.setLoginDate(convertDateToLong( loginDate))
                            stockViewModel.setExpiryDate(convertDateToLong( expirationDate))
                            stockViewModel.addStockItem()
                            navController.navigate("ingredients")
                        }
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
fun MyDatePickerComponent(initialDate: Long,onDateSelected: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date(initialDate)) }
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd", Locale.US) }
    var textFieldValue by remember { mutableStateOf(dateFormat.format(selectedDate)) }
    LaunchedEffect(initialDate) {
        selectedDate = Date(initialDate)
        textFieldValue = dateFormat.format(selectedDate)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { /* 禁止直接編輯 */ },
            label = { Text("Select date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Edit Date")
                }
            },
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate.time
            )
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                selectedDate = Date(millis)
                                textFieldValue = dateFormat.format(selectedDate)
                                onDateSelected(textFieldValue)
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
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                                )
                            },
                            headline = { /* 移除預設標題 */ },
                            showModeToggle = false
                        )
                    }
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            )
        }
    }
}










