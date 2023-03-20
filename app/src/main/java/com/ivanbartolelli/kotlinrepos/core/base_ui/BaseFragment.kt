package com.ivanbartolelli.kotlinrepos.core.base_ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

open class BaseFragment : Fragment(){
    lateinit var customSnackbarLoader : CustomSnackbarLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customSnackbarLoader = CustomSnackbarLoader(view)
    }
}