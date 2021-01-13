package com.example.mobilepresence.model

import com.example.mobilepresence.model.response.LoginResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("imei") imei: Int,
    ) : Single<LoginResponse.LoginResponse>
}