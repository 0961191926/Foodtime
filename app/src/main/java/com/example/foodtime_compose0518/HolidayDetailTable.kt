package com.example.foodtime_compose0518

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "holiday_detail_table",
    foreignKeys = [ForeignKey(
        entity = HolidayTable::class,
        parentColumns = ["holidayId"],
        childColumns = ["holidayId"],
        onDelete = ForeignKey.CASCADE // 當Holiday被刪除時，自動刪除相關細節
    )]
)

data class HolidayDetailTable(
    @PrimaryKey(autoGenerate = true)
    val detailId: Int = 0,

    @ColumnInfo(name = "holidayId")
    val holidayId: Int, // 外鍵，連接到 HolidayTable

    @ColumnInfo(name = "item_name")
    val itemName: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int
)