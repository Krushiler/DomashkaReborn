package com.example.domashkareborn

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

public object UserStatus : BaseObservable() {
    var isModerator = false

    @Bindable
    public fun getIsModerator():Boolean{
        return isModerator
    }

    public fun setIsModerator(isModerator: Boolean){
        this.isModerator = isModerator
        notifyPropertyChanged(BR.isModerator)
    }

}