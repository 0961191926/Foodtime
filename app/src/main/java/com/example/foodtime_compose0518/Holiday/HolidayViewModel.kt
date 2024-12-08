package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

class HolidayViewModel(val dao: FoodDao,val holidayDetailDao: HolidayDetailDao,val itemDao: ItemDao) : ViewModel() {
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
        viewModelScope.launch(Dispatchers.IO) {
            dao.delete(holiday)
        }
    }

    fun addHolidayDetail(holidayId: Int, itemId: Int, quantity: Int){
        viewModelScope.launch{
            holidayDetailDao.insert(HolidayDetailTable(holidayId = holidayId, itemId = itemId, quantity = quantity))
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

    fun getItemNameById(itemId: Int): Flow<String> {
        return flow {
            val itemName = itemDao.getItemNameById(itemId) // 呼叫 suspend 函數
            emit(itemName)
            // 發射結果
        }
            .distinctUntilChanged() .flowOn(Dispatchers.IO) // 確保操作執行在 IO 執行緒
    }


    fun getHolidayDetailsByHolidayId(holidayId: Int): Flow<List<HolidayDetailTable>> {
            return holidayDetailDao.getDetailsByHolidayId(holidayId).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    fun getHolidayDetailItems(holidayId: Int): Flow<List<HolidayDetailItem>> {
        return getHolidayDetailsByHolidayId(holidayId).map { holidayDetails ->
            holidayDetails.mapNotNull { holidayDetail ->
                // 遞交異步邏輯到 Flow 管理
                val itemNameFlow = getItemNameById(holidayDetail.itemId)
                val itemName = runBlocking { itemNameFlow.firstOrNull() } // 同步取得 itemName
                itemName?.let {
                    val cover1 = ImageMapper.getImageResourceByName(it) // 映射圖片資源
                    HolidayDetailItem(holidayDetail, it, cover1) // 建立 HolidayDetailItem
                }
            }
        }
    }


// 新建数据类 HolidayDetailItem
data class HolidayDetailItem(
    val holidayDetail: HolidayDetailTable,
    val itemName: String,
    val cover1: Int
)






}




