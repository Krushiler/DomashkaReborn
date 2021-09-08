package com.example.domashkareborn.ui.rings

import android.app.Application
import android.view.View
import androidx.compose.animation.core.snap
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.domashkareborn.UserStatus
import com.example.domashkareborn.ciceronnav.NormalRouter
import com.example.domashkareborn.ui.homework.ObservingLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RingsViewModel(
    firebase: Firebase,
    router: NormalRouter,
    userStatus: UserStatus,
    application: Application
) : AndroidViewModel(application) {

    private val userRef = firebase.database.reference.child(firebase.auth.uid.toString())

    var ringsLiveData: MutableLiveData<List<String>> = MutableLiveData()
    var rings = MutableList(20){""}

    val isModeratorLiveData: ObservingLiveData<UserStatus> = ObservingLiveData<UserStatus>()
    var isModerator = false

    init {
        userRef.keepSynced(true)
        getRings()
        getModerator()
    }

    private fun getModerator(){
        isModeratorLiveData.postValue(UserStatus)
        isModerator = isModeratorLiveData.value?.getIsModerator() == false
    }

    private fun getRings(){
        ringsLiveData.postValue(rings)
        val ringsListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (curRing in rings.indices){
                    var curRung = snapshot.child("time$curRing").value.toString()
                    if (curRung != "null") {
                        rings[curRing] = curRung
                    }
                }
                ringsLiveData.postValue(rings)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        userRef.addValueEventListener(ringsListener)
    }

    var ringIds = mutableListOf<Int>()
    var firstIds = mutableListOf<Int>()
    var secondIds = mutableListOf<Int>()

    public fun saveRings(enteredRings:List<String>){
        for (i in enteredRings.indices){
            userRef.child("time$i").setValue(enteredRings[i])
        }
    }

    public fun getRingIds(count: Int):List<Int>{
        if (ringIds.size != count){
            ringIds = mutableListOf<Int>()
            for (i in 1..count){
                ringIds.add(View.generateViewId())
            }
        }
        return ringIds
    }
    public fun getFirstIds(count: Int):List<Int>{
        if (firstIds.size != count){
            firstIds = mutableListOf<Int>()
            for (i in 1..count){
                firstIds.add(View.generateViewId())
            }
        }
        return firstIds
    }
    public fun getSecondIds(count: Int):List<Int>{
        if (secondIds.size != count){
            secondIds = mutableListOf<Int>()
            for (i in 1..count){
                secondIds.add(View.generateViewId())
            }
        }
        return secondIds
    }

    public fun setLiveData(){
        ringsLiveData.postValue(rings)
    }

}