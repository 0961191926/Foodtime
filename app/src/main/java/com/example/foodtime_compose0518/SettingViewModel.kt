package com.example.foodtime_compose0518

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(val dao: SettingDao) : ViewModel() {
    var newSettingName by mutableStateOf("")
    var newSettingDay by mutableStateOf(0)
    var newSettingBoolean by mutableStateOf(false)

    // 使用 Flow 來持有設定資料，並讓 UI 更新
    val settingList: StateFlow<List<SettingTable>> = dao.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 預設食材資料
    private val defaultSettings = listOf(
        SettingTable(settingDay = 7, settingName = "豆芽菜", settingNotify = true),
        SettingTable(settingDay = 14, settingName = "牛肉", settingNotify = true),
        SettingTable(settingDay = 5, settingName = "花椰菜", settingNotify = true),
        SettingTable(settingDay = 9, settingName = "高麗菜", settingNotify = true),
        SettingTable(settingDay = 2, settingName = "蛤蜊", settingNotify = false),
        SettingTable(settingDay = 5, settingName = "鱈魚", settingNotify = false),
        SettingTable(settingDay = 28, settingName = "雞蛋", settingNotify = false),
        SettingTable(settingDay = 7, settingName = "茄子", settingNotify = false),
        SettingTable(settingDay = 30, settingName = "洋蔥", settingNotify = false),
        SettingTable(settingDay = 12, settingName = "紅蘿蔔", settingNotify = false),
        SettingTable(settingDay = 5, settingName = "豆腐", settingNotify = false),
        SettingTable(settingDay = 5, settingName = "魚", settingNotify = false),
        SettingTable(settingDay = 9, settingName = "番茄", settingNotify = false)
    )

    init {
        loadDefaultSettings()
    }

    // 載入預設食材，如果資料庫中沒有則插入
    private fun loadDefaultSettings() {
        viewModelScope.launch {
            // 使用 collect() 以獲得實時資料
            val existingSettings = dao.getAllUsers().first()
            val existingNames = existingSettings.map { it.settingName }
            val newSettings = defaultSettings.filter { it.settingName !in existingNames }

            // 將新設定插入資料庫
            newSettings.forEach { dao.insert(it) }
        }
    }

    // 更新食材設定
    fun updateSettingItem(setting: SettingTable) {
        viewModelScope.launch {
            dao.update(setting)
        }
    }

    // 刪除食材設定
    fun deleteSettingItem(setting: SettingTable) {
        viewModelScope.launch {
            dao.delete(setting)
        }
    }
}
