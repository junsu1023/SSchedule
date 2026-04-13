package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.example.samsung_work_schedule.theme.ScheduleTheme

@Composable
fun FloatingActionButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = ScheduleTheme.colors.containerColor1,
        contentColor = ScheduleTheme.colors.iconColor3,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "추가 버튼"
        )
    }
}