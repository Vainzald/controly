package com.example.ofbil.usecases.Movimientos.EscribirDatos

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.example.ofbil.Model.Categorias
import com.example.ofbil.Model.Transaccion
import com.example.ofbil.Model.Usuario
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.Provider.Preferencias.PrefenciasUsuario
import com.example.ofbil.Provider.Servicios.Firebase.database
import com.example.ofbil.R
import com.example.ofbil.adapter.ViewPageAdapter
import com.example.ofbil.databinding.ActivityHomeBinding.inflate
import com.example.ofbil.databinding.ActivityInsertarDatosBinding
import com.example.ofbil.databinding.ActivityInsertarDatosBinding.inflate
import com.example.ofbil.databinding.AuthMainBinding.inflate
import com.example.ofbil.databinding.FragmentHomeBinding.inflate
import com.example.ofbil.usecases.common.DatePickerFragment
import com.example.ofbil.usecases.common.categoriaas.CategoriasActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class InsertarDatosActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInsertarDatosBinding
    private var transaccionTipo : String = "Ingreso"
    private lateinit var mes : String
    private lateinit var year : String
    private lateinit var Dia : String
    private lateinit var fechaCompleta : String
    private lateinit var nombreCategoria : String

    private var db = database(null)

    private var responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
        if (resultado.resultCode == RESULT_OK){
            nombreCategoria = resultado.data?.getStringExtra("Nombre")!!
            binding.edtCategoria.text = nombreCategoria
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarDatosBinding.inflate(layoutInflater)

        setup()
        setContentView(binding.root)
    }

    private fun setup() {
        paintDate()
        //Para activar la fecha
        elegirTransaccion()
        binding.edtCategoria.setOnClickListener{

            val iListarCategorias = Intent(this, CategoriasActivity::class.java)
            iListarCategorias.putExtra("tipoTransaccion", transaccionTipo )
            responseLauncher.launch(iListarCategorias)
        }
        binding.edtDate.setOnClickListener{ showDatePickerDialog()}
        binding.ingresarTransaccion.setOnClickListener { ingresarDatos() }
        binding.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")

    }
    fun onDateSelected(day: Int, month: Int, Year : Int){
        val fechaa = "${Year.toString()}/${month+1}/${day.toString()}"
        val mesAdelantado = month + 1
        fechaCompleta = fechaa
        mes = mesAdelantado.toString()
        Dia = day.toString()
        year = Year.toString()

        binding.edtDate.text = fechaa
    }

    private fun ingresarDatos(){
        val nombreCuenta = prefs.getNombreCuenta()
        val categorias = Categorias("Hipoteca", "Algo")
        val cantidad = binding.edtCantidad.text
        val estado = "Original"
        val nombreGasto = binding.edtNombre.text.toString()
        val ingreso = Transaccion(
            categorias = categorias,
            cantidad = cantidad.toString().toInt(),
            fechaCompleta = fechaCompleta,
            year = year.toInt(),
            mes = mes.toInt(),
            dia = Dia.toInt(),
            nombreGasto = nombreGasto,
            estado = estado,
            tipoMovimiento = transaccionTipo,
            nombreCategoria = nombreCategoria,
            nombreCuenta = nombreCuenta
        )
        db.addIngreso(ingreso)

    }



    private fun elegirTransaccion(){
        binding.ingreso.setOnClickListener {
            transaccionTipo = binding.ingreso.text.toString()
            binding.edtCategoria.text = ""
            binding.ingreso.setBackgroundColor(getColor(R.color.coloIcono))
            binding.ingreso.setTextColor(getColor(R.color.Gainsboro))
            binding.gasto.setBackgroundColor(getColor(R.color.Lavender_Blush))
            binding.gasto.setTextColor(getColor(R.color.steel_teal))
        }
        binding.gasto.setOnClickListener {
            binding.edtCategoria.text = ""
            transaccionTipo = binding.gasto.text.toString()
            binding.ingreso.setBackgroundColor(getColor(R.color.Lavender_Blush))
            binding.ingreso.setTextColor(getColor(R.color.steel_teal))
            binding.gasto.setBackgroundColor(getColor(R.color.coloIcono))
            binding.gasto.setTextColor(getColor(R.color.Gainsboro))
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun paintDate(){
        val c: Date = Date()
        val calender = Calendar.getInstance()
        mes = (calender.get(Calendar.MONTH) + 1).toString()
        year = calender.get(Calendar.YEAR).toString()
        Dia = calender.get(Calendar.DAY_OF_MONTH).toString()
        fechaCompleta = "$year/$mes/$Dia"
        binding.edtDate.text = fechaCompleta

    }





}