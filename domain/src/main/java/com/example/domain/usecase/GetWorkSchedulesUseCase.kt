package com.example.domain.usecase

import com.example.domain.model.WorkSchedule
import com.example.domain.repository.WorkScheduleRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetWorkSchedulesUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<List<WorkSchedule>> {
        return repository.getWorkSchedules(startDate, endDate)
    }
}
