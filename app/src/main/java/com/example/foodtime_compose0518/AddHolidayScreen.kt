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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime_compose0518Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.myprimary1
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight


import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodtime_compose0518.ui.theme.*
import com.example.foodtime_compose0518.HolidayViewModel
import com.example.foodtime_compose0518.HolidayViewModelFactory
import com.example.foodtime_compose0518.FoodDatabase
import com.example.foodtime_compose0518.HolidayTable


class AddHolidayScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Foodtime_compose0518Theme {
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
fun HolidayAdd(navController: NavController, holidayViewModel: HolidayViewModel) {
    var holidayName by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "增加節日",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 10.dp),
                    fontFamily = displayFontFamily
                )

                OutlinedTextField(
                    value = holidayName,
                    onValueChange = { holidayName = it },
                    label = { Text("enter holiday") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontFamily = bodyFontFamily,
                        fontSize = 16.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(440.dp))

            Row {
                Button(
                    colors = ButtonDefaults.buttonColors(myprimary1),
                    onClick = {
                        holidayViewModel.setHolidayName(holidayName)
                        holidayViewModel.addHoliday()
                        navController.navigate("holidays")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(35.dp)
                ) {
                    Text("確認")
                }

                Button(
                    onClick = { navController.navigate("holidays") },
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
                        fontSize = 16.sp,
                        fontFamily = bodyFontFamily,
                        style = TextStyle(color = myprimary1)
                    )
                }
            }
        }
    }
}

