package com.example.mobilepresence.di

import com.example.mobilepresence.model.local.AppDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
}