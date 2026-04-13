package com.example.domain.usecase

import com.example.domain.model.WorkSchedule
import com.example.domain.repository.WorkScheduleRepository
import javax.inject.Inject

class SaveWorkSchedulesUseCase @Inject constructor(
    private val repository: WorkScheduleRepository
) {
    suspend operator fun invoke(workSchedules: List<WorkSchedule>) {
        repository.saveWorkSchedules(workSchedules)
    }
}
