package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NormalViewModelFactory(private val dao: NormalDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NormalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NormalViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}