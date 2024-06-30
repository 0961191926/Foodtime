package com.example.foodtime_compose0518

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight
import com.example.foodtime_compose0518.ui.theme.primaryLight

//class LogoutScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Foodtime_compose0518Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting5("Android")
//                }
//            }
//        }
//    }
//}





@Composable
fun LoginScreen (navController: NavHostController) {
    val focusManager = LocalFocusManager.current
    // State to hold email and password
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "登入頁面",
            style = TextStyle(
                fontSize = 25.sp,
                color = androidx.compose.ui.graphics.Color.Black,
                fontFamily = displayFontFamily
                // 如果您有自定义字体文件，请将其添加到项目并在此处设置字体系列
            ),
            modifier = Modifier.padding(top = 100.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            // Email TextField
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("帳號") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = bodyFontFamily, // 使用自定義字體
                    fontSize = 16.sp // 設置字體大小
                )

            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("密碼") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = bodyFontFamily, // 使用自定義字體
                    fontSize = 16.sp // 設置字體大小
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    navController.navigate("home_page") // Navigate to the main screen
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(primaryLight),

                ) {
                Text(
                    text = "登入",
                    fontSize = 20.sp,
                    fontFamily = bodyFontFamily,
                    style = TextStyle(color = onPrimaryLight)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    navController.navigate("home_page") // Navigate to the main screen
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(onPrimaryLight),

                ) {
                Text(
                    text = "註冊",
                    fontSize = 20.sp,
                    fontFamily = bodyFontFamily,
                    style = TextStyle(color = primaryLight)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    Foodtime0518_Theme {
// 创建一个 NavHostController 以用于预览
        val navController = rememberNavController()
        LoginScreen(navController = navController)
    }
}
