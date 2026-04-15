package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.WorkScheduleEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkScheduleDao {
    @Query("SELECT * FROM work_schedules WHERE date BETWEEN :startDate AND :endDate")
    fun getWorkSchedules(startDate: LocalDate, endDate: LocalDate): Flow<List<WorkScheduleEntity>>

    @Query("SELECT * FROM work_schedules WHERE date = :date LIMIT 1")
    suspend fun getWorkSchedule(date: LocalDate): WorkScheduleEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertWorkSchedules(workSchedules: List<WorkScheduleEntity>)

    @Query("DELETE FROM work_schedules WHERE date BETWEEN :startDate AND :endDate")
    suspend fun deleteWorkSchedules(startDate: LocalDate, endDate: LocalDate)
}