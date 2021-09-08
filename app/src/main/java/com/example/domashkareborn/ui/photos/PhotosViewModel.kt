package com.example.domashkareborn.ui.photos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.domashkareborn.UserStatus
import com.example.domashkareborn.ciceronnav.NormalRouter
import com.google.firebase.ktx.Firebase

class PhotosViewModel(
    var firebase: Firebase,
    private val router: NormalRouter,
    var userStatus: UserStatus,
    application: Application
) : AndroidViewModel(application) {

}