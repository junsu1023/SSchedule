package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ShiftDetailDialog(
    date: LocalDate,
    schedule: WorkSchedule?,
    onDismiss: () -> Unit,
    onSave: (WorkType, String) -> Unit,
    onDelete: () -> Unit
) {
    val notes = remember(schedule) { mutableStateOf(schedule?.note ?: "") }
    val currentWorkType = remember(schedule) { mutableStateOf(schedule?.type ?: WorkType.NONE) }
    val showMenu = remember { mutableStateOf(false) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(40.dp),
            color = ScheduleTheme.colors.background1,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                DialogHeader(title = stringResource(R.string.shift_details), onDismiss = onDismiss)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ShiftBadge(currentWorkType.value)

                    Box {
                        IconButton(
                            onClick = { showMenu.value = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "근무 수정",
                                tint = ScheduleTheme.colors.textColor8,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false },
                            modifier = Modifier.background(ScheduleTheme.colors.background1)
                        ) {
                            listOf(WorkType.DAY, WorkType.SW, WorkType.GY, WorkType.OFF).forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = when(type) {
                                                WorkType.DAY -> stringResource(R.string.day)
                                                WorkType.SW -> stringResource(R.string.sw)
                                                WorkType.GY -> stringResource(R.string.gy)
                                                WorkType.OFF -> stringResource(R.string.off)
                                                else -> ""
                                            },
                                            style = TextStyle(
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = ScheduleTheme.colors.textColor3
                                            )
                                        )
                                    },
                                    onClick = {
                                        currentWorkType.value = type
                                        showMenu.value = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                DetailItem(
                    icon = Icons.Default.CalendarToday,
                    label = stringResource(R.string.date),
                    value = date.format(dateFormatter)
                )

                Spacer(modifier = Modifier.height(24.dp))

                DetailItem(
                    icon = Icons.Default.AccessTime,
                    label = stringResource(R.string.hours),
                    value = getWorkTimeRange(currentWorkType.value),
                    badgeText = if(currentWorkType.value == WorkType.OFF || currentWorkType.value == WorkType.NONE) "0.0 시간" else "8.0 시간"
                )

                Spacer(modifier = Modifier.height(32.dp))

                SectionHeader(icon = Icons.AutoMirrored.Outlined.StickyNote2, label = stringResource(R.string.notes))

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes.value,
                    onValueChange = { notes.value = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.notes_placeholder),
                            fontSize = 14.sp,
                            color = ScheduleTheme.colors.textColor4
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = ScheduleTheme.colors.surfaceColor1,
                        focusedContainerColor = ScheduleTheme.colors.surfaceColor1,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                ActionButtons(
                    onDelete = onDelete,
                    onSave = { onSave(currentWorkType.value, notes.value) }
                )
            }
        }
    }
}

@Composable
private fun ShiftBadge(workType: WorkType) {
    val label = when (workType) {
        WorkType.DAY -> stringResource(R.string.day)
        WorkType.SW -> stringResource(R.string.sw)
        WorkType.GY -> stringResource(R.string.gy)
        WorkType.OFF -> stringResource(R.string.off)
        WorkType.NONE -> stringResource(R.string.shift_none)
    }

    val color = when (workType) {
        WorkType.DAY -> ScheduleTheme.colors.day
        WorkType.SW -> ScheduleTheme.colors.sw
        WorkType.GY -> ScheduleTheme.colors.gy
        WorkType.OFF -> ScheduleTheme.colors.off
        WorkType.NONE -> ScheduleTheme.colors.textColor3
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).background(color, CircleShape))

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = label,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
        }
    }
}

private fun getWorkTimeRange(workType: WorkType): String {
    return when (workType) {
        WorkType.DAY -> "06:00 ~ 14:00"
        WorkType.SW -> "14:00 ~ 22:00"
        WorkType.GY -> "22:00 ~ 06:00"
        else -> "0:00 ~ 00:00"
    }
}

@Composable
private fun DialogHeader(title: String, onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ScheduleTheme.colors.textColor3)
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.background(ScheduleTheme.colors.iconColor4, CircleShape).size(36.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "닫기",
                modifier = Modifier.size(20.dp),
                tint = ScheduleTheme.colors.iconColor5
            )
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, label: String, value: String, badgeText: String? = null) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconBox(icon)

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ScheduleTheme.colors.textColor8
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ScheduleTheme.colors.textColor3
                    )
                )

                badgeText?.let {
                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = ScheduleTheme.colors.surfaceColor1,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconBox(icon: ImageVector) {
    Surface(
        modifier = Modifier.size(40.dp),
        shape = RoundedCornerShape(12.dp),
        color = ScheduleTheme.colors.indicatorColor1
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = ScheduleTheme.colors.iconColor1,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = ScheduleTheme.colors.textColor8,
            modifier = Modifier.size(20.dp
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = ScheduleTheme.colors.textColor8
            )
        )
    }
}

@Composable
private fun ActionButtons(onDelete: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onDelete,
            modifier = Modifier.weight(1f).height(58.dp),
            shape = RoundedCornerShape(36.dp),
            border = BorderStroke(1.dp, ScheduleTheme.colors.surfaceColor1)
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "삭제",
                tint = ScheduleTheme.colors.iconColor6
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.delete_shift),
                color = ScheduleTheme.colors.textColor9,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f).height(58.dp),
            shape = RoundedCornerShape(36.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ScheduleTheme.colors.buttonColor1)
        ) {
            Text(
                text = stringResource(R.string.save_changes),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}