package com.example.samsung_work_schedule.feature.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.feature.calender.component.*
import com.example.samsung_work_schedule.feature.calender.viewmodel.CalendarViewModel
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val totalPageCount = Int.MAX_VALUE
    val initialPage = totalPageCount / 2
    val pagerState = rememberPagerState(pageCount = { totalPageCount }, initialPage = initialPage)
    val baseMonth = remember { YearMonth.now() }
    val currentYearMonth = baseMonth.plusMonths((pagerState.currentPage - initialPage).toLong())

    LaunchedEffect(currentYearMonth) {
        viewModel.onMonthChanged(currentYearMonth)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = ScheduleTheme.colors.background2,
            topBar = {
                TopAppBar(
                    title = { TopBarTitle(currentYearMonth) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ScheduleTheme.colors.background2)
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.setBottomSheetVisible(true) })
            },
            modifier = Modifier.blur(if(uiState.isBottomSheetVisible || uiState.selectedDateForDetail != null) 10.dp else 0.dp)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScheduleTheme.colors.background2),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item {
                    CalendarHeader(currentYearMonth.monthValue)
                }

                item {
                    CalendarArea(
                        pagerState = pagerState,
                        baseMonth = baseMonth,
                        initialPage = initialPage,
                        workSchedules = uiState.workSchedules,
                        onDateClick = { viewModel.onDateSelected(it) }
                    )
                }

                item {
                    val todaySchedule = uiState.workSchedules.find { it.date == LocalDate.now() }

                    TodayShiftCard(
                        schedule = todaySchedule,
                        onDetailClick = { viewModel.onDateSelected(LocalDate.now()) }
                    )
                }
            }
        }

        if (uiState.isBottomSheetVisible) {
            ShiftEntrySheet(
                onDismiss = { viewModel.setBottomSheetVisible(false) },
                onSave = { startDate, endDate, shiftType ->
                    val type = WorkType.valueOf(shiftType)
                    var current = startDate

                    while (!current.isAfter(endDate)) {
                        val existingSchedule = uiState.workSchedules.find { it.date == current }

                        viewModel.saveSchedule(current, current, type, existingSchedule?.note ?: "")
                        current = current.plusDays(1)
                    }

                    viewModel.setBottomSheetVisible(false)
                }
            )
        }

        uiState.selectedDateForDetail?.let { date ->
            val schedule = uiState.workSchedules.find { it.date == date }

            ShiftDetailDialog(
                date = date,
                schedule = schedule,
                onDismiss = { viewModel.onDateSelected(null) },
                onSave = { type, notes ->
                    viewModel.saveSchedule(date, date, type, notes)
                    viewModel.onDateSelected(null)
                },
                onDelete = {
                    viewModel.deleteSchedule(date, date)
                    viewModel.onDateSelected(null)
                }
            )
        }
    }
}

@Composable
fun TodayShiftCard(
    schedule: WorkSchedule?,
    onDetailClick: () -> Unit
) {
    val (shiftTitle, shiftTime) = when (schedule?.type) {
        WorkType.DAY -> "DAY" to "06:00 ~\n14:00"
        WorkType.SW -> "SW" to "14:00 ~\n22:00"
        WorkType.GY -> "GY" to "22:00 ~\n06:00"
        WorkType.OFF -> "OFF" to "00:00 ~\n00:00"
        else -> "정보 없음" to "00:00 ~\n00:00"
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0066FF), Color(0xFF0044CC))
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.work_info),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = ScheduleTheme.colors.textColor5.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = shiftTitle,
                        style = TextStyle(
                            fontSize = 38.sp,
                            color = ScheduleTheme.colors.textColor5,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = shiftTime,
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = ScheduleTheme.colors.textColor5
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val noteText = if (schedule?.note.isNullOrBlank()) {
                    "오늘 • 메모 없음"
                } else {
                    "오늘 • ${schedule.note}"
                }

                Text(
                    text = noteText,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = ScheduleTheme.colors.textColor5
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDetailClick,
                    colors = ButtonDefaults.buttonColors(containerColor = ScheduleTheme.colors.background1.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.detail),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ScheduleTheme.colors.textColor5,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}