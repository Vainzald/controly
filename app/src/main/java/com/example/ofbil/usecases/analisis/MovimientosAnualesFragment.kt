package com.example.ofbil.usecases.analisis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ofbil.Model.infoMovimientos
import com.example.ofbil.R
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.adapter.MovimientosAdapter
import com.example.ofbil.adapter.ViewPageAdapter
import com.example.ofbil.databinding.FragmentMovimientosAnualesBinding
import com.example.ofbil.databinding.FragmentMovimientosBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayoutMediator

class MovimientosAnualesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    companion object{
        const val TIPO_MOVIMIENTO = "tipo_movimiento"
        const val YEAR_ASIGNED = "year_asigned"
    }

    private var tipo_movimiento : String? = null
    private var year_asigner : Int? = null
    private var _binding : FragmentMovimientosAnualesBinding? = null
    private val binding get() = _binding!!
    private val VMFirestore = FirestoreViewModel()
    private val lista = mutableListOf<infoMovimientos>()
    private var totalMovimientos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tipo_movimiento = it.getString(TIPO_MOVIMIENTO)
            year_asigner = it.getInt(YEAR_ASIGNED)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentMovimientosAnualesBinding.inflate(inflater, container, false)
        setupFragment()
        return binding.root
    }

    fun setupFragment(){
        VMFirestore.VMListarMovimientosCategoriaAnual(tipo_movimiento.orEmpty(), year_asigner!!){ datos -> recibirDatos(datos)}
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
        val pieDataSet = PieDataSet(listPieChar, "")
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS, 255)
        pieDataSet.valueTextSize = 9f
        pieDataSet.valueTextColor = ColorTemplate.rgb("E0E0E2")
        val pieData = PieData(pieDataSet)


        binding.pieChart.description.isEnabled = false
        binding.pieChart.data =pieData
        binding.pieChart.setNoDataText("Aqui podras ver tus ingresos categorizados por año")
        binding.pieChart.centerText = tipo_movimiento
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.animateY(1000, Easing.EaseInCirc)


    }
    fun initRecyclerView(list: List<infoMovimientos>){
        Log.d("lista", "$list")
        val manager = LinearLayoutManager(binding.root.context)
        val decoration = DividerItemDecoration(binding.root.context, manager.orientation)
        val adapter = MovimientosAdapter(list)
        binding.recylcer.adapter = MovimientosAdapter(list)
        binding.recylcer.layoutManager = LinearLayoutManager(binding.root.context)
        binding.recylcer.addItemDecoration(decoration)
    }


}