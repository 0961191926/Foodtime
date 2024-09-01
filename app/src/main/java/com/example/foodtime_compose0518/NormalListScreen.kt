package com.example.foodtime_compose0518

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.input.TextFieldValue
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem2(
    note: NormalTable,
    cover1: Int,
    normalViewModel:NormalViewModel,
    onClick: (NormalTable) -> Unit,
    onRemove: () -> Unit,
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
            NoteContent(note, cover1, onClick,normalViewModel)
        },
        backgroundContent = { DismissBackground(dismissState) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContent(note:NormalTable, cover1: Int,onClick: (NormalTable) -> Unit,normalViewModel: NormalViewModel) {
    val quantity = remember { mutableStateOf(note.number.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
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

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = note.normalitemName,
            fontFamily = displayFontFamily,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (note.number > 0) {
                    note.number--
                    normalViewModel.updateNormalItem(note)
                    quantity.value = note.number.toString()
                }
            }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "减少数量")
            }

            TextField(
                value = quantity.value,
                onValueChange = { newValue ->
                    // 确保输入的是有效的数字
                    val number = newValue.toIntOrNull()
                    if (number != null && number >= 0) {
                        note.number = number
                        normalViewModel.updateNormalItem(note)
                        quantity.value = newValue
                    } else if (newValue.isEmpty()) {
                        quantity.value = ""
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .width(50.dp)
                    .background(MaterialTheme.colorScheme.background),
                textStyle = TextStyle(
                    fontFamily = bodyFontFamily,
                    fontSize = 20.sp
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            IconButton(onClick = {
                note.number++
                quantity.value = note.number.toString()
                normalViewModel.updateNormalItem(note)
            }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加数量")
            }
        }
    }
}

@Composable
fun Normallist(navController: NavController, normalViewModel: NormalViewModel) {
    var nolist = normalViewModel.normalList.collectAsState(emptyList())

    LazyColumn {
        items(nolist.value, key = { it.normalitemId }) { note2 ->
            NoteItem2(
                note = note2,
                cover1 = R.drawable.apple,
                normalViewModel = normalViewModel, // 传递正确的 viewModel 实例
                onClick = { },
                onRemove = { normalViewModel.deleteNormalItem(note2) }
            )
            Divider()
        }
    }

    Padding16dp {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("NormalListAddFragment")
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

//@Preview(showBackground = true)
//@Composable
//fun NormalListPreview() {
//    val navController = rememberNavController()
//    Normallist(navController = navController, viewModel = NormalViewModel())
//}