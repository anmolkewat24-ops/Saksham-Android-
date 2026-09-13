package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GovBluePrimaryDark,
    onPrimary = GovBlueDark,
    primaryContainer = Color(0xFF1E3A5F),
    onPrimaryContainer = Color.White,
    secondary = GrowthGreenDarkTheme,
    onSecondary = Color.Black,
    secondaryContainer = GrowthGreenDark,
    onSecondaryContainer = Color.White,
    tertiary = SaffronAccentDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = SurfaceDarkVariant,
    onSurfaceVariant = Color(0xFFD1D5DB),
    outline = SlateBorderDark,
    outlineVariant = Color(0xFF1F2937)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GovBluePrimary,
    onPrimary = Color.White,
    primaryContainer = GovBlueContainer,
    onPrimaryContainer = GovBlueDark,
    secondary = GrowthGreen,
    onSecondary = Color.White,
    secondaryContainer = GrowthGreenLight,
    onSecondaryContainer = GrowthGreenDark,
    tertiary = SaffronAccent,
    tertiaryContainer = SaffronLight,
    onTertiaryContainer = SaffronDark,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onBackground = SlateDark,
    onSurface = SlateDark,
    surfaceVariant = GovBlueLight,
    onSurfaceVariant = SlateMedium,
    outline = SlateBorderStrong,
    outlineVariant = SlateBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disabled dynamic color by default to preserve the high-contrast, official government identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
