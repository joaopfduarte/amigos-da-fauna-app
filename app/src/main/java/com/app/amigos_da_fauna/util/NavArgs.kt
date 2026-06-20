package com.app.amigos_da_fauna.util

import androidx.lifecycle.SavedStateHandle

fun SavedStateHandle.getIntNavArg(key: String): Int {
    return when (val value = get<Any?>(key)) {
        is Int -> value
        is String -> value.toIntOrNull() ?: -1
        else -> -1
    }
}

fun currentIsoTimestamp(): String {
    return java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
        .apply { timeZone = java.util.TimeZone.getTimeZone("UTC") }
        .format(java.util.Date())
}
