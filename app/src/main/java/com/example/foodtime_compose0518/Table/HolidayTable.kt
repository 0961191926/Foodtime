package com.example.foodtime_compose0518

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holiday_table")
data class HolidayTable(
    @PrimaryKey(autoGenerate = true)
    val holidayId: Int = 0,

    @ColumnInfo(name = "holiday_name")
    val holidayName: String,

    @ColumnInfo(name = "date")
    val Date: Long
)
