package com.example.foodtime_compose0518

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme

//class NormalListScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Foodtime_compose0518Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Normallist(navController = navController)
//                }
//            }
//        }
//    }
//}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    Foodtime0518_Theme {
        Greeting4("Android")
    }
}

@Composable
fun Normallist(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                // Image and Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.broccoli),
                        contentDescription = "Navigation Menu",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(12.dp)
                    )
                    Text(
                        text = "花椰菜",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                // Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = "Localized description")
                    }
                    OutlinedTextField(
                        value = "1",
                        onValueChange = {},
                        label = { },
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Outlined.Clear, contentDescription = "Localized description")
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                // Image and Text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.meat),
                        contentDescription = "Navigation Menu",
                        modifier = Modifier
                            .size(65.dp)
                            .padding(12.dp)
                    )
                    Text(
                        text = "豬肉",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                // Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = "Localized description"
                        )
                    }
                    OutlinedTextField(
                        value = "1",
                        onValueChange = {},
                        label = { },
                        modifier = Modifier
                            .width(120.dp)
                            .padding(horizontal = 8.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Outlined.Clear, contentDescription = "Localized description")
                    }
                }
            }

            Padding16dp {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate("AddFood")
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Create,
                            "Extended floating action button."
                        )
                    },
                    text = { Text(text = "新增食材") },
                )
            }
        }
    }
}


@Preview
@Composable
fun Normallistpreview() {
    val navController = rememberNavController()
    Normallist(navController)
}