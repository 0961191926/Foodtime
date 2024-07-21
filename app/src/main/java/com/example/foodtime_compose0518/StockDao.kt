package com.example.foodtime_compose0518

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stock_table")
    fun getAllStockItems(): Flow<List<StockTable>>

    @Insert
    suspend fun insert(item: StockTable)

    @Update
    suspend fun update(item: StockTable)

    @Delete
    suspend fun delete(item: StockTable)
}
