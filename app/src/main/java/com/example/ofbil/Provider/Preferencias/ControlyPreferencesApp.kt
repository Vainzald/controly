package com.example.ofbil.Provider.Preferencias

import android.app.Application

class ControlyPreferencesApp : Application() {

    companion object{
        lateinit var prefs : PrefenciasUsuario
    }
    override fun onCreate() {
        super.onCreate()
        prefs = PrefenciasUsuario(applicationContext)
    }
}