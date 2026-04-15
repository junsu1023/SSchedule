
package com.example.samsung_work_schedule.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.samsung_work_schedule.feature.calender.CalendarScreen
import com.example.samsung_work_schedule.feature.setting.SettingScreen

@Composable
fun ScheduleNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route,
        modifier = modifier
    ) {
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }

        composable(Screen.Setting.route) {
            SettingScreen(onBackClick = { navController.popBackStack() })
        }
    }
}