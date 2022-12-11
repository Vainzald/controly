package com.example.ofbil.Model

data class Usuario(
    val nombre: String,
    val apellido : String,
//    val organizacion : String,
    val fechaNacimiento : String,
    val idUsuario : String,
    val notificacion : Boolean
)
data class UsuarioInfoCuenta(
    val nombreCuenta : String,
    val cantidad : Int
)