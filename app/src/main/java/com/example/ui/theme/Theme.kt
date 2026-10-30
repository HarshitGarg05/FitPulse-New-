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
 */
object AppleRadius {
    val smallControl = 10.dp
    val inputField = 12.dp
    val standardCard = 16.dp
    val largeCard = 20.dp
    val heroCard = 24.dp
    val bottomSheet = 28.dp
    val pill = 999.dp
}

private val AppleHealthLightColorScheme = lightColorScheme(
    primary = AppleMove,
    onPrimary = Color.White,
    primaryContainer = AppleMoveRedContainerLight,
    onPrimaryContainer = Color(0xFF5C0018),
    secondary = AppleStand,
    onSecondary = Color.White,
    secondaryContainer = AppleStandBlueContainerLight,
    onSecondaryContainer = Color(0xFF002D62),
    tertiary = AppleExercise,
    onTertiary = Color.White,
    tertiaryContainer = AppleExerciseGreenContainerLight,
    onTertiaryContainer = Color(0xFF053814),
    background = AppleBgLight,
    onBackground = AppleTextPrimaryLight,
    surface = AppleSurfaceLight,
    onSurface = AppleTextPrimaryLight,
    surfaceVariant = AppleSurfaceSecondaryLight,
    onSurfaceVariant = AppleTextSecondaryLight,
    outline = AppleOutlineLight,
    outlineVariant = AppleSeparatorLight,
    error = AppleStateError,
    onError = Color.White
)

private val AppleHealthDarkColorScheme = darkColorScheme(
    primary = AppleMove,
    onPrimary = Color.White,
    primaryContainer = AppleMoveRedContainerDark,
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = AppleStand,
    onSecondary = Color.White,
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
    outline = AppleOutlineDark,
    outlineVariant = AppleSeparatorDark,
    error = AppleStateError,
    onError = Color.White
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
    themeMode: ThemeMode = ThemeMode.DARK,
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
