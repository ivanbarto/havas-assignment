package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

object ShareUtils {
    private const val LABEL_CLIPBOARD_URL = "github url"
    private const val TEXT_MIME_TYPE = "text/plain"

    fun shareText(
        context: Context,
        text: String
    ) {

        val intent = Intent(Intent.ACTION_SEND)

        intent.type = TEXT_MIME_TYPE
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val shareIntent = Intent.createChooser(intent, null)
        context.startActivity(shareIntent)
    }

    fun copyToClipboard(text:String, context: Context){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText(LABEL_CLIPBOARD_URL, text)
        clipboard.setPrimaryClip(clip)
    }
}