package com.example.foodtime_compose0518

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodDao {

    @Insert
    suspend fun insert(holiday: HolidayTable)

    @Update
    fun update(holiday: HolidayTable)

    @Delete
    fun delete(holiday: HolidayTable)
}

