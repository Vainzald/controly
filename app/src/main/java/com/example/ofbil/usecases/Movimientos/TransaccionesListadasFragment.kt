package com.example.ofbil.usecases.Movimientos

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ofbil.Model.infoMovimientos
import com.example.ofbil.R
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.adapter.MovimientosAdapter
import com.example.ofbil.databinding.FragmentAnalisisBinding
import com.example.ofbil.databinding.FragmentTransaccionesListadasBinding


class TransaccionesListadasFragment : Fragment() {
    companion object {
        const val TIPO_TRANSACCION = "tipo_transaccion"

    }
    private var _binding : FragmentTransaccionesListadasBinding? = null
    private val binding get() = _binding!!
    private var tipo_transaccion : String? = null
    private lateinit var VMFirestore : FirestoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tipo_transaccion = it.getString(TIPO_TRANSACCION)

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTransaccionesListadasBinding.inflate(inflater, container, false)
        setup()
        return binding.root
    }

    private fun setup(){
        VMFirestore = FirestoreViewModel()
        if(tipo_transaccion != "TODO"){
            VMFirestore.VMGetMovementData(tipo_transaccion.orEmpty()){dataList -> obtainDataMovement(dataList)}

        }else{
            VMFirestore.VMtodosLosMovimientos(){lista, total -> initRecycler(lista)}
        }
    }
    private fun initRecycler(listaMovimientos: List<infoMovimientos>){
        val manager = LinearLayoutManager(layoutInflater.context)
        val decoration = DividerItemDecoration(layoutInflater.context,manager.orientation )
        binding.recyclerView.adapter = MovimientosAdapter(listaMovimientos)
        binding.recyclerView.layoutManager = LinearLayoutManager(layoutInflater.context)
        binding.recyclerView.addItemDecoration(decoration)
    }

    private fun obtainDataMovement (listaMovimientos : List<infoMovimientos>){
        initRecycler(listaMovimientos)
    }



}