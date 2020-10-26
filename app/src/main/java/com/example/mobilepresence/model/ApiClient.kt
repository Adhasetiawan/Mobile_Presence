package com.example.mobilepresence.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.mobilepresence.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun provideApiClient(okHttpClient : OkHttpClient) : ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun provideOkHttpClient(pref: SharedPreference) : OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.connectTimeout(20, TimeUnit.SECONDS) // connect timeout
        httpClient.readTimeout(30, TimeUnit.SECONDS)  // socket timeout
        httpClient.writeTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        httpClient.addInterceptor {
            val request = it.request().newBuilder()
                .addHeader("Authorization", "Bearer ${pref.accessToken}")
                .build()
            it.proceed(request)
        }

        return httpClient.build()
    }
}