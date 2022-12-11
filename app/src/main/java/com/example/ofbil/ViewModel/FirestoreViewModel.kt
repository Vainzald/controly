package com.example.ofbil.ViewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.ofbil.Model.*
import com.example.ofbil.Provider.Servicios.Firebase.database

class FirestoreViewModel : ViewModel(){
    private var db = database(null)

    fun VMtodosLosMovimientos(listenerListMovimientos: (List<infoMovimientos>, Int) -> Unit) {
        db.listarMovimientos(listenerListMovimientos)
    }

    fun addInformationUser(informationUser : UsuarioInfoCuenta, initActivity : () -> Unit){
        db.addInformationUser(informationUser, initActivity)
    }

    fun VMretrieveUserInformation(mail : String, initActivity : (activity : Activity) -> Unit){
        db.retrieveUserInformation(mail,initActivity)
    }

    fun recuperarUsuario(setData :() -> Unit){

    }

    fun VMlistarCategorias( typeCategory : String,giveList : (listCategorias : List<Categorias>) -> Unit){
        db.listarCategorias(typeCategory ,giveList)
    }
    fun VMlistarMovimientosCategoria(fecha:Fecha,  tipoMovimiento : String, darDatos : (infoMovimientos) -> Unit){
        db.listarMovimientosPorCategoria(tipoMovimiento, darDatos, fecha)
    }

    fun VMlistarMovimientosAnual(year: Int,darGastos : (List<infoMovimientoBarChar>, List<infoMovimientoBarChar>) -> Unit){

        db.resultadoMovimientosAnual(year,darGastos)
    }


    fun VMmainUserData(nombreCuenta : String, dataUser : (UsuarioInfoCuenta) -> Unit){
        db.mainUserData(nombreCuenta, dataUser)
    }

    fun VMGetMovementData(tipoMovimiento: String, listenerData : (List<infoMovimientos>) -> Unit){
        db.getMovementData(tipoMovimiento, listenerData)
    }

    fun VMListarMovimientosCategoriaAnual(tipoMovimiento: String,year: Int ,darDatos: (infoMovimientos) -> Unit, ){
        db.listarMovimientosPorCategoriaAnual(tipoMovimiento, year, darDatos)
    }
    fun VMUpdateNotifies(accept : Boolean){
        db.updateNotifications(accept)
    }

}