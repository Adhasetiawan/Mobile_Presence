package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.model.repository.LoginRepository
import com.example.mobilepresence.model.repository.PostRepository
import com.example.mobilepresence.model.response.AbsenceObject
import com.example.mobilepresence.model.response.LocationObject
import com.example.mobilepresence.model.response.PostObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class PostViewmodel(
    private val postRepository: PostRepository,
    private val loginRepository: LoginRepository,
    private val schedulerProvider: SchedulerProvider
) :
    ViewModel() {
    private val compositeDisposables = CompositeDisposable()

    //get Id dan user name
    fun getIdUser() = loginRepository.getId()
    fun getName () = loginRepository.getName()

    //variabel respon value presence
    private val postResponse: MutableLiveData<UiState<PostObject.PostResponse>> = MutableLiveData()
    //observasi variabel value presence
    fun getPostResponse(): LiveData<UiState<PostObject.PostResponse>> {
        return postResponse
    }
    //fungsi utama fitur presence
    fun Post(post: String, date: String, arrivetime: String, leavingtime: String, latitude: Double, longitude: Double, location: String, id_user: Int) {
        postResponse.postValue(UiState.Loading(true))
        postRepository.post(post, date, arrivetime, leavingtime, latitude, longitude, location, id_user)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                postResponse.postValue(UiState.Success(it))
            }, { postResponse.postValue(UiState.Error(it)) }).addTo(compositeDisposables)
    }

    //Setting lokasi
    private val locationResponse = MutableLiveData<Resource<LocationObject.LocationResponse>>()
    fun getLocationResponse() = locationResponse
    fun getLocation(id_location: Int) {
        postRepository.getLocation(id_location)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                locationResponse.postValue(Resource.Success(it))
            }, {
            }).addTo(compositeDisposables)
    }
}