package com.example.mobilepresence.di

import com.example.mobilepresence.model.local.AppDatabase
import com.example.mobilepresence.model.local.dao.TrackRecordDao
import org.koin.dsl.module

//local room dao masuk sini
val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
    single { provideTrackRecordDao(get()) }
}

fun provideTrackRecordDao(database: AppDatabase) : TrackRecordDao = database.TrackRecordDao()