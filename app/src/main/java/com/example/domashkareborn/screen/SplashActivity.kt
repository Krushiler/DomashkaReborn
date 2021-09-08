package com.example.domashkareborn.screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.domashkareborn.FIREBASE_PREFERENCES
import com.example.domashkareborn.FIREBASE_PREFERENCES_CLASSCODE
import com.example.domashkareborn.FIREBASE_PREFERENCES_MODERATORCODE
import com.example.domashkareborn.R
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel:SplashActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var sharedPreferences: SharedPreferences = getSharedPreferences(
            FIREBASE_PREFERENCES,
            Context.MODE_PRIVATE
        )

        viewModel.loginLiveData.observe(this){
            if (it){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }
}