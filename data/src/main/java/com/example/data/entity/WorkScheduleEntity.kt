package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "work_schedules")
data class WorkScheduleEntity(
    @PrimaryKey val date: LocalDate,
    val type: String,
    val note: String = ""
)
