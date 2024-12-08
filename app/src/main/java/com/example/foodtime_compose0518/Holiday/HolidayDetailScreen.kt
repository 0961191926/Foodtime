package com.example.foodtime_compose0518

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.foodtime_compose0518.ui.theme.Foodtime0518_Theme
import com.example.foodtime_compose0518.ui.theme.bodyFontFamily
import com.example.foodtime_compose0518.ui.theme.displayFontFamily
import com.example.foodtime_compose0518.ui.theme.primaryContainerLight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HolidayNoteItem(
    note: HolidayDetailTable,
    holidayViewModel: HolidayViewModel,
    onClick: (HolidayDetailTable) -> Unit,
    itemName: String,
    cover1: Int
) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(note.quantity) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    holidayViewModel.viewModelScope.launch(Dispatchers.IO) {
                        holidayViewModel.deleteHolidayDetail(note)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "項目已刪除", Toast.LENGTH_SHORT).show()
                        }
                    }
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
                    contentDescription = "Note cover",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(start = 16.dp)
                        .clickable { onClick(note) }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = itemName,
                    fontFamily = displayFontFamily,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                QuantityController(
                    quantity = quantity,
                    onQuantityChange = { newQuantity ->
                        quantity = newQuantity
                        holidayViewModel.updateHolidayDetail(note.copy(quantity = newQuantity))
                    }
                )
            }
        },
        backgroundContent = { DismissBackground(dismissState) }
    )
}

@Composable
fun QuantityController(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (quantity > 0) onQuantityChange(quantity - 1)
        }) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "減少數量")
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.width(50.dp),
            style = TextStyle(
                fontFamily = bodyFontFamily,
                fontSize = 20.sp
            )
        )

        IconButton(onClick = {
            onQuantityChange(quantity + 1)
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "增加數量")
        }
    }
}

@Composable

fun HolidayDetailScreen(navController: NavController, holidayId: Int, holidayViewModel: HolidayViewModel) {
    val holidayDetailItemsState = remember { mutableStateListOf<HolidayViewModel.HolidayDetailItem>() }

    // 用 LaunchedEffect 來觸發數據更新
    LaunchedEffect(holidayId) {
        holidayViewModel.getHolidayDetailItems(holidayId).collect { items ->
            holidayDetailItemsState.clear()  // 清空舊的數據
            holidayDetailItemsState.addAll(items)  // 添加新的數據
        }
    }

    LazyColumn {
        items(holidayDetailItemsState, key = { it.holidayDetail.detailId }) { item ->
            HolidayNoteItem(
                note = item.holidayDetail,
                holidayViewModel = holidayViewModel,
                onClick = { /* 處理點擊事件 */ },
                itemName = item.itemName,
                cover1 = item.cover1
            )
            Divider()
        }
    }

    // 延遲加載新食材
    Padding16dp {
        androidx.compose.material3.ExtendedFloatingActionButton(
            onClick = {
                navController.navigate("HolidayAddFragment/$holidayId")
            },
            icon = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "新增食材"
                )
            },
            text = { Text(text = "新增食材") }
        )
    }
}








