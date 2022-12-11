package com.example.ofbil.usecases.Inicio

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.os.BuildCompat
import com.example.ofbil.HomeActivity
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.Provider.Servicios.Firebase.AutenticacionUsuario
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.databinding.ActivityPerfilBinding
import com.google.firebase.messaging.FirebaseMessaging

class PerfilActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPerfilBinding
    private lateinit var auth : AutenticacionUsuario
    private lateinit var VMFirestore : FirestoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)

        setup()
        setContentView(binding.root)
    }



    fun setup(){
        back()
        VMFirestore = FirestoreViewModel()
        dataUser()
        auth = AutenticacionUsuario()
        Log.d("Notificaciones", prefs.getNotificaciones().toString())
        val notification = prefs.getNotificaciones()
        binding.aceptarNotificaciones.isChecked = notification
        binding.aceptarNotificaciones.setOnCheckedChangeListener{ no,si ->
            if(si){
                if(!notification){
                    FirebaseMessaging.getInstance().subscribeToTopic("Concejos")
                    VMFirestore.VMUpdateNotifies(true)
                }
                prefs.saveNotificaciones(true)

            }
            else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Concejos")
                VMFirestore.VMUpdateNotifies(false)
                prefs.saveNotificaciones(false)
            }
        }


        binding.botin.setOnClickListener {
            prefs.saveNotificaciones(false)
            prefs.cerrarSesion()
            auth.cerrarSesion()
            finish()
            HomeActivity().finish()
            val iSlpah = Intent(this, InicioActivity::class.java)
            startActivity(iSlpah)
        }
        binding.backActivity.setOnClickListener {
            finish()
            controlActivity()
        }

    }

    fun dataUser(){
        binding.mail.text = prefs.getId()
        binding.nombreCuenta.text = prefs.getNombreCuenta()
    }
    @androidx.annotation.OptIn(androidx.core.os.BuildCompat.PrereleaseSdkCheck::class)
    fun back(){
        if (BuildCompat.isAtLeastT()) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                // Back is pressed... Finishing the activity
                controlActivity()
                finish()

            }
        } else {
            onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Back is pressed... Finishing the activity
                    controlActivity()
                    finish()
                }
            })
        }
    }

    fun controlActivity(){
        val iHome = Intent(this, HomeActivity::class.java)
        startActivity(iHome)

    }
}