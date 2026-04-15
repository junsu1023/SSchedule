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
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val currentColor = remember(darkTheme) { if(darkTheme) darkColors else lightColors }
    val rememberedColors = remember(darkTheme) { currentColor.copy() }.apply { updateColorsFrom(currentColor) }

    CompositionLocalProvider(
        LocalColors provides rememberedColors
    ) {
        content()
    }
}