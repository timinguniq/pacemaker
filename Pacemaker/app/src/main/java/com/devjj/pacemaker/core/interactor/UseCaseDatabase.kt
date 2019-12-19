package com.devjj.pacemaker.core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCaseDatabase<out Type, in Params> where Type : Any {

    abstract suspend fun run(paramas: Params): Type

    operator fun invoke(params: Params, onResult: (Type) -> Unit = {}) {
        val job = GlobalScope.async{run(params)}
        GlobalScope.launch(Dispatchers.Main) { onResult(job.await()) }
    }
    class None
}