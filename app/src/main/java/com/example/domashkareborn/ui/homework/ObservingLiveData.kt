package com.example.domashkareborn.ui.homework

import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData


class ObservingLiveData<T:BaseObservable>: MutableLiveData<T>() {

    override fun setValue(value: T?) {
        super.setValue(value)
        value?.addOnPropertyChangedCallback(callback)
    }
    var callback: Observable.OnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            value = value
        }
    }

}