package com.example.foodtime_compose0518

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
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

    private val nodeRef1 = Firebase.database.reference.child("a")
    private val nodeRef2 = Firebase.database.reference.child("b")
    private val _stockItem = MutableStateFlow<StockTable>(StockTable())
    val stockItem: Flow<StockTable> get() = _stockItem

    var redLightDays: Int = 0
        private set

    var yellowLightDays: Int = 0
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            redLightDays = settingDao.getAdjustmentDaysByName("RedLightEnabled")
            yellowLightDays = settingDao.getAdjustmentDaysByName("YellowLightEnabled")
        }
    }


    private val _UnexpiredList = MutableStateFlow<List<StockTable>>(emptyList())
    val UnexpiredList: StateFlow<List<StockTable>> get() = _UnexpiredList


    // 根據設定名稱查詢設定天數
    suspend fun loadAdjustmentDays(settingName: String): Int {
        // 获取设置的天数
        return settingDao.getAdjustmentDaysByName(settingName)?: 0
    }

    private val _dataFlow = MutableStateFlow<List<StockTable>>(emptyList())
    val dataFlow: StateFlow<List<StockTable>> get() = _dataFlow

    init {

        Log.d("FirebaseDebug", "NodeRef1 path: ${nodeRef1.path}")
        Log.d("FirebaseDebug", "NodeRef2 path: ${nodeRef2.path}")

        viewModelScope.launch {
            dao.getUnexpiredStockItems()
                .collect { items ->
                    _UnexpiredList.value = items
                }
        }

        // Listener for nodeRef1
        val nodeRef1Listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FirebaseDebug", "NodeRef1 Snapshot: ${snapshot.value}")
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val dataList = mutableListOf<StockTable>()
                        for (dataSnapshot in snapshot.children) {
                            val isWritten = dataSnapshot.child("isWritten").getValue(Boolean::class.java) ?: false

                            if (!isWritten) {
                                try {
                                    val dataModel = dataSnapshot.getValue(StockTable::class.java)
                                    dataModel?.let {
                                        val existingItem = dao.getItemByUUID(it.uuid)
                                        if (existingItem == null) {
                                            val dataEntity = StockTable(
                                                it.stockitemId ?: 0,
                                                it.stockitemName ?: "",
                                                it.number,
                                                it.loginDate,
                                                it.loginDate, // Directly use loginDate
                                                it.uuid
                                            )
                                            dataList.add(dataEntity)
                                            dataSnapshot.ref.child("isWritten").setValue(true)
                                        }
                                    }
                                } catch (e: DatabaseException) {
                                    Log.e("DataConversion", "Error converting data: ${e.message}")
                                }
                            }
                        }

                        if (dataList.isNotEmpty()) {
                            dao.insert(dataList)
                            Log.d("StockViewModel", "Inserted data list from nodeRef1: $dataList")
                            _dataFlow.value = dataList
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "NodeRef1 Error: ${error.message}")
            }
        }

        // Listener for nodeRef2
        val nodeRef2Listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FirebaseDebug", "NodeRef2 Snapshot: ${snapshot.value}")
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val dataList = mutableListOf<StockTable>()
                        for (dataSnapshot in snapshot.children) {
                            val isWritten = dataSnapshot.child("isWritten").getValue(Boolean::class.java) ?: false

                            if (!isWritten) {
                                try {
                                    val dataModel = dataSnapshot.getValue(StockTable::class.java)
                                    dataModel?.let {
                                        val existingItem = dao.getItemByUUID(it.uuid)
                                        if (existingItem == null) {
                                            val settingName = it.stockitemName ?: ""
                                            Log.d("FirebaseDebug", "NodeRef2 $settingName")
                                            val adjustmentDays = loadAdjustmentDays(settingName)?: 0
                                            Log.d("FirebaseDebug", "NodeRef23 $adjustmentDays ")
                                            val adjustedExpiryDate = it.loginDate + (adjustmentDays * 24 * 60 * 60 * 1000L)
                                            Log.d("FirebaseDebug", "NodeRef24 ")
                                            val dataEntity = StockTable(
                                                it.stockitemId ?: 0,
                                                it.stockitemName ?: "",
                                                it.number,
                                                it.loginDate,
                                                adjustedExpiryDate, // Use adjusted expiry date
                                                it.uuid
                                            )
                                            dataList.add(dataEntity)
                                            dataSnapshot.ref.child("isWritten").setValue(true)
                                        }
                                    }
                                } catch (e: DatabaseException) {
                                    Log.e("DataConversion", "Error converting data: ${e.message}")
                                }
                            }
                        }

                        if (dataList.isNotEmpty()) {
                            dao.insert(dataList)
                            Log.d("StockViewModel", "Inserted data list from nodeRef2: $dataList")
                            _dataFlow.value = dataList
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "NodeRef2 Error: ${error.message}")
            }
        }

        // Attach listeners to Firebase database references
        nodeRef1.addValueEventListener(nodeRef1Listener)
        Log.d("FirebaseDebug", "NodeRef1 listener attached")

        nodeRef2.addValueEventListener(nodeRef2Listener)
        Log.d("FirebaseDebug", "NodeRef2 listener attached")
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
    fun getLightDays(LightName:String): Int {
        var LightDays = 0
        CoroutineScope(Dispatchers.IO).launch {
            LightDays = settingDao.getAdjustmentDaysByName(LightName)

        }
        return LightDays
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

        val redlight = redLightDays/100.0
        val yellowlight = yellowLightDays/100.0
        Log.e("lightSignalkk","redLightDays$redLightDays yellowLightDays$yellowLightDays")
        return when {
            freshness in yellowlight..1.0 -> R.drawable.greenlight // Fresh
            freshness in redlight..yellowlight -> R.drawable.yellowlight
            freshness in 0.000001..redlight -> R.drawable.redlight
            else -> R.drawable.skull // Not fresh
        }


    }

}