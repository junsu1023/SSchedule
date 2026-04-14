package com.example.samsung_work_schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.samsung_work_schedule.navigation.ScheduleNavigation
import com.example.samsung_work_schedule.navigation.Screen
import com.example.samsung_work_schedule.theme.ScheduleTheme

@Composable
fun ScheduleApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navigateToRoute: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val items = listOf(
        Screen.Calendar,
        Screen.Setting
    )

    Scaffold(
        containerColor = ScheduleTheme.colors.background2,
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                shadowElevation = 16.dp,
                color = ScheduleTheme.colors.background2
            ) {
                NavigationBar(containerColor = ScheduleTheme.colors.background2) {
                    items.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                        NavigationBarItem(
                            icon = { NavigationBarIcon(screen) },
                            label = null,
                            selected = selected,
                            onClick = { navigateToRoute(screen.route) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ScheduleTheme.colors.iconColor1,
                                selectedTextColor = ScheduleTheme.colors.textColor1,
                                unselectedIconColor = ScheduleTheme.colors.iconColor2,
                                unselectedTextColor = ScheduleTheme.colors.textColor2,
                                indicatorColor = ScheduleTheme.colors.indicatorColor1
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        ScheduleNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun NavigationBarIcon(screen: Screen) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = if (screen == Screen.Calendar) Icons.Default.CalendarMonth else Icons.Default.Settings,
            contentDescription = if(screen == Screen.Calendar) "달력 화면" else "설정 화면",
            modifier = Modifier.size(width = 20.dp, height = 16.dp)
        )

        Text(
            text = when (screen) {
                Screen.Calendar -> stringResource(R.string.calendar)
                Screen.Setting -> stringResource(R.string.setting)
            },
            fontSize = 14.sp
        )
    }
}