package com.example.domain.usecase

import com.example.domain.repository.SettingRepository
import javax.inject.Inject

class SetDarkModeUseCase @Inject constructor(
    private val repository: SettingRepository
) {
    suspend operator fun invoke(enabled: Boolean) = repository.setDarkModeEnabled(enabled)
}