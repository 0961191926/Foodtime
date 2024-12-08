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

    // 查詢所有設定
    @Query("SELECT * FROM setting_table")
    fun getAllUsers(): Flow<List<SettingTable>>

    // 插入新的設定
    @Insert(onConflict = OnConflictStrategy.IGNORE) // 如果已經存在相同食材，則忽略
    suspend fun insert(setting: SettingTable)

    // 更新設定
    @Update
    suspend fun update(setting: SettingTable)

    // 刪除設定
    @Delete
    suspend fun delete(setting: SettingTable)

}
