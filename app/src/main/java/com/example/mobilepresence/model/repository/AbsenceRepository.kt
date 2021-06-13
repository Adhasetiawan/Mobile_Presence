package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.response.AbsenceObject
import io.reactivex.Single

class AbsenceRepository (val apiService: ApiService) {
    //ketersedian data atau respon pada fitur absence
    fun absence(leavingtime : String, date : String, id_user : Int) : Single<AbsenceObject.AbsenceResponse>{
        return apiService.absence(leavingtime, date, id_user)
    }
}