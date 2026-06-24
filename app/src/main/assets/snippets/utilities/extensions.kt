package com.example.app.utils

import android.content.Context
import android.widget.Toast
import android.util.Log

// Toast Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// String Extensions
fun String.isValidEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

fun String.isNotEmpty(): Boolean {
    return this.trim().isNotEmpty()
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

// Int Extensions
fun Int.toDp(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

fun Int.toSp(context: Context): Float {
    return this * context.resources.displayMetrics.scaledDensity
}

// Logging Extensions
fun <T> T.logDebug(tag: String = "DEBUG"): T {
    Log.d(tag, this.toString())
    return this
}

fun <T> T.logError(tag: String = "ERROR"): T {
    Log.e(tag, this.toString())
    return this
}

// List Extensions
fun <T> List<T>.getOrNull(index: Int): T? {
    return if (index in 0 until size) this[index] else null
}

fun <T> List<T>.swap(i: Int, j: Int): List<T> {
    val list = this.toMutableList()
    val temp = list[i]
    list[i] = list[j]
    list[j] = temp
    return list
}
