package com.example.foodtime_compose0518

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "normal_table")
data class NormalTable (
    @PrimaryKey(autoGenerate = true)
    val normalitemId: Int = 0,

    @ColumnInfo(name = "normalitem_name")
    val normalitemName: String,

    @ColumnInfo(name = "number")
    var number: Int

)
