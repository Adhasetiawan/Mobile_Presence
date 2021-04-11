package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.response.PassChangeObject
import io.reactivex.Single

class PassChangeRepository (val apiService: ApiService) {

    //API Trigger untuk menjalankan fungsi pada kelas interface dengan parameter sudah yang disiapkan
    fun PassChange (id_user : Int, pass_one : String, pass_two : String) : Single<PassChangeObject.PasschangeResponse>{
        return apiService.passchange(id_user, pass_one, pass_two)
    }
}