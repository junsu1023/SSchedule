package com.example.samsung_work_schedule

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScheduleApplication: Application() {
    companion object {
        lateinit var context: ScheduleApplication
    }

    override fun onCreate() {
        super.onCreate()

        context = this
    }
}