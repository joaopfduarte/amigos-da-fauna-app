package com.app.amigos_da_fauna

import com.app.amigos_da_fauna.util.mergeById
import org.junit.Assert.assertEquals
import org.junit.Test

class MergeByIdTest {
    data class Item(val id: Int, val name: String)

    @Test
    fun mergeById_deduplicatesIncomingItems() {
        val existing = listOf(Item(1, "a"), Item(2, "b"))
        val incoming = listOf(Item(2, "b2"), Item(3, "c"))
        val result = mergeById(existing, incoming, replace = false) { it.id }
        assertEquals(listOf(1, 2, 3), result.map { it.id })
    }
}
