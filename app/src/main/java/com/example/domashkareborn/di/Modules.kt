package com.example.domashkareborn.di

import com.example.domashkareborn.UserStatus
import com.example.domashkareborn.ciceronnav.NormalRouter
import com.example.domashkareborn.screen.MainActivityViewModel
import com.example.domashkareborn.screen.SplashActivityViewModel
import com.example.domashkareborn.ui.homework.HomeworkViewModel
import com.example.domashkareborn.ui.login.LoginViewModel
import com.example.domashkareborn.ui.photos.PhotosViewModel
import com.example.domashkareborn.ui.registration.RegistrationViewModel
import com.example.domashkareborn.ui.rings.RingsViewModel
import com.example.domashkareborn.ui.subjects.SubjectsViewModel
import com.github.terrakok.cicerone.Cicerone
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
    single { Cicerone.create(NormalRouter()) }
    single { get<Cicerone<NormalRouter>>().router }
    single { get<Cicerone<NormalRouter>>().getNavigatorHolder() }
}

val viewModelsModule = module {
    viewModel { HomeworkViewModel(get(), get(), get(), get()) }
    viewModel { PhotosViewModel(get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get())}
    viewModel { RegistrationViewModel(get(), get())}
    viewModel { MainActivityViewModel(get(), get(), get()) }
    viewModel { SplashActivityViewModel(get(), get()) }
    viewModel { RingsViewModel(get(), get(), get(), get()) }
    viewModel { SubjectsViewModel(get(), get(), get(), get()) }
}

val dataSourceModule = module {
    single { Firebase }
    single { UserStatus }
}