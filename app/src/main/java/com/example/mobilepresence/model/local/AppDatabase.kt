package com.example.mobilepresence.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobilepresence.model.local.dao.TrackRecordDao
import com.example.mobilepresence.model.local.entity.TrackRecord

@Database(
    entities =[TrackRecord::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TrackRecordDao(): TrackRecordDao

    companion object {
        fun getInstance(context: Context) : AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "mobilepresence_db"
        ).build()
    }
}