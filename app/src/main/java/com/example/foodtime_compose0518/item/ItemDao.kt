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
interface ItemDao {
    @Query("SELECT * FROM item_table")
    fun getAllUsers(): Flow<List<ItemTable>>

    @Query("SELECT item_name FROM item_table WHERE item_Id = :itemId")
    suspend fun getItemNameById(itemId: Int): String

    @Insert
    suspend fun insert(item: ItemTable)

    @Update
    suspend fun update(item: ItemTable)

    @Delete
    suspend fun delete(item: ItemTable)
}