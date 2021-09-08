package com.example.domashkareborn

import com.example.domashkareborn.ui.homework.HomeworkFragment
import com.example.domashkareborn.ui.rings.RingsFragment
import com.example.domashkareborn.ui.subjects.SubjectsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

@Suppress("FunctionName")
object Screens {
    fun HomeworkScreen() = FragmentScreen { HomeworkFragment() }
    fun SubjectsScreen() = FragmentScreen { SubjectsFragment() }
    fun RingsScreen() = FragmentScreen { RingsFragment() }
}