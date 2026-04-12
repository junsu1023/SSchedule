package com.example.samsung_work_schedule

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.samsung_work_schedule.navigation.ScheduleNavigation

@Composable
fun  ScheduleApp() {
    val navController = rememberNavController()

    ScheduleNavigation(navController = navController)
}