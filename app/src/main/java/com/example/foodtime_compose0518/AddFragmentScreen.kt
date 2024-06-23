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
fun AddFragmentScreen(navController: NavController) {
    TemplateScreen(
        title = "新增食材"
    ) {
        AddFragmentContent(navController = navController)
    }
}

@Preview
@Composable
private fun AddFragmentPreview() {
    val  NavController = rememberNavController()
    AddFragmentScreen(NavController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFragmentContent(navController: NavController) { // 接收 navController
    var ingredientName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(1) } // 修改为 Int 类型
    var loginDate by remember { mutableStateOf("2024/05/12") }
    var expirationDate by remember { mutableStateOf("2024/05/12") }
    val context = LocalContext.current

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
                DateTextField(
                    text = loginDate,
                    onChange = { loginDate = it },
                    label = "選擇日期",
                    isIllegalInput = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "有效期限",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )

                DateTextField(
                    text = expirationDate,
                    onChange = { expirationDate = it },
                    label = "選擇日期",
                    isIllegalInput = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(120.dp))

            Row {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        primaryLight // 使用您定义的颜色
                    ),
                    onClick = {
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



@Preview
@Composable
private fun Addcontentpreview() {
    AddFragmentContent(navController = rememberNavController())
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

@ExperimentalMaterial3Api
@Composable
fun CustomDatePickerDialog (
    state: DatePickerState,
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Cancel",
    onDismissRequest: () -> Unit,
    onConfirmButtonClicked: (Long?) -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = { //OK按鈕
            androidx.compose.material.TextButton(onClick = { onConfirmButtonClicked(state.selectedDateMillis) }) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = { //cancel按鈕
            androidx.compose.material.TextButton(onClick = onDismissRequest) {
                Text(text = dismissButtonText)
            }
        },
        content = {
            DatePicker( //headline, title 切換模式的按鈕不需要
                state = state,
                showModeToggle = false,
                headline = null,
                title = null,
            )
        }
    )
}






