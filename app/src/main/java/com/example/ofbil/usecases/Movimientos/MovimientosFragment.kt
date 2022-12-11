package com.example.ofbil.usecases.Movimientos

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.ofbil.Model.UsuarioInfoCuenta
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.adapter.ViewPageAdapter
import com.example.ofbil.databinding.FragmentMovimientosBinding
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.example.ofbil.usecases.Movimientos.EscribirDatos.InsertarDatosActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MovimientosFragment : Fragment() {

    private var _binding : FragmentMovimientosBinding? = null
//    private  var launcher: BaseActivityRouter = BaseActivityRouter()
    private val binding get() = _binding!!
    private var VMFirestore = FirestoreViewModel()
    private val adapter by lazy { ViewPageAdapter(layoutInflater.context as FragmentActivity, childFragmentManager) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =FragmentMovimientosBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }


    private fun setup(){


        binding.frgMvButtonFloat.setOnClickListener {
            val iInsertar = Intent(layoutInflater.context, InsertarDatosActivity::class.java)

//            Toast.makeText(layoutInflater.context, "Hola", Toast.LENGTH_SHORT).show()
            startActivity(iInsertar)
        }
        setUpTabs()


    }
    private fun setUpTabs() {

        binding.frgMVPager.adapter = adapter
        val tabLayoutMediator = TabLayoutMediator(binding.frgMvTablayout, binding.frgMVPager, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when(position + 1){
                1-> {
                    tab.text = "Todos"
                }
                2-> {
                    tab.text = "Gastos"
                }
                3-> {
                    tab.text = "Ingresos"
                }

            }
        })

        tabLayoutMediator.attach()

    }



}