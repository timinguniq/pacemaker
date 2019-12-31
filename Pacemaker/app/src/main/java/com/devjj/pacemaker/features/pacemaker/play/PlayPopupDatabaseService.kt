package com.devjj.pacemaker.features.pacemaker.play

import com.devjj.pacemaker.core.di.database.ExerciseDatabase
import com.devjj.pacemaker.features.pacemaker.addition.AdditionData
import com.devjj.pacemaker.features.pacemaker.entities.ExerciseEntity
import javax.inject.Inject

class PlayPopupDatabaseService
@Inject constructor(val db: ExerciseDatabase){
    fun playPopup() = db.ExerciseDAO().getAll()

    // DB에 ExerciseData를 업데이트(수정)하는 함수.
    fun updateExerciseData(playPopupData: PlayPopupData) = db.ExerciseDAO().update(
        ExerciseEntity(playPopupData.id, playPopupData.part_img, playPopupData.name, playPopupData.mass,
            playPopupData.rep, playPopupData.set, playPopupData.interval, playPopupData.achivement)
    )

}