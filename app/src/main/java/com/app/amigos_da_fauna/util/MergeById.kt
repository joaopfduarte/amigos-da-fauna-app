package com.app.amigos_da_fauna.util

fun <T : Any> mergeById(existing: List<T>, incoming: List<T>, replace: Boolean, idSelector: (T) -> Int): List<T> {
    if (replace) {
        val incomingIds = incoming.map(idSelector).toSet()
        val kept = existing.filter { idSelector(it) !in incomingIds }
        return kept + incoming
    }
    val existingIds = existing.map(idSelector).toSet()
    return existing + incoming.filter { idSelector(it) !in existingIds }
}
