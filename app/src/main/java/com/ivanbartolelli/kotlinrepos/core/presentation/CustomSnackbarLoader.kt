package com.ivanbartolelli.kotlinrepos.core.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.ivanbartolelli.kotlinrepos.R
import com.ivanbartolelli.kotlinrepos.databinding.CustomSnackbarAlertBinding


class CustomSnackbarLoader(private val rootView : View) {
    private var snackbar: Snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
    private var binding: CustomSnackbarAlertBinding = CustomSnackbarAlertBinding.inflate(LayoutInflater.from(rootView.context))

    init {
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(binding.root, 0)
    }

    fun showWarning(warning : String){
        binding.snackIcon.setImageDrawable(ContextCompat.getDrawable(rootView.context, R.drawable.ic_warning))
        binding.message.text = warning

        snackbar.show()
    }

    fun showInfo(infoMessage : String){
        binding.snackIcon.visibility = View.GONE
        binding.message.text = infoMessage

        snackbar.show()
    }
}