package com.example.foodtime_compose0518

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date

@Dao
interface StockDao {

        @Query("SELECT * FROM stock_table " +
                "WHERE stockitem_name LIKE :stockitemName " +
                "AND expiry_date >= :currentDate " +
                "ORDER BY expiry_date;")
        fun getItemByName(stockitemName: String,currentDate: Long = getCurrentDateWithoutTime()): Flow<List<StockTable>>

        @Query("SELECT * FROM stock_table WHERE stockitemId = :id")
        suspend fun getItemById(id: Int): StockTable

        @Query("SELECT * FROM stock_table " +
                "WHERE expiry_date >= :currentDate " +
                "ORDER BY expiry_date;")
        fun getUnexpiredStockItems(currentDate: Long = getCurrentDateWithoutTime()): Flow<List<StockTable>>

        @Query("SELECT * FROM stock_table " +
                "WHERE expiry_date < :currentDate " +
                "ORDER BY expiry_date;")
        fun getExpiredStockItems(currentDate: Long = getCurrentDateWithoutTime()): Flow<List<StockTable>>
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(item: kotlin.collections.List<com.example.foodtime_compose0518.StockTable>)

        @Update
        suspend fun update(item: StockTable)

        @Delete
        suspend fun delete(item: StockTable)

        @Query("SELECT * FROM stock_table WHERE uuid = :uuid LIMIT 1")
        suspend fun getItemByUUID(uuid: String): StockTable?

        @Query("DELETE FROM stock_table WHERE expiry_date < :currentDate")
        suspend fun deleteExpiredStockItems(currentDate: Long = System.currentTimeMillis())

        companion object {
                fun getCurrentDateWithoutTime(): Long {
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val currentDate = sdf.format(Date())
                        return sdf.parse(currentDate).time
                }
        }

}
