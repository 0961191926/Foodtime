package com.example.foodtime_compose0518

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow



@Dao
interface FoodDao {
    @Query("SELECT * FROM holiday_table")
    fun getAllUsers(): Flow<List<HolidayTable>>

    @Insert
    suspend fun insert(holiday: HolidayTable)

    @Update
    fun update(holiday: HolidayDetailTable)

    @Delete
    fun delete(holiday: HolidayTable)

    @Delete
    suspend fun deleteHolidayDetail(item: HolidayDetailTable)
}

