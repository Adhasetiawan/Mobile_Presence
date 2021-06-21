package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.local.entity.TrackRecord
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.model.repository.LoginRepository
import com.example.mobilepresence.model.repository.TrackRecordRepository
import com.example.mobilepresence.model.response.RecordObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class TrackRecordViewModel(private val trackRecordRepository: TrackRecordRepository, private val loginRepository: LoginRepository, private val schedulerProvider: SchedulerProvider) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    fun id_user() = loginRepository.getId()

    private val responseHeader : MutableLiveData<UiState<List<RecordObject.ObjectResponse>>> = MutableLiveData()
    fun getResponseHeader() : LiveData<UiState<List<RecordObject.ObjectResponse>>> = responseHeader

    private val listTrackRecord : MutableLiveData<Resource<List<TrackRecord>>> = MutableLiveData()
    fun observeListTrackRecord(): LiveData<Resource<List<TrackRecord>>> = listTrackRecord
    fun getTrackRecord(id_user:Int, date_one:String, date_two:String, page:Int){
        trackRecordRepository.getListTrackRecord(id_user, date_one, date_two, page)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe ({
                        listTrackRecord.postValue(Resource.Success(it.data))
            },{}).addTo(compositeDisposable)
    }
}