package com.example.samsung_work_schedule.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.RemoteViews
import com.example.domain.model.WorkType
import com.example.domain.usecase.GetWorkSchedulesUseCase
import com.example.samsung_work_schedule.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.app.PendingIntent
import com.example.samsung_work_schedule.MainActivity
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import javax.inject.Inject
import androidx.compose.ui.graphics.toArgb
import com.example.samsung_work_schedule.theme.lightColors

@AndroidEntryPoint
class WorkScheduleWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var getWorkSchedulesUseCase: GetWorkSchedulesUseCase

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
            val views = RemoteViews(context.packageName, R.layout.widget_work_schedule)
            val currentMonth = YearMonth.now()
            
            views.setTextViewText(R.id.tv_widget_month, "${currentMonth.year}.${String.format(Locale.getDefault(), "%02d", currentMonth.monthValue)}")

            val firstDayOfMonth = currentMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
            val startDate = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())
            val lastDayOfMonth = currentMonth.atEndOfMonth()
            val calendarDates = (0 until 42).map { startDate.plusDays(it.toLong()) }

            val scheduleMap = try {
                getWorkSchedulesUseCase(calendarDates.first(), calendarDates.last()).first()
                    .associateBy({ it.date }, { it.type })
            } catch (e: Exception) {
                emptyMap()
            }

            val hasSixWeeks = calendarDates[35].isBefore(lastDayOfMonth.plusDays(1))
            views.setViewVisibility(R.id.row_5, if (hasSixWeeks) View.VISIBLE else View.GONE)

            val rowIds = listOf(R.id.row_0, R.id.row_1, R.id.row_2, R.id.row_3, R.id.row_4, R.id.row_5)
            
            rowIds.forEachIndexed { rowIndex, rowId ->
                views.removeAllViews(rowId)
                for (colIndex in 0 until 7) {
                    val dateIndex = rowIndex * 7 + colIndex
                    val date = calendarDates[dateIndex]
                    val cellViews = createCellRemoteViews(context, date, scheduleMap, currentMonth)
                    views.addView(rowId, cellViews)
                }
            }

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 
                0, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            views.setOnClickPendingIntent(R.id.ll_calendar_container, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun createCellRemoteViews(
        context: Context,
        date: LocalDate,
        schedules: Map<LocalDate, WorkType>,
        currentMonth: YearMonth
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.item_widget_calendar_day)
        val workType = schedules[date] ?: WorkType.NONE
        val isCurrentMonth = YearMonth.from(date) == currentMonth
        val isToday = date == LocalDate.now()

        views.setTextViewText(R.id.tv_day, date.dayOfMonth.toString())
        
        if (isToday) {
            views.setTextColor(R.id.tv_day, lightColors.textColor1.toArgb())
            views.setInt(R.id.rl_day_container, "setBackgroundColor", lightColors.todayBackground.toArgb())
        } else if (isCurrentMonth) {
            views.setTextColor(R.id.tv_day, lightColors.textColor7.toArgb())
            views.setInt(R.id.rl_day_container, "setBackgroundColor", lightColors.background1.toArgb())
        } else {
            views.setTextColor(R.id.tv_day, lightColors.textColor6.toArgb())
            views.setInt(R.id.rl_day_container, "setBackgroundColor", lightColors.dayBackground1.toArgb())
        }

        if (isCurrentMonth && workType != WorkType.NONE) {
            val prevWorkType = schedules[date.minusDays(1)]
            val nextWorkType = schedules[date.plusDays(1)]
            val isConnectedLeft = workType == prevWorkType
            val isConnectedRight = workType == nextWorkType
            val barColor = getWorkColor(workType)

            views.setViewVisibility(R.id.ll_work_bar_container, View.VISIBLE)
            
            val centerRes = when {
                isConnectedLeft && isConnectedRight -> android.R.color.white
                isConnectedLeft -> R.drawable.bg_work_bar_right 
                isConnectedRight -> R.drawable.bg_work_bar_left
                else -> R.drawable.bg_work_bar_single
            }
            
            if (isConnectedLeft && isConnectedRight) {
                views.setImageViewResource(R.id.v_work_bar_center, android.R.color.white)
                views.setInt(R.id.v_work_bar_center, "setColorFilter", barColor)
            } else {
                views.setImageViewResource(R.id.v_work_bar_center, centerRes)
                views.setInt(R.id.v_work_bar_center, "setColorFilter", barColor)
            }

            views.setInt(R.id.v_work_bar_left, "setBackgroundColor", if (isConnectedLeft) barColor else Color.TRANSPARENT)
            views.setInt(R.id.v_work_bar_right, "setBackgroundColor", if (isConnectedRight) barColor else Color.TRANSPARENT)
        } else {
            views.setViewVisibility(R.id.ll_work_bar_container, View.GONE)
        }

        return views
    }

    private fun getWorkColor(type: WorkType): Int {
        return when (type) {
            WorkType.DAY -> lightColors.day.toArgb()
            WorkType.SW -> lightColors.sw.toArgb()
            WorkType.GY -> lightColors.gy.toArgb()
            WorkType.OFFICE -> lightColors.office.toArgb()
            WorkType.OFF -> lightColors.off.toArgb()
            else -> Color.TRANSPARENT
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_WIDGET_UPDATE || intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            val appWidgetIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(ComponentName(context, WorkScheduleWidgetProvider::class.java))
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds)
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
