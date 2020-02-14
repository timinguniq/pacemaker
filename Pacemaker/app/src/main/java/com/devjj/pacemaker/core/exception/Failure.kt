package com.devjj.pacemaker.core.exception

sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object DatabaseError : Failure()
    object SharedPreferencesError: Failure()

    abstract class FeatureFailure : Failure()
}