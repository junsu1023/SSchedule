package com.example.samsung_work_schedule.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.samsung_work_schedule.ScheduleApplication
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShiftAlarmManager @Inject constructor() {
    private val context = ScheduleApplication.context
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(schedule: WorkSchedule) {
        val startTime = when (schedule.type) {
            WorkType.DAY -> LocalTime.of(4, 59)
            WorkType.SW -> LocalTime.of(12, 59)
            WorkType.GY -> LocalTime.of(20, 57)
            else -> return
        }

        val alarmDateTime = LocalDateTime.of(schedule.date, startTime)
        val zonedDateTime = alarmDateTime.atZone(ZoneId.systemDefault())
        val triggerAtMillis = zonedDateTime.toInstant().toEpochMilli()

        if (triggerAtMillis <= System.currentTimeMillis()) {
            Log.w("ShiftAlarm", "Alarm time is in the past. Skipping.")
            return
        }

        val intent = Intent(context, WorkAlarmReceiver::class.java).apply {
            action = "com.example.samsung_work_schedule.ACTION_WORK_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.date.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(date: LocalDate) {
        val intent = Intent(context, WorkAlarmReceiver::class.java).apply {
            action = "com.example.samsung_work_schedule.ACTION_WORK_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            date.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}