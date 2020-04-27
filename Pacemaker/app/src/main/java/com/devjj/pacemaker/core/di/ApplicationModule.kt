package com.devjj.pacemaker.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.core.di.database.StatisticsDatabase
import com.devjj.pacemaker.core.di.sharedpreferences.SettingSharedPreferences
import com.devjj.pacemaker.features.pacemaker.addition.AdditionRepository
import com.devjj.pacemaker.features.pacemaker.home.HomeRepository
import com.devjj.pacemaker.features.pacemaker.history.HistoriesRepository
import com.devjj.pacemaker.features.pacemaker.historydetail.HistoryDetailsRepository
import com.devjj.pacemaker.features.pacemaker.playpopup.PlayPopupRepository
import com.devjj.pacemaker.features.pacemaker.statistics.StatisticsRepository
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
    fun provideExerciseDatabase(): ExerciseDatabase {
        val db = Room.databaseBuilder(
            application,
            ExerciseDatabase::class.java,
            "exercises"
        ).addCallback(object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Thread{
                    db.beginTransactionNonExclusive()
                }
            }
        }).build()
        return db
    }

    @Provides
    @Singleton
    fun provideExerciseHistoryDatabase(): ExerciseHistoryDatabase {
        val db = Room.databaseBuilder(
            application,
            ExerciseHistoryDatabase::class.java,
            "exerciseHistories"
        ).build()
        return db
    }

    @Provides
    @Singleton
    fun provideStatisticsDatabase(): StatisticsDatabase {
        val db = Room.databaseBuilder(
            application,
            StatisticsDatabase::class.java,
            "statistics"
        ).addCallback(object : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Thread{
                    db.beginTransactionNonExclusive()
                }
            }
        }).build()
        return db
    }

    @Provides
    @Singleton
    fun provideSettingSharedPreferences(): SettingSharedPreferences {
        return SettingSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(dataSource: HomeRepository.DbRepository): HomeRepository = dataSource

    @Provides
    @Singleton
    fun provideAdditionRepository(dataSource: AdditionRepository.DbRepository): AdditionRepository = dataSource

    @Provides
    @Singleton
    fun provideHistoryRepository(dataSource : HistoriesRepository.HistoryDatabase): HistoriesRepository = dataSource

    @Provides
    @Singleton
    fun providePlayPopupRepository(dataSource: PlayPopupRepository.DbRepository): PlayPopupRepository = dataSource

    @Provides
    @Singleton
    fun provideHistoryDetailRepository(dataSource: HistoryDetailsRepository.HistoryDetailDatabase) : HistoryDetailsRepository = dataSource

    @Provides
    @Singleton
    fun provideStatisticsDetailRepository(dataSource : StatisticsRepository.DbRepository) : StatisticsRepository = dataSource

}