package com.example.ofbil.ViewModel

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ofbil.Model.Categorias
import com.example.ofbil.databinding.ItemCategoriasBinding

class CategoriasViewHolder(view : View): RecyclerView.ViewHolder(view){
    val binding = ItemCategoriasBinding.bind(view)

    fun render(categorias: Categorias, listenerItem: (String) -> Unit){
        binding.iCategoryNombreCategoria.text = categorias.nombre
        binding.iCategoryDescripcion.text = categorias.descripcion
        binding.itemCategorias.setOnClickListener { listenerItem(categorias.nombre!!) }

    }
}