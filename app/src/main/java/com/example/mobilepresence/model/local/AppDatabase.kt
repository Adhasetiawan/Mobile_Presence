package com.example.mobilepresence.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(
//    entities =[Movie::class],
//    version = 1
//)
abstract class AppDatabase : RoomDatabase() {
//    abstract fun MovieDao(): MovieDao

    companion object {
        fun getInstance(context: Context) : AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "movie_db"
        ).build()
    }
}