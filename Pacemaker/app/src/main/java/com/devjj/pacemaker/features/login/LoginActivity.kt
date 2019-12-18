package com.devjj.pacemaker.features.login

import android.content.Context
import android.content.Intent
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.platform.BaseActivity

class LoginActivity : BaseActivity() {

    override var layout = R.layout.activity_layout
    override var fragmentId = R.id.aLayout_fragmentContainer

    companion object {
        fun callingIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun fragment() = LoginFragment()
}