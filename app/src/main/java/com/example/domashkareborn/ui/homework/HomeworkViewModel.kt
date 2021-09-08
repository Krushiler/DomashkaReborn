package com.example.domashkareborn.ui.homework

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import com.example.domashkareborn.*
import com.example.domashkareborn.ciceronnav.NormalRouter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.security.auth.Subject

class HomeworkViewModel(
    var firebase: Firebase,
    private val router: NormalRouter,
    var userStatus: UserStatus,
    application: Application
) : AndroidViewModel(application) {

    val homeworkLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val scheduleLiveData: MutableLiveData<List<Int>> = MutableLiveData()
    val additionalSubjectsLiveData: MutableLiveData<List<String>> = MutableLiveData()
    val isModeratorLiveData: ObservingLiveData<UserStatus> = ObservingLiveData<UserStatus>()

    var homework = MutableList(60) { "" }
    var schedule = MutableList(60) { 0 }
    var additionalSubjects:MutableList<String> = mutableListOf()
    var isModerator = false

    private val userRef = firebase.database.reference.child(firebase.auth.uid.toString())

    init {
        userRef.keepSynced(true)
        getHomework()
        getAdditionalSubjects()
        getModerator()
    }

    public fun getAdditionalSubjects(){
        val additionalSubjectsListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                additionalSubjects = mutableListOf()
                for (additionalSubjectsSnapshot:DataSnapshot in snapshot.child("AddictiveSubjects").children){
                    additionalSubjects.add(additionalSubjectsSnapshot.value.toString())
                }
                additionalSubjectsLiveData.postValue(additionalSubjects)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        userRef.addValueEventListener(additionalSubjectsListener)
    }

    public fun getHomework() {
        homeworkLiveData.postValue(homework)
        scheduleLiveData.postValue(schedule)
        val homeworkListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (curHomework in homework.indices) {
                    var curSubFrom = snapshot.child("Homework$curHomework").value.toString()
                    if (curSubFrom!="null") {
                        homework[curHomework] = curSubFrom
                    }
                }
                for (curSubject in schedule.indices) {
                    var curSubFromFb = snapshot.child("Subjects$curSubject").value.toString()
                    if (curSubFromFb.isDigitsOnly()) {
                        schedule[curSubject] = curSubFromFb.toInt()
                    }
                }
                homeworkLiveData.postValue(homework)
                scheduleLiveData.postValue(schedule)
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        userRef.addValueEventListener(homeworkListener)
    }

    public fun getModerator(){
        isModeratorLiveData.postValue(UserStatus)
        isModerator = isModeratorLiveData.value?.getIsModerator() == false
    }


    var homeworkETIDs = mutableListOf<Int>()
    var subjectSPIDs = mutableListOf<Int>()
    var tasksIDs = mutableListOf<Int>()

    public fun getTasksIds(count: Int):List<Int>{
        if (tasksIDs.size != count){
            tasksIDs = mutableListOf<Int>()
            for (i in 1..count){
                tasksIDs.add(View.generateViewId())
            }
        }
        return tasksIDs
    }

    public fun getSubjectSPIds(count: Int):List<Int>{
        if (subjectSPIDs.size != count){
            subjectSPIDs = mutableListOf<Int>()
            for (i in 1..count){
                subjectSPIDs.add(View.generateViewId())
            }
        }
        return subjectSPIDs
    }
    public fun getHomeworkETIds(count: Int):List<Int>{
        if (homeworkETIDs.size != count){
            homeworkETIDs = mutableListOf<Int>()
            for (i in 1..count){
                homeworkETIDs.add(View.generateViewId())
            }
        }
        return homeworkETIDs
    }

    var logout:MutableLiveData<Boolean> = MutableLiveData(false)



    public fun saveHomework(enteredHomewrok:List<String>){
        for (i in enteredHomewrok.indices){
            userRef.child("Homework$i").setValue(enteredHomewrok[i])
        }
    }
    public fun saveShedule(enteredShedule:List<Int>){
        for (i in enteredShedule.indices){
            userRef.child("Subjects$i").setValue(enteredShedule[i].toString())
        }
    }


    public fun logout(){
        firebase.auth.signOut()
        var sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences(
            FIREBASE_PREFERENCES,
            Context.MODE_PRIVATE
        )
        var editor = sharedPreferences.edit()
        editor.remove(FIREBASE_PREFERENCES_CLASSCODE)
        editor.remove(FIREBASE_PREFERENCES_MODERATORCODE)
        editor.apply()
        logout.postValue(true)
    }

    public fun setLiveData(){
        homeworkLiveData.postValue(homework)
        scheduleLiveData.postValue(schedule)
    }

    public fun goToSubjects(){
        router.navigate(Screens.SubjectsScreen())
    }

}