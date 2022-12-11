package com.example.ofbil.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ofbil.usecases.Inicio.HomeFragment
import com.example.ofbil.usecases.analisis.MovimientosAnualesFragment
import com.example.ofbil.usecases.analisis.MovimientosAnualesFragment.Companion.TIPO_MOVIMIENTO
import com.example.ofbil.usecases.analisis.MovimientosAnualesFragment.Companion.YEAR_ASIGNED

class MovimientosViewPagerAdapter(fragmentManager: FragmentActivity, val year : Int) : FragmentStateAdapter(fragmentManager){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragmentMovimientos = MovimientosAnualesFragment()
        when(position){
            0 -> {
                fragmentMovimientos.arguments = Bundle().apply {
                    putString(TIPO_MOVIMIENTO, "Gasto")
                    putInt(YEAR_ASIGNED, year)
                }
            }
            1 -> {
                fragmentMovimientos.arguments = Bundle().apply {
                    putString(TIPO_MOVIMIENTO, "Ingreso")
                    putInt(YEAR_ASIGNED, year)
                }
            }
            else -> HomeFragment()
        }
        return fragmentMovimientos
    }

}