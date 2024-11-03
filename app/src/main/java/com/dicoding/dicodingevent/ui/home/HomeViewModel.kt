package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.dicodingevent.ui.EventViewModel

class HomeViewModel : EventViewModel() {
    override var tag = "Home_Fragment"

    init {
        findEvent("active")
        findEvent("nonActive")
    }
}