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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime_compose0518Theme
import com.example.foodtime_compose0518.ui.theme.onPrimaryLight
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.myprimary1

class FoodDetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Foodtime_compose0518Theme {
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
    Foodtime_compose0518Theme {
        Greeting("Android")
    }
}

@Composable
fun DetailFragment(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color(0xFFECF5FF))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // 食材名稱输入框
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.apple),
                contentDescription = null, // 提供描述以支持辅助功能
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = "品名： 蘋果",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(16.dp),
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp, // 指定文本大小
                    fontWeight = FontWeight.Bold // 选配，增加字体粗细
                )
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "數量：",
                fontSize = 28.sp,
                modifier = Modifier.padding(start = 100.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "一份",
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 10.dp)
            )
        }



        Spacer(modifier = Modifier.height(60.dp))

        // 登入日期 DatePicker
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "登入日期",
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )

            // 在这里调用 DateTextField 组件
            DateTextField(
                text = "2024/05/12", // 初始化日期
                onChange = { /* 处理日期变化的回调 */ },
                label = "選擇日期",
                isIllegalInput = false,
                modifier = Modifier.weight(1f) // 填充剩余空间
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "保存期限",
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 10.dp),
                fontFamily = displayFontFamily
            )

            // 在这里调用 DateTextField 组件
            DateTextField(
                text = "2024/05/12", // 初始化日期
                onChange = { /* 处理日期变化的回调 */ },
                label = "選擇日期",
                isIllegalInput = false,
                modifier = Modifier.weight(1f) // 填充剩余空间
            )
        }

        Spacer(modifier = Modifier.height(120.dp))

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
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        myprimary1 // 使用您定义的颜色
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .padding(horizontal = 30.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(35.dp) // 设置按钮的弧度
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
                        fontSize = 22.sp,
                        fontFamily = bodyFontFamily,
                        style = TextStyle(color = myprimary1)
                    )
                }
            }
        }




    }
}



@Preview
@Composable
private fun FooddetailPreview() {
    val navController = rememberNavController()
    DetailFragment(navController=navController)
}