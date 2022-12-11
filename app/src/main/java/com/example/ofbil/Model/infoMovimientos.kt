package com.example.ofbil.Model

import com.google.android.datatransport.cct.StringMerger
import java.util.Objects

data class infoMovimientos (
    val nombreCategoria : String,
    val cantidad : Int,
    val fecha : String,
    val nombreIngreso : String? = null,
    val tipoIngreso : String? = null,
    val cantidadTransacciones : Int = 0
)
data class infoMovimientoBarChar(
    val cantidad : Int,
    val mes : Float
){
    companion object{
        val listaGastosProvider = mutableListOf<infoMovimientoBarChar>(
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 1f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 2f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 3f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 4f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 5f
            ),infoMovimientoBarChar(
                cantidad = 1,
                mes = 6f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 7f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 8f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 9f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 10f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 11f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 12f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 2f
            ),

        )

        val listaIngresosProvider = mutableListOf<infoMovimientoBarChar>(
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 1f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 2f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 3f
            ),infoMovimientoBarChar(
                cantidad = 1,
                mes = 4f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 5f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 6f
            ),infoMovimientoBarChar(
                cantidad = 1,
                mes = 7f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 8f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 9f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 10f
            ),infoMovimientoBarChar(
                cantidad = 1,
                mes = 11f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 12f
            ),
            infoMovimientoBarChar(
                cantidad = 1,
                mes = 2f
            ),
        )
    }
}


