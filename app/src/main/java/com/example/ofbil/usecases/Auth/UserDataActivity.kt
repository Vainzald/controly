package com.example.ofbil.usecases.Auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ofbil.HomeActivity
import com.example.ofbil.Model.UsuarioInfoCuenta
import com.example.ofbil.Provider.Preferencias.PrefenciasUsuario
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.databinding.ActivityUserDataBinding
import com.example.ofbil.usecases.common.alertForError

class UserDataActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserDataBinding
    private var VMFirebase = FirestoreViewModel()
    private var error = alertForError()
    private lateinit var prefs : SharedPreferences
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDataBinding.inflate(layoutInflater)
        setup()
        setContentView(binding.root)
    }

    fun setup(){
        binding.actDataBtnAddInfo.setOnClickListener { dataUser() }
    }

    fun dataUser(){

        if (binding.actDataNombreCuenta.text.isNotEmpty() && binding.actDataCantidad.text.isNotEmpty()){
            val nombreCuenta = binding.actDataNombreCuenta.text.toString()
            val cantidad = binding.actDataCantidad.text.toString().toInt()
            val informationUser = UsuarioInfoCuenta(
                nombreCuenta = nombreCuenta,
                cantidad = cantidad
            )
            VMFirebase.addInformationUser(informationUser) { iniciarActividad() }

        }
        else{
            error.showError(
                this,
                "Error",
            "Porfavor, rellenar todos los datos")
        }


    }
    fun iniciarActividad(){
        val iIniciarActividad = Intent(this, HomeActivity::class.java)
        startActivity(iIniciarActividad)
    }



}