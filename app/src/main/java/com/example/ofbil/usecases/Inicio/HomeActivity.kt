package com.example.ofbil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ofbil.databinding.ActivityHomeBinding
import com.example.ofbil.usecases.Base.BaseFragmentRouter
import com.example.ofbil.usecases.Inicio.HomeFragment
import com.example.ofbil.usecases.Movimientos.MovimientosFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var router : BaseFragmentRouter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configRouterFragment()
        //Setup
        setup()

        // Guardado de datos


    }

    private fun setup(){

        title = "Inicio"
        router.launch(HomeFragment())
        binding.navBottom.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.btNv_home -> router.launch(HomeFragment())
                R.id.btNv_Movimientos -> router.launch(MovimientosFragment())
            }
            true
        }


    }


    fun configRouterFragment(){
        val initConfig = BaseFragmentRouter(supportFragmentManager, R.id.nav_frgHome)
        router = initConfig
    }
    fun initFragment(fragment : Fragment){
        val support = supportFragmentManager
        val transacction = support.beginTransaction()
        transacction.replace(R.id.nav_frgHome, fragment)
        transacction.commit()
    }

}