package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import javax.inject.Inject

class HomeDatabaseService
@Inject constructor(val db: ExerciseDatabase){
    fun home() = db.ExerciseDAO().getAll()

    // DB에 ExerciseData를 삭제하는 함수.
    fun deleteExerciseData(homeData: HomeData) = db.ExerciseDAO().delete(
        ExerciseEntity(homeData.id, homeData.part_img, homeData.name, homeData.mass, homeData.rep,
            homeData.setGoal, 0, homeData.interval, false)
    )
}