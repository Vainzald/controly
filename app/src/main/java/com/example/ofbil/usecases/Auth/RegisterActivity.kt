package com.example.ofbil.usecases.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ofbil.HomeActivity
import com.example.ofbil.Model.Usuario
import com.example.ofbil.Provider.Servicios.Firebase.AutenticacionUsuario
import com.example.ofbil.Provider.Servicios.Firebase.database
import com.example.ofbil.R
import com.example.ofbil.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : AutenticacionUsuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAuth()

        setup()
    }

    private fun setup(){
        binding.signUpBoton.setOnClickListener {
            val mail = binding.regEdtMail.text.toString()
            val password = binding.regEdtPassword.text.toString()
            if(mail.isNotEmpty() && password.isNotEmpty()){
                val datos = Usuario(
                    nombre = binding.regEdtNombre.text.toString(),
                    apellido = binding.regEdtApellido.text.toString(),
                    fechaNacimiento = binding.regEdtDate.text.toString(),
                    mail = mail
                )
                auth.signUp(mail, password, datos)
                auth.showHome()
            }else{
                Toast.makeText(this, "No se pudo registrar para autenticar", Toast.LENGTH_SHORT).show()
            }





        }
        binding.googleBoton.setOnClickListener {

            // Configuracion
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,100)

        }
    }

    private fun initAuth() {
        val register = AutenticacionUsuario(this, HomeActivity())
        auth = register

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            auth.showHome()
                        }else{
                            auth.showAlert()
                        }
                    }

                }
            }catch (e : ApiException){
                auth.showAlert()
            }

        }

    }


}