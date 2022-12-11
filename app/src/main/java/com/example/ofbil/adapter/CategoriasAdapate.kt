package com.example.ofbil.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ofbil.Model.Categorias
import com.example.ofbil.R
import com.example.ofbil.ViewModel.CategoriasViewHolder

class CategoriasAdapater(private val itemCategorias : List<Categorias>, private val listenerItem : (String) -> Unit) : RecyclerView.Adapter<CategoriasViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CategoriasViewHolder(layoutInflater.inflate(R.layout.item_categorias, parent, false))
    }

    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {
        val item =itemCategorias[position]
        holder.render(item, listenerItem)
    }

    override fun getItemCount(): Int = itemCategorias.size
}