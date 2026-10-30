package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. Apple Health / Fitness Light Theme Tokens
// ==========================================
val AppleBgLight = Color(0xFFF2F2F7)
val AppleSurfaceLight = Color(0xFFFFFFFF)
val AppleSurfaceSecondaryLight = Color(0xFFFAFAFC)
val AppleSurfaceTertiaryLight = Color(0xFFE5E5EA)
val AppleCardBorderLight = Color(0xFFE8E8ED)
val AppleNestedCardBorderLight = Color(0xFFEBEBF0)
val AppleSeparatorLight = Color(0xFFD1D1D6)
val AppleOutlineLight = Color(0xFFD1D1D6)

// Typography Colors Light
val AppleTextPrimaryLight = Color(0xFF000000)
val AppleTextSecondaryLight = Color(0xFF8E8E93)
val AppleTextTertiaryLight = Color(0xFFAEAEB2)
val AppleTextPlaceholderLight = Color(0xFFC7C7CC)
val AppleTextDisabledLight = Color(0xFFC7C7CC)
val AppleTextInverseLight = Color(0xFFFFFFFF)

// ==========================================
// 2. Apple Health / Fitness Dark Theme Tokens
// ==========================================
val AppleBgDark = Color(0xFF000000)
val AppleSurfaceDark = Color(0xFF1C1C1E)
val AppleSurfaceSecondaryDark = Color(0xFF2C2C2E)
val AppleSurfaceTertiaryDark = Color(0xFF3A3A3C)
val AppleCardBorderDark = Color(0xFF38383A)
val AppleNestedCardBorderDark = Color(0xFF38383A)
val AppleSeparatorDark = Color(0xFF38383A)
val AppleOutlineDark = Color(0xFF38383A)

// Typography Colors Dark
val AppleTextPrimaryDark = Color(0xFFFFFFFF)
val AppleTextSecondaryDark = Color(0xFF8E8E93)
val AppleTextTertiaryDark = Color(0xFF636366)

// ==========================================
// 3. Apple System Accent Colors
// ==========================================
val AppleSystemBlue = Color(0xFF007AFF)
val AppleSystemGreen = Color(0xFF34C759)
val AppleSystemRed = Color(0xFFFF3B30)
val AppleSystemOrange = Color(0xFFFF9500)
val AppleSystemYellow = Color(0xFFFFCC00)
val AppleSystemPurple = Color(0xFFAF52DE)
val AppleSystemTeal = Color(0xFF5AC8FA)
val AppleSystemIndigo = Color(0xFF5856D6)
val AppleSystemPink = Color(0xFFFF2D55)
val AppleSystemMint = Color(0xFF00C7BE)

// ==========================================
// 4. Fitness Metric Colors
// ==========================================
val AppleMove = Color(0xFFFA114F)
val AppleExercise = Color(0xFF30D158)
val AppleStand = Color(0xFF0A84FF)
val AppleCalories = Color(0xFFFF9F0A)
val AppleRecovery = Color(0xFF5AC8FA)
val AppleStrengthProgression = Color(0xFF5E5CE6)
val ApplePersonalRecords = Color(0xFFFFD60A)
val AppleMuscleGroups = Color(0xFFBF5AF2)
val AppleMobility = Color(0xFF00C7BE)
val AppleHeartMetrics = Color(0xFFFF2D55)

// ==========================================
// 5. Component State Colors
// ==========================================
val AppleStateSuccess = Color(0xFF34C759)
val AppleStateWarning = Color(0xFFFF9500)
val AppleStateError = Color(0xFFFF3B30)
val AppleStateInfo = Color(0xFF007AFF)
val AppleStateDisabled = Color(0xFFAEAEB2)

// ==========================================
// Backward-Compatibility Aliases
// ==========================================
val AppleMoveRed = AppleMove
val AppleMoveRedLight = AppleMove
val AppleMoveRedContainerLight = Color(0xFFFFE5EC)
val AppleMoveRedContainerDark = Color(0xFF4C081B)
val AppleMoveRedTrackDark = Color(0xFF33000E)
val AppleMoveRedTrackLight = Color(0xFFFFD6E0)
val AppleMoveRedTrack = AppleMoveRedTrackDark

val AppleExerciseGreen = AppleExercise
val AppleExerciseGreenLight = AppleExercise
val AppleExerciseGreenDarkText = Color(0xFF248A3D)
val AppleExerciseGreenContainerLight = Color(0xFFEBFCE8)
val AppleExerciseGreenContainerDark = Color(0xFF1B3B00)
val AppleExerciseGreenTrackDark = Color(0xFF142E0C)
val AppleExerciseGreenTrackLight = Color(0xFFE4F9C6)
val AppleExerciseGreenTrack = AppleExerciseGreenTrackDark

val AppleStandCyan = AppleStand
val AppleStandCyanLight = AppleStand
val AppleStandCyanDarkText = AppleSystemBlue
val AppleStandBlue = AppleStand
val AppleStandBlueDark = AppleStand
val AppleStandBlueContainerLight = Color(0xFFE5F1FF)
val AppleStandBlueContainerDark = Color(0xFF002B59)
val AppleStandCyanTrackDark = Color(0xFF00284D)
val AppleStandCyanTrackLight = Color(0xFFC9F5FF)
val AppleStandCyanTrack = AppleStandCyanTrackDark

val AppleOverloadOrange = AppleCalories
val AppleMindPurple = AppleMuscleGroups
val AppleIndigo = AppleStrengthProgression
val AppleTeal = AppleRecovery
val ApplePink = AppleHeartMetrics
val AppleMint = AppleMobility
val AppleGoldYellow = ApplePersonalRecords

val AppleHealthLightBg = AppleBgLight
val AppleHealthLightSurface = AppleSurfaceLight
val AppleHealthLightContainer = AppleSurfaceTertiaryLight
val AppleHealthLightOutline = AppleSeparatorLight
val AppleHealthLightOnSurface = AppleTextPrimaryLight
val AppleHealthLightOnVariant = AppleTextSecondaryLight

val AppleHealthDarkBg = AppleBgDark
val AppleHealthDarkSurface = AppleSurfaceDark
val AppleHealthDarkContainer = AppleSurfaceSecondaryDark
val AppleHealthDarkOutline = AppleSeparatorDark
val AppleHealthDarkOnSurface = AppleTextPrimaryDark
val AppleHealthDarkOnVariant = AppleTextSecondaryDark

val AppleAccessoryCircleDark = AppleSurfaceSecondaryDark
val AppleAccessoryCircleLight = AppleSurfaceTertiaryLight

val GoogleFitHeartTeal = AppleExercise
val GoogleFitHeartTealDarkText = AppleExerciseGreenDarkText
val GoogleFitStepsBlue = AppleStand
val GoogleFitStepsBlueDark = AppleStand
val GoogleFitCaloriesOrange = AppleCalories
val GoogleFitSleepPurple = AppleMuscleGroups
val GoogleFitVitalsRed = AppleMove
val GoogleFitNutritionYellow = ApplePersonalRecords

val FitHeartTealDark = AppleExercise
val FitHeartTealLight = AppleExerciseGreenDarkText
val FitStepsBlueDark = AppleStand
val FitStepsBlueLight = AppleStand
val FitCaloriesOrange = AppleCalories
val FitSleepPurple = AppleMuscleGroups

val ReadinessOptimal = AppleStateSuccess
val ReadinessModerate = AppleStateWarning
val ReadinessLow = AppleStateError
val GoldPrBadge = ApplePersonalRecords
