
package com.example.samsung_work_schedule.feature.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samsung_work_schedule.feature.calender.component.CalendarArea
import com.example.samsung_work_schedule.feature.calender.component.FloatingActionButton
import com.example.samsung_work_schedule.feature.calender.component.TopBarTitle
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.YearMonth
import com.example.samsung_work_schedule.feature.calender.component.CalendarHeader
import com.example.samsung_work_schedule.R
import androidx.compose.runtime.*
import com.example.samsung_work_schedule.feature.calender.component.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val totalPageCount = Int.MAX_VALUE
    val initialPage = totalPageCount / 2
    val pagerState = rememberPagerState(pageCount = { totalPageCount }, initialPage = initialPage)
    val baseMonth = remember { YearMonth.now() }
    val currentYearMonth = baseMonth.plusMonths((pagerState.currentPage - initialPage).toLong())
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { TopBarTitle(currentYearMonth) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScheduleTheme.colors.background1)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true })
        },
        containerColor = ScheduleTheme.colors.background1
    ) { paddingValues ->
        if (showBottomSheet) {
            ShiftEntrySheet(
                onDismiss = { showBottomSheet = false },
                onSave = {
                    // TODO: 저장 로직 구현
                    showBottomSheet = false
                }
            )
        }

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
                    initialPage = initialPage
                )
            }

            item {
                NextShiftCard()

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun NextShiftCard() {
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
                    onClick = { /* TODO */ },
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