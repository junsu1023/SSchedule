package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.LocalDate
import java.time.YearMonth
import com.example.samsung_work_schedule.R
import androidx.compose.foundation.clickable

import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType

@Composable
fun CalendarArea(
    pagerState: PagerState,
    baseMonth: YearMonth,
    initialPage: Int,
    workSchedules: List<WorkSchedule>,
    selectedDate: LocalDate? = null,
    isDialog: Boolean = false,
    onDateClick: (LocalDate) -> Unit = {}
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth(),
        pageSpacing = 16.dp,
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) { page ->
        val displayMonth = baseMonth.plusMonths((page - initialPage).toLong())

        MonthCalendar(
            modifier = Modifier.fillMaxWidth(),
            yearMonth = displayMonth,
            workSchedules = workSchedules,
            selectedDate = selectedDate,
            isDialog = isDialog,
            onDateClick = onDateClick
        )
    }
}

@Composable
fun MonthCalendar(
    modifier: Modifier = Modifier,
    yearMonth: YearMonth,
    workSchedules: List<WorkSchedule>,
    selectedDate: LocalDate? = null,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEachIndexed { index, day ->
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

            HorizontalDivider(color = ScheduleTheme.colors.gridColor)

            val firstDayOfMonth = yearMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
            val daysInMonth = yearMonth.lengthOfMonth()
            val prevMonth = yearMonth.minusMonths(1)
            val prevMonthDays = prevMonth.lengthOfMonth()
            val today = LocalDate.now()

            repeat(6) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    for (columnIndex in 0 until 7) {
                        val cellIndex = rowIndex * 7 + columnIndex
                        val dayNumRaw = cellIndex - firstDayOfWeek + 1
                        val isCurrentMonthDay = dayNumRaw in 1..daysInMonth

                        val date = when {
                            dayNumRaw < 1 -> prevMonth.atDay(prevMonthDays + dayNumRaw)
                            dayNumRaw > daysInMonth -> yearMonth.plusMonths(1).atDay(dayNumRaw - daysInMonth)
                            else -> yearMonth.atDay(dayNumRaw)
                        }

                        val isToday = isCurrentMonthDay && date == today
                        val isSelected = date == selectedDate

                        Box(modifier = Modifier.weight(1f)) {
                            val schedule = workSchedules.find { it.date == date }

                            DayCell(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onDateClick(date) },
                                day = date.dayOfMonth.toString(),
                                schedule = schedule,
                                isToday = isToday,
                                isSelected = isSelected,
                                isCurrentMonth = isCurrentMonthDay,
                                isDialog = isDialog
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
                if (rowIndex < 5) {
                    HorizontalDivider(color = ScheduleTheme.colors.gridColor)
                }
            }
        }
    }
}

@Composable
fun DayCell(
    modifier: Modifier = Modifier,
    day: String,
    schedule: WorkSchedule?,
    isToday: Boolean,
    isSelected: Boolean = false,
    isCurrentMonth: Boolean,
    isDialog: Boolean = false
) {
    val backgroundColor = when {
        isSelected -> ScheduleTheme.colors.containerColor1.copy(alpha = 0.15f)
        isToday -> ScheduleTheme.colors.todayBackground
        !isCurrentMonth -> ScheduleTheme.colors.dayBackground1
        else -> ScheduleTheme.colors.dayBackground2
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(backgroundColor)
            .then(
                if (isSelected) Modifier.border(1.5.dp, ScheduleTheme.colors.containerColor1)
                else Modifier
            )
    ) {
        if (day.isNotEmpty()) {
            if (isToday && !isDialog) {
                Surface(
                    color = ScheduleTheme.colors.containerColor1,
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
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
                    if (isCurrentMonth && !isDialog) {
                        val dotColor = when (schedule?.type) {
                            WorkType.DAY -> ScheduleTheme.colors.day
                            WorkType.SW -> ScheduleTheme.colors.sw
                            WorkType.GY -> ScheduleTheme.colors.gy
                            WorkType.OFF -> ScheduleTheme.colors.off
                            else -> null
                        }

                        dotColor?.let {
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(it, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}
