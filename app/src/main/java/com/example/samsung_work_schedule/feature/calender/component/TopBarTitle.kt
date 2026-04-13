package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.YearMonth

@Composable
fun TopBarTitle(currentYearMonth: YearMonth) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(end = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "달력 아이콘",
                tint = ScheduleTheme.colors.iconColor1,
                modifier = Modifier.size(34.dp)
            )

            Text(
                text = stringResource(R.string.s_schedule),
                style = TextStyle(
                    fontSize = 20.sp,
                    color = ScheduleTheme.colors.textColor1,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Text(
            text = stringResource(R.string.current_year_month, currentYearMonth.year, currentYearMonth.monthValue),
            style = TextStyle(
                fontSize = 18.sp,
                color = ScheduleTheme.colors.textColor1,
                fontWeight = FontWeight.Bold
            )
        )
    }
}