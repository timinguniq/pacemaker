package com.devjj.pacemaker.features.pacemaker.addition

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.home.HomeData
import javax.inject.Inject

interface AdditionRepository {
    // 추가화면의 데이터를 불러오는 함수.
    fun theAdditionData(id: Int) : AdditionData
    // DB에 ExerciseData를 추가하는 함수.
    fun insertExerciseData(additionData: AdditionData) : AdditionData
    // DB에 ExerciseData를 업데이트(수정)하는 함수.
    fun updateExerciseData(additionData: AdditionData) : AdditionData

    class DbRepository
    @Inject constructor(private val db: ExerciseDatabase) :
        AdditionRepository {
        override fun theAdditionData(id: Int): AdditionData {
            var tempExerciseEntity: ExerciseEntity? = db.ExerciseDAO().searchData(id)
            var tempAdditionData = AdditionData.empty()
            if(tempExerciseEntity != null){
                tempAdditionData = tempExerciseEntity.toAdditionData()
            }
            return tempAdditionData
        }

        override fun insertExerciseData(additionData: AdditionData): AdditionData {
            db.ExerciseDAO().insert(ExerciseEntity(0, additionData.part_img, additionData.name,
                    additionData.mass, additionData.set, additionData.interval, false))
            return AdditionData.empty()
        }

        override fun updateExerciseData(additionData: AdditionData): AdditionData {
            db.ExerciseDAO().update(ExerciseEntity(additionData.id, additionData.part_img, additionData.name,
                additionData.mass, additionData.set, additionData.interval, false))
            return additionData
        }
    }
}
