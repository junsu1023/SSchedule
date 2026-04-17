package com.example.domain.usecase

import com.example.domain.repository.WorkScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

class DeleteWorkSchedulesUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(startDate: LocalDate, endDate: LocalDate) {
        repository.deleteWorkSchedules(startDate, endDate)
    }
}
