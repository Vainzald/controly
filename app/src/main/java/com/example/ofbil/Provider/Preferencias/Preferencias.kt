package com.example.ofbil.Provider.Preferencias

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

enum class PreferenciasKey(val value: String) {
    TOKEN("token"),
    AUTH_USERS("authUsers")
}
object Prefencias {
    fun set(context : Context, key : PreferenciasKey, value: String ){
        val editor = prefs(context).edit()
        editor.putString(key.value, value).apply()
    }
    fun string(context: Context, key: PreferenciasKey) : String?{
        return prefs(context).getString(key.value, null)
    }
    fun clear(context: Context){
        val editor = prefs(context).edit()
        editor.clear().apply()
    }

    private fun prefs(context: Context): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}