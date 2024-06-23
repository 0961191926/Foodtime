package com.example.foodtime_compose0518

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HolidayViewModel(val dao: FoodDao) : ViewModel() {
    var newHolidayName = ""


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




