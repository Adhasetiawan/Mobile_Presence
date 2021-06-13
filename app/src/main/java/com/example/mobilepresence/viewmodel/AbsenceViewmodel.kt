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

    // variabel response dari API absence
    val absenceResponse : MutableLiveData<UiState<AbsenceObject.AbsenceResponse>> = MutableLiveData()

    // fungsi untuk mendapatkan response dari variabel
    fun getAbsenceResponse () : LiveData<UiState<AbsenceObject.AbsenceResponse>>{
        return absenceResponse
    }

    // fungsi untuk menjalankan API absence
    fun absence(leavingtime : String, date : String, id_user : Int){
        absenceResponse.postValue(UiState.Loading(true))
        absenceRepository.absence(leavingtime,date, id_user)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                absenceResponse.postValue(UiState.Success(it))
            },{
                absenceResponse.postValue(UiState.Error(it))
            }).addTo(compositeDisposables)
    }

    fun getIduser() = loginRepository.getId()
    fun getName() = loginRepository.getName()
}