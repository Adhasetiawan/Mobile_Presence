package com.example.mobilepresence.di

import com.example.mobilepresence.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//viewmodel masuk sini
val viewModelModule = module {
    viewModel { LoginViewmodel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { PassChangeViewmodel(get(), get()) }
    viewModel { PostViewmodel(get(), get(), get()) }
    viewModel { AbsenceViewmodel(get(), get(), get()) }
    viewModel { TrackRecordViewModel(get(), get(), get()) }
}