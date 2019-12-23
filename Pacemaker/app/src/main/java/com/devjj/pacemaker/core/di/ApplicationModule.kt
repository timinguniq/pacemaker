package com.devjj.pacemaker.core.di

import android.content.Context
import androidx.room.Room
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideExerciseDatabase(): ExerciseDatabase{
        val db = Room.databaseBuilder(
            application,
            ExerciseDatabase::class.java,
            "exercises"
        ).build()

        return db
    }

    @Provides
    @Singleton
    fun provideExerciseHistoryDatabase(): ExerciseHistoryDatabase{
        val db = Room.databaseBuilder(
            application,
            ExerciseHistoryDatabase::class.java,
            "exerciseHistories"
        ).build()

        return db
    }

    @Provides @Singleton fun provideHistoryRepository(dataSource : HistoriesRepository.HistoryDatabase): HistoriesRepository =dataSource
}