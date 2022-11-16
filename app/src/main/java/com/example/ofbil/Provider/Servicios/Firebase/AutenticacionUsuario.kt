package com.example.ofbil.Provider.Servicios.Firebase

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.example.ofbil.HomeActivity
import com.example.ofbil.Model.Usuario
import com.example.ofbil.ProviderType
import com.example.ofbil.R
import com.example.ofbil.usecases.Auth.AuthActivity
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.properties.Delegates

class AutenticacionUsuario(val context: Context, val activity : Activity)  {
    private val GOOGLE_SIGN_IN = 100
    private var sesion by Delegates.notNull<Boolean>()
    private var intent : BaseActivityRouter = BaseActivityRouter()


    fun signUp(mail : String, Contraseña : String,usuario: Usuario ){
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(
                mail,
                Contraseña
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.addOnCompleteListener {
                        initDB(usuario)
                    }

                } else {
                    showAlert()
                }
            }


    }
    fun signIn(mail: String, contraseña : String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(mail, contraseña).addOnCompleteListener{
                if (it.isSuccessful){
                    showHome()
                }else{
                    showAlert()
                }
            }



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
    private fun initDB(usuario : Usuario){
        val db = database()
        db.guardar(usuario)
    }





}