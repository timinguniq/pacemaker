package com.devjj.pacemaker.features.pacemaker.playpopup

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Right
import com.devjj.pacemaker.core.functional.Either.Left
import javax.inject.Inject

interface PlayPopupRepository {
    // 위에 recyclerview 데이터를 가져오는 함수.
    fun playPopupData() : Either<Failure, List<PlayPopupData>>
    // DB에 ExerciseData를 업데이트(수정) 후 갱신까지.
    fun updateExerciseData(playPopupData: PlayPopupData): Either<Failure, Unit>
    // DB에 ExerciseHistoryData를 추가하는 함수.
    fun insertExerciseHistoryData(playPopupData: PlayPopupData): Either<Failure, PlayPopupData>
    // DB에 날짜를 받아 ExerciseHistoryData를 삭제하는 함수.
    fun deleteExerciseHistoryData(): Either<Failure, PlayPopupData>
    // DB에 StatisticsData를 추가하는 함수
    fun insertStatisticsData(todaySetDone: Int, todaySetGoal: Int): Either<Failure, PlayPopupData>

    class DbRepository
    @Inject constructor(private val service: PlayPopupDatabaseService) :
        PlayPopupRepository {
        override fun playPopupData(): Either<Failure, List<PlayPopupData>> {
            return try{
                Right(service.playPopup().map { it.toPlayPopup() })
            }catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

        override fun updateExerciseData(playPopupData: PlayPopupData): Either<Failure, Unit> {
            return try{
                Right(service.updateExerciseData(playPopupData))
            }catch(exception: Throwable){
                Left(DatabaseError)
            }
        }

        override fun insertExerciseHistoryData(playPopupData: PlayPopupData): Either<Failure, PlayPopupData> {
            service.insertExerciseHistoryData(playPopupData)
            return try {
                Right(playPopupData)
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }


        override fun deleteExerciseHistoryData(): Either<Failure, PlayPopupData> {
            service.deleteExerciseHistoryData()
            return try {
                Right(PlayPopupData.empty())
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

        override fun insertStatisticsData(todaySetDone: Int, todaySetGoal: Int): Either<Failure, PlayPopupData> {
            service.insertStatisticsData(todaySetDone, todaySetGoal)
            return try {
                Right(PlayPopupData.empty())
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

    }
}