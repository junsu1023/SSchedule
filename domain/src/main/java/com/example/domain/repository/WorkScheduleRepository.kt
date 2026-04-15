package com.example.domain.repository

import com.example.domain.model.WorkSchedule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkScheduleRepository {
    fun getWorkSchedules(startDate: LocalDate, endDate: LocalDate): Flow<List<WorkSchedule>>
    suspend fun getWorkSchedule(date: LocalDate): WorkSchedule?
    suspend fun saveWorkSchedules(workSchedules: List<WorkSchedule>)
    suspend fun deleteWorkSchedules(startDate: LocalDate, endDate: LocalDate)
}