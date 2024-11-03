package com.dicoding.dicodingevent.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.dicodingevent.data.FavoriteEvent
import com.dicoding.dicodingevent.data.FavoriteEventDao
import com.dicoding.dicodingevent.data.FavoriteEventRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteEventRepository(application: Application) {
    private val mFavoriteEventDao: FavoriteEventDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteEventRoomDatabase.getDatabase(application)
        mFavoriteEventDao = db.favoriteEventDao()
    }

    fun insert(favoriteEvent: FavoriteEvent){
        executorService.execute { mFavoriteEventDao.insert(favoriteEvent) }
    }

    fun delete(favoriteEvent: FavoriteEvent){
        executorService.execute { mFavoriteEventDao.delete(favoriteEvent) }
    }

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> = mFavoriteEventDao.getFavoriteEventById(id)
    fun getAllFavoriteEvent(): LiveData<List<FavoriteEvent>> = mFavoriteEventDao.getAllFavoriteEvent()
}