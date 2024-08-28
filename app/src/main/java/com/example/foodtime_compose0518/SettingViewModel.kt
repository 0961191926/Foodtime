package com.example.foodtime_compose0518

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SettingViewModel(val dao: SettingDao) : ViewModel() {
    var newSettingName = ""
    var newSettingDay = 0
    var newSettingBoolean = false

    val settingList: Flow<List<SettingTable>> = dao.getAllUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSettingName(name: String) {
        newSettingName = name
    }

    fun setSettingDay(day: Int) {
        newSettingDay = day
    }

    fun setSettingBoolean(notify: Boolean) {
        newSettingBoolean = notify
    }

    fun addSetting() {
        viewModelScope.launch {
            dao.insert(
                SettingTable(
                    settingName = newSettingName,
                    settingDay = newSettingDay,
                    settingNotify =  newSettingBoolean
                )
            )
        }
    }

    fun updateNormalItem(setting: SettingTable) {
        viewModelScope.launch {
            dao.update(setting)
        }
    }

    fun deleteNormalItem(setting: SettingTable) {
        viewModelScope.launch {
            dao.delete(setting)
        }
    }
}