package com.example.ofbil.Provider.Servicios.Firebase

import android.content.SharedPreferences
import android.util.Log
import com.example.ofbil.Model.Transaccion
import com.example.ofbil.Model.Usuario
import com.example.ofbil.Model.infoMovimientos
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class database {
    private val db = FirebaseFirestore.getInstance()
    private val id = FirebaseMessaging.getInstance()
    private val uid = Firebase.auth.currentUser!!.email
    private lateinit var user : Any
    private lateinit var prefs : SharedPreferences
    fun guardar(usuario: Usuario){
        val ingresos = mapOf(
            "idIngreso" to "123",
            "cantidad" to 1234
        )
        db.collection("Usuario").document(usuario.idUsuario).set(
            hashMapOf("apellido" to usuario.apellido,
                "fecha_nacimiento" to usuario.fechaNacimiento,
                "nombre" to usuario.nombre,
                "idUsuario" to usuario.idUsuario,
                "categorias" to ingresos
            )
        )

    }

    fun recuperarUsurio(darUsuario: (Usuario) -> Unit, getUser: (String, String, String) -> Unit) {
        db.collection("Usuario").document("$uid")
            .get(Source.DEFAULT)
            .addOnCompleteListener { datos ->
                val usuario = Usuario(
                    nombre = datos.result.data?.get("nombre").toString(),
                    apellido = datos.result.data?.get("apellido").toString(),
                    fechaNacimiento = datos.result.data?.get("fecha_nacimiento").toString(),
                    idUsuario = datos.result.data?.get("idUsuario").toString()
                )
                user = usuario
                darUsuario(usuario)
                getUser(usuario.idUsuario, usuario.apellido, usuario.nombre)


            }





    }
    fun addIngreso(ingreso: Transaccion){
        db.collection("Movimientos").document().set(
            hashMapOf(
                "nombreIngreso" to ingreso.nombreGasto,
                "date" to ingreso.fecha,
                "estado" to ingreso.estado,
                "cantidad" to ingreso.cantidad,
                "refUser" to "Usuario/$uid",
                "tipoMovimiento" to ingreso.tipoMovimiento
            )
        )
    }
    fun listarMovimientos(listenerListMovimientos: (List<infoMovimientos>, Int) -> Unit) {
        val listaMovimientos = mutableListOf<infoMovimientos>()
        var total = 0
        db.collection("Movimientos")
            .whereEqualTo("refUser", "Usuario/$uid")
            .get()
            .addOnSuccessListener { respuesta ->
                respuesta.map { resultado ->
                    total += resultado.data.get("cantidad").toString().toInt()
                   val detallesTransaccion = infoMovimientos(
                       resultado.data.get("nombreIngreso").toString(),
                       resultado.data.get("cantidad").toString().toFloat()
                   )
                    listaMovimientos.add(detallesTransaccion)
                }
                listenerListMovimientos(listaMovimientos, total)

            }
    }


}