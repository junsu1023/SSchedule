package com.example.samsung_work_schedule.widget

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.domain.model.WorkType
import com.example.domain.usecase.GetWorkSchedulesUseCase
import com.example.samsung_work_schedule.R
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.ui.graphics.toArgb
import com.example.samsung_work_schedule.theme.lightColors

@AndroidEntryPoint
class WorkScheduleWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WorkScheduleRemoteViewsFactory(this.applicationContext)
    }
}

class WorkScheduleRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    private lateinit var getWorkSchedulesUseCase: GetWorkSchedulesUseCase

    private var calendarDates = listOf<LocalDate>()
    private var workSchedules = mapOf<LocalDate, WorkType>()
    private var currentMonth = YearMonth.now()

    override fun onCreate() {
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            WidgetEntryPoint::class.java
        )
        getWorkSchedulesUseCase = entryPoint.getWorkSchedulesUseCase()
    }

    override fun onDataSetChanged() {
        runBlocking {
            currentMonth = YearMonth.now()
            val firstDayOfMonth = currentMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
            val lastDayOfMonth = currentMonth.atEndOfMonth()
            
            val dates = mutableListOf<LocalDate>()
            val startDate = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())
            
            var currentDate = startDate
            while (currentDate.isBefore(lastDayOfMonth.plusDays(1)) || dates.size % 7 != 0) {
                dates.add(currentDate)
                currentDate = currentDate.plusDays(1)
                
                if (dates.size >= 42) break
            }
            calendarDates = dates

            try {
                val schedules = getWorkSchedulesUseCase(dates.first(), dates.last()).first()
                workSchedules = schedules.associateBy({ it.date }, { it.type })
            } catch (e: Exception) {
                workSchedules = emptyMap()
            }
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = calendarDates.size

    override fun getViewAt(position: Int): RemoteViews {
        val date = calendarDates[position]
        val workType = workSchedules[date]
        val isCurrentMonth = YearMonth.from(date) == currentMonth
        val isToday = date == LocalDate.now()

        val views = RemoteViews(context.packageName, R.layout.item_widget_calendar_day)
        
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

        if (isCurrentMonth && workType != null && workType != WorkType.NONE) {
            val prevWorkType = workSchedules[date.minusDays(1)]
            val nextWorkType = workSchedules[date.plusDays(1)]
            
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
            views.setImageViewResource(R.id.v_work_bar_center, centerRes)
            views.setInt(R.id.v_work_bar_center, "setColorFilter", barColor)

            if (isConnectedLeft) {
                views.setInt(R.id.v_work_bar_left, "setBackgroundColor", barColor)
            } else {
                views.setInt(R.id.v_work_bar_left, "setBackgroundColor", Color.TRANSPARENT)
            }

            if (isConnectedRight) {
                views.setInt(R.id.v_work_bar_right, "setBackgroundColor", barColor)
            } else {
                views.setInt(R.id.v_work_bar_right, "setBackgroundColor", Color.TRANSPARENT)
            }
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

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface WidgetEntryPoint {
    fun getWorkSchedulesUseCase(): GetWorkSchedulesUseCase
}
