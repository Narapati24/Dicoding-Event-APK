package com.dicoding.dicodingevent.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.ui.setting.SettingPreferences
import com.dicoding.dicodingevent.ui.setting.SettingViewModel

class ViewModelSettingFactory private constructor(
    private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelSettingFactory? = null
        @JvmStatic
        fun getInstance(pref: SettingPreferences): ViewModelSettingFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelSettingFactory(pref)
                }
            }
            return INSTANCE as ViewModelSettingFactory
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(pref)  as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}