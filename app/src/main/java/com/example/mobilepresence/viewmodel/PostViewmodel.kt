package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.repository.PostRepository
import com.example.mobilepresence.model.response.LocationObject
import com.example.mobilepresence.model.response.PostObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class PostViewmodel(val postRepository: PostRepository, val schedulerProvider: SchedulerProvider) :
    ViewModel() {
    private val compositeDisposables = CompositeDisposable()

    //value yang di observe
    private val postResponse: MutableLiveData<UiState<PostObject.PostResponse>> = MutableLiveData()

    //fungsi untuk menjalankan metode observasi pada value post trackrec
    fun getPostResponse(): LiveData<UiState<PostObject.PostResponse>> {
        return postResponse
    }

    //fungsi untuk menjalankan API post trackrec
    fun Post(
        post: String,
        date: String,
        arrivetime: String,
        leavingtime: String,
        latitude: Double,
        longitude: Double,
        location: String,
        id_user: Int
    ) {
        postResponse.postValue(UiState.Loading(true))
        postRepository.post(
            post,
            date,
            arrivetime,
            leavingtime,
            latitude,
            longitude,
            location,
            id_user
        )
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                postResponse.postValue(UiState.Success(it))
            }, { postResponse.postValue(UiState.Error(it)) }).addTo(compositeDisposables)
    }

    private val locationResponse: MutableLiveData<UiState<LocationObject.LocationResponse>> =
        MutableLiveData()

    fun getLocationResponse(): LiveData<UiState<LocationObject.LocationResponse>> {
        return locationResponse
    }

    fun getLocation() {
        locationResponse.postValue(UiState.Loading(true))
        postRepository.getLocation()
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                locationResponse.postValue(UiState.Success(it))
            }, {
                locationResponse.postValue(UiState.Error(it))
            }).addTo(compositeDisposables)
    }
}