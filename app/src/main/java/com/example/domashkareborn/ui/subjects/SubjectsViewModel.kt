package com.example.domashkareborn.ui.subjects

import android.app.Application
import android.util.Log
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.domashkareborn.UserStatus
import com.example.domashkareborn.ciceronnav.NormalRouter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SubjectsViewModel(
    firebase: Firebase,
    router: NormalRouter,
    userStatus: UserStatus,
    application: Application
) : AndroidViewModel(application) {

    private val userRef = firebase.database.reference.child(firebase.auth.uid.toString())

    var subjectsLiveData: MutableLiveData<List<String>> = MutableLiveData()
    var subjects:MutableList<String> = mutableListOf()

    init {
        getSubjects()
    }

    fun getSubjects(){
        userRef.keepSynced(true)
        subjectsLiveData.postValue(subjects)
        val subjectsListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                subjects = mutableListOf()
                for (subjectsSnapshot:DataSnapshot in snapshot.child("AddictiveSubjects").children){
                    subjects.add(subjectsSnapshot.value.toString())
                }
                subjectsLiveData.postValue(subjects)
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        userRef.addValueEventListener(subjectsListener)
    }

    public fun saveSubjects(enteredSubjects:List<String>){
        userRef.child("AddictiveSubjects").setValue(enteredSubjects)
    }

    public fun setLiveData(){
        subjectsLiveData.postValue(subjects)
    }
}