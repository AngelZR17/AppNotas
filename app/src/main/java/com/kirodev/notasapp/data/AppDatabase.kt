package com.kirodev.notasapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notes::class, Tasks::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun NotesDao(): NotesDao
    abstract fun TasksDao(): TasksDao
}