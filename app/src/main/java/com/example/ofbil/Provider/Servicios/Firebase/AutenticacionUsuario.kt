package com.example.ofbil.Provider.Servicios.Firebase

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ofbil.Model.Usuario
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.usecases.Auth.UserDataActivity
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AutenticacionUsuario(val context: Context? = null, var activity : Activity? = null) {

    private val GOOGLE_SIGN_IN = 100
    private var sesion: Boolean = false
    private var intent: BaseActivityRouter = BaseActivityRouter()
    private var db: database = database(context)
    private var idUsuario: String = ""
    private var apellido: String = ""
    private var nombre: String = ""
    private var VMFirebase = FirestoreViewModel()


    fun signUp(mail: String, Contraseña: String, usuario: Usuario): Boolean {

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(
                mail,
                Contraseña
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    it.addOnCompleteListener {
                        db.guardar(usuario)
                    }

                } else {
                    showAlert()
                }
            }
        return sesion


    }

    fun signIn(mail: String, contraseña: String) {

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(mail, contraseña).addOnCompleteListener {
                if (it.isSuccessful) {

                    db.recuperarUsurio(mail) {usuario -> guardarPreferencias(usuario.notificacion, usuario.idUsuario) }
                    VMFirebase.VMretrieveUserInformation(mail) { activity -> showExtraActivity(activity) }
                    prefs.saveId(mail)
                } else {
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
        val homeIntent = Intent(context, activity!!::class.java)
        intent.launch(homeIntent, context!!)


    }

    fun showAlert(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }



    fun showExtraActivity(activity: Activity){
        val iActivityExtra = Intent(context, activity::class.java)
        intent.launch(iActivityExtra, context!!)
    }

    fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
    }
    fun guardarPreferencias(notificaciones : Boolean, idUsuario : String){
        Log.d("Se guardo la preferencia??", notificaciones.toString())
        prefs.saveNotificaciones(notificaciones)
        prefs.saveId(idUsuario)
    }







}