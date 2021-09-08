package com.example.domashkareborn.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelStore
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domashkareborn.R
import com.example.domashkareborn.Screens
import com.example.domashkareborn.ciceronnav.NormalNavigator
import com.example.domashkareborn.ciceronnav.NormalRouter
import com.example.domashkareborn.databinding.ActivityMainBinding
import com.example.domashkareborn.ui.homework.HomeworkFragment
import com.example.domashkareborn.ui.rings.RingsFragment
import com.github.terrakok.cicerone.NavigatorHolder
import org.koin.android.viewmodel.ext.android.viewModel
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeActivity

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    private val viewModel:MainActivityViewModel by viewModel()

    private val router: NormalRouter by inject()
    private val navigatorHolder: NavigatorHolder by inject()
    private val navigator = object : NormalNavigator(this, R.id.fragment) {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            super.setupFragmentTransaction(
                screen,
                fragmentTransaction,
                currentFragment,
                nextFragment
            )
            fragmentTransaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            /* fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                 R.anim.slide_in_right, R.anim.slide_out_right)*/
        }
    }

    var isback = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.bottomNavigation.apply {
            setOnNavigationItemSelectedListener {
                if (!isback) {
                    var selectedScreen = Screens.HomeworkScreen()

                    if (it.itemId != binding.bottomNavigation.selectedItemId) {
                        when (it.itemId) {
                            R.id.menuHomework -> selectedScreen = Screens.HomeworkScreen()
                            R.id.menuRings -> selectedScreen = Screens.RingsScreen()
                        }
                        router.navigate(selectedScreen)
                    }
                }
                true
            }
            setOnNavigationItemReselectedListener {}
        }

        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.menuHomework
            router.newRootChain(Screens.HomeworkScreen())
        }
        viewModel.logoutLiveData.observe(this){
            if (it){
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        viewModel.emptyLiveData.observe(this){}
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 1){
            isback = true
            binding.bottomNavigation.selectedItemId =R.id.menuHomework

            isback = false
        }

        if (supportFragmentManager.backStackEntryCount > 1) {

            isback = true
            when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2).name) {
                Screens.HomeworkScreen().screenKey -> binding.bottomNavigation.selectedItemId =
                    R.id.menuHomework
                Screens.RingsScreen().screenKey -> binding.bottomNavigation.selectedItemId =
                    R.id.menuRings
            }

            isback = false
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}