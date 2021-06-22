package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.response.DetailObject
import io.reactivex.Single

class DetailRespository (private val apiService: ApiService) {

    fun getDetail (date : String, id_user:Int) : Single <DetailObject.DetailResponse>{
        return apiService.detailRec(date, id_user)
    }
}