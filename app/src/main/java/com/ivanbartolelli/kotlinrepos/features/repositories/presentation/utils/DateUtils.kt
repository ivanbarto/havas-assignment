package com.ivanbartolelli.kotlinrepos.features.repositories.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    private const val SHORT_DATE_PATTERN = "dd/MM/yyyy"

    private fun getDateTimeFormatter(): SimpleDateFormat {
        return SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault())
    }

    private fun getsShortDateFormatter(): SimpleDateFormat {
        return SimpleDateFormat(SHORT_DATE_PATTERN, Locale.getDefault())
    }

    private fun toDate(dateString: String, formatter: SimpleDateFormat): Date? {
        return formatter.parse(dateString)
    }

    private fun toDateString(date: Date?, formatter: SimpleDateFormat): String {
        return formatter.format(date ?: Date())
    }

    fun getShortDateString(dateTime : String): String{
        return toDateString(
            toDate(dateTime, getDateTimeFormatter()),
            getsShortDateFormatter()
        )
    }
}