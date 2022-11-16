package com.example.ofbil.usecases.Base

import android.content.Context
import android.content.Intent

class BaseActivityRouter {
    fun launch(intent: Intent, activity : Context) = activity.startActivity(intent)
}
interface BaseFragmentRouter {

}