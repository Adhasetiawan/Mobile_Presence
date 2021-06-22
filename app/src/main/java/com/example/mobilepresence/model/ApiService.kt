package com.example.mobilepresence.model

import com.example.mobilepresence.model.response.*
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
    @POST("presence")
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
    @GET("getlocation/{id_location}")
    fun getLocation(
        @Path ("id_location") id_location : Int
    ) : Single<LocationObject.LocationResponse>

    //API Endpoint untuk fitur absence
    @PUT("absence")
    @FormUrlEncoded
    fun absence(
        @Field("leavingtime") leavingtime : String,
        @Field("date") date : String,
        @Field("id_user") id_user : Int
    ): Single<AbsenceObject.AbsenceResponse>

    //APiI Endpoint untuk get trackrecord
    @GET("trackrecord/{id_user}/{date_one}/{date_two}")
    fun trackRecord(
        @Path ("id_user") id_user : Int,
        @Path ("date_one") date_one : String,
        @Path ("date_two") date_two : String
    ) : Single <RecordObject.TrackrecordResponse>
}