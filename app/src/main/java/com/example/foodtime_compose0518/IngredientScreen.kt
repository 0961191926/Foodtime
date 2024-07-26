package com.example.foodtime_compose0518

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.ExperimentalMaterial3Api

data class Note(val id: Int, val productname: String, val valid_date: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: Note,
    cover1: Int,
    cover2: Int,
    onClick: (Note) -> Unit,
    onRemove: (Note) -> Unit
) {
    val context = LocalContext.current
    val currentItem = note
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(currentItem)
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
fun NoteContent(note: Note, cover1: Int, cover2: Int, onClick: (Note) -> Unit) {
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
                text = note.productname,
                fontFamily = displayFontFamily,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.valid_date,
                fontFamily = bodyFontFamily,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Image(
            painter = painterResource(cover2),
            contentDescription = "Note cover 2",
            modifier = Modifier
                .size(30.dp)
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
fun NoteList(navController: NavController) {
    val notes = remember { mutableStateListOf(
        Note(id = 1, productname = "蘋果", valid_date = "2024/05/03"),
        Note(id = 2, productname = "花椰菜", valid_date = "2024/05/03"),
        Note(id = 3, productname = "牛肉", valid_date = "2024/05/03"),
        Note(id = 4, productname = "蘋果", valid_date = "2024/05/03")
    ) }

    LazyColumn {
        items(notes, key = { it.id }) { note ->
            NoteItem(
                note = note,
                cover1 = R.drawable.apple,
                cover2 = R.drawable.redlight,
                onClick = { navController.navigate("FoodDetail") },
                onRemove = { notes.remove(it) }
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
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        content()
    }
}

@Composable
fun IngredientsScreen(navController: NavController) {
    Column {
        NoteList(navController = navController)
        Spacer(modifier = Modifier.weight(1f))
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

@Preview(showBackground = true)
@Composable
fun IngredientsScreenPreview() {
    val navController = rememberNavController()
    IngredientsScreen(navController = navController)
}
