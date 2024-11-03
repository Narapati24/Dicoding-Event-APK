package com.dicoding.dicodingevent.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.FavoriteEvent
import com.dicoding.dicodingevent.repository.FavoriteEventRepository

class FavoriteEventViewModel(application: Application):ViewModel() {
    private val mFavoriteEventRepository : FavoriteEventRepository = FavoriteEventRepository(application)

    fun insert(favoriteEvent: FavoriteEvent){
        mFavoriteEventRepository.insert(favoriteEvent)
    }

    fun delete(favoriteEvent: FavoriteEvent){
        mFavoriteEventRepository.delete(favoriteEvent)
    }

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent>{
        return mFavoriteEventRepository.getFavoriteEventById(id)
    }

    fun getAllFavoriteEvent(): LiveData<List<FavoriteEvent>>{
        return mFavoriteEventRepository.getAllFavoriteEvent()
    }
}