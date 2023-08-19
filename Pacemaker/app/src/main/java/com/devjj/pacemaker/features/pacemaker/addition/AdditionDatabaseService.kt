package com.devjj.pacemaker.features.pacemaker.addition

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.extension.cur_division
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import javax.inject.Inject

class AdditionDatabaseService
@Inject constructor(val db: ExerciseDatabase) {
    fun theAdditionData(id: Int) = db.ExerciseDAO().searchData(id)

    // DB에 ExerciseData를 추가하는 함수.
    fun insertExerciseData(additionData: AdditionData) = db.ExerciseDAO().insert(
        ExerciseEntity(0, cur_division, additionData.part_img, additionData.name, additionData.mass, additionData.rep,
            additionData.setGoal, 0, additionData.interval, false))

    // DB에 ExerciseData를 업데이트(수정)하는 함수.
    fun updateExerciseData(additionData: AdditionData) = db.ExerciseDAO().update(
        ExerciseEntity(additionData.id, cur_division, additionData.part_img, additionData.name, additionData.mass,
            additionData.rep, additionData.setGoal, 0, additionData.interval, false))

}