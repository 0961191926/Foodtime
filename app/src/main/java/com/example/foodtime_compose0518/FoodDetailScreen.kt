package com.example.foodtime_compose0518

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.primaryLight
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.foodtime_compose0518.MainActivity


class FoodDetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Foodtime0518_Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Foodtime0518_Theme {
        Greeting("Android")
    }
}

@Composable
fun DetailFragment(navController: NavController, stockitemId: Int, stockViewModel: StockViewModel) {
    stockViewModel.fetchStockItem(stockitemId)
    val stocklistById = stockViewModel.stockItem.collectAsState(StockTable())

    var expirationDate by remember { mutableStateOf(stocklistById.value.expiryDate) }
    var loginDate by remember { mutableStateOf(stocklistById.value.loginDate) }
    var stockname by remember { mutableStateOf(stocklistById.value.stockitemName) }
    var number by remember { mutableStateOf(stocklistById.value.number) } // 现在是用户可编辑的




    val currentImageRes = imageMapping[stockname.lowercase()] ?: R.drawable.kang // 默认图片

    LaunchedEffect(stocklistById.value) {
        expirationDate = stocklistById.value.expiryDate
        loginDate = stocklistById.value.loginDate
        stockname = stocklistById.value.stockitemName
        number = stocklistById.value.number
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // 食材名稱输入框
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = currentImageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                value = stockname,
                onValueChange = { stockname = it },
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(end = 10.dp),
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 顯示數量
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "數量",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )
            Spacer(modifier = Modifier.width(50.dp))
            IconButton(onClick = { number += 1 }) {
                Icon(
                    Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "Increase number"
                )
            }

            OutlinedTextField(
                value = number.toString(),
                onValueChange = {
                    number = it.toIntOrNull() ?: 1
                },
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            IconButton(onClick = { if (number > 0) number-- }) {
                Icon(
                    Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Decrease number"
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // 登入日期 DatePicker
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "登入日期",
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )
            MyDatePickerComponent(initialDate = loginDate) { selectedDate ->
                loginDate = convertDateToLong(selectedDate)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 保存期限
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "保存期限",
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )
            expirationDate?.let {
                MyDatePickerComponent(it) { selectedDate ->
                    expirationDate = convertDateToLong(selectedDate)
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        val dataEntity = StockTable(
                            stockitemId = stockitemId,
                            stockitemName = stockname,
                            number = number,  // 使用用户输入的数量
                            loginDate = loginDate,
                            expiryDate = expirationDate,
                            uuid = ""
                        )
                        stockViewModel.updateStockItem(dataEntity)
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(primaryLight),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = "更新食材資訊",
                        fontSize = 22.sp,
                        fontFamily = bodyFontFamily,
                        style = TextStyle(color = onPrimaryLight)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(onPrimaryLight),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text(
                        text = "取消",
                        fontSize = 22.sp,
                        fontFamily = bodyFontFamily,
                        style = TextStyle(color = primaryLight)
                    )
                }
            }
        }
    }
}

fun convertDateToLong(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    val date = dateFormat.parse(dateString)
    return date?.time ?: 0L
}



