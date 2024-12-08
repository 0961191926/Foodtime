package com.example.foodtime_compose0518

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow


class StockViewModel(val dao: StockDao, val settingDao: SettingDao) : ViewModel() {
    var newStockName = ""
    var newNumber = 0
    var newLoginDate: Long = 0L
    var newExpiryDate: Long = 0L
    var newuuid = ""
    private val freshrange=0.5
    private val unfreshrange=0.2
    private val nodeRef1 = Firebase.database.reference.child("a")
    private val nodeRef2 = Firebase.database.reference.child("b")
    private val _stockItem = MutableStateFlow<StockTable>(StockTable())
    val stockItem: Flow<StockTable> get() = _stockItem

    private val _UnexpiredList = MutableStateFlow<List<StockTable>>(emptyList())
    val UnexpiredList: StateFlow<List<StockTable>> get() = _UnexpiredList


    // 根據設定名稱查詢設定天數
    suspend fun loadAdjustmentDays(settingName: String): Int {
        // 获取设置的天数
        return settingDao.getAdjustmentDaysByName(settingName)?: 0
    }

    init {
        viewModelScope.launch {
            dao.getUnexpiredStockItems()
                .collect { items ->
                    _UnexpiredList.value = items
                }
        }
    }

    private val _dataFlow = MutableStateFlow<List<StockTable>>(emptyList())
    val dataFlow: StateFlow<List<StockTable>> get() = _dataFlow

    init {
        val combinedListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val dataList = mutableListOf<StockTable>()
                        for (dataSnapshot in snapshot.children) {
                            // 检查该项是否已经标记为已写入
                            val isWritten = dataSnapshot.child("isWritten").getValue(Boolean::class.java) ?: false

                            if (!isWritten) {
                                try {
                                    val dataModel = dataSnapshot.getValue(StockTable::class.java)
                                    dataModel?.let {
                                        val existingItem = dao.getItemByUUID(it.uuid)
                                        if (existingItem == null) {
                                            // 获取 stockitemName 来查询调整天数
                                            val settingName = it.stockitemName ?: ""  // 获取 stockitemName

                                            // 判断当前节点是 nodeRef1 还是 nodeRef2
                                            if (dataSnapshot.ref == nodeRef1) {
                                                // 节点1：直接写入
                                                val dataEntity = StockTable(
                                                    it.stockitemId ?: 0,
                                                    it.stockitemName ?: "",
                                                    it.number,
                                                    it.loginDate,
                                                    it.loginDate, // 直接使用 loginDate
                                                    it.uuid
                                                )
                                                // 添加到数据列表
                                                dataList.add(dataEntity)

                                            } else if (dataSnapshot.ref == nodeRef2) {
                                                // 节点2：需要调整日期
                                                val adjustmentDays = loadAdjustmentDays(settingName)  // 根据 stockitemName 查询设置的天数

                                                // 计算调整后的过期日期
                                                val adjustedExpiryDate = it.loginDate + (adjustmentDays * 24 * 60 * 60 * 1000L)

                                                // 创建新的 StockTable 实体
                                                val dataEntity = StockTable(
                                                    it.stockitemId ?: 0,
                                                    it.stockitemName ?: "",
                                                    it.number,
                                                    it.loginDate,
                                                    adjustedExpiryDate,  // 使用调整后的过期日期
                                                    it.uuid
                                                )
                                                // 添加到数据列表
                                                dataList.add(dataEntity)
                                            }

                                            // 标记为已写入，防止未来的重复
                                            dataSnapshot.ref.child("isWritten").setValue(true)
                                        }
                                    }
                                } catch (e: DatabaseException) {
                                    Log.e("DataConversionhaha", "Error converting data: ${e.message}")
                                }
                            }
                        }

                        // 如果有数据，则插入到本地数据库并更新数据流
                        if (dataList.isNotEmpty()) {
                            dao.insert(dataList)
                            Log.d("StockViewModel", "Inserted data list: $dataList")
                            _dataFlow.value = dataList
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "Error: ${error.message}")
            }
        }

        // 添加监听器到 Firebase 数据库
        nodeRef2.addValueEventListener(combinedListener)  // 监听 nodeRef2，进行日期调整
        nodeRef1.addValueEventListener(combinedListener)  // 监听 nodeRef1，直接写入
    }






    val expiredList: Flow<List<StockTable>> = dao.getExpiredStockItems().stateIn(
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

    fun setLoginDate(date: Long) {
        newLoginDate = date
    }

    fun setExpiryDate(date: Long) {
        newExpiryDate = date
    }


    fun addStockItem() {
        viewModelScope.launch {
            val datalist =
                StockTable(
                    stockitemName = newStockName,
                    number = newNumber,
                    loginDate = newLoginDate,
                    expiryDate = newExpiryDate,
                    uuid = newuuid

                )
            dao.insert(listOf(datalist))
        }
    }


    fun fetchStockItem(id: Int) {
        viewModelScope.launch {
            val item = withContext(Dispatchers.IO) {
                dao.getItemById(id)
            }
            _stockItem.value = item
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

    fun deleteExpiredStockItem() {
        viewModelScope.launch {
            dao.deleteExpiredStockItems()
        }
    }

    fun convertDateToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val date = dateFormat.parse(dateString)
        return date?.time ?: 0L
    }

    fun fetchStockItemByName(stockitemName: String) {
        viewModelScope.launch {
            if (stockitemName.isEmpty()) {
                dao.getUnexpiredStockItems()
                    .collect { items ->
                        _UnexpiredList.value = items
                    }
            } else {
                dao.getItemByName("%$stockitemName%")
                    .collect { items ->
                        _UnexpiredList.value = items
                    }
            }
        }
    }

    fun resetSearch() {
        viewModelScope.launch {
            _UnexpiredList.value = dao.getUnexpiredStockItems().first()
        }
    }

    fun freshness(note: StockTable): Double {
        var loginDate = note.loginDate //登入日期
        var expiryDate = note.expiryDate //到期日期
        var currentDate = System.currentTimeMillis() // 現在日期
        val daysDifference = (expiryDate - loginDate) / (1000 * 60 * 60 * 24)
        var n =
           when {
            daysDifference <= 30 -> 2.75 // 30天以內
            daysDifference in 30..180  -> 2.5 // 30到180天之間
            else -> 2.3 // 超過180天
        }
        var passedtime = (currentDate-loginDate).toDouble()
        var totaltime = (expiryDate-loginDate).toDouble()

        var result = exp(ln(0.01) * (passedtime/totaltime).pow(n))
        return result
    }
    fun lightSignal(freshness: Double): Int {
        return when {
            freshness in 0.5..1.0 -> R.drawable.greenlight // Fresh
            freshness in 0.2..0.5 -> R.drawable.yellowlight
            freshness in 0.000001..0.2 -> R.drawable.redlight
            else -> R.drawable.skull // Not fresh
        }
    }

}