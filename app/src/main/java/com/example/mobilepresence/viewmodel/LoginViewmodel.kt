package com.example.mobilepresence.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilepresence.model.UiState
import com.example.mobilepresence.model.repository.LoginRepository
import com.example.mobilepresence.model.response.LoginObject
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.math.BigInteger

class LoginViewmodel (private val loginRepository: LoginRepository, private val schedulerProvider : SchedulerProvider) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val loginResponse : MutableLiveData<UiState<LoginObject.LoginResponse>> = MutableLiveData()

    fun getLoginResponse() : LiveData<UiState<LoginObject.LoginResponse>>{
        return loginResponse
    }

    fun login(email: String, password: String, imei: BigInteger){
        //UiStatus
        loginResponse.postValue(UiState.Loading(true))
        //API Running
        loginRepository.login(email, password, imei)
            .observeOn(schedulerProvider.ui())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                loginResponse.postValue(UiState.Success(it))
            },{
                loginResponse.postValue(UiState.Error(it))
            }).addTo(compositeDisposable)
    }

    fun isLogin() : Boolean = loginRepository.isLogin()

    fun loginStatus(){
        loginRepository.loginStatus(true)
    }

    fun saveToken(token: String?){
        loginRepository.token(token)
    }

    fun savePict(pictUrl: String?){
        loginRepository.savePict(pictUrl)
    }

    fun saveResponse (name: String?, id: Int?, division: String?){
        loginRepository.saveResponse(name, id, division)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}