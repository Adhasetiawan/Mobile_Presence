package com.example.mobilepresence.di

import com.example.mobilepresence.model.SharedPreference
import com.example.mobilepresence.model.repository.*
import com.example.mobilepresence.util.NetworkHelper
import com.example.mobilepresence.util.scheduler.AppSchedulerProvider
import com.example.mobilepresence.util.scheduler.SchedulerProvider
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.dsl.module


//respository masuk sini
val appModule = module {
    factory { GroupAdapter<GroupieViewHolder>() }
    single<SchedulerProvider> { AppSchedulerProvider() }
    single { NetworkHelper(get()) }
    single { SharedPreference(get()) }
    single { LoginRepository(get(), get())}
    single { PassChangeRepository(get()) }
    single { PostRepository(get()) }
    single { AbsenceRepository(get()) }
    single { TrackRecordRepository(get(), get()) }
    single { DetailRespository(get()) }
}