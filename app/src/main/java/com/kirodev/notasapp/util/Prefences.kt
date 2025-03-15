package com.kirodev.notasapp.util

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    private val nombrePreferencias = "MisPreferencias"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(nombrePreferencias, Context.MODE_PRIVATE)

    fun guardarEntero(clave: String, valor: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(clave, valor)
        editor.apply()
    }

    fun obtenerEntero(clave: String, valorPorDefecto: Int): Int {
        return sharedPreferences.getInt(clave, valorPorDefecto)
    }

    fun guardarString(clave: String, valor: String) {
        val editor = sharedPreferences.edit()
        editor.putString(clave, valor)
        editor.apply()
    }

    fun obtenerString(clave: String, valorPorDefecto: String): String? {
        return sharedPreferences.getString(clave, valorPorDefecto)
    }

}