package com.example.ofbil.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ofbil.Model.infoMovimientos
import com.example.ofbil.R
import com.example.ofbil.ViewModel.MovimientosViewHolder

class MovimientosAdapter(private val listMovimientos : List<infoMovimientos>) : RecyclerView.Adapter<MovimientosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimientosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovimientosViewHolder(layoutInflater.inflate(R.layout.item_movimientos, parent, false))
    }

    override fun onBindViewHolder(holder: MovimientosViewHolder, position: Int) {
        val item = listMovimientos[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = listMovimientos.size
}