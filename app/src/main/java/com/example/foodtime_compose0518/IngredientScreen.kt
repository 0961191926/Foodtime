package com.example.foodtime_compose0518
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Note(val id: Int, val productname: String, val valid_date: String,val logindata:String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: StockTable,
    cover1: Int,
    cover2: Int,
    stockViewModel: StockViewModel,
    onClick: (StockTable) -> Unit,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val currentItem = note
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove()
                    Toast.makeText(context, "項目已刪除", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        },
        positionalThreshold = { it * .25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        content = {
            NoteContent(note, cover1, cover2, onClick)
        },
        backgroundContent = { DismissBackground(dismissState) },
    )
}

@Composable
fun NoteContent(note:StockTable, cover1: Int, cover2: Int, onClick: (StockTable) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)  // 使用 IntrinsicSize.Min 來適應內容
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(cover1),
            contentDescription = "Note cover 1",
            modifier = Modifier
                .size(50.dp)
                .padding(start = 16.dp)
                .clickable { onClick(note) }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = note.stockitemName,
                fontFamily = displayFontFamily,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = convertLongToDateString( note.expiryDate),
                fontFamily = bodyFontFamily,
                style = MaterialTheme.typography.bodyMedium
            )


        }

        Image(
            painter = painterResource(cover2),
            contentDescription = "Note cover 2",
            modifier = Modifier
                .size(50.dp)
                .padding(end = 16.dp)
                .clickable { onClick(note) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF1744) // 紅色，表示刪除
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(8.dp, 6.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete",
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Composable
fun NoteList(navController: NavController,stockViewModel: StockViewModel) {
    val datalist=stockViewModel.UnexpiredList.collectAsState(emptyList())


    LazyColumn {
        items(datalist.value, key = { it.stockitemId }) { note ->
            val cover1 = imageMapping[note.stockitemName] ?: R.drawable.kang // 默认图片
            NoteItem(
                note = note,
                cover1 = cover1,
                cover2 = stockViewModel.lightSignal(stockViewModel.freshness(note)),
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

@Composable
fun Padding16dp(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .zIndex(Float.MAX_VALUE),
        contentAlignment = Alignment.BottomEnd
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsScreen(navController: NavController, stockViewModel: StockViewModel) {
    val searchQuery = remember { mutableStateOf("") }
    val isSearchActive = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        stockViewModel.resetSearch()
        searchQuery.value = ""
    }
    Box(modifier = Modifier.fillMaxSize()) {
         Column {
            // 添加 SearchBar
             SearchBar(
                 query = searchQuery.value,
                 onQueryChange = {
                     searchQuery.value = it
                     stockViewModel.fetchStockItemByName(it)
                     //isSearchActive.value = it.isNotEmpty()
                 },
                 onSearch = {
//                    if(searchQuery.value.isBlank()){
//                        stockViewModel.fetchAllStockItems()
//                    }else {
//                        stockViewModel.fetchStockItemByName(searchQuery.value)
//                    }
//                    isSearchActive.value = true
                 },
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(16.dp),
                 placeholder = { Text(text = "搜索食材") },
                 active = false,
                 onActiveChange = { },
                 content = {},
                 leadingIcon = { Icon(
                     imageVector = Icons.Default.Search,
                     contentDescription = "Search Icon",
                     modifier = Modifier
                         .clickable {

                         }
                 ) }
             )
            Divider()
            NoteList(
                navController = navController,
                stockViewModel = stockViewModel
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Padding16dp {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("AddFragment")
                },
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        "Extended floating action button."
                    )
                },
                text = { Text(text = "新增食材") },
            )
        }
    }
}
fun convertLongToDateString(dateLong: Long): String {
    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    val date = Date(dateLong)
    return dateFormat.format(date)
}





