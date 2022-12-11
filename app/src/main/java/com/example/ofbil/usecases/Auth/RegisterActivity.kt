package com.example.ofbil.usecases.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ofbil.HomeActivity
import com.example.ofbil.Model.Usuario
import com.example.ofbil.Provider.Servicios.Firebase.AutenticacionUsuario
import com.example.ofbil.Provider.Servicios.Firebase.database
import com.example.ofbil.R
import com.example.ofbil.databinding.ActivityRegisterBinding
import com.example.ofbil.usecases.common.DatePickerFragment
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


//    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ resultado ->
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
                    idUsuario = mail,
                    false
                )
                auth.activity = UserDataActivity()
                auth.signUp(mail, password, datos)

            }else{
                Toast.makeText(this, "No se pudo registrar para autenticar", Toast.LENGTH_SHORT).show()
            }

        }
        binding.regEdtDate.setOnClickListener { showDatePickerDialog() }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")

    }

    private fun initAuth() {
        val register = AutenticacionUsuario(this, HomeActivity())
        auth = register

    }

    fun onDateSelected(day: Int, month: Int, Year : Int){

        var mes = month + 1
        var Dia = day.toString()
        var year = Year.toString()
        val fechaa = "${year}/${mes}/${Dia}"
        binding.regEdtDate.text = fechaa

    }





}