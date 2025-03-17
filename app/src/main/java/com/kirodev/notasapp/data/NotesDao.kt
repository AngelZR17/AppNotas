package com.kirodev.notasapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes WHERE notes.id=:id")
    suspend fun getNoteById(id: Int) : Notes?

    @Query("SELECT * FROM Notes ORDER BY dateUpdated DESC")
    fun getNotes() : LiveData<List<Notes>>

    @Delete
    fun deleteNote(note: Notes) : Int

    @Update
    fun updateNote(note: Notes) : Int

    @Insert
    fun insertNote(note: Notes)
}