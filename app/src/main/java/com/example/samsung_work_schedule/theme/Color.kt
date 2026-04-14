package com.example.samsung_work_schedule.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF0066FF)
val TextGray = Color(0xFF666666)

val lightColors = ScheduleColors(
    background1 = Color(0xFFFFFFFF),
    background2 = Color(0xFFF7FAFC),
    indicatorColor1 = Color(0xFFEFF6FF),
    textColor1 = Color(0xFF1D4ED8),
    textColor2 = Color(0xFF94A3B8),
    textColor3 = Color(0xFF181C1E),
    textColor4 = Color(0xFF717786),
    textColor5 = Color(0xFFFFFFFF),
    textColor6 = Color(0xFFBBBBBB),
    textColor7 = Color(0xFF000000),
    textColor8 = Color(0xFF414755),
    textColor9 = Color(0xFFE57373),
    iconColor1 = Color(0xFF1D4ED8),
    iconColor2 = Color(0xFF94A3B8),
    iconColor3 = Color(0xFFFFFFFF),
    iconColor4 = Color(0xFFF1F4F6),
    iconColor5 = Color(0xFF414755),
    iconColor6 = Color(0xFFE57373),
    containerColor1 = Color(0xFF1D4ED8),
    containerColor2 = Color(0xFFFFFFFF),
    surfaceColor1 = Color(0xFFF1F4F6),
    surfaceColor2 = Color(0xFFF1F5F9),
    day = Color(0xFF00A389),
    sw = Color(0xFF8E59FF),
    gy = Color(0xFFFF9500),
    off = Color(0xFFE91E63),
    gridColor = Color(0xFFF7FAFC),
    todayBackground = Color(0xFFE6F0FF),
    dayBackground1 = Color(0xFFF8F9FA),
    dayBackground2 = Color(0xFFFFFFFF),
    transparent = Color(0x00000000),
    dimColor = Color(0xFFF7FAFC),

    primary = PrimaryBlue,
    textSecondary = TextGray
)

val darkColors = lightColors

val LocalColors = staticCompositionLocalOf { lightColors }

class ScheduleColors(
    background1: Color,
    background2: Color,
    indicatorColor1: Color,
    textColor1: Color,
    textColor2: Color,
    textColor3: Color,
    textColor4: Color,
    textColor5: Color,
    textColor6: Color,
    textColor7: Color,
    textColor8: Color,
    textColor9: Color,
    iconColor1: Color,
    iconColor2: Color,
    iconColor3: Color,
    iconColor4: Color,
    iconColor5: Color,
    iconColor6: Color,
    containerColor1: Color,
    containerColor2: Color,
    surfaceColor1: Color,
    surfaceColor2: Color,
    day: Color,
    sw: Color,
    gy: Color,
    off: Color,
    gridColor: Color,
    todayBackground: Color,
    dayBackground1: Color,
    dayBackground2: Color,
    transparent: Color,
    dimColor: Color,

    primary: Color,
    textSecondary: Color
) {
    var background1 by mutableStateOf(background1)
        private set
    var background2 by mutableStateOf(background2)
        private set
    var indicatorColor1 by mutableStateOf(indicatorColor1)
        private set
    var textColor1 by mutableStateOf(textColor1)
        private set
    var textColor2 by mutableStateOf(textColor2)
        private set
    var textColor3 by mutableStateOf(textColor3)
        private set
    var textColor4 by mutableStateOf(textColor4)
        private set
    var textColor5 by mutableStateOf(textColor5)
        private set
    var textColor6 by mutableStateOf(textColor6)
        private set
    var textColor7 by mutableStateOf(textColor7)
        private set
    var textColor8 by mutableStateOf(textColor8)
        private set
    var textColor9 by mutableStateOf(textColor9)
        private set
    var iconColor1 by mutableStateOf(iconColor1)
        private set
    var iconColor2 by mutableStateOf(iconColor2)
        private set
    var iconColor3 by mutableStateOf(iconColor3)
        private set
    var iconColor4 by mutableStateOf(iconColor4)
        private set
    var iconColor5 by mutableStateOf(iconColor5)
        private set
    var iconColor6 by mutableStateOf(iconColor6)
        private set
    var containerColor1 by mutableStateOf(containerColor1)
        private set
    var containerColor2 by mutableStateOf(containerColor2)
        private set
    var surfaceColor1 by mutableStateOf(surfaceColor1)
        private set
    var surfaceColor2 by mutableStateOf(surfaceColor2)
        private set
    var day by mutableStateOf(day)
        private set
    var sw by mutableStateOf(sw)
        private set
    var gy by mutableStateOf(gy)
        private set
    var off by mutableStateOf(off)
        private set
    var gridColor by mutableStateOf(gridColor)
        private set
    var todayBackground by mutableStateOf(todayBackground)
        private set
    var dayBackground1 by mutableStateOf(dayBackground1)
        private set
    var dayBackground2 by mutableStateOf(dayBackground2)
        private set
    var transparent by mutableStateOf(transparent)
        private set
    var dimColor by mutableStateOf(dimColor)
        private set

    var primary by mutableStateOf(primary)
    var textSecondary by mutableStateOf(textSecondary)
        private set

    fun copy() = ScheduleColors(
        background1 = background1,
        background2 = background2,
        indicatorColor1 = indicatorColor1,
        textColor1 = textColor1,
        textColor2 = textColor2,
        textColor3 = textColor3,
        textColor4 = textColor4,
        textColor5 = textColor5,
        textColor6 = textColor6,
        textColor7 = textColor7,
        textColor8 = textColor8,
        textColor9 = textColor9,
        iconColor1 = iconColor1,
        iconColor2 = iconColor2,
        iconColor3 = iconColor3,
        iconColor4 = iconColor4,
        iconColor5 = iconColor5,
        iconColor6 = iconColor6,
        containerColor1 = containerColor1,
        containerColor2 = containerColor2,
        surfaceColor1 = surfaceColor1,
        surfaceColor2 = surfaceColor2,
        day = day,
        sw = sw,
        gy = gy,
        off = off,
        gridColor = gridColor,
        todayBackground = todayBackground,
        dayBackground1 = dayBackground1,
        dayBackground2 = dayBackground2,
        transparent = transparent,
        dimColor = dimColor,

        primary = primary,
        textSecondary = textSecondary,
    )

    fun updateColorsFrom(other: ScheduleColors) {
        background1 = other.background1
        background2 = other.background2
        indicatorColor1 = other.indicatorColor1
        textColor1 = other.textColor1
        textColor2 = other.textColor2
        textColor3 = other.textColor3
        textColor4 = other.textColor4
        textColor5 = other.textColor5
        textColor6 = other.textColor6
        textColor7 = other.textColor7
        textColor8 = other.textColor8
        textColor9 = other.textColor9
        iconColor1 = other.iconColor1
        iconColor2 = other.iconColor2
        iconColor3 = other.iconColor3
        iconColor4 = other.iconColor4
        iconColor5 = other.iconColor5
        iconColor6 = other.iconColor6
        containerColor1 = other.containerColor1
        containerColor2 = other.containerColor2
        surfaceColor1 = other.surfaceColor1
        surfaceColor2 = other.surfaceColor2
        day = other.day
        sw = other.sw
        gy = other.gy
        off = other.off
        gridColor = other.gridColor
        todayBackground = other.todayBackground
        dayBackground1 = other.dayBackground1
        dayBackground2 = other.dayBackground2
        transparent = other.transparent
        dimColor = other.dimColor

        primary = other.primary
        textSecondary = other.textSecondary
    }
}