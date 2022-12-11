package com.example.ofbil.usecases.Inicio

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat.getColor
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ofbil.HomeActivity
import com.example.ofbil.Model.Fecha
import com.example.ofbil.Model.UsuarioInfoCuenta
import com.example.ofbil.Model.infoMovimientos
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.Provider.Servicios.Firebase.database
import com.example.ofbil.R
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.adapter.MovimientosAdapter
import com.example.ofbil.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private var db : database = database(null)
    private val binding get() = _binding!!
    private val viewModeldb = FirestoreViewModel()
    private val lista = mutableListOf<infoMovimientos>()
    private var totalMovimientos = 0
    private lateinit var nombreCuenta : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =FragmentHomeBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }



    private fun setup(){
        retrievePreferenceData()

        viewModeldb.VMlistarMovimientosCategoria(fechaActual(),"Gasto") {datos1 -> recibirDatos(datos1)}
        viewModeldb.VMmainUserData(nombreCuenta){data -> dataUser(data)}
        binding.imageView.setOnClickListener {
            val iMostrar = Intent(layoutInflater.context, PerfilActivity::class.java)
            startActivity(iMostrar)
            val iHome = activity
            iHome?.finish()
        }

    }




    fun recibirDatos(modelTransacciones : infoMovimientos){
        if (modelTransacciones.cantidad > 0){
            totalMovimientos +=  modelTransacciones.cantidad.toInt()
            lista.add(modelTransacciones)
        }
        dibujarGrafico(lista)
        initRecyclerView(lista)

    }

    fun dibujarGrafico(list: List<infoMovimientos>){
        val listPieChar : ArrayList<PieEntry> = ArrayList()
        for ((nombre , porcentaje) in list){
            if (porcentaje > 0.0){
                listPieChar.add(PieEntry(porcentaje.toFloat(), ""))
            }

        }
        val pieDataSet = PieDataSet(listPieChar, "Gastos")
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS, 255)
        pieDataSet.valueTextSize = 7f
        pieDataSet.valueTextColor = R.color.Ash_Gray

        val pieData = PieData(pieDataSet)

        binding.frgHomeGraficoGastos.description.isEnabled = false

        binding.frgHomeGraficoGastos.data =pieData
        binding.frgHomeGraficoGastos.centerText = "Gastos"
        binding.frgHomeGraficoGastos.setUsePercentValues(true)
        binding.frgHomeGraficoGastos.animateY(1000, Easing.EaseInCirc)


    }


    fun retrievePreferenceData(){
        nombreCuenta = prefs.getNombreCuenta()
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun dataUser(data : UsuarioInfoCuenta){
        if (data.cantidad < 0){
            binding.frgHomeCantidad.setTextColor(R.color.Fire_Engine_Red)
        }
        binding.frgHomeCantidad.text = "$ ${data.cantidad}"
        binding.frgHomeNombre.text = data.nombreCuenta
    }
    fun fechaActual() : Fecha {
        val calender = Calendar.getInstance()
        val dia = calender.get(Calendar.DAY_OF_MONTH)
        val mes = calender.get(Calendar.MONTH)  + 1
        val year = calender.get(Calendar.YEAR)
        val fecha = Fecha(
            mes = mes,
            año = year,
            dia = dia,
            ""
        )
        return fecha

    }

     fun initRecyclerView(list: List<infoMovimientos>){
         val manager = GridLayoutManager(binding.root.context, 4)
         val decoration = DividerItemDecoration(binding.root.context, manager.orientation)
         val adapter = MovimientosAdapter(list)
         binding.recylerPorcentajes.adapter = MovimientosAdapter(list)
         binding.recylerPorcentajes.layoutManager = LinearLayoutManager(binding.root.context)
         binding.recylerPorcentajes.addItemDecoration(decoration)
     }


}