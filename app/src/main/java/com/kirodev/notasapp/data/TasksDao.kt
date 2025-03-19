package com.kirodev.notasapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TasksDao {
    @Query("SELECT * FROM Tasks WHERE tasks.id=:id")
    suspend fun getTaskById(id: Int) : Tasks?

    @Query("SELECT * FROM Tasks")
    fun getTask() : LiveData<List<Tasks>>

    @Delete
    fun deleteTask(task: Tasks) : Int

    @Update
    fun updateTask(task: Tasks) : Int

    @Insert
    fun insertTask(task: Tasks)
}