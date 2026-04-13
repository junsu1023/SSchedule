package com.example.domain.model

import java.time.LocalDate

data class WorkSchedule(
    val date: LocalDate,
    val type: WorkType
)
