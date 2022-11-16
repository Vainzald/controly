package com.example.ofbil.usecases.Auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.example.ofbil.HomeActivity
import com.example.ofbil.Provider.Servicios.Firebase.AutenticacionUsuario
import com.example.ofbil.ProviderType
import com.example.ofbil.R
import com.example.ofbil.databinding.AuthMainBinding
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    private lateinit var binding : AuthMainBinding
    private lateinit var auth : AutenticacionUsuario
    private var intent : BaseActivityRouter = BaseActivityRouter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAuth()
        setup()
        notification()
//        session()
    }

//    override fun onStart() {
//        super.onStart()
//        binding.authLayout.visibility = VISIBLE
//    }
    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if (email != null && provider != null){
            binding.authLayout.visibility = INVISIBLE
            auth.showHome()
        }
    }

    private fun notification(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            it.result?.let {
                println("token ${it}")
            }
        }
    }

    private fun setup() {
        title = "Autenticacion"

        binding.loginBoton.setOnClickListener {
            if( binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()){
                auth.signIn(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())

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
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN){
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