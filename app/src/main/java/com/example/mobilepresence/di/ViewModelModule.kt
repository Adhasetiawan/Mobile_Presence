package com.example.mobilepresence.di

import com.example.mobilepresence.viewmodel.LoginViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewmodel(get(), get()) }
}