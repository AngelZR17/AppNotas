package com.kirodev.notasapp.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date

@Entity(tableName = Constants.TABLE_NAME, indices =[Index(value = ["id"], unique = true)])
data class Notes @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)    val id: Int? = null,
    @ColumnInfo(name = "note")          val note: String,
    @ColumnInfo(name = "title")         val title: String,
    @ColumnInfo(name = "dateUpdated")   val dateUpdated: String = fechaHoraActual(),
)

fun fechaHoraActual() : String {
    val date = Date()
    val sdf = SimpleDateFormat("dd/MMMM/yyyy hh:mm a")
    return sdf.format(date)
}