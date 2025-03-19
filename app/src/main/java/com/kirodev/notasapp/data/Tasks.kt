package com.kirodev.notasapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity(tableName = Constants.TABLE_NAME_TASKS, indices =[Index(value = ["id"], unique = true)])
data class Tasks @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)    val id: Int? = null,
    @ColumnInfo(name = "task")          val task: String,
    @ColumnInfo(name = "dateUpdated")   val dateUpdated: String = fechaHoraActualTask()
)

fun fechaHoraActualTask() : String {
    val date = Date()
    val sdf = SimpleDateFormat("dd/MMMM/yyyy hh:mm a")
    return sdf.format(date)
}