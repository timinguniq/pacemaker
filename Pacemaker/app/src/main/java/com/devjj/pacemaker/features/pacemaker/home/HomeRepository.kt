package com.devjj.pacemaker.features.pacemaker.home

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.exception.Failure.DatabaseError
import com.devjj.pacemaker.core.functional.Either
import com.devjj.pacemaker.core.functional.Either.Left
import com.devjj.pacemaker.core.functional.Either.Right
import javax.inject.Inject

interface HomeRepository {
    fun homeData(): Either<Failure, List<HomeData>>

    class DbRepository
    @Inject constructor(
        private val service: HomeDatabaseService
    ) :
        HomeRepository {
        override fun homeData(): Either<Failure, List<HomeData>> {
            return try {
                Right(service.home().map { it.toHomeData() })
            } catch (exception: Throwable) {
                Left(DatabaseError)
            }
        }
    }
}