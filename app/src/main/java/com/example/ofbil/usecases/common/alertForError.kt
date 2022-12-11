package com.example.ofbil.usecases.common

import android.app.AlertDialog
import android.content.Context

class alertForError {
    fun showError(context: Context, head : String, body : String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(head)
        builder.setMessage(body)
        builder.setPositiveButton("Aceptar", null)
        val dialog : AlertDialog = builder.create()
        dialog.show()
    }
}