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

        @Query("SELECT * FROM stock_table WHERE stockitemId = :id")
        suspend fun getItemById(id: Int): StockTable

        @Query("SELECT * FROM stock_table ORDER BY expiry_date")
        fun getAllStockItems(): Flow<List<StockTable>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(item: kotlin.collections.List<com.example.foodtime_compose0518.StockTable>)

        @Update
        suspend fun update(item: StockTable)

        @Delete
        suspend fun delete(item: StockTable)


}
