package com.example.ofbil.usecases.Base

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager

class BaseActivityRouter {
    fun launch(intent: Intent, activity : Context) = activity.startActivity(intent)
}
class BaseFragmentRouter (val FragmentManager : FragmentManager, val navigator: Int) {
    fun launch(fragment : Fragment ){
        val fragmentTransaction = FragmentManager.beginTransaction()
        fragmentTransaction.replace(navigator, fragment)
        fragmentTransaction.commit()

    }
}