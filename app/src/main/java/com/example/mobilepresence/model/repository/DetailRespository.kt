package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.response.DetailObject
import com.example.mobilepresence.model.response.EditObject
import io.reactivex.Single

class DetailRespository (private val apiService: ApiService) {

    fun getDetail (date : String, id_user:Int) : Single <DetailObject.DetailResponse>{
        return apiService.detailRec(date, id_user)
    }

    fun editAct (date:String, post:String, id_user:Int) : Single<EditObject.EditResponse>{
        return apiService.editAct(date, post, id_user)
    }
}