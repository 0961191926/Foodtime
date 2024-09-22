package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HolidayViewModel(val dao: FoodDao,val holidayDetailDao: HolidayDetailDao) : ViewModel() {
    var newHolidayName = ""
    var newHolidayDate: Long = 0L
    val holidayList : Flow<List<HolidayTable>> =dao.getAllUsers().stateIn(

    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
    )

    val holidaydetailList : Flow<List<HolidayDetailTable>> =holidayDetailDao.getAllUsers().stateIn(

        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun setHolidayName(holidayName: String) {
        newHolidayName = holidayName
    }

    fun addHoliday() {
        viewModelScope.launch {
            dao.insert(HolidayTable(holidayName = newHolidayName, Date = newHolidayDate))
        }
    }

    fun setHolidayDate(date: Long) {
        newHolidayDate = date
    }

    fun updateHoliday(item: HolidayDetailTable) {
        viewModelScope.launch {
            dao.update(item)
        }
    }

    fun deleteHoliday(holiday: HolidayTable) {
        viewModelScope.launch {
            dao.delete(holiday)
        }
    }

    fun addHolidayDetail(holidayId: Int, itemName: String, quantity: Int){
        viewModelScope.launch{
            holidayDetailDao.insert(HolidayDetailTable(holidayId = holidayId, itemName = itemName, quantity = quantity))
        }
    }

    fun updateHolidayDetail(holidayDetail: HolidayDetailTable) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(holidayDetail)
        }
    }

    fun deleteHolidayDetail(item: HolidayDetailTable) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteHolidayDetail(item)
        }
    }



    fun getHolidayDetailsByHolidayId(holidayId: Int): Flow<List<HolidayDetailTable>> {
            return holidayDetailDao.getDetailsByHolidayId(holidayId).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }






}




