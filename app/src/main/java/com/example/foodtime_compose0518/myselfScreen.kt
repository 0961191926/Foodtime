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
import com.example.foodtime_compose0518.ui.theme.onSurfaceVariantLight

//class myselfScreen : ComponentActivity() {
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
fun LoginScreen(navController: NavController) {
    Surface(
//        color = Color(android.graphics.Color.parseColor("#f0ebf5"))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "關於自己",
                fontSize = 25.sp,
                color = onSurfaceVariantLight,
                fontFamily = displayFontFamily,
                modifier = Modifier.padding(top = 100.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            UserInputField(label = "帳號", hint = "請輸入您的帳號")
            UserInputField(label = "密碼", hint = "請輸入您的密碼", isPassword = true)

            Spacer(modifier = Modifier.height(10.dp))
//            Button(
//                onClick = onLoginClick,
//                modifier = Modifier.fillMaxWidth(),
//            ) {
//                Text(text = "註冊", fontSize = 20.sp)
//            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    navController.navigate("new_page") // Navigate to the main screen
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(myprimary1),
            ) {
                Text(text = "登入", fontSize = 20.sp, fontFamily = displayFontFamily)
            }
        }
    }
}

@Composable
fun UserInputField(label: String, hint: String, isPassword: Boolean = false) {
    val textState = remember { mutableStateOf("") }

    OutlinedTextField(
        value = textState.value,
        onValueChange = { textState.value = it },
        label = { Text(label) },
        placeholder = { Text(hint) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = bodyFontFamily, // 使用自定義字體
            fontSize = 16.sp // 設置字體大小
        )

    )
}

@Preview
@Composable
fun PreviewLoginScreen() {
    Foodtime_compose0518Theme {
        LoginScreen(navController = rememberNavController())
    }
}