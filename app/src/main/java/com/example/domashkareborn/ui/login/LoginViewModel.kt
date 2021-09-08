package com.example.domashkareborn.ui.login

import android.app.Application
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domashkareborn.FIREBASE_PREFERENCES
import com.example.domashkareborn.FIREBASE_PREFERENCES_CLASSCODE
import com.example.domashkareborn.FIREBASE_PREFERENCES_MODERATORCODE
import com.example.domashkareborn.screen.LoginActivity
import com.google.android.gms.common.util.MurmurHash3
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.yield
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

class LoginViewModel(var firebase:Firebase, application: Application) : AndroidViewModel(
    application
) {
    val isLogining = MutableLiveData<Boolean>()
    val isLogined = MutableLiveData<Boolean>()

    public fun login(classCode: String, moderatorCode: String) {
        isLogining.postValue(true)
        firebase.auth.signInWithEmailAndPassword("$classCode@li1irk.ru","li1irk").addOnCompleteListener {
            if (it.isSuccessful) {
                var sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences(
                    FIREBASE_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                var editor = sharedPreferences.edit()
                editor.putString(FIREBASE_PREFERENCES_CLASSCODE, classCode)
                editor.putString(FIREBASE_PREFERENCES_MODERATORCODE, moderatorCode)
                editor.apply()
                isLogined.postValue(true)
                isLogining.postValue(false)
            } else {
                isLogining.postValue(false)
                Toast.makeText(getApplication(), "Неверный код класса", LENGTH_LONG).show()
            }
        }
    }


}