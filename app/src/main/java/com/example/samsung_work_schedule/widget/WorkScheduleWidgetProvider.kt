package com.example.samsung_work_schedule.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.domain.model.WorkType
import com.example.domain.usecase.GetWorkScheduleByDateUseCase
import com.example.samsung_work_schedule.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class WorkScheduleWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var getWorkScheduleByDateUseCase: GetWorkScheduleByDateUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        scope.launch {
            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)

            val todaySchedule = getWorkScheduleByDateUseCase(today)
            val tomorrowSchedule = getWorkScheduleByDateUseCase(tomorrow)

            val views = RemoteViews(context.packageName, R.layout.widget_work_schedule)

            // Update Today Card
            views.setTextViewText(R.id.tv_widget_today_type, getWorkTypeText(context, todaySchedule?.type))
            views.setTextViewText(R.id.tv_widget_today_time, getWorkTimeText(todaySchedule?.type).replace(" ~ ", " -\n"))

            // Update Tomorrow Card
            views.setTextViewText(R.id.tv_widget_tomorrow_type, getWorkTypeText(context, tomorrowSchedule?.type))
            views.setTextViewText(R.id.tv_widget_tomorrow_time, getWorkTimeText(tomorrowSchedule?.type).replace(" ~ ", " -\n"))

            // Click Intent (Open App)
//            val intent = Intent(context, MainActivity::class.java)
//            val pendingIntent = PendingIntent.getActivity(
//                context,
//                0,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            views.setOnClickPendingIntent(R.id.iv_widget_icon, pendingIntent)
            // 전체 배경 클릭 시에도 앱 실행
            // views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getWorkTypeText(context: Context, type: WorkType?): String {
        return when (type) {
            WorkType.DAY -> context.getString(R.string.day)
            WorkType.SW -> context.getString(R.string.sw)
            WorkType.GY -> context.getString(R.string.gy)
            WorkType.OFF -> context.getString(R.string.off)
            WorkType.OFFICE -> context.getString(R.string.office)
            else -> context.getString(R.string.shift_none)
        }
    }

    private fun getWorkTimeText(type: WorkType?): String {
        return when (type) {
            WorkType.DAY -> "06:00 ~ 14:00"
            WorkType.SW -> "14:00 ~ 22:00"
            WorkType.GY -> "22:00 ~ 06:00"
            else -> "--:-- ~ --:--"
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_WIDGET_UPDATE || intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, WorkScheduleWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    companion object {
        const val ACTION_WIDGET_UPDATE = "com.example.samsung_work_schedule.WIDGET_UPDATE"

        fun triggerUpdate(context: Context) {
            val intent = Intent(context, WorkScheduleWidgetProvider::class.java).apply {
                action = ACTION_WIDGET_UPDATE
            }
            context.sendBroadcast(intent)
        }
    }
}
