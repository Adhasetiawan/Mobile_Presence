package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.repository.PassChangeRepository
import com.example.mobilepresence.model.response.PassChangeObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class PassChangeViewmodel (val passChangeRepository: PassChangeRepository, val schedulerProvider: SchedulerProvider) : ViewModel(){

    private val compositeDisposables = CompositeDisposable()

    //value yang di observe
    private val changeResponse : MutableLiveData<UiState<PassChangeObject.PasschangeResponse>> = MutableLiveData()

    //fungsi untuk menjalankan fitur observer
    fun getChangeResponse() : LiveData<UiState<PassChangeObject.PasschangeResponse>>{
        return changeResponse
    }

    //Trigger API pada Passchange
    fun Passchange (id_user : Int, pass_one : String, pass_two : String){
        changeResponse.postValue(UiState.Loading(true))
        passChangeRepository.PassChange(id_user, pass_one, pass_two)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                changeResponse.postValue(UiState.Success(it))
            },{
                changeResponse.postValue(UiState.Error(it))
            }).addTo(compositeDisposables)
    }
}