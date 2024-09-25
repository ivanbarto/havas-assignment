package com.ivanbartolelli.assignment.core.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment(){
    lateinit var customSnackbarLoader : CustomSnackbarLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customSnackbarLoader = CustomSnackbarLoader(view)
    }
}