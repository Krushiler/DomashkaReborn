package com.example.domashkareborn.screen

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.domashkareborn.FIREBASE_PREFERENCES
import com.example.domashkareborn.FIREBASE_PREFERENCES_CLASSCODE
import com.example.domashkareborn.FIREBASE_PREFERENCES_MODERATORCODE
import com.example.domashkareborn.UserStatus
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SplashActivityViewModel(var firebase: Firebase,
                              application: Application
) : AndroidViewModel(application) {

    var loginLiveData = MutableLiveData<Boolean>()

    init {
        firebase.database.setPersistenceEnabled(true)
        login()
    }

    fun login(){
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    var sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences(
                        FIREBASE_PREFERENCES,
                        Context.MODE_PRIVATE
                    )
                    var classCode = sharedPreferences.getString(FIREBASE_PREFERENCES_CLASSCODE, "")
                    firebase.auth.signInWithEmailAndPassword("$classCode@li1irk.ru","li1irk").addOnCompleteListener {
                        if (it.isSuccessful){
                            loginLiveData.postValue(true)
                        }else{
                            loginLiveData.postValue(false)
                        }
                    }
                } else {
                    loginLiveData.postValue(true)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                loginLiveData.postValue(true)
            }
        })
    }

}