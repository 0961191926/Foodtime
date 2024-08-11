import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foodtime_compose0518.Note
import com.example.foodtime_compose0518.NoteItem
import com.example.foodtime_compose0518.R

@Composable
fun ExpireScreen(navController: NavController) {
    Column {
        ExpiredNoteList(navController = navController)
        Spacer(modifier = Modifier.weight(1f))
        Padding16dp {
        }
    }
}

@Composable
fun ExpiredNoteList(navController: NavController) {
    val notes = remember { mutableStateListOf(
        Note(id = 1, productname = "蘋果", valid_date = "2024/05/03", logindata = "2024/05/03"),
        Note(id = 2, productname = "花椰菜", valid_date = "2024/05/03", logindata= "2024/05/03"),
        Note(id = 3, productname = "牛肉", valid_date = "2024/05/03",logindata = "2024/05/03"),
        Note(id = 4, productname = "蘋果", valid_date = "2024/05/03",logindata = "2024/05/03")
    ) }


}

