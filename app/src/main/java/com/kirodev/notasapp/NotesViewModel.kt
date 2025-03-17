package com.kirodev.notasapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirodev.notasapp.data.Notes
import com.kirodev.notasapp.data.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(
    private val db: NotesDao,
) : ViewModel() {

    val notes: LiveData<List<Notes>> = db.getNotes()

    fun deleteNotes(note: Notes) {
        viewModelScope.launch(Dispatchers.IO){
            db.deleteNote(note)
        }
    }

    fun updateNote(note: Notes) {
        viewModelScope.launch(Dispatchers.IO){
            db.updateNote(note)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNote(title: String, note: String) {
        viewModelScope.launch(Dispatchers.IO){
            db.insertNote(Notes(title = title, note = note))
        }
    }

    suspend fun getNote(noteId : Int) : Notes? {
        return db.getNoteById(noteId)
    }

}

class NotesViewModelFactory(
    private val db: NotesDao,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  NotesViewModel(
            db = db,
        ) as T
    }

}