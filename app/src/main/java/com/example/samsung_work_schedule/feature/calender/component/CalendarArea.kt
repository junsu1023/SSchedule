
package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.LocalDate
import java.time.YearMonth

@Immutable
data class ScheduleState(
    val schedules: Map<LocalDate, WorkType>
)

@Composable
fun CalendarArea(
    pagerState: PagerState,
    baseMonth: YearMonth,
    initialPage: Int,
    workSchedules: List<WorkSchedule>,
    selectedDate: LocalDate? = null,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    isDialog: Boolean = false,
    onDateClick: (LocalDate) -> Unit = {}
) {
    val scheduleState = remember(workSchedules) {
        ScheduleState(workSchedules.associateBy({ it.date }, { it.type }))
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 16.dp,
        contentPadding = PaddingValues(horizontal = 24.dp),
        beyondViewportPageCount = 1
    ) { page ->
        val displayMonth = remember(page, baseMonth, initialPage) {
            baseMonth.plusMonths((page - initialPage).toLong())
        }

        MonthCalendar(
            modifier = Modifier.fillMaxWidth(),
            yearMonth = displayMonth,
            scheduleState = scheduleState,
            selectedDate = selectedDate,
            startDate = startDate,
            endDate = endDate,
            isDialog = isDialog,
            onDateClick = onDateClick
        )
    }
}

@Composable
fun MonthCalendar(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    scheduleState: ScheduleState,
    selectedDate: LocalDate? = null,
    startDate: LocalDate? = null,
    endDate: LocalDate? = null,
    isDialog: Boolean = false,
    onDateClick: (LocalDate) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.border(
            width = 0.5.dp,
            color = ScheduleTheme.colors.gridColor,
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Column {
            CalendarHeader()

            HorizontalDivider(color = ScheduleTheme.colors.gridColor)

            // Cache calendar date calculations
            val calendarDates = remember(yearMonth) {
                val firstDayOfMonth = yearMonth.atDay(1)
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                val daysInMonth = yearMonth.lengthOfMonth()
                val prevMonth = yearMonth.minusMonths(1)
                val prevMonthDays = prevMonth.lengthOfMonth()

                List(42) { cellIndex ->
                    val dayNumRaw = cellIndex - firstDayOfWeek + 1
                    when {
                        dayNumRaw < 1 -> Pair(prevMonth.atDay(prevMonthDays + dayNumRaw), false)
                        dayNumRaw > daysInMonth -> Pair(yearMonth.plusMonths(1).atDay(dayNumRaw - daysInMonth), false)
                        else -> Pair(yearMonth.atDay(dayNumRaw), true)
                    }
                }
            }

            val today = remember { LocalDate.now() }
            val currentOnDateClick by rememberUpdatedState(onDateClick)

            repeat(6) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    for (columnIndex in 0 until 7) {
                        val cellIndex = rowIndex * 7 + columnIndex
                        val (date, isCurrentMonthDay) = calendarDates[cellIndex]

                        key(date) {
                            val isToday = isCurrentMonthDay && date == today
                            val isSelected = date == selectedDate
                            val isInRange = startDate != null && endDate != null &&
                                    !date.isBefore(startDate) && !date.isAfter(endDate)
                            val workType = scheduleState.schedules[date]
                            val onCellClick = remember(date) {
                                { currentOnDateClick(date) }
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                DayCell(
                                    modifier = Modifier.fillMaxSize(),
                                    day = date.dayOfMonth.toString(),
                                    workType = workType,
                                    isToday = isToday,
                                    isSelected = isSelected,
                                    isInRange = isInRange,
                                    isStartDate = date == startDate,
                                    isCurrentMonth = isCurrentMonthDay,
                                    isDialog = isDialog,
                                    onClick = onCellClick
                                )

                                if (columnIndex < 6) {
                                    VerticalDivider(
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .fillMaxHeight(),
                                        color = ScheduleTheme.colors.gridColor
                                    )
                                }
                            }
                        }
                    }
                }

                if (rowIndex < 5) {
                    HorizontalDivider(color = ScheduleTheme.colors.gridColor)
                }
            }
        }
    }
}

@Composable
fun CalendarHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(47.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val days = remember { listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT") }
        days.forEachIndexed { index, day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    color = ScheduleTheme.colors.textColor4,
                    fontWeight = FontWeight.Bold
                )
            )
            if (index < 6) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = ScheduleTheme.colors.gridColor
                )
            }
        }
    }
}

@Composable
fun DayCell(
    modifier: Modifier = Modifier,
    day: String,
    workType: WorkType?,
    isToday: Boolean,
    isSelected: Boolean = false,
    isInRange: Boolean = false,
    isStartDate: Boolean = false,
    isCurrentMonth: Boolean,
    isDialog: Boolean = false,
    onClick: () -> Unit
) {
    val colors = ScheduleTheme.colors
    val backgroundColor = remember(isStartDate, isInRange, isSelected, isToday, isCurrentMonth, colors) {
        when {
            isStartDate -> colors.paleRed
            isInRange -> colors.paleRed.copy(alpha = 0.7f)
            isSelected -> colors.containerColor1.copy(alpha = 0.15f)
            isToday -> colors.todayBackground
            !isCurrentMonth -> colors.dayBackground1
            else -> colors.dayBackground2
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(backgroundColor)
            .then(
                if (isSelected || isStartDate) {
                    val borderColor = if (isStartDate) colors.borderColor1 else colors.containerColor1
                    Modifier.border(1.5.dp, borderColor)
                } else Modifier
            )
            .clickable(onClick = onClick)
    ) {
        if (day.isNotEmpty()) {
            if (isToday && !isDialog) {
                TodayBadge(modifier = Modifier.align(Alignment.TopEnd))
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1.2f))

                Text(
                    text = day,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = if (isSelected || isToday) ScheduleTheme.colors.containerColor1
                        else if (isCurrentMonth) ScheduleTheme.colors.textColor7
                        else ScheduleTheme.colors.textColor6,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
                    )
                )

                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (isCurrentMonth && !isDialog && workType != null && workType != WorkType.NONE) {
                        WorkDot(type = workType)
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayBadge(modifier: Modifier = Modifier) {
    Surface(
        color = ScheduleTheme.colors.containerColor1,
        shape = RoundedCornerShape(bottomStart = 8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.today),
            style = TextStyle(
                fontSize = 9.sp,
                color = ScheduleTheme.colors.textColor5,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
            maxLines = 1
        )
    }
}

@Composable
private fun WorkDot(type: WorkType) {
    if (type == WorkType.NONE) return

    val dotColor = when (type) {
        WorkType.DAY -> ScheduleTheme.colors.day
        WorkType.SW -> ScheduleTheme.colors.sw
        WorkType.GY -> ScheduleTheme.colors.gy
        WorkType.OFFICE -> ScheduleTheme.colors.office
        WorkType.OFF -> ScheduleTheme.colors.off
        WorkType.NONE -> ScheduleTheme.colors.transparent
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .background(dotColor, CircleShape)
        )
    }
}