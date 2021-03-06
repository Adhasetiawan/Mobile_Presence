package com.example.mobilepresence.model.repository

import com.example.mobilepresence.model.ApiService
import com.example.mobilepresence.model.response.AbsenceObject
import com.example.mobilepresence.model.response.LocationObject
import com.example.mobilepresence.model.response.PostObject
import io.reactivex.Single

class PostRepository(val apiService: ApiService) {

    //Repository data dari respon pada Endpoint presence
    fun post(post: String, date: String, arrivetime: String, leavingtime: String, latitude: Double, longitude: Double, location: String, id_user: Int): Single<PostObject.PostResponse> {
        return apiService.post(post, date, arrivetime, leavingtime, latitude, longitude, location, id_user)
    }

    //Repository data dari respon pada Endpoint getLocation
    fun getLocation(id_location: Int): Single<LocationObject.LocationResponse> {
        return apiService.getLocation(id_location)
    }
}