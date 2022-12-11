package com.example.ofbil.Model

data class Transaccion(

    val categorias: Categorias,
    val cantidad : Int,
    val fechaCompleta: String,
    val year : Int,
    val mes : Int,
    val dia : Int,
    val nombreGasto : String,
    val estado : String,
    val tipoMovimiento : String,
    val nombreCategoria : String,
    val nombreCuenta : String

)