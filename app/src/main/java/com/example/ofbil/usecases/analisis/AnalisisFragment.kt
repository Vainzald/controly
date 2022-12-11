package com.example.ofbil.usecases.analisis

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentActivity
import com.example.ofbil.Model.infoMovimientoBarChar
import com.example.ofbil.Model.infoMovimientos
import com.example.ofbil.R
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.adapter.MovimientosViewPagerAdapter
import com.example.ofbil.adapter.ViewPageAdapter
import com.example.ofbil.databinding.FragmentAnalisisBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


class AnalisisFragment : Fragment() {

    private var _binding: FragmentAnalisisBinding? = null
    private val binding get() = _binding!!
    private var vmFirestore = FirestoreViewModel()
    private var listaGastos = mutableListOf<infoMovimientoBarChar>()
    private var listIngresos = mutableListOf<infoMovimientoBarChar>()



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnalisisBinding.inflate(inflater, container, false)
        setup()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setup() {
        val c: Date = Date()

        retornarValores()
        setUpTabs()
        binding.frgAnalisisImSearch.setOnClickListener {
            val year = binding.frgAnalisisSpFiltro.text.toString().toInt()
            retornarValores(year)
            setUpTabs(year)
        }
        setUpTabs()

        val calender = Calendar.getInstance()
        val formatFecha = SimpleDateFormat("yyyy/MM/dd")
        calender.get(Calendar.WEEK_OF_YEAR)
        Toast.makeText(layoutInflater.context, formatFecha.format(c), Toast.LENGTH_SHORT).show()


    }

    fun retornarValores(year: Int = 2022) {

        vmFirestore.VMlistarMovimientosAnual (year) { listGastos, listIngresos ->
            repartirListas(
                listGastos,
                listIngresos
            )
        }

    }


    fun dibujarGrafico(
        listaMovGastos: List<infoMovimientoBarChar>,
        listaMovIngresos: List<infoMovimientoBarChar>
    ) {
        val barDataSetGastos : BarDataSet
        val barDataSetIngresos : BarDataSet
        val listIngresosBarEntry : ArrayList<BarEntry> = ArrayList()
        val listGastosBarEntry : ArrayList<BarEntry> = ArrayList()
        for ((cantidad,mes) in listaMovGastos){
            listGastosBarEntry.add(BarEntry(mes, cantidad.toFloat()))
        }
        for((cantidad, mes) in listaMovIngresos){
            listIngresosBarEntry.add(BarEntry(mes, cantidad.toFloat()))
        }

        barDataSetGastos = BarDataSet(listGastosBarEntry, "Gastos")
        barDataSetIngresos = BarDataSet(listIngresosBarEntry, "Ingresos")
        val barData = BarData(barDataSetGastos, barDataSetIngresos)
        barData.isHighlightEnabled =true
        diseñoBarChart(barDataSetGastos, barDataSetIngresos)
        barData.barWidth = 0.45f
        binding.frgAnalisisBarChar.data = barData
        binding.frgAnalisisBarChar.groupBars(0.5.toFloat(), 0.06f, 0.02f)



//        Log.d("hay Ingresos", "$listaMovIngresos")
    }
    private fun diseñoBarChart(dataSetGastos : BarDataSet,dataSetIngresos : BarDataSet){
        dataSetGastos.color = R.color.Fire_Engine_Red
        dataSetIngresos.color = R.color.coloIcono
        dataSetGastos.valueTextColor=Color.BLACK
        dataSetGastos.isHighlightEnabled = true
        diseñoAxis()
        diseñoLegend()
        binding.frgAnalisisBarChar.isDragEnabled = true
        binding.frgAnalisisBarChar.setVisibleXRangeMaximum(7f)
//        binding.frgAnalisisBarChar.setFitBars(true)
//        binding.frgAnalisisBarChar.setDrawValueAboveBar(true)
        binding.frgAnalisisBarChar.setDrawBorders(false)
//        binding.frgAnalisisBarChar.setGridBackgroundColor(R.color.Lavender_Blush)
        binding.frgAnalisisBarChar.setPinchZoom(false)
        binding.frgAnalisisBarChar.isDoubleTapToZoomEnabled = false
        binding.frgAnalisisBarChar.isScaleYEnabled= false

        binding.frgAnalisisBarChar.isScaleXEnabled= false
        binding.frgAnalisisBarChar.setDrawGridBackground(false)
        binding.frgAnalisisBarChar.animateY(2000)
        binding.frgAnalisisBarChar.setNoDataText("Aqui podras ver tus gastos y ingresos")
        binding.frgAnalisisBarChar.invalidate()

    }
    private fun diseñoAxis(){
        val meses = listOf<String>("","Enero", "Febrero","Marzo", "Abril","Mayo", "Junio","Julio", "Agosto","Septiembre", "Octubre","Noviembre", "Diciembre")
        val axisY =  binding.frgAnalisisBarChar.axisLeft
        val axisX=  binding.frgAnalisisBarChar.axisRight
        val axis =  binding.frgAnalisisBarChar.xAxis
        axis.valueFormatter = IndexAxisValueFormatter(meses)
        axis.granularity=1f
        axis.isGranularityEnabled = true
        axis.labelRotationAngle = 90f


        axisY.setDrawGridLines(false)
        axisX.setDrawGridLines(false)
        axisX.isEnabled = false
        axis.setDrawGridLines(false)
        axis.setCenterAxisLabels(false)
        axis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun diseñoLegend(){
        val legend = binding.frgAnalisisBarChar.legend;
        legend.formSize = 10f;
        legend.form = Legend.LegendForm.CIRCLE;
        legend.textSize = 12f;
        legend.textColor = Color.BLACK;
        legend.yEntrySpace = 5f;
        legend.xEntrySpace = 5f;
    }
    private fun setUpTabs(year: Int = 2022) {

        binding.frgAnalisisViewPager.adapter = MovimientosViewPagerAdapter(layoutInflater.context as FragmentActivity, year)
        val tabLayoutMediator = TabLayoutMediator(binding.frgAnalisisTblMovIng, binding.frgAnalisisViewPager, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when(position + 1){
                1-> {
                    tab.text = "Gastos"
                }
                2-> {
                    tab.text = "Ingresos"
                }


            }
        })

        tabLayoutMediator.attach()

    }
    fun repartirListas(
        listaMovGastos: List<infoMovimientoBarChar>,
        listaMovIngresos: List<infoMovimientoBarChar>
    ){
        dibujarGrafico(listaMovGastos, listaMovIngresos)
        movimientosTotales(listaMovIngresos, listaMovGastos)
    }

    fun movimientosTotales(
        listaMovIngresos: List<infoMovimientoBarChar>,
        listaMovGastos: List<infoMovimientoBarChar>
    ){
        var resultadoIngresos = 0
        var resultadoGastos = 0

        for ((cantidad, mes) in listaMovIngresos){
            if (cantidad > 1){
                resultadoIngresos += cantidad
            }
        }
        for ((cantidad, mes) in listaMovGastos){
            if (cantidad > 1){
                resultadoGastos += cantidad
            }
        }
        dibujarTotalesMovimientos(resultadoGastos, resultadoIngresos)

        Log.d("Resultado de la suma de ingresos", "${resultadoIngresos - resultadoGastos}")


    }

    @SuppressLint("SetTextI18n")
    fun dibujarTotalesMovimientos(totalGastos : Int, totalIngresos : Int){
        binding.frgAnalisisTotalGastos.text = "-$totalGastos"
        binding.frgAnalisisTotalIngresos.text = "-$totalIngresos"
        binding.frgAnalisisTotal.text = "${totalIngresos - totalGastos}"
    }

}
