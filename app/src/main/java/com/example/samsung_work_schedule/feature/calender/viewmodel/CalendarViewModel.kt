package com.example.samsung_work_schedule.feature.calender.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.domain.usecase.DeleteWorkSchedulesUseCase
import com.example.domain.usecase.GetWorkSchedulesUseCase
import com.example.domain.usecase.SaveWorkSchedulesUseCase
import com.example.samsung_work_schedule.feature.calender.state.CalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getWorkSchedulesUseCase: GetWorkSchedulesUseCase,
    private val saveWorkSchedulesUseCase: SaveWorkSchedulesUseCase,
    private val deleteWorkSchedulesUseCase: DeleteWorkSchedulesUseCase
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    private val _selectedDateForDetail = MutableStateFlow<LocalDate?>(null)
    private val _isBottomSheetVisible = MutableStateFlow(false)

    val uiState: StateFlow<CalendarUiState> = combine(
        _currentMonth,
        _selectedDateForDetail,
        _isBottomSheetVisible,
        _currentMonth.flatMapLatest { month ->
            val start = month.atDay(1).minusWeeks(1)
            val end = month.atEndOfMonth().plusWeeks(1)
            getWorkSchedulesUseCase(start, end)
        }
    ) { month, selectedDate, isVisible, schedules ->
        CalendarUiState(
            currentMonth = month,
            workSchedules = schedules,
            selectedDateForDetail = selectedDate,
            isBottomSheetVisible = isVisible
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun onMonthChanged(newMonth: YearMonth) {
        _currentMonth.value = newMonth
    }

    fun onDateSelected(date: LocalDate?) {
        _selectedDateForDetail.value = date
    }

    fun setBottomSheetVisible(visible: Boolean) {
        _isBottomSheetVisible.value = visible
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
