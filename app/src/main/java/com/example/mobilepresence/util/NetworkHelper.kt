package com.example.mobilepresence.util

import android.content.Context
import android.net.ConnectivityManager
import timber.log.Timber

class NetworkHelper(val context: Context?)  {
    fun isConnectedToInternet():Boolean{
        try {
            if (context != null) {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
            }
            return false
        } catch (e: Exception) {
            Timber.e(e)
            return false
        }
    }
}