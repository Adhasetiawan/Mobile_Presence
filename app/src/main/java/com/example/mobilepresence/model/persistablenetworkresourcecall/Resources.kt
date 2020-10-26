package com.example.mobilepresence.model.persistablenetworkresourcecall

import com.example.mobilepresence.util.Status

sealed class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    data class Loading<T>(val loadingData: T?) : Resource<T>(Status.LOADING, loadingData, null)
    data class Success<T>(val successData: T?) : Resource<T>(Status.SUCCESS, successData, null)
    data class Error<T>(val msg: String, val error: T?) : Resource<T>(Status.ERROR, error, msg)
}