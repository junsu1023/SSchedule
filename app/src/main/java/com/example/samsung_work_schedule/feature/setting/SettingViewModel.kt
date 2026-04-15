package com.example.samsung_work_schedule.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.*
import com.example.samsung_work_schedule.feature.setting.state.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val setDarkModeUseCase: SetDarkModeUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val setNotificationsUseCase: SetNotificationsUseCase
) : ViewModel() {

    val uiState: StateFlow<SettingUiState> = combine(
        getDarkModeUseCase(),
        getNotificationsUseCase()
    ) { darkMode, notifications ->
        SettingUiState(
            isDarkModeEnabled = darkMode,
            isNotificationsEnabled = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingUiState()
    )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            setDarkModeUseCase(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            setNotificationsUseCase(enabled)
        }
    }
}