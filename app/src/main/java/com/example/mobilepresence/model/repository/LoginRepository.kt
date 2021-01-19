package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.SharedPreference
import com.example.mobilepresence.model.response.LoginObject
import io.reactivex.Single
import java.math.BigInteger

class LoginRepository (val apiService : ApiService, val sharedPreferance : SharedPreference){

    //API Trigger
    fun login (email: String, password: String, imei: BigInteger): Single<LoginObject.LoginResponse>{
        return apiService.login(email, password, imei)
    }

    //islogin
    fun isLogin (): Boolean = sharedPreferance.isLogin

    //Login Status
    fun loginStatus(isLogin: Boolean){
        sharedPreferance.isLogin = isLogin
    }

    //token
    fun token (token: String?){
        sharedPreferance.accessToken = token
    }

    //getToken
    fun getToken() = sharedPreferance.accessToken

    //savePict
    fun savePict(pict : String?){
        sharedPreferance.photo_profile
    }

    fun saveResponse (name : String?, id: Int?, division: String?){
        sharedPreferance.nameUser = name
        sharedPreferance.idUser = id
        sharedPreferance.divisionUser = division
    }

    //profilegetter
    fun getName() = sharedPreferance.nameUser
    fun getId () = sharedPreferance.idUser
    fun getDivision() = sharedPreferance.divisionUser
    fun getPict() = sharedPreferance.photo_profile

    //sharedpreference clear
    fun cleanpref (){
        sharedPreferance.clear()
    }
}