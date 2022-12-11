package com.example.ofbil.usecases.common.categoriaas

import android.content.Intent
import android.graphics.drawable.GradientDrawable.Orientation
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ofbil.Model.Categorias
import com.example.ofbil.ViewModel.FirestoreViewModel
import com.example.ofbil.adapter.CategoriasAdapater
import com.example.ofbil.databinding.ActivityCategoriasBinding

class CategoriasActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoriasBinding
    private lateinit var VMFirestore : FirestoreViewModel
    private lateinit var tipoTransaccion : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setup()
        setContentView(binding.root)
    }

    private fun setup(){
        tipoTransaccion()
        VMFirestore = FirestoreViewModel()
        VMFirestore.VMlistarCategorias(tipoTransaccion){ listCategorias -> pasarLista(listCategorias) }
        binding.imageView2.setOnClickListener {
            val intent = Intent()
            intent.putExtra("Nombre", "")
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.textView7.text = tipoTransaccion
    }

    private fun tipoTransaccion() {
        tipoTransaccion = intent.getStringExtra("tipoTransaccion")!!
    }

    private fun initRecyclerView(listCategorias : List<Categorias>){
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = CategoriasAdapater(listCategorias) {nombre -> clickListenerItem(nombre)}
        binding.recyclerView.addItemDecoration(decoration)
    }

    private fun pasarLista(listaCategorias : List<Categorias>) {
        Log.d("Lista de item", "$listaCategorias")
        initRecyclerView(listaCategorias)
    }

    private fun clickListenerItem( nombre : String ){
        val intent = Intent()
        intent.putExtra("Nombre", nombre)
        setResult(RESULT_OK, intent)
        finish()
    }


}