package com.kirodev.notasapp.app

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.room.Room
import com.kirodev.notasapp.Constants
import com.kirodev.notasapp.data.AppDatabase
import com.kirodev.notasapp.data.NotesDao

class NotesApplication : Application(){
    private var db : AppDatabase? = null

    init {
        instance = this
    }

    private fun getDb(): AppDatabase {
        return if (db != null){
            db!!
        } else {
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                AppDatabase::class.java, Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
            db!!
        }
    }

    companion object {
        private var instance: NotesApplication? = null

        fun getDao(): NotesDao {
            return instance!!.getDb().NotesDao()
        }
    }
}