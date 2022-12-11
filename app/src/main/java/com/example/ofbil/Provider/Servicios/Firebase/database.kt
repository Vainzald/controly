package com.example.ofbil.Provider.Servicios.Firebase

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ofbil.HomeActivity
import com.example.ofbil.Model.*
import com.example.ofbil.Model.infoMovimientoBarChar.Companion.listaGastosProvider
import com.example.ofbil.Model.infoMovimientoBarChar.Companion.listaIngresosProvider
import com.example.ofbil.Provider.Preferencias.ControlyPreferencesApp.Companion.prefs
import com.example.ofbil.usecases.Auth.UserDataActivity
import com.example.ofbil.usecases.Base.BaseActivityRouter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class database(val contexto : Context?) {
    private val db = FirebaseFirestore.getInstance()
    private val uid = Firebase.auth.currentUser?.email
    private lateinit var user : Any
    private var launcher = BaseActivityRouter()

    fun guardar(usuario: Usuario){
        db.collection("Usuario").document(usuario.idUsuario).set(
            hashMapOf("apellido" to usuario.apellido,
                "fecha_nacimiento" to usuario.fechaNacimiento,
                "nombre" to usuario.nombre,
                "idUsuario" to usuario.idUsuario,
            )
        )
            .addOnCompleteListener {
                val iInformationExtra = Intent(contexto, UserDataActivity::class.java)
                launcher.launch(iInformationExtra, contexto!!)
            }
    }

    fun recuperarUsurio(mail : String,darUsuario: (Usuario) -> Unit) {
        var mailAcceso = mail
        if (mailAcceso.isEmpty()){
           mailAcceso = uid!!
        }
        db.collection("Usuario").document(mailAcceso)
            .get(Source.DEFAULT)
            .addOnCompleteListener { datos ->
                val usuario = Usuario(
                    nombre = datos.result.data?.get("nombre").toString(),
                    apellido = datos.result.data?.get("apellido").toString(),
                    fechaNacimiento = datos.result.data?.get("fecha_nacimiento").toString(),
                    idUsuario = datos.result.data?.get("idUsuario").toString(),
                    notificacion = datos.result.data?.get("notificaciones").toString().toBoolean()
                )
                prefs.saveId(datos.result.data?.get("idUsuario").toString())
                user = usuario
                darUsuario(usuario)

            }
    }

    fun addIngreso(ingreso: Transaccion){
        val rutaMovimientos = db.collection("/Movimientos/").document()
        val rutaCuenta = db.collection("Usuario/$uid/Cuenta").document("${ingreso.nombreCuenta}_$uid")

        rutaMovimientos.set( hashMapOf(
                "nombreIngreso" to ingreso.nombreGasto,
                "fechaCompleta" to ingreso.fechaCompleta,
                "año" to ingreso.year,
                "mes" to ingreso.mes,
                "dia" to ingreso.dia,
                "estado" to ingreso.estado,
                "cantidad" to ingreso.cantidad,
                "refUser" to "Usuario/$uid",
                "tipoMovimiento" to ingreso.tipoMovimiento,
                "categoria" to ingreso.nombreCategoria
            )).addOnCompleteListener {
                if (it.isSuccessful){
                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(rutaCuenta)
                        val cantidad = snapshot.data?.get("cantidad_inicial").toString().toInt()
                        val resultado : Int
                        if (ingreso.tipoMovimiento == "Ingreso"){
                            resultado = cantidad + ingreso.cantidad
                        }else{
                            resultado = cantidad - ingreso.cantidad
                        }
                        transaction.update(rutaCuenta, "cantidad_inicial", resultado)
                    }
                }
        }
    }

    fun listarMovimientos(listenerListMovimientos: (List<infoMovimientos>, Int) -> Unit) {
        val listaMovimientos = mutableListOf<infoMovimientos>()
        var total = 0
        db.collection("Movimientos")
            .whereEqualTo("refUser", "Usuario/$uid" )
            .orderBy("año", Query.Direction.DESCENDING)
            .orderBy("mes", Query.Direction.DESCENDING)
            .orderBy("dia", Query.Direction.DESCENDING)

            .get()
            .addOnSuccessListener { respuesta ->
                respuesta.map { resultado ->
                    total += resultado.data.get("cantidad").toString().toInt()
                   val detallesTransaccion = infoMovimientos(
                       nombreCategoria = resultado.data.get("categoria").toString(),
                       cantidad = resultado.data.get("cantidad").toString().toInt(),
                       fecha= resultado.data.get("fechaCompleta").toString(),
                       nombreIngreso = resultado.data.get("nombreIngreso").toString(),
                       tipoIngreso = resultado.data.get("tipoMovimiento").toString()

                   )
                    listaMovimientos.add(detallesTransaccion)
                }
                listenerListMovimientos(listaMovimientos, total)
            }
    }

    fun recuperarDatosUsuarioDesdeOtraColeccion(){

    }

    fun addInformationUser(additionalData : UsuarioInfoCuenta, initActivity : () -> Unit){

        db.collection("Usuario/$uid/Cuenta/").document("${additionalData.nombreCuenta}_$uid").set(
            hashMapOf(
                "cantidad_inicial" to additionalData.cantidad,
                "nombre_cuenta" to additionalData.nombreCuenta,
                "ref_user" to "$uid"
            )
        ).addOnCompleteListener {
            initActivity()
            prefs.saveNombre_cuenta(additionalData.nombreCuenta)
        }
    }

    fun retrieveUserInformation(mail : String,initActivity : (activity: Activity) -> Unit){
        db.collection("Usuario/$mail/Cuenta")
            .whereEqualTo("ref_user","$mail")
            .get()
            .addOnSuccessListener {
                Log.d("Esta vacio o No", it.isEmpty.toString())
                if(it.isEmpty){
                    initActivity(UserDataActivity())
                }else{
                    for (respuesta in it){
                        prefs.saveNombre_cuenta(respuesta.data.get("nombre_cuenta").toString())
                    }
                    initActivity(HomeActivity())
                }
            }
    }

    fun listarCategorias(typeCategory : String,giveList: (listCategorias: List<Categorias>) -> Unit){

        val listCategory = mutableListOf<Categorias>()
        db.collection("/Categoria/")
            .whereEqualTo("tipoTransaccion",typeCategory)
            .get()
            .addOnSuccessListener { response ->
                response.map { data ->
                    val modelCategory = Categorias(
                        nombre = data.data.get("nombreCategoria").toString(),
                        descripcion = data.data.get("Descripcion").toString()
                    )
                    listCategory.add(modelCategory)
                }
                giveList(listCategory)
            }
    }

    fun listarMovimientosPorCategoria(tipoMovimiento: String, darDatos: (infoMovimientos) -> Unit, fecha: Fecha){

        var total = 0
        db.collection("Categoria")
            .whereEqualTo("tipoTransaccion" , tipoMovimiento)
            .get()
            .addOnSuccessListener { response ->
                for(data in response){

                    val nombreCategoria = data.data.get("nombreCategoria")

                    db.collection("/Movimientos/")
                        .whereEqualTo("refUser", "Usuario/$uid")
                        .whereEqualTo("tipoMovimiento", tipoMovimiento)
                        .whereEqualTo("categoria", nombreCategoria)
                        .whereEqualTo("mes", fecha.mes)
                        .whereEqualTo("año", fecha.año)
                        .get()
                        .addOnSuccessListener { response ->
                            response.map { eso ->
                                val nombreCategoriaMovimiento = eso.data.get("categoria")
                                if (nombreCategoria.toString() == nombreCategoriaMovimiento.toString()){
                                    total += eso.data.get("cantidad").toString().toInt()

                                }
                            }
                            val modelCategoria = infoMovimientos(
                                nombreCategoria.toString(),
                                total,
                                ""
                            )
                            darDatos(modelCategoria)
                            total = 0
                        }
                }
            }
    }

    fun resultadoMovimientosAnual(year : Int,darGastos: (List<infoMovimientoBarChar>, List<infoMovimientoBarChar>) -> Unit) {

        var sumaGastos = 0
        var sumaIngresos = 0
        var mesCorrespondiente = 0
        val listaGastos = listaGastosProvider
        val listaIngresos = listaIngresosProvider


        for (mes in 1..12){
            db.collection("Movimientos")
                .whereEqualTo("refUser", "Usuario/$uid").whereEqualTo("año", year).orderBy("mes")
                .get()
                .addOnSuccessListener { response ->
                    for (it in response){

                        if (mes == it.data.get("mes").toString().toInt()){
                            if (it.data.get("tipoMovimiento")=="Gasto"){
                                sumaGastos += it.data.get("cantidad").toString().toInt()
                            }
                            if (it.data.get("tipoMovimiento") == "Ingreso"){
                                sumaIngresos += it.data.get("cantidad").toString().toInt()
                            }
                        }

                    }

                    val infoGastos = infoMovimientoBarChar(
                        sumaGastos,
                        mes.toFloat()
                    )
                    val infoIngresos = infoMovimientoBarChar(
                        sumaIngresos,
                        mes.toFloat()
                    )

                    listaGastos[mes-1] = infoGastos
                    listaIngresos[mes - 1]=infoIngresos
                    if (listaGastos.size >= 12 && listaIngresos.size >= 12){
                        darGastos(listaGastos, listaIngresos)
                    }

                    sumaGastos = 0
                    sumaIngresos = 0


                }
                .addOnFailureListener {

                }

        }

    }
    //* Lo dejare aqui porque se que puedo ir mas alla, disculpen el desorden

//    fun resultadoMovimientosIngresos(darIngresos: (infoMovimientoBarChar) -> Unit) {
//        var sumaIngresos = 0
//        val listaMeses = listOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12)
//        for (meses in listaMeses){
//            db.collection("Movimientos")
//                .whereEqualTo("refUser","Usuario/$uid")
//                .whereEqualTo("tipoMovimiento", "Ingreso")
//                .whereEqualTo("mes", meses.toString())
//                .whereEqualTo("año", "2022")
//                .get()
//                .addOnSuccessListener {response ->
//                    response.map {
//                        if (meses.toString() == it.data.get("mes")){
//                            sumaIngresos += it.data.get("cantidad").toString().toInt()
//                        }
//
//                    }
//                    val info = infoMovimientoBarChar(
//                        sumaIngresos,
//                        meses
//                    )
//                    darIngresos(info)
//                    sumaIngresos = 0
//
//
//                }
//
//
//        }
//    }


    fun mainUserData(nombreCuenta: String, dataUser: (UsuarioInfoCuenta) -> Unit){
        val routeUser = db.collection("Usuario/").document("$uid")
        val userAccountData = db.collection("/Usuario/$uid/Cuenta").document("${nombreCuenta}_$uid")
        db.runTransaction { transaction ->
            val dataUserName = transaction.get(routeUser)
            val moneyDataUser =  transaction.get(userAccountData)
            val infoUser = UsuarioInfoCuenta(
                dataUserName.data?.get("nombre").toString(),
                moneyDataUser.data?.get("cantidad_inicial").toString().toInt()
            )
            dataUser(infoUser)
        }

    }

    fun getMovementData(tipoMovimiento: String, listenerData: (List<infoMovimientos>) -> Unit){
        val listaMovimientos = mutableListOf<infoMovimientos>()
        if(tipoMovimiento != "TODO"){
            db.collection("Movimientos")
                .whereEqualTo("refUser","Usuario/$uid")
                .whereEqualTo("tipoMovimiento", tipoMovimiento)
                .orderBy("año", Query.Direction.DESCENDING)
                .orderBy("mes", Query.Direction.DESCENDING)
                .orderBy("dia", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { response ->


                        response.map { data ->

                            val model = infoMovimientos(
                                nombreCategoria = data.data.get("categoria").toString(),
                                cantidad = data.data.get("cantidad").toString().toInt(),
                                fecha = data.data.get("fechaCompleta").toString(),
                                nombreIngreso = data.data.get("nombreIngreso").toString(),
                                tipoIngreso = data.data.get("tipoMovimiento").toString()
                            )
                            listaMovimientos.add(model)
                        }

                        listenerData(listaMovimientos)


                }
        }

    }

    fun listarMovimientosPorCategoriaAnual(tipoMovimiento: String, year: Int, darDatos: (infoMovimientos) -> Unit){

        var total = 0
        var cantidadTransacciones = 0
        db.collection("Categoria")
            .whereEqualTo("tipoTransaccion" , tipoMovimiento)
            .get()
            .addOnSuccessListener { response ->
                for(data in response){

                    val nombreCategoria = data.data.get("nombreCategoria")

                    db.collection("/Movimientos/")
                        .whereEqualTo("refUser", "Usuario/$uid")
                        .whereEqualTo("tipoMovimiento", tipoMovimiento)
                        .whereEqualTo("categoria", nombreCategoria)
                        .whereEqualTo("año",year)
                        .get()
                        .addOnSuccessListener { response ->
                            response.map { eso ->
                                val nombreCategoriaMovimiento = eso.data.get("categoria")
                                if (nombreCategoria.toString() == nombreCategoriaMovimiento.toString()){
                                    cantidadTransacciones++
                                    total += eso.data.get("cantidad").toString().toInt()

                                }
                            }
                            val modelCategoria = infoMovimientos(
                                nombreCategoria.toString(),
                                total,
                                "",
                                "",
                                "",
                                cantidadTransacciones
                            )
                            darDatos(modelCategoria)
                            total = 0
                            cantidadTransacciones = 0
                        }
                }
            }
    }
    fun updateNotifications(aceptar : Boolean){
        Log.d("recibir argumento", aceptar.toString())
        db.collection("Usuario").document(uid!!).update("notificaciones", aceptar).addOnCompleteListener {
            if (!it.isSuccessful){
             db.collection("Usuario").document(uid).set(hashMapOf("notificaciones" to aceptar))
            }
        }.addOnFailureListener {
            Log.w(TAG, it.message.orEmpty())
        }

    }



}