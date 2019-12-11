package com.devjj.pacemaker.core.navigation

import android.content.Context
import android.view.View
import com.devjj.pacemaker.features.login.LoginActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor(){
    fun showLogin(context: Context) = context.startActivity(LoginActivity.callingIntent(context))

    class Extras(val transitionSharedElement: View)
}