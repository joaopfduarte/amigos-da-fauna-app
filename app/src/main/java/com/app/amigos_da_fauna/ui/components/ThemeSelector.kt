package com.app.amigos_da_fauna.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

@Composable
fun ThemeSelector(
    selected: ThemePreference,
    onSelected: (ThemePreference) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = FaunaTheme.colors
    val options = listOf(
        ThemePreference.SYSTEM to "Sistema",
        ThemePreference.LIGHT to "Claro",
        ThemePreference.DARK to "Escuro",
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { (value, label) ->
            FilterChip(
                selected = selected == value,
                onClick = { onSelected(value) },
                label = { Text(label) },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = colors.primary,
                    selectedLabelColor = colors.white,
                ),
            )
        }
    }
}
