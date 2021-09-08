package com.example.domashkareborn.screen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
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
import org.koin.experimental.property.inject

class MainActivityViewModel(
    var firebase: Firebase,
    var userStatus: UserStatus,
    application: Application
) :AndroidViewModel(application) {
    private val userRef = firebase.database.reference.child(firebase.auth.uid.toString())
    var emptyLiveData = liveData<String> {}
    var wasLogined = true
    var logoutLiveData = MutableLiveData<Boolean>(false)

    init {
        setOnline()
        setModeratorState()
    }

    private fun setOnline(){
        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected && !wasLogined) {
                    var sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences(
                        FIREBASE_PREFERENCES,
                        Context.MODE_PRIVATE
                    )
                    var classCode = sharedPreferences.getString(FIREBASE_PREFERENCES_CLASSCODE, "")
                    firebase.auth.signInWithEmailAndPassword("$classCode@li1irk.ru","li1irk").addOnCompleteListener {
                        if (!it.isSuccessful){
                            logoutLiveData.postValue(true)
                        }
                        wasLogined = true
                    }
                } else {
                    wasLogined = false
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setModeratorState(){
        val moderatorStateListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences(
                    FIREBASE_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                var curModerCode = sharedPreferences.getString(FIREBASE_PREFERENCES_MODERATORCODE, "")
                var realCode = snapshot.child("EditorCode").value.toString()
                userStatus.setIsModerator(curModerCode == realCode)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        userRef.addValueEventListener(moderatorStateListener)
    }
}