package com.example.domain.usecase

import com.example.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val repository: SettingRepository
) {
    operator fun invoke(): Flow<Boolean> = repository.isDarkModeEnabled()
}