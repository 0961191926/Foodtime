import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foodtime_compose0518.Note
import com.example.foodtime_compose0518.NoteItem
import com.example.foodtime_compose0518.NoteList
import com.example.foodtime_compose0518.R
import com.example.foodtime_compose0518.StockViewModel
import com.example.foodtime_compose0518.ImageMapper

@Composable
fun ExpireScreen(navController: NavController,stockViewModel:StockViewModel) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                ExpiredNoteList(navController =navController , stockViewModel =stockViewModel )
                Spacer(modifier = Modifier.weight(1f))
            }
            com.example.foodtime_compose0518.Padding16dp {
                ExtendedFloatingActionButton(
                    onClick = {
                        stockViewModel.deleteExpiredStockItem()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            "Extended floating action button."
                        )
                    },
                    text = { Text(text = "刪除全部") },
                )
            }
        }
    }
@Composable
fun ExpiredNoteList(navController: NavController,stockViewModel: StockViewModel) {

        val datalist=stockViewModel.expiredList.collectAsState(emptyList())


        LazyColumn {
            items(datalist.value, key = { it.stockitemId }) { note ->
                NoteItem(
                    note = note,
                    cover1 = ImageMapper.getImageResourceByName(note.stockitemName),
                    cover2 = R.drawable.skull,
                    stockViewModel = stockViewModel,
                    onClick = { navController.navigate("FoodDetail/${note.stockitemId}",) },
                    onRemove ={ stockViewModel.deleteStockItem(note)
                    }
                )
                // 移除了 Divider
                Divider()
            }
        }
    }



