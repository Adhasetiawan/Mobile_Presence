package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.response.AbsenceObject
import io.reactivex.Single

class AbsenceRepository (val apiService: ApiService) {
    fun absence(id_user: Int, date : String, leavingtime : String) : Single<AbsenceObject.AbsenceResponse>{
        return apiService.absence(id_user, date, leavingtime)
    }
}