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
interface SettingDao {
    @Query("SELECT * FROM setting_table")
    fun getAllUsers(): Flow<List<SettingTable>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setting: SettingTable)

    @Update
    suspend fun update(setting: SettingTable)

    @Delete
    suspend fun delete(setting: SettingTable)
}