package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ItemViewModel(val dao: ItemDao) : ViewModel() {

    var newItemId = 0
    var newItemName=""


    val itemList: Flow<List<ItemTable>> = dao.getAllUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun setItemName(itemName: String) {
        newItemName = itemName
    }
   fun setItemId(itemId: Int) {
        newItemId = itemId
    }


    fun addItem() {
        viewModelScope.launch {
            dao.insert(
                ItemTable(
                    itemName = newItemName,
                    itemId = newItemId,
                )
            )
        }
    }




}
