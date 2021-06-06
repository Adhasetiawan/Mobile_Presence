package com.example.mobilepresence.model

import com.example.mobilepresence.model.response.LocationObject
import com.example.mobilepresence.model.response.LoginObject
import com.example.mobilepresence.model.response.PassChangeObject
import com.example.mobilepresence.model.response.PostObject
import io.reactivex.Single
import retrofit2.http.*
import java.math.BigInteger

interface ApiService {

    //API Endpoint untuk fitur login
    @POST("auth/api/v1/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("imei") imei: BigInteger,
    ): Single<LoginObject.LoginResponse>

    //API Endpoint untuk fitur passchange
    @PUT("auth/api/v1/updatepass")
    @FormUrlEncoded
    fun passchange(
        @Field("id_user") id_user: Int,
        @Field("pass_one") pass_one: String,
        @Field("pass_two") pass_two: String
    ): Single<PassChangeObject.PasschangeResponse>

    //API Endpoint untuk fitur post trackrec
    @POST("tracrecord")
    @FormUrlEncoded
    fun post(
        @Field("post") post: String,
        @Field("date") date: String,
        @Field("arrivetime") arrivetime: String,
        @Field("leavingtime") leavingtime: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("location") location: String,
        @Field("id_user") id_user: Int
    ): Single<PostObject.PostResponse>

    //API Endpoint untuk get location
    @GET("getlocation")
    fun getLocation(
        @Query ("id_location") id_location : Int = 1
    ) : Single<LocationObject.LocationResponse>
}