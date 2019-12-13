package com.devjj.pacemaker.features.pacemaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.platform.BaseActivity
import com.devjj.pacemaker.core.platform.BaseFragment
import com.devjj.pacemaker.features.pacemaker.home.HomeFragment

class PacemakerActivity : BaseActivity() {

    companion object{
        fun callingIntent(context: Context) = Intent(context, PacemakerActivity::class.java)
    }

    override fun fragment() = HomeFragment()
}
