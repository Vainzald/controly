package com.example.ofbil.ViewModel

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ofbil.Model.infoMovimientos
import com.example.ofbil.databinding.ItemMovimientosBinding

class MovimientosViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    val binding = ItemMovimientosBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(modelMovimientos : infoMovimientos){
        binding.iCategoryCategoria.text = modelMovimientos.nombreCategoria
        binding.iCategoryNombreTransaccion.text = modelMovimientos.nombreIngreso
        binding.iCategoryCantidad.text = modelMovimientos.cantidad.toString()
        if (modelMovimientos.fecha.isNotEmpty()){
            binding.iCategoryFechaTransaccion.text = "Realizado el ${modelMovimientos.fecha}"
        }

        if (modelMovimientos.tipoIngreso == "Ingreso"){
            binding.textView10.text = "+"
        }

        if (modelMovimientos.tipoIngreso == "Gasto"){
            binding.textView10.text = "-"
        }
        if(modelMovimientos.cantidadTransacciones >= 1){
            binding.iCategoryCantidadTransacciones.text = "${modelMovimientos.cantidadTransacciones.toString()} transacciones realizadas"
        }

    }
}