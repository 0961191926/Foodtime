package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StockViewModelFactory(private val dao: StockDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
