package com.example.domashkareborn.ciceronnav

import android.content.ActivityNotFoundException
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

open class NormalNavigator(activity: FragmentActivity, containerId: Int) : AppNavigator(
    activity,
    containerId
) {
    var localStack = localStackCopy

    open fun normalForward(command: NormalForward) {
        when (val screen = command.screen) {
            is ActivityScreen -> {
                checkAndStartActivity(screen)
            }
            is FragmentScreen -> {
                commitFragmentScreen(screen, true)
            }
        }
    }

    private fun checkAndStartActivity(screen: ActivityScreen) {
        // Check if we can start activity
        val activityIntent = screen.createIntent(activity)
        try {
            activity.startActivity(activityIntent, screen.startActivityOptions)
        } catch (e: ActivityNotFoundException) {
            unexistingActivity(screen, activityIntent)
        }
    }

    override fun applyCommand(command: Command) {
        when (command) {
            is NormalForward -> normalForward(command)
            is Forward -> forward(command)
            is Replace -> replace(command)
            is BackTo -> backTo(command)
            is Back -> backS()
        }
    }

    open fun backS() {
        if (localStackCopy.isNotEmpty()) {
            fragmentManager.popBackStack()
            localStackCopy.removeAt(localStackCopy.lastIndex)
        } else {
            activityBack()
        }
    }

    fun commitFragmentScreen(
        screen: FragmentScreen,
        addToBackStack: Boolean
    ) {
        var fragment: Fragment
        if (fragmentManager.findFragmentByTag(screen.screenKey) != null) {
            fragment = fragmentManager.findFragmentByTag(screen.screenKey)!!
        } else {
            fragment = screen.createFragment(fragmentFactory)
        }
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        setupFragmentTransaction(
            screen,
            transaction,
            fragmentManager.findFragmentById(containerId),
            fragment
        )
        if (screen.clearContainer) {
            transaction.replace(containerId, fragment, screen.screenKey)
        } else {
            transaction.add(containerId, fragment, screen.screenKey)
        }
        if (addToBackStack) {
            transaction.addToBackStack(screen.screenKey)
            localStackCopy.add(screen.screenKey)
        }

        transaction.commit()
    }
}