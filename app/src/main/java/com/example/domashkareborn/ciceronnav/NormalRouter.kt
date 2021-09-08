package com.example.domashkareborn.ciceronnav

import android.util.Log
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen

open class NormalRouter : Router() {
    fun navigate(screen: Screen){
        executeCommands(NormalForward(screen))
    }
}