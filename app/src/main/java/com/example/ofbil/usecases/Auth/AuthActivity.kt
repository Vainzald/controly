package com.example.ofbil.usecases.Auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ofbil.HomeActivity
import com.example.ofbil.Provider.Preferencias.PrefenciasUsuario
import com.example.ofbil.Provider.Servicios.Firebase.AutenticacionUsuario
import com.example.ofbil.R
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.databinding.AuthMainBinding
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    private lateinit var binding : AuthMainBinding
    private lateinit var auth : AutenticacionUsuario
    private var intent : BaseActivityRouter = BaseActivityRouter()
    private lateinit var prefs : SharedPreferences
    private lateinit var VMdb : FirestoreViewModel

//    private var responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
//
//        val task = GoogleSignIn.getSignedInAccountFromIntent(resultado.data)
//
//        try {
//            val account = task.getResult(ApiException::class.java)
//            if (account != null){
//                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
//                    if (it.isSuccessful){
//                        auth.showHome()
//                    }else{
//                        auth.showAlert()
//                    }
//                }
//
//            }
//        }catch (e : ApiException){
//            auth.showAlert()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAuth()
        setup()


    }


    private fun setup() {
        title = "Autenticacion"

        binding.loginBoton.setOnClickListener {
            if( binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                auth.signIn(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )

            }

        }



        binding.button.setOnClickListener {
            val iRegister = Intent(this, RegisterActivity::class.java)
            intent.launch(iRegister,this)
        }


    }

    private fun initAuth(){
        val register = AutenticacionUsuario(this, HomeActivity())
        auth = register

    }



}