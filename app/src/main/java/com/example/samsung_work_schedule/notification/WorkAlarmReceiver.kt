package com.example.samsung_work_schedule.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.domain.model.WorkSchedule
import com.example.domain.model.WorkType
import com.example.domain.usecase.GetNotificationsUseCase
import com.example.domain.usecase.GetWorkScheduleByDateUseCase
import com.example.domain.usecase.GetWorkSchedulesUseCase
import com.example.samsung_work_schedule.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class WorkAlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var getWorkScheduleByDateUseCase: GetWorkScheduleByDateUseCase
    @Inject
    lateinit var getWorkSchedulesUseCase: GetWorkSchedulesUseCase
    @Inject
    lateinit var getNotificationsUseCase: GetNotificationsUseCase
    @Inject
    lateinit var shiftAlarmManager: ShiftAlarmManager
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("WorkAlarmReceiver", "onReceive triggered with action: $action")

        when (action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d("WorkAlarmReceiver", "Boot completed. Re-registering alarms.")
                scope.launch {
                    val today = LocalDate.now()
                    val futureSchedules = getWorkSchedulesUseCase(today, today.plusDays(30)).first()
                    Log.d("WorkAlarmReceiver", "Found ${futureSchedules.size} future schedules.")
                    futureSchedules.forEach { schedule ->
                        shiftAlarmManager.scheduleAlarm(schedule)
                    }
                }
            }
            "com.example.samsung_work_schedule.ACTION_WORK_ALARM" -> {
                handleAlarm(context)
            }
            else -> {
                Log.d("WorkAlarmReceiver", "Unknown action: $action")
            }
        }
    }

    private fun handleAlarm(context: Context) {
        scope.launch {
            val isEnabled = getNotificationsUseCase().first()
            Log.d("WorkAlarmReceiver", "Notification setting enabled: $isEnabled")
            if (!isEnabled) return@launch

            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)

            val todaySchedule = getWorkScheduleByDateUseCase(today)
            val tomorrowSchedule = getWorkScheduleByDateUseCase(tomorrow)

            Log.d("WorkAlarmReceiver", "Today: $todaySchedule, Tomorrow: $tomorrowSchedule")

            if (todaySchedule != null && todaySchedule.type != WorkType.OFF && todaySchedule.type != WorkType.NONE) {
                showNotification(context, todaySchedule, tomorrowSchedule)
            } else {
                Log.d("WorkAlarmReceiver", "No notification needed (Today is OFF or empty).")
            }
        }
    }

    private fun showNotification(context: Context, today: WorkSchedule, tomorrow: WorkSchedule?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "work_schedule_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }

        fun getWorkTypeText(type: WorkType?): String {
            return when (type) {
                WorkType.DAY -> context.getString(R.string.day)
                WorkType.SW -> context.getString(R.string.sw)
                WorkType.GY -> context.getString(R.string.gy)
                WorkType.OFF -> context.getString(R.string.off)
                else -> context.getString(R.string.shift_none)
            }
        }

        fun getWorkTimeText(type: WorkType?): String {
            return when (type) {
                WorkType.DAY -> "06:00 ~ 14:00"
                WorkType.SW -> "14:00 ~ 22:00"
                WorkType.GY -> "22:00 ~ 06:00"
                else -> "--:-- ~ --:--"
            }
        }

        val todayType = getWorkTypeText(today.type)
        val todayTime = getWorkTimeText(today.type)
        val tomorrowType = getWorkTypeText(tomorrow?.type)
        val tomorrowTime = getWorkTimeText(tomorrow?.type)

        val remoteViews = RemoteViews(context.packageName, R.layout.notification_work_schedule).apply {
            setTextViewText(R.id.tv_today_type, todayType)
            setTextViewText(R.id.tv_today_time, todayTime.replace(" ~ ", " -\n"))

            setTextViewText(R.id.tv_tomorrow_type, tomorrowType)
            setTextViewText(R.id.tv_tomorrow_time, tomorrowTime.replace(" ~ ", " -\n"))
        }

        // 알림 클릭 시 앱 실행을 위한 Intent 설정
        val mainIntent = Intent(context, com.example.samsung_work_schedule.MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val mainPendingIntent = android.app.PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContentIntent(mainPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(today.date.hashCode(), notification)
    }
}