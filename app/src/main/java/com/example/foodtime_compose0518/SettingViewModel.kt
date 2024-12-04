package com.example.foodtime_compose0518

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(val dao: SettingDao) : ViewModel() {
    var newSettingName by mutableStateOf("")
    var newSettingDay by mutableStateOf(0)
    var newSettingBoolean by mutableStateOf(false)

    val settingList: StateFlow<List<SettingTable>> = dao.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSetting() {
        viewModelScope.launch {
            dao.insert(SettingTable(settingName = newSettingName, settingDay = newSettingDay, settingNotify = newSettingBoolean))
        }
    }

    fun updateSettingItem(setting: SettingTable) {
        viewModelScope.launch { dao.update(setting) }
    }

    fun deleteSettingItem(setting: SettingTable) {
        viewModelScope.launch { dao.delete(setting) }
    }
}
