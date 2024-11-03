package com.dicoding.dicodingevent.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.ui.DetailViewModel
import com.dicoding.dicodingevent.ui.FavoriteEventViewModel

class ViewModelFactory internal constructor(
    private val mApplication: Application,
    private val eventId: String? = null
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application, eventId: String? = null): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, eventId)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteEventViewModel::class.java)) {
            return FavoriteEventViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return eventId?.let { DetailViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}