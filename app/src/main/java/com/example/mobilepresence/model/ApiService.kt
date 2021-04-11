package com.example.mobilepresence.model

import com.example.mobilepresence.model.response.LoginObject
import com.example.mobilepresence.model.response.PassChangeObject
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT
import java.math.BigInteger

interface ApiService {

    //API Endpoint untuk fitur login
    @POST("auth/api/v1/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("imei") imei: BigInteger,
    ) : Single<LoginObject.LoginResponse>

    //API Endpoint untuk fitur passchange
    @PUT
    @FormUrlEncoded
    fun passchange(
        @Field("id_user") id_user : Int,
        @Field("pass_one") pass_one : String,
        @Field("pass_two") pass_two : String
    ) : Single<PassChangeObject.PasschangeResponse>
}