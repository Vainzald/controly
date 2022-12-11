package com.example.ofbil.Provider.Preferencias

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class PrefenciasUsuario(context: Context) {
    private val preferences = "preferencias"
    private val idUser = "idUser"
    private val nombre = "nombre"
    private val apellido = "apellido"
    private val nombre_cuenta = "nombre_cuenta"
    private val aceptar_Notificaciones = "notificaciones"

    val storage = context.getSharedPreferences(preferences, 0)

    fun saveId(id : String){
        storage.edit().putString(idUser, id).apply()
    }
    fun saveNombre_cuenta(nombreCuenta : String){
        storage.edit().putString(nombre_cuenta, nombreCuenta).apply()
    }
    fun getId(): String{
        return storage.getString(idUser, "")!!
    }
    fun  getNombreCuenta() : String {
        return storage.getString(nombre_cuenta, "")!!
    }
    fun saveNotificaciones(acept : Boolean){
        storage.edit().putBoolean(aceptar_Notificaciones, acept).apply()
    }
    fun getNotificaciones() : Boolean{
        return storage.getBoolean(aceptar_Notificaciones, false)
    }

    fun cerrarSesion() {
        storage.edit().remove(nombre_cuenta).apply()
        storage.edit().remove(idUser).apply()
        storage.edit().remove(nombre).apply()
        storage.edit().putBoolean(aceptar_Notificaciones, false).apply()



    }

}