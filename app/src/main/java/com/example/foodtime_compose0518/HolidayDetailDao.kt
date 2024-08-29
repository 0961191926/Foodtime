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
interface HolidayDetailDao {
    @Query("SELECT * FROM holiday_detail_table")
    fun getAllUsers(): Flow<List<HolidayDetailTable>>

    @Insert
    suspend fun insert(item: HolidayDetailTable)

    @Update
    suspend fun update(item: HolidayDetailTable)

    @Delete
    suspend fun delete(item: HolidayDetailTable)

    @Query("SELECT * FROM holiday_detail_table WHERE holidayId = :holidayId")
    fun getDetailsByHolidayId(holidayId: Int): Flow<List<HolidayDetailTable>>


}