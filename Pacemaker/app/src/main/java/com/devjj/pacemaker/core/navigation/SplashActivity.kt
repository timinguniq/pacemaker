package com.devjj.pacemaker.core.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devjj.pacemaker.R
import com.devjj.pacemaker.features.login.Authenticator
import kotlinx.android.synthetic.*

class SplashActivity : AppCompatActivity() {
/*

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE){
        (application as AndroidApplication).appComponent
    }
*/

    //@Inject internal lateinit var navigator: Navigator

    lateinit var navigator: Navigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // TODO : 2초나 3초 뒤에 화면 전환
        //appComponent.inject(this)
        // navigator.showMain(this)
        navigator = Navigator(Authenticator())
        navigator.showMain(this)
        
    }
}
