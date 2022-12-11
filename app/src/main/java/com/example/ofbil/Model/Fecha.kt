package com.example.ofbil.Model

data class Fecha(
    val mes : Int,
    var año : Int,
    val dia : Int? = null,
    val fechaCompleta : String? = null
)