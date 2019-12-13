package com.devjj.pacemaker.core.navigation

import android.content.Context
import android.view.View
import com.devjj.pacemaker.features.login.Authenticator
import com.devjj.pacemaker.features.login.LoginActivity
import com.devjj.pacemaker.features.pacemaker.PacemakerActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator
@Inject constructor(private val authenticator: Authenticator){
    fun showLogin(context: Context) = context.startActivity(LoginActivity.callingIntent(context))

    fun showMain(context: Context) {
        when (authenticator.userLoggedIn()) {
            true -> showPacemaker(context)
            false -> showLogin(context)
        }
    }

    fun showPacemaker(context: Context) = context.startActivity(PacemakerActivity.callingIntent(context))


    class Extras(val transitionSharedElement: View)
}