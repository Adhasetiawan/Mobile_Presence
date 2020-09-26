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
            .create(ApiService::class.java) }

    const val cachesize = (5 * 1024 * 1024).toLong()
    fun provideOkHttpClient(pref : SharedPreference, context: Context, cacheInterceptor: CacheInterceptor) : OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.cache(Cache(context.cacheDir, cachesize))
        httpClient.connectTimeout(20, TimeUnit.SECONDS) // connect timeout
        httpClient.readTimeout(30, TimeUnit.SECONDS)  // socket timeout
        httpClient.writeTimeout(60, TimeUnit.SECONDS)
        httpClient.callTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging) }
        httpClient.addInterceptor (cacheInterceptor)
        httpClient.addInterceptor {
            val request = it.request().newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .addHeader("Authorization", "Bearer ${pref.accessToken}")
                .build()
            it.proceed(request) }
        return httpClient.build() }

    class CacheInterceptor(val context: Context) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = if (hasNetwork(context)!!) {
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            } else {
                request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
            }
            return chain.proceed(request)
        }
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}