package com.app.amigos_da_fauna.ui.screen.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.amigos_da_fauna.domain.model.ThemePreference
import com.app.amigos_da_fauna.ui.AppViewModel
import com.app.amigos_da_fauna.ui.components.ThemeSelector
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

@Composable
fun AboutScreen(
    onThemeChange: (ThemePreference) -> Unit,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel(),
) {
    val themePreference by appViewModel.themePreference.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors

    val features = listOf(
        "Mapeamento geográfico interativo das localizações dos animais;",
        "Quizzes educativos para testar conhecimentos de forma lúdica;",
        "Catálogo detalhado sobre características ecológicas e habitats;",
        "Abordagem visual voltada para a sustentabilidade e respeito à natureza.",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Sobre o projeto Amigos da Fauna",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.brown,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.cardAlt),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "O Amigos da Fauna é um projeto de conscientização ambiental e educação que busca aproximar as pessoas da rica biodiversidade da fauna brasileira.",
                    color = colors.brown,
                    lineHeight = 24.sp,
                )
                Text(
                    text = "Para que o aprendizado seja engajador e recompensador, nosso projeto conta com:",
                    color = colors.brown,
                    modifier = Modifier.padding(top = 12.dp),
                )
                features.forEach { item ->
                    Text(
                        text = "🌿 $item",
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }

        Text(
            text = "Mapeamento PWA → Android Nativo",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.brown,
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                MappingItem(
                    pwa = "Geolocation API (navegador)",
                    native = "Fused Location Provider",
                    usage = "Mapa de localização dos animais e posição do usuário",
                )
                MappingItem(
                    pwa = "Web Share API (navigator.share)",
                    native = "Intent.ACTION_SEND",
                    usage = "Compartilhamento de informações sobre animais da fauna",
                )
            }
        }

        Text(
            text = "Tema do app",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.brown,
        )
        ThemeSelector(
            selected = themePreference,
            onSelected = {
                appViewModel.setThemePreference(it)
                onThemeChange(it)
            },
        )
    }
}

@Composable
private fun MappingItem(pwa: String, native: String, usage: String) {
    val colors = FaunaTheme.colors
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text("PWA", color = colors.textMuted, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(pwa, color = colors.text, fontWeight = FontWeight.SemiBold)
        Text("Android", color = colors.textMuted, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text(native, color = colors.primary, fontWeight = FontWeight.SemiBold)
        Text(usage, color = colors.textSecondary, modifier = Modifier.padding(top = 4.dp))
    }
}
