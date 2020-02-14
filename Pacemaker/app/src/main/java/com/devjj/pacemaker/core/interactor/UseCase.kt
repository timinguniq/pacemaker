package com.devjj.pacemaker.core.interactor

import com.devjj.pacemaker.core.exception.Failure
import com.devjj.pacemaker.core.functional.Either
import kotlinx.coroutines.*

abstract class UseCase<out Type, in Params> where Type : Any{
    abstract suspend fun run(paramas: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure,Type>) -> Unit = {}){
        val job = GlobalScope.async {run (params)}
        runBlocking { job.join() }
        GlobalScope.launch(Dispatchers.Main) {onResult(job.await()) }
    }

    class None
}