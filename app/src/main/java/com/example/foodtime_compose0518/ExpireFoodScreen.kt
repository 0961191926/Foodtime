package com.example.foodtime_compose0518


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

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
fun ExpireScreen(navController: NavHostController) {
    Column {
        NoteItem(
            note = Note(id = 1, productname = "蘋果", valid_date = "2024/05/03"),
            cover1 = R.drawable.apple,
            cover2 = R.drawable.skull,
            onClick = { navController.navigate("FoodDetail") }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
        NoteItem(
            note = Note(id = 1, productname = "花椰菜", valid_date = "2024/05/03"),
            cover1 = R.drawable.broccoli,
            cover2 = R.drawable.skull,
            onClick = { /* Remove the navigation operation */ }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
        NoteItem(
            note = Note(id = 1, productname = "牛肉", valid_date = "2024/05/03"),
            cover1 = R.drawable.meat,
            cover2 = R.drawable.skull,
            onClick = { /* Remove the navigation operation */ }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)
        NoteItem(
            note = Note(id = 1, productname = "蘋果", valid_date = "2024/05/03"),
            cover1 = R.drawable.apple,
            cover2 = R.drawable.skull,
            onClick = { /* Remove the navigation operation */ }
        )
        Divider(modifier = Modifier.height(1.dp), color = Color.Gray)

        Spacer(modifier = Modifier.weight(1f))

        Padding16dp {}
    }
}