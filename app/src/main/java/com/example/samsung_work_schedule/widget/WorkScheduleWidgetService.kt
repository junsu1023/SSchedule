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
import androidx.core.content.ContextCompat

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
        val dayString = date.dayOfMonth.toString()

        val views = RemoteViews(context.packageName, R.layout.item_widget_calendar_day)
        
        // Hide all day text views first
        views.setViewVisibility(R.id.tv_day_current, View.GONE)
        views.setViewVisibility(R.id.tv_day_today, View.GONE)
        views.setViewVisibility(R.id.tv_day_other, View.GONE)

        when {
            isToday -> {
                views.setViewVisibility(R.id.tv_day_today, View.VISIBLE)
                views.setTextViewText(R.id.tv_day_today, dayString)
                views.setInt(R.id.rl_day_container, "setBackgroundResource", R.color.widget_day_bg_today)
            }
            isCurrentMonth -> {
                views.setViewVisibility(R.id.tv_day_current, View.VISIBLE)
                views.setTextViewText(R.id.tv_day_current, dayString)
                views.setInt(R.id.rl_day_container, "setBackgroundResource", R.color.widget_bg)
            }
            else -> {
                views.setViewVisibility(R.id.tv_day_other, View.VISIBLE)
                views.setTextViewText(R.id.tv_day_other, dayString)
                views.setInt(R.id.rl_day_container, "setBackgroundResource", R.color.widget_day_bg_other)
            }
        }

        if (isCurrentMonth && workType != null && workType != WorkType.NONE) {
            val prevWorkType = workSchedules[date.minusDays(1)]
            val nextWorkType = workSchedules[date.plusDays(1)]
            
            val isConnectedLeft = workType == prevWorkType
            val isConnectedRight = workType == nextWorkType
            val barColor = ContextCompat.getColor(context, getWorkColorRes(workType))

            views.setViewVisibility(R.id.ll_work_bar_container, View.VISIBLE)

            val centerRes = when {
                isConnectedLeft && isConnectedRight -> android.R.color.white
                isConnectedLeft -> R.drawable.bg_work_bar_right
                isConnectedRight -> R.drawable.bg_work_bar_left
                else -> R.drawable.bg_work_bar_single
            }
            views.setImageViewResource(R.id.v_work_bar_center, centerRes)
            views.setInt(R.id.v_work_bar_center, "setColorFilter", barColor)

            views.setInt(R.id.v_work_bar_left, "setBackgroundColor", if (isConnectedLeft) barColor else Color.TRANSPARENT)
            views.setInt(R.id.v_work_bar_right, "setBackgroundColor", if (isConnectedRight) barColor else Color.TRANSPARENT)
        } else {
            views.setViewVisibility(R.id.ll_work_bar_container, View.GONE)
        }

        return views
    }

    private fun getWorkColorRes(type: WorkType): Int {
        return when (type) {
            WorkType.DAY -> R.color.widget_work_day
            WorkType.SW -> R.color.widget_work_sw
            WorkType.GY -> R.color.widget_work_gy
            WorkType.OFFICE -> R.color.widget_work_office
            WorkType.OFF -> R.color.widget_work_off
            else -> android.R.color.transparent
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
