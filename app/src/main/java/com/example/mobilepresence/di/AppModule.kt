package com.example.mobilepresence.di

import com.example.mobilepresence.model.SharedPreference
import com.example.mobilepresence.model.repository.LoginRepository
import com.example.mobilepresence.model.repository.PassChangeRepository
import com.example.mobilepresence.util.NetworkHelper
import com.example.mobilepresence.util.scheduler.AppSchedulerProvider
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.koin.dsl.module


//respository masuk sini
val appModule = module {
    factory { GroupAdapter<ViewHolder>() }
    single<SchedulerProvider> { AppSchedulerProvider() }
    single { NetworkHelper(get()) }
    single { SharedPreference(get()) }
    single { LoginRepository(get(), get())}
    single { PassChangeRepository(get()) }
}