package com.kirodev.notasapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun NotesDao(): NotesDao
}