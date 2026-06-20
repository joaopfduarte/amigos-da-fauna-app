package com.app.amigos_da_fauna.util

import android.content.Context
import android.content.Intent
import com.app.amigos_da_fauna.domain.model.Animal

object ShareUtils {
    fun shareAnimal(context: Context, animal: Animal) {
        val deepLink = "amigosdafauna://animals/${animal.id}"
        val message = buildString {
            appendLine("🌿 ${animal.name} — Amigos da Fauna")
            appendLine()
            appendLine("Altura: ${animal.height}")
            appendLine("Peso: ${animal.weight}")
            appendLine()
            appendLine("Conheça mais sobre a fauna da Mata Atlântica!")
            appendLine()
            appendLine("Abra a página do animal: $deepLink")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Compartilhar ${animal.name}")
            putExtra(Intent.EXTRA_TEXT, message)
        }
        context.startActivity(Intent.createChooser(intent, "Compartilhar ${animal.name}"))
    }
}
