package com.example.samsung_work_schedule.feature.calender.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samsung_work_schedule.R
import com.example.samsung_work_schedule.theme.ScheduleTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class ShiftEntry(val entry: String) {
    DAY("DAY"), SW("SW"), GY("GY"), OFFICE("OFFICE"), OFF("OFF")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftEntrySheet(
    onDismiss: () -> Unit,
    onSave: (startDate: LocalDate, endDate: LocalDate, shiftType: String) -> Unit
) {
    val nextMonthFirstDay = remember { LocalDate.now().plusMonths(1).withDayOfMonth(1) }
    var selectedShift by remember { mutableStateOf(ShiftEntry.DAY.name) }
    var startDate by remember { mutableStateOf(nextMonthFirstDay) }
    var endDate by remember { mutableStateOf(nextMonthFirstDay.plusDays(5)) }
    var pickingDateFor by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        dragHandle = null,
        containerColor = ScheduleTheme.colors.containerColor2,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.new_shift_entry),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = ScheduleTheme.colors.textColor3
                        )
                    )

                    Text(
                        text = stringResource(R.string.configure_schedule_block_details),
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ScheduleTheme.colors.textColor8
                        )
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .background(color = ScheduleTheme.colors.iconColor4, shape = CircleShape)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        modifier = Modifier.size(18.dp),
                        tint = ScheduleTheme.colors.iconColor5
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                DateInputBox(
                    label = stringResource(R.string.start_date),
                    date = startDate,
                    onClick = { pickingDateFor = "START" },
                    modifier = Modifier.weight(1f),
                    containerColor = ScheduleTheme.colors.paleRed,
                    borderColor = ScheduleTheme.colors.borderColor1
                )

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                        .size(24.dp)
                        .background(ScheduleTheme.colors.containerColor1.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("~", color = ScheduleTheme.colors.textColor1, fontWeight = FontWeight.Bold)
                }

                DateInputBox(
                    label = stringResource(R.string.end_date),
                    date = endDate,
                    onClick = { pickingDateFor = "END" },
                    modifier = Modifier.weight(1f),
                    containerColor = ScheduleTheme.colors.containerColor1.copy(alpha = 0.1f),
                    borderColor = ScheduleTheme.colors.containerColor1.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.shift_assignment),
                style = TextStyle(
                    fontSize = 11.sp,
                    color = ScheduleTheme.colors.textColor8,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ShiftTypeCard(
                    label = ShiftEntry.DAY.entry,
                    icon = Icons.Default.WbSunny,
                    color = ScheduleTheme.colors.day,
                    isSelected = selectedShift == ShiftEntry.DAY.name,
                    onClick = { selectedShift = ShiftEntry.DAY.name },
                    modifier = Modifier.weight(1f)
                )

                ShiftTypeCard(
                    label = ShiftEntry.SW.entry,
                    icon = Icons.Default.Nightlight,
                    color = ScheduleTheme.colors.sw,
                    isSelected = selectedShift == ShiftEntry.SW.name,
                    onClick = { selectedShift = ShiftEntry.SW.name },
                    modifier = Modifier.weight(1f)
                )

                ShiftTypeCard(
                    label = ShiftEntry.GY.entry,
                    icon = Icons.Default.DarkMode,
                    color = ScheduleTheme.colors.gy,
                    isSelected = selectedShift == ShiftEntry.GY.name,
                    onClick = { selectedShift = ShiftEntry.GY.name },
                    modifier = Modifier.weight(1f)
                )

                ShiftTypeCard(
                    label = stringResource(R.string.office),
                    icon = Icons.Default.Work,
                    color = ScheduleTheme.colors.office,
                    isSelected = selectedShift == ShiftEntry.OFFICE.name,
                    onClick = { selectedShift = ShiftEntry.OFFICE.name },
                    modifier = Modifier.weight(1f)
                )

                ShiftTypeCard(
                    label = ShiftEntry.OFF.entry,
                    icon = Icons.Default.CalendarMonth,
                    color = ScheduleTheme.colors.off,
                    isSelected = selectedShift == ShiftEntry.OFF.name,
                    onClick = { selectedShift = ShiftEntry.OFF.name },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ScheduleTheme.colors.textColor8
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { onSave(startDate, endDate, selectedShift) },
                    colors = ButtonDefaults.buttonColors(containerColor = ScheduleTheme.colors.containerColor1),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(52.dp)
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ScheduleTheme.colors.textColor5
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (pickingDateFor != null) {
        val initialDate = if(pickingDateFor == "START") startDate else endDate

        CustomDatePickerDialog(
            initialDate = initialDate,
            startDate = if (pickingDateFor == "END") startDate else null,
            endDate = if (pickingDateFor == "START") endDate else null,
            onDateSelected = { selectedDate ->
                if (pickingDateFor == "START") {
                    startDate = selectedDate

                    if (endDate.isBefore(selectedDate)) {
                        endDate = selectedDate
                    }
                } else {
                    endDate = selectedDate

                    if (startDate.isAfter(selectedDate)) {
                        startDate = selectedDate
                    }
                }

                pickingDateFor = null
            },
            onDismiss = { pickingDateFor = null }
        )
    }
}

@Composable
fun DateInputBox(
    label: String,
    date: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = ScheduleTheme.colors.surfaceColor1,
    borderColor: Color = Color.Transparent
) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH) }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 11.sp,
                color = ScheduleTheme.colors.textColor8,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            onClick = onClick,
            color = containerColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription ="달력 아이콘",
                    tint = ScheduleTheme.colors.iconColor1,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = date.format(formatter),
                    style = TextStyle(fontSize = 14.sp, color = ScheduleTheme.colors.textColor3)
                )
            }
        }
    }
}

@Composable
fun ShiftTypeCard(
    label: String,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) color.copy(alpha = 0.5f) else Color.Transparent
    val backgroundColor = if (isSelected) color.copy(alpha = 0.08f) else ScheduleTheme.colors.surfaceColor2

    Surface(
        onClick = onClick,
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .aspectRatio(0.85f)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = label,
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color),
                maxLines = 1,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}