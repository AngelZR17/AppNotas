package com.kirodev.notasapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kirodev.notasapp.data.Notes
import com.kirodev.notasapp.data.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.kirodev.notasapp.data.Tasks
import com.kirodev.notasapp.data.TasksDao

class TaskViewModel(
    private val db: TasksDao,
) : ViewModel() {

    val tasks: LiveData<List<Tasks>> = db.getTask()
    private val _selectedTask = MutableLiveData<Tasks?>(null)
    val selectedTask: LiveData<Tasks?> get() = _selectedTask

    fun setSelectedTask(task: Tasks?) {
        _selectedTask.value = task
    }

    fun deleteTask(task: Tasks) {
        viewModelScope.launch(Dispatchers.IO){
            db.deleteTask(task)
        }
    }

    fun updateTask(task: Tasks) {
        viewModelScope.launch(Dispatchers.IO){
            db.updateTask(task)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createTask(task: String) {
        viewModelScope.launch(Dispatchers.IO){
            db.insertTask(Tasks(task = task))
        }
    }

    suspend fun getTask(taskId : Int) : Tasks? {
        return db.getTaskById(taskId)
    }

}

class TaskViewModelFactory(
    private val db: TasksDao,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(
            db = db,
        ) as T
    }

}