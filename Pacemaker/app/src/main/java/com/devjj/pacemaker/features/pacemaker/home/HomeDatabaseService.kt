package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.extension.cur_division
import com.devjj.pacemaker.core.functional.Dlog
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import javax.inject.Inject

class HomeDatabaseService
@Inject constructor(val db: ExerciseDatabase){
    fun home() = db.ExerciseDAO().getDivisionAll(cur_division)

    // DB에 ExerciseData를 삭제하는 함수.
    fun deleteExerciseData(homeData: HomeData) = db.ExerciseDAO().delete(
        ExerciseEntity(homeData.id, cur_division, homeData.part_img, homeData.name, homeData.mass, homeData.rep,
            homeData.setGoal, 0, homeData.interval, false)
    )

    // DB에서 ExerciseData들을 swap하는 함수.
    fun swapExerciseData(homeData1Id: Int, homeData2Id: Int){
        Dlog.d("tempHomeData1Id : $homeData1Id")
        Dlog.d("tempHomeData2Id : $homeData2Id")
        val tempHomeData1Id = homeData1Id
        val tempHomeData2Id = homeData2Id

        db.ExerciseDAO().updateId(homeData1Id, -1)
        db.ExerciseDAO().updateId(homeData2Id, tempHomeData1Id)
        db.ExerciseDAO().updateId(-1, tempHomeData2Id)

    }
}