package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme

@Composable
fun CalendarHeader(month: Int) {
    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.current_month, month),
        style = TextStyle(
            fontSize = 48.sp,
            color = ScheduleTheme.colors.textColor3,
            fontWeight = FontWeight.ExtraBold
        )
    )

    Spacer(modifier = Modifier.height(8.dp))

    KindRow()
}

@Composable
fun KindRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        KindItem(color = ScheduleTheme.colors.day, label = stringResource(R.string.day))
        KindItem(color = ScheduleTheme.colors.sw, label = stringResource(R.string.sw))
        KindItem(color = ScheduleTheme.colors.gy, label = stringResource(R.string.gy))
        KindItem(color = ScheduleTheme.colors.office, label = stringResource(R.string.office))
        KindItem(color = ScheduleTheme.colors.off, label = stringResource(R.string.off))
    }
}

@Composable
fun KindItem(color: Color, label: String) {
    Surface(
        shape = CircleShape,
        color = color.copy(alpha = 0.1f),
        modifier = Modifier.height(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape)
            )

            Text(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}