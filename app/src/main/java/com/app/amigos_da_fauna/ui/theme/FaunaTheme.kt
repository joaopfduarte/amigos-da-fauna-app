package com.app.amigos_da_fauna.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.app.amigos_da_fauna.domain.model.ThemePreference

data class FaunaColors(
    val text: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val background: Color,
    val backgroundSecondary: Color,
    val card: Color,
    val cardAlt: Color,
    val primary: Color,
    val primaryDisabled: Color,
    val accent: Color,
    val brown: Color,
    val border: Color,
    val borderLight: Color,
    val inputBackground: Color,
    val imagePlaceholder: Color,
    val error: Color,
    val errorBackground: Color,
    val errorBorder: Color,
    val success: Color,
    val tabActive: Color,
    val tabInactive: Color,
    val white: Color,
)

val LocalFaunaColors = staticCompositionLocalOf { LightFaunaColors }

val LightFaunaColors = FaunaColors(
    text = Color(0xFF11181C),
    textSecondary = Color(0xFF666666),
    textMuted = Color(0xFF555555),
    background = Color(0xFFFFFFFF),
    backgroundSecondary = Color(0xFFF5F5F5),
    card = Color(0xFFFFFFFF),
    cardAlt = Color(0x66F5F1E3),
    primary = Color(0xFF4A5D23),
    primaryDisabled = Color(0xFFA2B086),
    accent = Color(0xFFD38345),
    brown = Color(0xFF5D4037),
    border = Color(0xFFD9D9D9),
    borderLight = Color(0xFFCCCCCC),
    inputBackground = Color(0xFFFAFAFA),
    imagePlaceholder = Color(0xFFEDE8D6),
    error = Color(0xFFF5222D),
    errorBackground = Color(0xFFFFF1F0),
    errorBorder = Color(0xFFFFA39E),
    success = Color(0xFF2E7D32),
    tabActive = Color(0xFF2E7D32),
    tabInactive = Color(0xFF888888),
    white = Color(0xFFFFFFFF),
)

val DarkFaunaColors = FaunaColors(
    text = Color(0xFFECEDEE),
    textSecondary = Color(0xFFB0B3B8),
    textMuted = Color(0xFF9BA1A6),
    background = Color(0xFF151718),
    backgroundSecondary = Color(0xFF1E2022),
    card = Color(0xFF252729),
    cardAlt = Color(0x992D3228),
    primary = Color(0xFF7CB342),
    primaryDisabled = Color(0xFF556B2F),
    accent = Color(0xFFE09860),
    brown = Color(0xFFBCAAA4),
    border = Color(0xFF3A3D40),
    borderLight = Color(0xFF444444),
    inputBackground = Color(0xFF2A2D30),
    imagePlaceholder = Color(0xFF3A3D35),
    error = Color(0xFFFF7875),
    errorBackground = Color(0xFF3D2020),
    errorBorder = Color(0xFF5C2020),
    success = Color(0xFF81C784),
    tabActive = Color(0xFF7CB342),
    tabInactive = Color(0xFF888888),
    white = Color(0xFFFFFFFF),
)

@Composable
fun AmigosDaFaunaTheme(
    themePreference: ThemePreference = ThemePreference.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themePreference) {
        ThemePreference.DARK -> true
        ThemePreference.LIGHT -> false
        ThemePreference.SYSTEM -> isSystemInDarkTheme()
    }

    val faunaColors = if (darkTheme) DarkFaunaColors else LightFaunaColors
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = faunaColors.primary,
            secondary = faunaColors.accent,
            background = faunaColors.background,
            surface = faunaColors.card,
            onPrimary = faunaColors.white,
            onBackground = faunaColors.text,
            onSurface = faunaColors.text,
            error = faunaColors.error,
        )
    } else {
        lightColorScheme(
            primary = faunaColors.primary,
            secondary = faunaColors.accent,
            background = faunaColors.background,
            surface = faunaColors.card,
            onPrimary = faunaColors.white,
            onBackground = faunaColors.text,
            onSurface = faunaColors.text,
            error = faunaColors.error,
        )
    }

    CompositionLocalProvider(LocalFaunaColors provides faunaColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

object FaunaTheme {
    val colors: FaunaColors
        @Composable
        get() = LocalFaunaColors.current
}
