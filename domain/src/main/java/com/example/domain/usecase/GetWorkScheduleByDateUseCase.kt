package com.example.domain.usecase

import com.example.domain.model.WorkSchedule
import com.example.domain.repository.WorkScheduleRepository
import java.time.LocalDate
import javax.inject.Inject

class GetWorkScheduleByDateUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(date: LocalDate): WorkSchedule? {
        return repository.getWorkSchedule(date)
    }
}