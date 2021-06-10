package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.repository.AbsenceRepository
import com.example.mobilepresence.model.repository.LoginRepository
import com.example.mobilepresence.model.response.AbsenceObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class AbsenceViewmodel(private val absenceRepository: AbsenceRepository, private val loginRepository: LoginRepository, private val schedulerProvider: SchedulerProvider) : ViewModel() {

    private val compositeDisposables = CompositeDisposable()

    val absenceResponse : MutableLiveData<UiState<AbsenceObject.AbsenceResponse>> = MutableLiveData()

    fun getAbsenceResponse () : LiveData<UiState<AbsenceObject.AbsenceResponse>>{
        return absenceResponse
    }

    fun absence(id_user: Int, date: String, leavingtime : String){
        absenceResponse.postValue(UiState.Loading(true))
        absenceRepository.absence(id_user, date, leavingtime)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                absenceResponse.postValue(UiState.Success(it))
            },{
                absenceResponse.postValue(UiState.Error(it))
            }).addTo(compositeDisposables)
    }

    fun getIduser() = loginRepository.getId()
}