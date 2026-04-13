package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun CustomDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val baseMonth = remember { YearMonth.from(initialDate) }
    val totalPageCount = 24000
    val initialPage = totalPageCount / 2
    val pagerState = rememberPagerState(pageCount = { totalPageCount }, initialPage = initialPage)
    val coroutineScope = rememberCoroutineScope()
    val currentYearMonth = baseMonth.plusMonths((pagerState.currentPage - initialPage).toLong())
    var tempSelectedDate by remember { mutableStateOf(initialDate) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = "전월")
                    }

                    Text(
                        text = stringResource(R.string.current_year_month, currentYearMonth.year, currentYearMonth.monthValue),
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = ScheduleTheme.colors.textColor3,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    IconButton(
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = "명월")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CalendarArea(
                    pagerState = pagerState,
                    baseMonth = baseMonth,
                    initialPage = initialPage,
                    workSchedules = emptyList(),
                    selectedDate = tempSelectedDate,
                    isDialog = true,
                    onDateClick = { tempSelectedDate = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(R.string.cancel),
                            color = ScheduleTheme.colors.textColor8
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = { onDateSelected(tempSelectedDate) }
                    ) {
                        Text(
                            text = "확인",
                            color = ScheduleTheme.colors.containerColor1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}