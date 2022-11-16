package com.example.ofbil.Provider.Servicios.Firebase

import com.example.ofbil.Model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class database {
    private val db = FirebaseFirestore.getInstance()
    fun guardar(usuario: Usuario){

        db.collection("Usuario").document(usuario.mail).set(
            hashMapOf("apellido" to usuario.apellido,
                "fecha_nacimiento" to usuario.fechaNacimiento,
                "nombre" to usuario.nombre
            )
        )

    }
}