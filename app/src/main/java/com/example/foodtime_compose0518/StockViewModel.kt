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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.util.Date

class StockViewModel(val dao: StockDao) : ViewModel() {
    var newStockName = ""
    var newNumber = 0
    var newLoginDate=""
    var newExpiryDate=""
    private val database = Firebase.database.reference.child("a")
    private val _stockItem = MutableStateFlow<StockTable>(StockTable())

    private val _dataFlow = MutableStateFlow<List<StockTable>>(emptyList())
    val dataFlow: StateFlow<List<StockTable>> get() = _dataFlow
    init {


        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    val dataList = mutableListOf<StockTable>()
                    for (dataSnapshot in snapshot.children) {
                        try {
                            val dataModel = dataSnapshot.getValue(StockTable::class.java)
                            dataModel?.let {
                                val dataEntity = StockTable(
                                    it.stockitemId ?: 0,
                                    it.stockitemName ?: "",
                                    it.number,
                                    it.expiryDate,
                                    it.loginDate
                                )
                                dataList.add(dataEntity)
                            }
                        } catch (e: DatabaseException) {
                            // 處理轉換失敗的情況
                            Log.e("DataConversionhaha", "Error converting data: ${e.message}")
                        }
                    }
                    dao.insert(dataList)
                    Log.d("StockViewModel", "Inserted data list: $dataList")
                   _dataFlow.value=dataList
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e("DatabaseError", "Error: ${error.message}")
            }
        })
    }
    val stockList: Flow<List<StockTable>> = dao.getAllStockItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val stockItem: Flow<StockTable> get() = _stockItem

    fun setStockName(name: String) {
        newStockName = name
    }

    fun setNumber(number: Int) {
        newNumber = number
    }

    fun setLoginDate(date:String) {
        newLoginDate = date
    }

    fun setExpiryDate(date: String) {
        newExpiryDate = date
    }

    fun addStockItem() {
        viewModelScope.launch {
            val datalist=
                StockTable(
                    stockitemName = newStockName,
                    number = newNumber,
                    loginDate = newLoginDate ,
                    expiryDate = newExpiryDate ,
                )
            dao.insert(listOf(datalist) )
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
}
