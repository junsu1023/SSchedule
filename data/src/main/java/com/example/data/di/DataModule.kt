package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.dao.WorkScheduleDao
import com.example.data.local.WorkScheduleDatabase
import com.example.data.repository.WorkScheduleRepositoryImpl
import com.example.domain.repository.WorkScheduleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideWorkScheduleRepository(
        workScheduleDao: WorkScheduleDao
    ): WorkScheduleRepository {
        return WorkScheduleRepositoryImpl(workScheduleDao)
    }

    @Provides
    @Singleton
    fun provideWorkScheduleDatabase(
        @ApplicationContext context: Context
    ): WorkScheduleDatabase {
        return Room.databaseBuilder(
            context,
            WorkScheduleDatabase::class.java,
            WorkScheduleDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideWorkScheduleDao(database: WorkScheduleDatabase): WorkScheduleDao {
        return database.workScheduleDao()
    }
}
