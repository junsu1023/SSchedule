package com.example.samsung_work_schedule.navigation

sealed class Screen(val route: String) {
    data object Calendar: Screen("calendar")
    data object Setting: Screen("setting")
}