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
    val currentYearMonth by remember {
        derivedStateOf {
            baseMonth.plusMonths((pagerState.currentPage - initialPage).toLong())
        }
    }

    val onDateSelected = remember(viewModel) { { date: LocalDate? -> viewModel.onDateSelected(date) } }
    val onBottomSheetVisible = remember(viewModel) { { visible: Boolean -> viewModel.setBottomSheetVisible(visible) } }
    val onSaveSchedules = remember(viewModel) {
        { startDate: LocalDate, endDate: LocalDate, shiftType: String ->
            val type = try {
                WorkType.valueOf(shiftType)
            } catch (e: Exception) {
                WorkType.NONE
            }

            if (type != WorkType.NONE) {
                viewModel.saveSchedule(startDate, endDate, type, "")
            }
            viewModel.setBottomSheetVisible(false)
        }
    }
    val onSaveDetail = remember(viewModel) {
        { date: LocalDate, type: WorkType, notes: String ->
            viewModel.saveSchedule(date, date, type, notes)
            viewModel.onDateSelected(null)
        }
    }
    val onDeleteDetail = remember(viewModel) {
        { date: LocalDate ->
            viewModel.deleteSchedule(date, date)
            viewModel.onDateSelected(null)
        }
    }

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
                FloatingActionButton(onClick = { onBottomSheetVisible(true) })
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
                item(key = "header") {
                    CalendarHeader(currentYearMonth.monthValue)
                }

                item(key = "calendar") {
                    CalendarArea(
                        pagerState = pagerState,
                        baseMonth = baseMonth,
                        initialPage = initialPage,
                        workSchedules = uiState.workSchedules,
                        onDateClick = onDateSelected
                    )
                }

                item(key = "today_card") {
                    val today = remember { LocalDate.now() }
                    val todaySchedule = remember(uiState.workSchedules, today) {
                        uiState.workSchedules.find { it.date == today }
                    }

                    TodayShiftCard(
                        schedule = todaySchedule,
                        onDetailClick = { onDateSelected(today) }
                    )
                }
            }
        }

        if (uiState.isBottomSheetVisible) {
            ShiftEntrySheet(
                onDismiss = { onBottomSheetVisible(false) },
                onSave = onSaveSchedules
            )
        }

        uiState.selectedDateForDetail?.let { date ->
            val schedule = remember(uiState.workSchedules, date) {
                uiState.workSchedules.find { it.date == date }
            }

            ShiftDetailDialog(
                date = date,
                schedule = schedule,
                onDismiss = { onDateSelected(null) },
                onSave = { type, notes -> onSaveDetail(date, type, notes) },
                onDelete = { onDeleteDetail(date) }
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
        WorkType.OFFICE -> "OFFICE" to "08:00 ~\n17:00"
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