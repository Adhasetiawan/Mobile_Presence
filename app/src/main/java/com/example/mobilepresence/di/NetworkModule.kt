package com.example.mobilepresence.di

import com.example.mobilepresence.model.ApiClient
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.provideOkHttpClient(get()) }
    single { ApiClient.provideApiClient(get()) }
}