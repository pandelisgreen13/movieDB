package gr.pchasapis.moviedb.common.extensions

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


object DateFormatterExtentions {
    const val DATE_FORMAT = "yyyy-MM-dd"
}

@SuppressLint("SimpleDateFormat")
fun String.getFormattedDate(fromDateFormat: String, toDateFormat: String): String {
    try {
        val dateInMillis = parseStringDateToMillis(this, fromDateFormat)
        return getFormattedDate(dateInMillis, toDateFormat)
    } catch (e: Exception) {
        Timber.d(e)
    }
    return ""
}

fun parseStringDateToMillis(dateString: String, dateFormat: String): Long {
    try {
        return parseStringDate(dateString, dateFormat)!!.time
    } catch (e: Exception) {
        Timber.d(e)
    }
    return 0
}

@SuppressLint("SimpleDateFormat")
fun parseStringDate(dateString: String, dateFormat: String): Date? {
    try {
        val formatter = SimpleDateFormat(dateFormat)
        return formatter.parse(dateString)
    } catch (e: Exception) {
        Timber.d(e)
    }
    return null
}

@SuppressLint("SimpleDateFormat")
fun getFormattedDate(dateInMillis: Long, dateFormat: String): String {
    try {
        val formatter = SimpleDateFormat(dateFormat)
        return formatter.format(Date(dateInMillis))
    } catch (e: Exception) {
        Timber.d(e)
    }
    return ""
}

@SuppressLint("SimpleDateFormat")
fun getFormattedStringFromDate(date: Date, dateFormat: String): String {
    try {
        val formatter = SimpleDateFormat(dateFormat)
        return formatter.format(date)
    } catch (e: Exception) {
        Timber.d(e)
    }
    return ""
}
