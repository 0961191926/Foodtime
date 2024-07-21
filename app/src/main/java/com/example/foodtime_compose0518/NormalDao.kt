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
interface NormalDao {
    @Query("SELECT * FROM normal_table")
    fun getAllUsers(): Flow<List<NormalTable>>

    @Insert
    suspend fun insert(item: NormalTable)

    @Update
    suspend fun update(item: NormalTable)

    @Delete
    suspend fun delete(item: NormalTable)

}