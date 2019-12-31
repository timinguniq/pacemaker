package com.devjj.pacemaker.features.pacemaker.addition

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either.Right
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import javax.inject.Inject

interface AdditionRepository {
    // 추가화면의 데이터를 불러오는 함수.
    fun theAdditionData(id: Int): Either<Failure, AdditionData>

    // DB에 ExerciseData를 추가하는 함수.
    fun insertExerciseData(additionData: AdditionData): Either<Failure, AdditionData>

    // DB에 ExerciseData를 업데이트(수정)하는 함수.
    fun updateExerciseData(additionData: AdditionData): Either<Failure, AdditionData>

    class DbRepository
    @Inject constructor(private val service: AdditionDatabaseService) : AdditionRepository {

        override fun theAdditionData(id: Int): Either<Failure, AdditionData> {
            val tempExerciseEntity: ExerciseEntity? = service.theAdditionData(id)
            var tempAdditionData = AdditionData.empty()
            if (tempExerciseEntity != null) {
                tempAdditionData = tempExerciseEntity.toAdditionData()
            }
            return try {
                Right(tempAdditionData)
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

        override fun insertExerciseData(additionData: AdditionData): Either<Failure, AdditionData> {
            service.insertExerciseData(additionData)
            return try {
                Right(AdditionData.empty())
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

        override fun updateExerciseData(additionData: AdditionData): Either<Failure, AdditionData> {
            service.updateExerciseData(additionData)
            return try {
                Right(additionData)
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }
    }
}
