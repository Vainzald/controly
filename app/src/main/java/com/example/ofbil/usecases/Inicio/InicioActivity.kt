package com.example.ofbil.usecases.Inicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ofbil.HomeActivity
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.R
import com.example.ofbil.databinding.ActivityInicioBinding
import com.example.ofbil.usecases.Auth.AuthActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class InicioActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        setTheme(R.style.Theme_Controly)
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setup()
        setContentView(binding.root)
    }

    private fun setup(){
        val nombre_cuenta = prefs.getNombreCuenta()
        val mail = prefs.getId()

        if (nombre_cuenta.isNotEmpty() && mail.isNotEmpty()){
            val iHomeFragment = Intent(this, HomeActivity::class.java)
            startActivity(iHomeFragment)
            finish()
        }
        else{
            val iAuthActivity = Intent(this, AuthActivity::class.java)
            startActivity(iAuthActivity)
            finish()
        }
    }


}