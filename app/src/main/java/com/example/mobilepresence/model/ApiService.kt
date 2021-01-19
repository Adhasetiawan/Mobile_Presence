package com.example.mobilepresence.model

import com.example.mobilepresence.model.response.LoginObject
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.math.BigInteger

interface ApiService {
    @POST("auth/api/v1/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("imei") imei: BigInteger,
    ) : Single<LoginObject.LoginResponse>
}