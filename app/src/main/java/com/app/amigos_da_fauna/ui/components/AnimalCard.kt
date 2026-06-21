package com.app.amigos_da_fauna.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.app.amigos_da_fauna.domain.model.Animal
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

@Composable
fun AnimalCard(
    animal: Animal,
    onClick: () -> Unit,
    onQuizClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = FaunaTheme.colors

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = animal.imageUrl,
                    contentDescription = animal.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.imagePlaceholder),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = animal.name,
                    color = colors.text,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatItem(
                    label = "Altura",
                    value = animal.height,
                    modifier = Modifier.weight(1f),
                )
                StatItem(
                    label = "Peso",
                    value = animal.weight,
                    modifier = Modifier.weight(1f),
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onQuizClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                ) {
                    Text("Realizar Quiz", color = colors.white)
                }
                OutlinedButton(onClick = onShareClick, modifier = Modifier.fillMaxWidth()) {
                    Text("Compartilhar", color = colors.primary)
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val colors = FaunaTheme.colors
    Column(modifier = modifier) {
        Text(text = label.uppercase(), color = colors.textMuted, fontWeight = FontWeight.Bold)
        Text(
            text = value,
            color = colors.text,
            fontWeight = FontWeight.SemiBold,
            softWrap = true,
        )
    }
}
