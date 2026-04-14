package com.example.samsung_work_schedule.feature.calender.state

import com.example.domain.model.WorkSchedule
import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val workSchedules: List<WorkSchedule> = emptyList(),
    val selectedDateForDetail: LocalDate? = null,
    val isBottomSheetVisible: Boolean = false
)