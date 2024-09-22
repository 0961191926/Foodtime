package com.example.foodtime_compose0518

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting_table")
data class SettingTable(
    @PrimaryKey(autoGenerate = true)
    val settingId: Int = 0,

    @ColumnInfo(name = "name")
    val settingName: String,

    @ColumnInfo(name = "notify")
    val settingNotify: Boolean,

    @ColumnInfo(name = "day")
    val settingDay: Int
)

