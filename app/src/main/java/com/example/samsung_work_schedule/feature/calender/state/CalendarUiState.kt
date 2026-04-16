package com.example.samsung_work_schedule.feature.calender.state

import androidx.compose.runtime.Immutable
import com.example.domain.model.WorkSchedule
import java.time.LocalDate
import java.time.YearMonth

@Immutable
data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val workSchedules: List<WorkSchedule> = emptyList(),
    val selectedDateForDetail: LocalDate? = null,
    val isBottomSheetVisible: Boolean = false
)