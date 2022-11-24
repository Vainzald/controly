package com.example.ofbil.Provider.Servicios.Firebase

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ofbil.Model.Usuario
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class AutenticacionUsuario(val context: Context, val activity : Activity)  {

    private val GOOGLE_SIGN_IN = 100
    private var sesion : Boolean = false
    private var intent : BaseActivityRouter = BaseActivityRouter()
    private var db : database = database()
    private var idUsuario : String = ""
    private var apellido : String = ""
    private var nombre : String = ""


    fun signUp(mail : String, Contraseña : String,usuario: Usuario ) : Boolean{

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(
                mail,
                Contraseña
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.addOnCompleteListener {
                        db.guardar(usuario)
                        showHome()
                    }


                } else {
                    showAlert()
                }
            }
        return sesion


    }
    fun signIn(mail: String, contraseña: String, getUser: (String, String, String) -> Unit) {

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(mail, contraseña).addOnCompleteListener{
                if (it.isSuccessful){
                    
                    db.recuperarUsurio(

                        {usuario -> recuperaUsuario(usuario) },
                        getUser,)


                    showHome()
                }else{
                    showAlert()
                }
            }

        

    }
    fun confGoogle(contexto : Context, idClient : String) : Intent{
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(idClient)
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(contexto, googleConf)
        googleClient.signOut()
        return googleClient.signInIntent
    }

    fun showHome(){
        val homeIntent = Intent(context, activity::class.java)
        intent.launch(homeIntent, context)


    }
    fun showAlert(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
    fun recuperaUsuario(usuario : Usuario) {
        Log.d("Desde Recuperar Usuario", "$usuario")
        idUsuario = usuario.idUsuario
        nombre = usuario.nombre
        apellido = usuario.apellido

    }






}