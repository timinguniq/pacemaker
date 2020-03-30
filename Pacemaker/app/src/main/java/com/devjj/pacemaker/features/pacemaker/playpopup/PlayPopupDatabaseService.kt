package com.devjj.pacemaker.features.pacemaker.playpopup

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.core.di.database.ExerciseHistoryDatabase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseHistoryEntity
import javax.inject.Inject

class PlayPopupDatabaseService
@Inject constructor(val db: ExerciseDatabase, val dbHistroy: ExerciseHistoryDatabase){
    fun playPopup() = db.ExerciseDAO().getAll()

    // DB에 ExerciseData를 업데이트(수정)하는 함수.
    fun updateExerciseData(playPopupData: PlayPopupData) = db.ExerciseDAO().update(
        ExerciseEntity(playPopupData.id, playPopupData.part_img, playPopupData.name, playPopupData.mass,
            playPopupData.rep, playPopupData.setGoal, playPopupData.setDone, playPopupData.interval, playPopupData.achievement)
    )

    // DB에 ExerciseHistroyData를 추가하는 함수.
    fun insertExerciseHistoryData(playPopupData: PlayPopupData) = dbHistroy.ExerciseHistoryDAO().insert(
        ExerciseHistoryEntity(0, date, playPopupData.part_img, playPopupData.name, playPopupData.mass,
            playPopupData.rep, playPopupData.setGoal, playPopupData.setDone, playPopupData.interval,
            playPopupData.achievement, (100 * playPopupData.setDone/playPopupData.setGoal),
            height, weight, totalTime)
    )
}