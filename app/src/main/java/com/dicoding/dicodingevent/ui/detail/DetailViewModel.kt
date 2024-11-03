package com.dicoding.dicodingevent.ui

class DetailViewModel(eventId: String?) : EventViewModel(){
    init {
        findEvent("findId", eventId)
    }
}