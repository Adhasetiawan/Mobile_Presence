package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.persistablenetworkresourcecall.Resource
import com.example.mobilepresence.model.repository.DetailRespository
import com.example.mobilepresence.model.repository.LoginRepository
import com.example.mobilepresence.model.response.DetailObject
import com.example.mobilepresence.model.response.EditObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class DetailTrackRecordViewModel(
    private val detailRespository: DetailRespository,
    private val loginRepository: LoginRepository,
    private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    fun id_user() = loginRepository.getId()

    private val responseDetail = MutableLiveData<Resource<DetailObject.DetailResponse>>()
    fun getResponseDetail() = responseDetail
    fun getResponseTrackRecord(date: String, id_user: Int) {
        detailRespository.getDetail(date, id_user)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                responseDetail.postValue(Resource.Success(it))
            }, {}).addTo(compositeDisposable)
    }

    private val editResponse = MutableLiveData<UiState<EditObject.EditResponse>>()
    fun getEditResponse(): LiveData<UiState<EditObject.EditResponse>> = editResponse
    fun EditAct(date: String, post: String, id_user: Int) {
        editResponse.postValue(UiState.Loading(true))
        detailRespository.editAct(date, post, id_user)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                editResponse.postValue(UiState.Success(it))
            }, {
                editResponse.postValue(UiState.Error(it))
            }).addTo(compositeDisposable)
    }
}