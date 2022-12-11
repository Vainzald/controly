package com.example.ofbil

import android.content.SharedPreferences
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ofbil.Provider.Preferencias.PrefenciasUsuario
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.databinding.ActivityHomeBinding
import com.example.ofbil.usecases.Base.BaseFragmentRouter
import com.example.ofbil.usecases.Inicio.HomeFragment
import com.example.ofbil.usecases.Movimientos.MovimientosFragment
import com.example.ofbil.usecases.analisis.AnalisisFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var router : BaseFragmentRouter
    private lateinit var VMFirestore : FirestoreViewModel
    private lateinit var prefs : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configRouterFragment()
        //Setup
        setup()

        // Guardado de datos


    }

//    override fun onStart() {
//        super.onStart()
//        router.launchPreferences(HomeFragment(), initPreferences(), null)
//    }

    private fun setup(){

        title = "Inicio"
        router.launch(HomeFragment())
        binding.navBottom.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.btNv_home -> router.launch(HomeFragment())
                R.id.btNv_Movimientos -> router.launch(MovimientosFragment())
                R.id.btNv_Analisis -> router.launch(AnalisisFragment())
            }
            true
        }


    }

    private fun configRouterFragment(){
        val initConfig = BaseFragmentRouter(supportFragmentManager, R.id.nav_frgHome)
        router = initConfig
    }




}