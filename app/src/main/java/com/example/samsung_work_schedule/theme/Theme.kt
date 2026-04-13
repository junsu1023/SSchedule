package com.example.samsung_work_schedule.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember


object ScheduleTheme {
    val colors: ScheduleColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
}

@Composable
fun SamsungWorkScheduleTheme(
    content: @Composable () -> Unit
) {
    val isDarkMode = false  // 차후 SharedPreference에 저장하여 사용 예정
    val currentColor = remember(isDarkMode) { if(isDarkMode) darkColors else lightColors }
    val rememberedColors = remember(isDarkMode) { currentColor.copy() }.apply { updateColorsFrom(currentColor) }

    CompositionLocalProvider(
        LocalColors provides rememberedColors
    ) {
        content()
    }
}