package com.example.ofbil.adapter

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ofbil.usecases.Inicio.HomeFragment
import com.example.ofbil.usecases.Movimientos.MovimientosFragment
import com.example.ofbil.usecases.Movimientos.TransaccionesListadasFragment
import com.example.ofbil.usecases.Movimientos.TransaccionesListadasFragment.Companion.TIPO_TRANSACCION
import com.example.ofbil.usecases.analisis.AnalisisFragment

class ViewPageAdapter(fragmentManager : FragmentActivity, suport : FragmentManager) : FragmentStateAdapter(fragmentManager) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragmentTransacciones = TransaccionesListadasFragment()
        when(position){
            0-> {
                fragmentTransacciones.arguments = Bundle().apply {
                    putString(TIPO_TRANSACCION, "TODO")
                }
            }
            1 ->{fragmentTransacciones.arguments = Bundle().apply {
                putString(TIPO_TRANSACCION, "Gasto")
            }}
            2->{fragmentTransacciones.arguments = Bundle().apply {
                putString(TIPO_TRANSACCION, "Ingreso")
            }}
            else -> AnalisisFragment()
        }

        return fragmentTransacciones
    }
}