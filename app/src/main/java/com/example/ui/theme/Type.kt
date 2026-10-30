package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Native sans-serif font family as Apple SF Pro substitute
val AppleSfFontFamily = FontFamily.Default

// Compatibility aliases
val AppleSfDisplayFontFamily = AppleSfFontFamily
val SpaceGroteskFontFamily = AppleSfFontFamily
val PlusJakartaSansFontFamily = AppleSfFontFamily

/**
 * Apple iOS Human Interface Guidelines (HIG) Dynamic Type Scale
 */
object AppleTypeScale {
    val LargeTitle = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = (-0.5).sp
    )
    val Title1 = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = (-0.4).sp
    )
    val Title2 = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.3).sp
    )
    val Title3 = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 25.sp,
        letterSpacing = (-0.25).sp
    )
    val Headline = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp
    )
    val Body = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp
    )
    val Callout = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.15).sp
    )
    val Subheadline = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.1).sp
    )
    val Footnote = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.05).sp
    )
    val Caption1 = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
    val Caption2 = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 13.sp
    )

    // Metric Typography Scale (Tabular Numbers enabled for precision alignment)
    val HeroMetric = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 44.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.8).sp,
        fontFeatureSettings = "tnum"
    )
    val LargeMetric = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp,
        fontFeatureSettings = "tnum"
    )
    val MediumMetric = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.3).sp,
        fontFeatureSettings = "tnum"
    )
    val SmallMetric = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
        fontFeatureSettings = "tnum"
    )
}

val Typography = Typography(
    displayLarge = AppleTypeScale.HeroMetric,
    displayMedium = AppleTypeScale.LargeTitle,
    displaySmall = AppleTypeScale.LargeMetric,
    headlineLarge = AppleTypeScale.Title1,
    headlineMedium = AppleTypeScale.Title2,
    headlineSmall = AppleTypeScale.Title3,
    titleLarge = AppleTypeScale.Headline,
    titleMedium = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.15).sp
    ),
    titleSmall = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.1).sp
    ),
    bodyLarge = AppleTypeScale.Body,
    bodyMedium = AppleTypeScale.Callout,
    bodySmall = AppleTypeScale.Footnote,
    labelLarge = TextStyle(
        fontFamily = AppleSfFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.1).sp
    ),
    labelMedium = AppleTypeScale.Caption1,
    labelSmall = AppleTypeScale.Caption2
)
