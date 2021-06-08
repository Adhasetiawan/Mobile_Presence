package com.example.mobilepresence.di

import com.example.mobilepresence.viewmodel.LoginViewmodel
import com.example.mobilepresence.viewmodel.PassChangeViewmodel
import com.example.mobilepresence.viewmodel.PostViewmodel
import com.example.mobilepresence.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//viewmodel masuk sini
val viewModelModule = module {
    viewModel { LoginViewmodel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { PassChangeViewmodel(get(), get()) }
    viewModel { PostViewmodel(get(), get(), get()) }
}