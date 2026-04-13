package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.entity.WorkScheduleEntity
import com.example.data.local.dao.WorkScheduleDao

@Database(entities = [WorkScheduleEntity::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class WorkScheduleDatabase : RoomDatabase() {
    abstract fun workScheduleDao(): WorkScheduleDao

    companion object {
        const val DATABASE_NAME = "work_schedule_db"
    }
}
