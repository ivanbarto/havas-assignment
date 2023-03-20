package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentUtils {
    fun openURLInBrowser(context: Context, url: Uri) {

        val i = Intent(Intent.ACTION_VIEW)
        i.data = url
        context.startActivity(i)
    }
}
