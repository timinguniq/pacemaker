package com.devjj.pacemaker.core.platform

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.devjj.pacemaker.AndroidApplication
import com.devjj.pacemaker.R
import com.devjj.pacemaker.core.di.ApplicationComponent
import com.devjj.pacemaker.core.extension.inTransaction

/**
 * Base Activity class with helper methods for handling fragment transactions and back button
 * events.
 *
 * @see AppCompatActivity
 */
abstract class BaseActivity : AppCompatActivity() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE){
        (application as AndroidApplication).appComponent
    }

    abstract var layout: Int
    abstract var fragmentId: Int

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 세로 화면으로 고정하는 코드
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //

        setContentView(layout)

        addFragment(savedInstanceState)

    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            fragmentId) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    private fun addFragment(savedInstanceState: Bundle?) =
        savedInstanceState ?: supportFragmentManager.inTransaction { add(
            fragmentId, fragment()) }

    abstract fun fragment(): BaseFragment
}