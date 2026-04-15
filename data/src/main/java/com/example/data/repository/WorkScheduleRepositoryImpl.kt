package com.example.data.repository

import com.example.data.local.dao.WorkScheduleDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.model.WorkSchedule
import com.example.domain.repository.WorkScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class WorkScheduleRepositoryImpl @Inject constructor(
    private val dao: WorkScheduleDao
) : WorkScheduleRepository {
    override fun getWorkSchedules(startDate: LocalDate, endDate: LocalDate): Flow<List<WorkSchedule>> {
        return dao.getWorkSchedules(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getWorkSchedule(date: LocalDate): WorkSchedule? {
        return dao.getWorkSchedule(date)?.toDomain()
    }

    override suspend fun saveWorkSchedules(workSchedules: List<WorkSchedule>) {
        dao.insertWorkSchedules(workSchedules.map { it.toEntity() })
    }

    override suspend fun deleteWorkSchedules(startDate: LocalDate, endDate: LocalDate) {
        dao.deleteWorkSchedules(startDate, endDate)
    }
}