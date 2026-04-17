package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.domain.repository.SettingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import androidx.core.content.edit

class SettingRepositoryImpl(private val context: Context) : SettingRepository {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS = "notifications"
    }

    override fun isDarkModeEnabled(): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_DARK_MODE) {
                trySend(prefs.getBoolean(KEY_DARK_MODE, false))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getBoolean(KEY_DARK_MODE, false))
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun setDarkModeEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_DARK_MODE, enabled) }
    }

    override fun isNotificationsEnabled(): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_NOTIFICATIONS) {
                trySend(prefs.getBoolean(KEY_NOTIFICATIONS, true))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true))
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_NOTIFICATIONS, enabled) }
    }
}