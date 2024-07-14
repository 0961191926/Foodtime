package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HolidayViewModel(val dao: FoodDao) : ViewModel() {
    var newHolidayName = ""
    val holidayList : Flow<List<HolidayTable>> =dao.getAllUsers().stateIn(

    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
    )


    fun setHolidayName(holidayName: String) {
        newHolidayName = holidayName
    }

    fun addHoliday() {
        viewModelScope.launch {
                dao.insert(HolidayTable(holidayName = newHolidayName))
        }
    }

    fun updateHoliday(holiday: HolidayTable) {
        viewModelScope.launch {
            dao.update(holiday)
        }
    }

    fun deleteHoliday(holiday: HolidayTable) {
        viewModelScope.launch {
            dao.delete(holiday)
        }
    }
}




