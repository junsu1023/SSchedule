package com.example.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun isDarkModeEnabled(): Flow<Boolean>

    suspend fun setDarkModeEnabled(enabled: Boolean)

    fun isNotificationsEnabled(): Flow<Boolean>

    suspend fun setNotificationsEnabled(enabled: Boolean)
}