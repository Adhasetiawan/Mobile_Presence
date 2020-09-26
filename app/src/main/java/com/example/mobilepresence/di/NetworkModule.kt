package com.example.mobilepresence.di

import com.example.mobilepresence.model.ApiClient
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.provideOkHttpClient(get(),get(), get()) }
    single { ApiClient.provideApiClient(get()) }
    single { ApiClient.CacheInterceptor(get()) }
    single { ApiClient.hasNetwork(get()) }
}