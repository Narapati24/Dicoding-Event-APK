package com.dicoding.dicodingevent.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class EventViewModel : ViewModel() {
    private val _event = MutableLiveData<List<ListEventsItem>>()
    val event: LiveData<List<ListEventsItem>> = _event

    private val _eventDetail = MutableLiveData<ListEventsItem>()
    val eventDetail: LiveData<ListEventsItem> = _eventDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    protected val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    protected val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent: LiveData<List<ListEventsItem>> = _finishedEvent

    protected open var tag = "Event_Model"

    open fun findEvent(data: String, data2: String? = null){
        _isLoading.value = true
        _isLoadingFinished.value = true
        _isLoadingUpcoming.value = true
        val client = when (data){
            "active" -> ApiConfig.getApiService().getEvent(active = 1)
            "nonActive" -> ApiConfig.getApiService().getEvent(active = 0)
            "find" -> ApiConfig.getApiService().getEvent(keyword = data2)
            "findId" -> ApiConfig.getApiService().getEventDetail(data2!!)
            "all" -> ApiConfig.getApiService().getEvent()
            else -> ApiConfig.getApiService().getEvent()
        }
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _event.value = response.body()?.listEvents
                    when(data){
                        "findId" -> _eventDetail.value = response.body()?.event
                        "active" -> {
                            _upcomingEvent.value = response.body()?.listEvents
                            _isLoadingUpcoming.value = false
                        }
                        "nonActive" -> {
                            _finishedEvent.value = response.body()?.listEvents
                            _isLoadingFinished.value = false
                        }
                    }
                } else {
                    Log.e(tag, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(tag, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun resetLoading(){
        _isLoading.value = true
    }
}