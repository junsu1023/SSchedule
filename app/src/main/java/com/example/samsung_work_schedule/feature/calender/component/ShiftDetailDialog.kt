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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme

@Composable
fun ShiftDetailDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    val text = remember { mutableStateOf("") }

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

                ShiftBadge(label = stringResource(R.string.day_shift), color = ScheduleTheme.colors.day)

                Spacer(modifier = Modifier.height(32.dp))

                DetailItem(
                    icon = Icons.Default.CalendarToday,
                    label = stringResource(R.string.date),
                    value = "2026년 10월 13일"
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProgressDetailItem(
                    icon = Icons.Default.AccessTime,
                    label = stringResource(R.string.hours),
                    value = "08:00 — 16:00",
                    badgeText = "8.0 시간",
                    progress = 0.7f
                )

                Spacer(modifier = Modifier.height(32.dp))

                SectionHeader(icon = Icons.AutoMirrored.Outlined.StickyNote2, label = stringResource(R.string.notes))

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    placeholder = { Text(stringResource(R.string.notes_placeholder), fontSize = 14.sp, color = Color.LightGray) },
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

                ActionButtons(onDelete = onDelete, onSave = onSave)
            }
        }
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
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ScheduleTheme.colors.textColor3
            )
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .background(ScheduleTheme.colors.iconColor4, CircleShape)
                .size(36.dp)
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
private fun ShiftBadge(label: String, color: Color) {
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
private fun ProgressDetailItem(icon: ImageVector, label: String, value: String, badgeText: String, progress: Float) {
    Column {
        DetailItem(icon = icon, label = label, value = value, badgeText = badgeText)
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().padding(start = 56.dp).height(6.dp),
            color = ScheduleTheme.colors.containerColor1,
            trackColor = ScheduleTheme.colors.surfaceColor1,
            strokeCap = StrokeCap.Round
        )
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
            modifier = Modifier.weight(1f).height(52.dp),
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
                fontSize = 16.sp
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f).height(52.dp),
            shape = RoundedCornerShape(36.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ScheduleTheme.colors.primary)
        ) {
            Text(
                text = stringResource(R.string.save_changes),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}