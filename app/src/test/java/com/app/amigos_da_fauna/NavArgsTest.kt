package com.app.amigos_da_fauna

import androidx.lifecycle.SavedStateHandle
import com.app.amigos_da_fauna.util.getIntNavArg
import org.junit.Assert.assertEquals
import org.junit.Test

class NavArgsTest {
    @Test
    fun getIntNavArg_readsIntArgument() {
        val handle = SavedStateHandle(mapOf("animalId" to 42))
        assertEquals(42, handle.getIntNavArg("animalId"))
    }

    @Test
    fun getIntNavArg_readsStringArgument() {
        val handle = SavedStateHandle(mapOf("animalId" to "7"))
        assertEquals(7, handle.getIntNavArg("animalId"))
    }

    @Test
    fun getIntNavArg_returnsNegativeOneWhenMissing() {
        val handle = SavedStateHandle()
        assertEquals(-1, handle.getIntNavArg("animalId"))
    }
}
