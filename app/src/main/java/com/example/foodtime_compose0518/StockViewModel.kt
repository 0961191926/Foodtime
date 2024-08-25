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


class StockViewModel(val dao: StockDao) : ViewModel() {
    var newStockName = ""
    var newNumber = 0
    var newLoginDate: Long = 0L
    var newExpiryDate: Long = 0L
    var newuuid = ""
    private val database = Firebase.database.reference.child("a")
    private val _stockItem = MutableStateFlow<StockTable>(StockTable())
    val stockItem: Flow<StockTable> get() = _stockItem

    private val _UnexpiredList = MutableStateFlow<List<StockTable>>(emptyList())
    val UnexpiredList: StateFlow<List<StockTable>> get() = _UnexpiredList

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


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val dataList = mutableListOf<StockTable>()
                        for (dataSnapshot in snapshot.children) {
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
                                            it.expiryDate,
                                            it.uuid
                                        )
                                        dataList.add(dataEntity)
                                        dataSnapshot.ref.child("isWritten").setValue(true)
                                    }
                                }
                            } catch (e: DatabaseException) {
                                // 處理轉換失敗的情況
                                Log.e("DataConversionhaha", "Error converting data: ${e.message}")
                            }
                        }
                        if (dataList.isNotEmpty()) {
                            dao.insert(dataList)
                            Log.d("StockViewModel", "Inserted data list: $dataList")
                            _dataFlow.value = dataList
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e("DatabaseError", "Error: ${error.message}")
            }
        })
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
        val loginDate = note.loginDate //登入日期
        val expiryDate = note.expiryDate //到期日期
        val currentDate = System.currentTimeMillis() // 現在日期

        val n = when {
            (expiryDate-loginDate) <= 30 -> 2.75
            (expiryDate-loginDate) in 30..180 -> 2.5
            else -> 2.3
        }

        val result = exp(ln(0.01) * ((currentDate-loginDate).toDouble() / expiryDate-loginDate).pow(n))
        return result
    }
    fun lightSignal(freshness: Double): Int {
        return when {
            freshness in 1.0..0.5 -> R.drawable.greenlight // Fresh
            freshness in 0.2..0.5 -> R.drawable.yellowlight
            freshness in 0.1..0.2 -> R.drawable.redlight
            else -> R.drawable.skull // Not fresh
        }
    }

    fun calculateFreshness(note: StockTable): Double {
        var currentDate = System.currentTimeMillis() // 現在時間
        var expiryDate = note.expiryDate //到期時間
        var productionDate = currentDate - expiryDate //製造時間

        if (currentDate >= expiryDate) {
            return 0.0 // Already expired
        }
        var n = when {
            expiryDate-productionDate <= 30 -> 2.75
            (expiryDate-productionDate).toDouble() in 30.0..180.0 -> 2.5
            else -> 2.3
        }

        var totalShelfLife = (expiryDate - productionDate).toDouble() //保存期限
        var elapsedLife = (currentDate - productionDate).toDouble() // 已經過時間

        var freshness = exp(ln(0.01) * (elapsedLife/totalShelfLife).pow(n))
        return freshness // Ensure freshness is between 0 and 100
    }
}