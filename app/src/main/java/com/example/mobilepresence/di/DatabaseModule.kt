package com.example.mobilepresence.di

import com.example.mobilepresence.model.local.AppDatabase
import org.koin.dsl.module

//local room dao masuk sini
val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
}