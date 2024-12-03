package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class NormalViewModel(val dao: NormalDao) : ViewModel() {
    var newNormalName = ""
    var newNumber = 0

    val normalList: Flow<List<NormalTable>> = dao.getAllUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setNormalName(name: String) {
        newNormalName = name
    }

    fun setNumber(number: Int) {
        newNumber = number
    }

    fun addNormalItem() {
        viewModelScope.launch {
            dao.insert(
                NormalTable(
                    normalitemName = newNormalName,
                    number = newNumber,
                )
            )
        }
    }

    fun updateNormalItem(item: NormalTable) {
        viewModelScope.launch {
            dao.update(item)
        }
    }

    fun deleteNormalItem(item: NormalTable) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}
