package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ItemViewModel(val dao: ItemDao) : ViewModel() {
    var newNumber = 0

    val itemList: Flow<List<ItemTable>> = dao.getAllUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setNumber(number: Int) {
        newNumber = number
    }

}
