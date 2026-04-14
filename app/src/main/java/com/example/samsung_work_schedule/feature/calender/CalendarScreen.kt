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
import com.example.domain.model.WorkType
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.feature.calender.component.*
import com.example.samsung_work_schedule.feature.calender.viewmodel.CalendarViewModel
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {
    val totalPageCount = Int.MAX_VALUE
    val initialPage = totalPageCount / 2
    val pagerState = rememberPagerState(pageCount = { totalPageCount }, initialPage = initialPage)
    val baseMonth = remember { YearMonth.now() }
    val currentYearMonth = baseMonth.plusMonths((pagerState.currentPage - initialPage).toLong())
    val showBottomSheet = remember { mutableStateOf(false) }
    val showDetailDialog = remember { mutableStateOf(false) }
    val workSchedules by viewModel.workSchedules.collectAsState()

    LaunchedEffect(currentYearMonth) {
        viewModel.onMonthChanged(currentYearMonth)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { TopBarTitle(currentYearMonth) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = ScheduleTheme.colors.background1)
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showBottomSheet.value = true })
            },
            containerColor = ScheduleTheme.colors.background1,
            modifier = Modifier.blur(if(showBottomSheet.value || showDetailDialog.value) 10.dp else 0.dp)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .background(ScheduleTheme.colors.background2),
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
                        workSchedules = workSchedules,
                        onDateClick = { showDetailDialog.value = true }
                    )
                }

                item {
                    NextShiftCard(onDetailClick = { showDetailDialog.value = true })

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (showBottomSheet.value) {
            ShiftEntrySheet(
                onDismiss = { showBottomSheet.value = false },
                onSave = { startDate, endDate, shiftType ->
                    viewModel.saveSchedule(startDate, endDate, WorkType.valueOf(shiftType))
                    showBottomSheet.value = false
                }
            )
        }

        if(showDetailDialog.value) {
            ShiftDetailDialog(
                onDismiss = { showDetailDialog.value = false },
                onSave = { showDetailDialog.value = false },
                onDelete = { /* TODO */ }
            )
        }
    }
}

@Composable
fun NextShiftCard(onDetailClick: () -> Unit) {
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
                        text = "주간\n근무",
                        style = TextStyle(
                            fontSize = 38.sp,
                            color = ScheduleTheme.colors.textColor5,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "08:00 -\n16:00",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = ScheduleTheme.colors.textColor5
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "내일 • 일반 병동 B",
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