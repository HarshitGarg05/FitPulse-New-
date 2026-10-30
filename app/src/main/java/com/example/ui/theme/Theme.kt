package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ThemeMode(val label: String) {
    DARK("Apple Fitness Dark"),
    LIGHT("Apple Health Light"),
    SYSTEM("System Automatic")
}

/**
 * Standard Apple HIG Spacing System
 */
object AppleSpacing {
    val micro = 4.dp
    val tight = 8.dp
    val small = 12.dp
    val standard = 16.dp
    val medium = 20.dp
    val large = 24.dp
    val section = 32.dp
    val major = 40.dp
    val screenHorizontal = 20.dp
}

/**
 * Standard Apple HIG Corner Radius Geometry
 * - Level 2 Primary Content Cards: 20.dp
 * - Level 3 Secondary / Nested Cards: 16.dp
 */
object AppleRadius {
    val smallControl = 10.dp
    val inputField = 16.dp
    val nestedCard = 16.dp
    val standardCard = 20.dp
    val largeCard = 20.dp
    val heroCard = 20.dp
    val bottomSheet = 28.dp
    val pill = 999.dp
}

private val AppleHealthLightColorScheme = lightColorScheme(
    primary = AppleMove,
    onPrimary = AppleTextInverseLight,
    primaryContainer = AppleMoveRedContainerLight,
    onPrimaryContainer = Color(0xFF5C0018),
    secondary = AppleStand,
    onSecondary = AppleTextInverseLight,
    secondaryContainer = AppleStandBlueContainerLight,
    onSecondaryContainer = Color(0xFF002D62),
    tertiary = AppleExercise,
    onTertiary = AppleTextInverseLight,
    tertiaryContainer = AppleExerciseGreenContainerLight,
    onTertiaryContainer = Color(0xFF053814),
    background = AppleBgLight,
    onBackground = AppleTextPrimaryLight,
    surface = AppleSurfaceLight,
    onSurface = AppleTextPrimaryLight,
    surfaceVariant = AppleSurfaceSecondaryLight,
    onSurfaceVariant = AppleTextSecondaryLight,
    surfaceTint = Color.Transparent,
    surfaceContainerLowest = AppleBgLight,
    surfaceContainerLow = AppleSurfaceSecondaryLight,
    surfaceContainer = AppleSurfaceLight,
    surfaceContainerHigh = AppleSurfaceSecondaryLight,
    surfaceContainerHighest = AppleSurfaceTertiaryLight,
    outline = AppleCardBorderLight,
    outlineVariant = AppleSeparatorLight,
    error = AppleStateError,
    onError = AppleTextInverseLight
)

private val AppleHealthDarkColorScheme = darkColorScheme(
    primary = AppleMove,
    onPrimary = AppleTextInverseLight,
    primaryContainer = AppleMoveRedContainerDark,
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = AppleStand,
    onSecondary = AppleTextInverseLight,
    secondaryContainer = AppleStandBlueContainerDark,
    onSecondaryContainer = Color(0xFFD4E7FF),
    tertiary = AppleExercise,
    onTertiary = Color.Black,
    tertiaryContainer = AppleExerciseGreenContainerDark,
    onTertiaryContainer = Color(0xFFB6F6C6),
    background = AppleBgDark,
    onBackground = AppleTextPrimaryDark,
    surface = AppleSurfaceDark,
    onSurface = AppleTextPrimaryDark,
    surfaceVariant = AppleSurfaceSecondaryDark,
    onSurfaceVariant = AppleTextSecondaryDark,
    surfaceTint = Color.Transparent,
    surfaceContainerLowest = AppleBgDark,
    surfaceContainerLow = AppleSurfaceDark,
    surfaceContainer = AppleSurfaceDark,
    surfaceContainerHigh = AppleSurfaceSecondaryDark,
    surfaceContainerHighest = AppleSurfaceTertiaryDark,
    outline = AppleOutlineDark,
    outlineVariant = AppleSeparatorDark,
    error = AppleStateError,
    onError = AppleTextInverseLight
)

val FitPulseShapes = Shapes(
    extraSmall = RoundedCornerShape(AppleRadius.smallControl),
    small = RoundedCornerShape(AppleRadius.inputField),
    medium = RoundedCornerShape(AppleRadius.standardCard),
    large = RoundedCornerShape(AppleRadius.largeCard),
    extraLarge = RoundedCornerShape(AppleRadius.heroCard)
)

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val colorScheme = if (useDark) AppleHealthDarkColorScheme else AppleHealthLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = FitPulseShapes
    ) {
        CompositionLocalProvider(LocalContentColor provides colorScheme.onSurface) {
            content()
        }
    }
}
