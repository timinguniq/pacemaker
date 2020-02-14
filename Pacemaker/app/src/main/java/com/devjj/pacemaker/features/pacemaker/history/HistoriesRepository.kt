package com.devjj.pacemaker.features.pacemaker.history

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import javax.inject.Inject

interface HistoriesRepository {

    fun histories(): Either<Failure, List<History>>

    class HistoryDatabase
    @Inject constructor(
        private val service: HistoryDatabaseService
    ) : HistoriesRepository {
        override fun histories(): Either<Failure, List<History>> {
            return try {
                Right(service.histories().map { it.toHistory() })
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }

    }

}