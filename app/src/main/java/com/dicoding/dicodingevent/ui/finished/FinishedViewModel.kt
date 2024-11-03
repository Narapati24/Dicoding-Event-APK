package com.dicoding.dicodingevent.ui.finished

import com.dicoding.dicodingevent.ui.EventViewModel

class FinishedViewModel : EventViewModel() {
    override var tag = "FinishedEvent"
    init {
        findEvent("nonActive")
    }
}