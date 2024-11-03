package com.dicoding.dicodingevent.ui.upcoming

import com.dicoding.dicodingevent.ui.EventViewModel

class UpcomingViewModel : EventViewModel() {
    override var tag = "Upcoming_Fragment"
    init {
        findEvent("active")
    }
}