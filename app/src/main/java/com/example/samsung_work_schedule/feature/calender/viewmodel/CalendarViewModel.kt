package com.example.samsung_work_schedule.feature.calender.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.domain.usecase.DeleteWorkSchedulesUseCase
import com.example.domain.usecase.GetWorkSchedulesUseCase
import com.example.domain.usecase.SaveWorkSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getWorkSchedulesUseCase: GetWorkSchedulesUseCase,
    private val saveWorkSchedulesUseCase: SaveWorkSchedulesUseCase,
    private val deleteWorkSchedulesUseCase: DeleteWorkSchedulesUseCase
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val workSchedules: StateFlow<List<WorkSchedule>> = _currentMonth
        .flatMapLatest { month ->
            val start = month.atDay(1).minusWeeks(1)
            val end = month.atEndOfMonth().plusWeeks(1)
            getWorkSchedulesUseCase(start, end)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onMonthChanged(newMonth: YearMonth) {
        _currentMonth.value = newMonth
    }

    fun saveSchedule(startDate: LocalDate, endDate: LocalDate, type: WorkType, note: String = "") {
        viewModelScope.launch {
            val schedules = mutableListOf<WorkSchedule>()
            var current = startDate

            while(!current.isAfter(endDate)) {
                schedules.add(WorkSchedule(current, type, note))
                current = current.plusDays(1)
            }

            saveWorkSchedulesUseCase(schedules)
        }
    }

    fun deleteSchedule(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            deleteWorkSchedulesUseCase(startDate, endDate)
        }
    }
}
