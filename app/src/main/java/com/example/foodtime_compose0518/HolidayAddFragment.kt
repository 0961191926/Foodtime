package com.example.foodtime_compose0518

import TextFieldWithDropdown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.primaryLight
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight

@Composable
fun HolidayAddFragmentScreen(navController: NavHostController, holidayId: Int, holidayViewModel: HolidayViewModel) {
    var ingredientName by remember { mutableStateOf(TextFieldValue("")) }
    var dropDownExpanded by remember { mutableStateOf(false) }
    // 使用 collectAsState 来获取食材建议
    val suggestions by holidayViewModel.getHolidayDetailsByHolidayId(holidayId).collectAsState(emptyList())
    val number = remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // 食材名稱输入框
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "食材名稱",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )
            TextFieldWithDropdown(
                modifier = Modifier.weight(1f),
                value = ingredientName,
                setValue = { newValue ->
                    ingredientName = newValue
                    dropDownExpanded = newValue.text.isNotEmpty() // 当输入内容不为空时展开下拉菜单
                },
                onDismissRequest = { dropDownExpanded = false },
                dropDownExpanded = dropDownExpanded,
                // 根据节日过滤食材建议
                list = suggestions.filter {
                    it.itemName.contains(ingredientName.text, ignoreCase = true)
                }.map { it.itemName }, // 显示 itemName 列表

                label = "輸入食材名稱"
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // 数量选择（保持不变）
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "數量",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )
            Spacer(modifier = Modifier.width(50.dp))

            IconButton(onClick = { number.value += 1 }) {
                Icon(
                    Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "Increase number"
                )
            }
            OutlinedTextField(
                value = number.value.toString(),
                onValueChange = {
                    number.value = it.toIntOrNull() ?: 1
                },
                label = { },
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            IconButton(onClick = { number.value = (number.value - 1).coerceAtLeast(1) }) {
                Icon(
                    Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Decrease number"
                )
            }
        }

        Spacer(modifier = Modifier.height(270.dp))

        Row {
            Button(
                colors = ButtonDefaults.buttonColors(
                    primaryLight // 使用您定义的颜色
                ),
                onClick = {
                    //增加食材
                    holidayViewModel.addHolidayDetail(holidayId, ingredientName.text, number.value)
                    navController.popBackStack() // 返回上一個畫面
                },
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(35.dp) // 设置按钮的弧度
            ) {
                Text(
                    "增加食材",
                    fontSize = 16.sp,
                    fontFamily = bodyFontFamily
                )
            }

            Button(
                onClick = {

                    navController.popBackStack()
                },
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
                Text(
                    text = "取消",
                    fontSize = 16.sp,
                    fontFamily = bodyFontFamily,
                    style = TextStyle(color = primaryLight)
                )
            }
        }
    }
}




