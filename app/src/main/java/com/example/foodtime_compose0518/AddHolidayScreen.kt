package com.example.foodtime_compose0518

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle


import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodtime_compose0518.ui.theme.*
import com.example.foodtime_compose0518.HolidayViewModel
import com.example.foodtime_compose0518.HolidayViewModelFactory
import com.example.foodtime_compose0518.FoodDatabase
import com.example.foodtime_compose0518.HolidayTable


import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight
import com.example.foodtime_compose0518.ui.theme.primaryLight

class AddHolidayScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {



            Foodtime0518_Theme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val application = applicationContext
                    val database = FoodDatabase.getInstance(application)
                    val viewModelFactory = HolidayViewModelFactory(database.foodDao)
                    val holidayViewModel: HolidayViewModel = viewModel(factory = viewModelFactory)
                    HolidayAdd(navController = rememberNavController(), holidayViewModel)



                }
            }
        }
    }
}

@Composable

fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Foodtime0518_Theme {
        Greeting3("Android")
    }
}

@Composable
fun HolidayAdd(navController: NavController,holidayViewModel: HolidayViewModel) {
    var Holiday = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFFECF5FF))

            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
        ) {

            // TextView for Label

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "增加節日",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )



                // EditText for User Input
                androidx.compose.material3.OutlinedTextField(
                    value = Holiday.value,
                    onValueChange = { Holiday.value = it },
                    label = { Text(text="節日") },

                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(


                        fontFamily = bodyFontFamily, // 使用自定義字體
                        fontSize = 16.sp // 設置字體大小
                    )


                )
            }

            Spacer(modifier = Modifier.height(440.dp))


            // Confirm Button
            Row {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        primaryLight // 使用您定义的颜色
                    ),
                    onClick = { navController.navigate("holidays") },

                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),

                    shape = RoundedCornerShape(35.dp) // 设置按钮的弧度

                ) {
                    Text("確認")
                }

                Button(
                    onClick = { navController.navigate("holidays") },

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

                    )
                }
            }
        }
    }
}





@Composable
fun HolidayInputField(
    holiday: MutableState<String>
) {
    OutlinedTextField(
        value = holiday.value,
        onValueChange = { holiday.value = it },
        label = { Text("節日") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp
        )
    )
}

@Preview
@Composable
fun HolidayInputFieldPreview() {
    val holidayState = remember { mutableStateOf("") }
    HolidayInputField(holiday = holidayState)
}




