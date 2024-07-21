package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class StockViewModel(val dao: StockDao) : ViewModel() {
    var newStockName = ""
    var newNumber = 0
    var newLoginDate: Date? = null
    var newExpiryDate: Date? = null

    val stockList: Flow<List<StockTable>> = dao.getAllStockItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setStockName(name: String) {
        newStockName = name
    }

    fun setNumber(number: Int) {
        newNumber = number
    }

    fun setLoginDate(date: Date) {
        newLoginDate = date
    }

    fun setExpiryDate(date: Date) {
        newExpiryDate = date
    }

    fun addStockItem() {
        viewModelScope.launch {
            dao.insert(
                StockTable(
                    stockitemName = newStockName,
                    number = newNumber,
                    loginDate = newLoginDate ?: Date(),
                    expiryDate = newExpiryDate ?: Date()
                )
            )
        }
    }

    fun updateStockItem(item: StockTable) {
        viewModelScope.launch {
            dao.update(item)
        }
    }

    fun deleteStockItem(item: StockTable) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}
