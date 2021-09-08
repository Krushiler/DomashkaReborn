package com.example.domashkareborn.ui.registration

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.domashkareborn.FIREBASE_PREFERENCES
import com.example.domashkareborn.FIREBASE_PREFERENCES_CLASSCODE
import com.example.domashkareborn.FIREBASE_PREFERENCES_MODERATORCODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationViewModel(var firebase: Firebase, application: Application) : AndroidViewModel(application) {

    val isLogining = MutableLiveData<Boolean>()
    val isLogined = MutableLiveData<Boolean>()

    public fun register(classCode: String, moderatorCode: String){
        isLogining.postValue(true)
        firebase.auth.createUserWithEmailAndPassword("$classCode@li1irk.ru","li1irk").addOnCompleteListener {
            if (it.isSuccessful) {
                var sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences(
                    FIREBASE_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                var editor = sharedPreferences.edit()
                editor.putString(FIREBASE_PREFERENCES_CLASSCODE, classCode)
                editor.putString(FIREBASE_PREFERENCES_MODERATORCODE, moderatorCode)
                editor.apply()
                firebase.database.reference.child(firebase.auth.uid.toString()).child("EditorCode").setValue(moderatorCode)
                isLogined.postValue(true)
                isLogining.postValue(false)
            } else {
                isLogining.postValue(false)
                Toast.makeText(getApplication(), "Неверный код класса", Toast.LENGTH_LONG).show()
            }
        }
    }

}