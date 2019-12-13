package com.devjj.pacemaker.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    abstract class FreatureFailure : Failure()
}