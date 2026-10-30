package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.local.ExerciseEntity
import com.example.data.repository.ExerciseProgressionReport
import com.example.ui.components.AppleDropdownField
import com.example.ui.components.AppleLargePageHeader
import com.example.ui.components.AppleLiquidGlassTextField
import com.example.ui.components.AppleSectionHeader
import com.example.ui.components.AppleSegmentedControl
import com.example.ui.components.ExerciseProgressionChart
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPrimaryButton
import com.example.ui.components.ProgressionChartMetric
import com.example.ui.components.StrengthStandardGaugeBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.graphics.luminance
import com.example.ui.theme.AppleCalories
import com.example.ui.theme.AppleCardBorderDark
import com.example.ui.theme.AppleCardBorderLight
import com.example.ui.theme.AppleExercise
import com.example.ui.theme.AppleExerciseGreen
import com.example.ui.theme.AppleMindPurple
import com.example.ui.theme.AppleMove
import com.example.ui.theme.AppleMoveRed
import com.example.ui.theme.AppleMuscleGroups
import com.example.ui.theme.AppleNestedCardBorderDark
import com.example.ui.theme.AppleNestedCardBorderLight
import com.example.ui.theme.AppleOverloadOrange
import com.example.ui.theme.ApplePersonalRecords
import com.example.ui.theme.AppleRadius
import com.example.ui.theme.AppleRecovery
import com.example.ui.theme.AppleSeparatorDark
import com.example.ui.theme.AppleSeparatorLight
import com.example.ui.theme.AppleStand
import com.example.ui.theme.AppleStandBlue
import com.example.ui.theme.AppleStateError
import com.example.ui.theme.AppleStateSuccess
import com.example.ui.theme.AppleStateWarning
import com.example.ui.theme.AppleStrengthProgression
import com.example.ui.theme.AppleSurfaceSecondaryDark
import com.example.ui.theme.AppleSurfaceSecondaryLight
import com.example.ui.theme.AppleSurfaceTertiaryDark
import com.example.ui.theme.AppleSurfaceTertiaryLight
import com.example.ui.theme.AppleSystemBlue
import com.example.ui.theme.AppleTextInverseLight
import com.example.ui.theme.AppleTypeScale
import com.example.ui.viewmodel.FitPulseUiState
import com.example.ui.viewmodel.FitnessViewModel
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.saveable.rememberSaveable
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun GoalsAndRecoveryScreen(
    state: FitPulseUiState,
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    var selectedMetric by remember { mutableStateOf(ProgressionChartMetric.TOP_WEIGHT) }
    var editingExercise by remember { mutableStateOf<ExerciseEntity?>(null) }
    var showCreateExerciseModal by remember { mutableStateOf(false) }

    val reports = state.dashboardAnalytics.overloadReports
    val selectedReport = state.selectedExerciseReport ?: reports.firstOrNull()
    val reportsByName = remember(reports) {
        reports.associateBy { it.exerciseName.lowercase(Locale.US) }
    }

    val muscleGroups = remember(state.allExercises) {
        listOf("All") + state.allExercises.map { it.muscleGroup }.distinct().sorted()
    }
    val equipmentOptions = remember(state.allExercises) {
        listOf("All") + state.allExercises.map { it.equipment }.distinct().sorted()
    }

    val isDarkRoot = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val rootCanvasBg = if (isDarkRoot) Color(0xFF000000) else Color(0xFFF2F2F7)

    // LEVEL 1: Main screen background (#F2F2F7 in Light Mode)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(rootCanvasBg)
            .testTag("exercises_and_overload_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(rootCanvasBg)
                .widthIn(max = 720.dp),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = 140.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "goals_header", contentType = "header") {
                AppleLargePageHeader(
                    overlineText = "GOALS, RECOVERY & OVERLOAD",
                    title = "Exercises",
                    accentColor = AppleStrengthProgression,
                    actionLabel = "New Exercise",
                    actionIcon = Icons.Default.Add,
                    actionTestTag = "new_custom_exercise_button",
                    onActionClick = {
                        editingExercise = null
                        showCreateExerciseModal = true
                    },
                    trailingBadge = {
                        Surface(
                            shape = RoundedCornerShape(AppleRadius.pill),
                            color = AppleStrengthProgression.copy(alpha = 0.14f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AppleStrengthProgression)
                                )
                                Text(
                                    text = "${state.allExercises.size} Exercises",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppleStrengthProgression,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                )
            }

            // 0. Apple Health Recovery, Readiness & Active Goals Summary Card
            item(key = "recovery_and_goals_overview", contentType = "overview_card") {
                RecoveryAndGoalsOverviewCard(state = state)
            }

            // 1. Progressive Overload Inspector Card
            if (selectedReport != null) {
                item(key = "progressive_overload_detail", contentType = "inspector_card") {
                    ProgressiveOverloadDetailCard(
                        allReports = reports,
                        report = selectedReport,
                        selectedMetric = selectedMetric,
                        unitLabel = state.unitLabel,
                        useLbs = state.useLbs,
                        userBodyweightKg = state.profile.weightKg,
                        formatWeight = { viewModel.formatWeight(it) },
                        formatVolume = { viewModel.formatVolume(it) },
                        toDisplayWeight = { viewModel.toDisplayWeightValue(it) },
                        onSelectExercise = { viewModel.selectExerciseForDetail(it) },
                        onSelectMetric = { selectedMetric = it }
                    )
                }
            }

            // 2. Search & Dropdown Filter Bar (Fixes Image 3: Uses Dropdowns instead of clipped chips)
            item(key = "exercise_filter_bar", contentType = "filter_card") {
                LiquidGlassCard(accentTint = AppleStandBlue) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "EXERCISE LIBRARY (${state.filteredExercises.size} EXERCISES)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppleStandBlue
                        )
                        AppleLiquidGlassTextField(
                            value = state.exerciseSearchQuery,
                            onValueChange = { viewModel.setExerciseSearchQuery(it) },
                            label = "Search Exercises",
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            placeholder = "Search by exercise, muscle, or equipment",
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            fieldModifier = Modifier.testTag("exercise_search_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            AppleDropdownField(
                                label = "Muscle Group",
                                selectedValue = state.selectedMuscleFilter,
                                options = muscleGroups,
                                onOptionSelected = { viewModel.setMuscleFilter(it) },
                                modifier = Modifier.weight(1f),
                                testTag = "muscle_filter_dropdown"
                            )

                            AppleDropdownField(
                                label = "Equipment",
                                selectedValue = state.selectedEquipmentFilter,
                                options = equipmentOptions,
                                onOptionSelected = { viewModel.setEquipmentFilter(it) },
                                modifier = Modifier.weight(1f),
                                testTag = "equipment_filter_dropdown"
                            )
                        }
                    }
                }
            }

            // 3. Exercise Catalog List with Individual Performance Summary
            items(
                items = state.filteredExercises,
                key = { it.id },
                contentType = { "exercise_library_card" }
            ) { exercise ->
                val report = reportsByName[exercise.name.lowercase(Locale.US)]
                val isSelected = selectedReport?.exerciseName.equals(exercise.name, ignoreCase = true)

                ExerciseLibraryCard(
                    exercise = exercise,
                    report = report,
                    isSelectedForAnalytics = isSelected,
                    formatWeight = { viewModel.formatWeight(it) },
                    onSelectForAnalytics = { viewModel.selectExerciseForDetail(exercise.name) },
                    onQuickAddToWorkout = {
                        val recW = report?.recommendedStartWeightKg ?: 40f
                        val recReps = report?.recommendedTargetReps ?: 8
                        val recSets = report?.recommendedTargetSets ?: 3
                        viewModel.addExerciseToActiveWorkout(
                            exerciseName = exercise.name,
                            muscleGroup = exercise.muscleGroup,
                            initialWeightDisplay = viewModel.toDisplayWeightValue(recW),
                            initialReps = recReps,
                            initialSetsCount = recSets
                        )
                    },
                    onEdit = {
                        editingExercise = exercise
                        showCreateExerciseModal = true
                    },
                    onDelete = { viewModel.deleteExercisePreservingHistory(exercise.id) }
                )
            }
        }
    }

    if (showCreateExerciseModal) {
        ExerciseEditorDialog(
            existing = editingExercise,
            onDismiss = {
                showCreateExerciseModal = false
                editingExercise = null
            },
            onSave = { name, muscle, equip, notes ->
                viewModel.saveCustomExercise(
                    id = editingExercise?.id,
                    name = name,
                    muscleGroup = muscle,
                    secondaryMuscles = editingExercise?.secondaryMuscles ?: "",
                    equipment = equip,
                    instructions = notes
                )
                showCreateExerciseModal = false
                editingExercise = null
            }
        )
    }
}

@Composable
private fun RecoveryAndGoalsOverviewCard(
    state: FitPulseUiState
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val nestedBg = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val nestedBorder = BorderStroke(
        width = if (isDark) 0.5.dp else 1.dp,
        color = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
    )
    val trackColor = if (isDark) Color(0xFF3A3A3C) else AppleSurfaceTertiaryLight

    val weeklyVisits = state.attendanceOverview.weeklyVisitDays
    val weeklyGoal = state.attendanceOverview.weeklyGoalDays.coerceAtLeast(1)
    val setsCompleted = state.dashboardAnalytics.periodCompletedSets
    val setsTarget = (weeklyGoal * 12).coerceAtLeast(12)
    val prCount = state.dashboardAnalytics.personalRecords.size

    // Calculate a realistic readiness score (0–100) from weekly workload balance
    val workloadRatio = setsCompleted.toFloat() / setsTarget.toFloat()
    val readinessScore = when {
        setsCompleted == 0 -> 94
        workloadRatio <= 0.85f -> 90
        workloadRatio <= 1.15f -> 82
        else -> 72
    }
    val readinessLabel = when {
        readinessScore >= 85 -> "Optimal Readiness"
        readinessScore >= 75 -> "Balanced Recovery"
        else -> "Active Recovery Advised"
    }
    val readinessAccent = when {
        readinessScore >= 85 -> AppleStateSuccess
        readinessScore >= 75 -> AppleRecovery
        else -> AppleStateWarning
    }

    LiquidGlassCard(accentTint = AppleRecovery) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "RECOVERY & READINESS",
                        style = AppleTypeScale.Caption1,
                        fontWeight = FontWeight.Bold,
                        color = AppleRecovery
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "$readinessScore%",
                            style = AppleTypeScale.LargeMetric,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Readiness Index",
                            style = AppleTypeScale.Subheadline,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(AppleRadius.pill),
                    color = readinessAccent.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = readinessLabel,
                        style = AppleTypeScale.Caption1,
                        fontWeight = FontWeight.Bold,
                        color = readinessAccent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            // Level-3 Nested Surface for Active Goal, Completed Goal & Upcoming Target Progress Bars
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppleRadius.nestedCard),
                color = nestedBg,
                shadowElevation = 0.dp,
                border = nestedBorder
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Active Goal: Weekly Attendance
                    val attendanceProgress = (weeklyVisits.toFloat() / weeklyGoal.toFloat()).coerceIn(0f, 1f)
                    val attendanceBarColor = if (weeklyVisits >= weeklyGoal) AppleStateSuccess else AppleMove
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (weeklyVisits >= weeklyGoal) "Completed Goal • Weekly Gym Days" else "Active Goal • Weekly Gym Days",
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$weeklyVisits/$weeklyGoal days",
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.Bold,
                                color = attendanceBarColor
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(AppleRadius.pill))
                                .background(trackColor)
                        ) {
                            if (attendanceProgress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(attendanceProgress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(AppleRadius.pill))
                                        .background(attendanceBarColor)
                                )
                            }
                        }
                    }

                    // Active Goal: Weekly Training Sets
                    val setsProgress = (setsCompleted.toFloat() / setsTarget.toFloat()).coerceIn(0f, 1f)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Active Target • Training Volume Sets",
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$setsCompleted/$setsTarget sets",
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.Bold,
                                color = AppleExercise
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(AppleRadius.pill))
                                .background(trackColor)
                        ) {
                            if (setsProgress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(setsProgress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(AppleRadius.pill))
                                        .background(AppleExercise)
                                )
                            }
                        }
                    }

                    // Upcoming Milestone: Personal Records
                    val nextPrTarget = ((prCount / 5) + 1) * 5
                    val prProgress = (prCount.toFloat() / nextPrTarget.toFloat()).coerceIn(0f, 1f)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Upcoming Milestone • Verified PRs",
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$prCount/$nextPrTarget PRs",
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.Bold,
                                color = AppleStrengthProgression
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(AppleRadius.pill))
                                .background(trackColor)
                        ) {
                            if (prProgress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(prProgress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(AppleRadius.pill))
                                        .background(AppleStrengthProgression)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressiveOverloadDetailCard(
    allReports: List<ExerciseProgressionReport>,
    report: ExerciseProgressionReport,
    selectedMetric: ProgressionChartMetric,
    unitLabel: String,
    useLbs: Boolean,
    userBodyweightKg: Float,
    formatWeight: (Float) -> String,
    formatVolume: (Float) -> String,
    toDisplayWeight: (Float) -> Float,
    onSelectExercise: (String) -> Unit,
    onSelectMetric: (ProgressionChartMetric) -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val displayWeightKg = report.personalRecordWeightKg
    val displayReps = report.personalRecordReps
    val displayVolumeKg = report.personalRecordVolumeKg
    val display1RmKg = report.personalRecordOneRepMaxKg

    LiquidGlassCard(accentTint = AppleStrengthProgression) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = AppleStrengthProgression,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "PROGRESSIVE OVERLOAD TRACKER",
                            style = AppleTypeScale.Caption1,
                            fontWeight = FontWeight.Bold,
                            color = AppleStrengthProgression
                        )
                    }
                    Text(
                        text = report.exerciseName,
                        style = AppleTypeScale.Title2,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${report.muscleGroup} • ${report.equipment} • ${report.totalSessionsLogged} ${if (report.totalSessionsLogged == 1) "session" else "sessions"} logged",
                        style = AppleTypeScale.Footnote,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                OverloadBadge(
                    status = report.overloadStatus,
                    dynamicLabel = report.dynamicStatusBadge
                )
            }

            // Exercise selector dropdown right inside the tracker card
            AppleDropdownField(
                label = "Switch Exercise to Analyze",
                selectedValue = report.exerciseName,
                options = allReports.map { it.exerciseName },
                onOptionSelected = onSelectExercise,
                subtitleProvider = { name ->
                    val r = allReports.firstOrNull { it.exerciseName == name }
                    if (r != null) "${r.muscleGroup} • ${r.equipment} • ${r.dynamicStatusBadge}" else null
                }
            )

            // Level-3 Nested Comparison / Prescription Banner
            Surface(
                shape = RoundedCornerShape(AppleRadius.nestedCard),
                color = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                shadowElevation = 0.dp,
                border = BorderStroke(
                    width = if (isDark) 0.5.dp else 1.dp,
                    color = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = report.overloadHeadline,
                        style = AppleTypeScale.Subheadline,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = report.overloadComparisonDetail,
                        style = AppleTypeScale.Footnote,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Equal-Height 4-Metric Row (single-pass height measurement)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EqualHeightMetricMiniBox(
                    label = "Best Load",
                    value = formatWeight(displayWeightKg),
                    accent = AppleMove,
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                )
                EqualHeightMetricMiniBox(
                    label = "Best Reps",
                    value = "$displayReps reps",
                    accent = AppleStand,
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                )
                EqualHeightMetricMiniBox(
                    label = "Peak Vol",
                    value = formatVolume(displayVolumeKg),
                    accent = AppleExercise,
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                )
                EqualHeightMetricMiniBox(
                    label = "Est. 1RM",
                    value = formatWeight(display1RmKg),
                    accent = AppleStrengthProgression,
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp)
                )
            }

            AppleSegmentedControl(
                items = ProgressionChartMetric.entries,
                selectedItem = selectedMetric,
                onItemSelected = onSelectMetric,
                labelProvider = { it.shortLabel },
                accentColor = AppleStrengthProgression
            )

            ExerciseProgressionChart(
                sessions = report.sessionHistory,
                metric = selectedMetric,
                useLbs = useLbs,
                unitLabel = unitLabel,
                recommendedWeightKg = report.recommendedStartWeightKg,
                recommendedReps = report.recommendedTargetReps,
                recommendedVolumeKg = report.recommendedTargetVolumeKg,
                recommended1RmKg = report.recommendedTargetOneRepMaxKg,
                accentColor = AppleStrengthProgression
            )

            StrengthStandardGaugeBar(
                exerciseName = report.exerciseName,
                currentOrTargetWeightKg = displayWeightKg,
                userBodyweightKg = userBodyweightKg,
                unitLabel = unitLabel,
                useLbs = useLbs
            )
        }
    }
}

@Composable
private fun ExerciseLibraryCard(
    exercise: ExerciseEntity,
    report: ExerciseProgressionReport?,
    isSelectedForAnalytics: Boolean,
    formatWeight: (Float) -> String,
    onSelectForAnalytics: () -> Unit,
    onQuickAddToWorkout: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    LiquidGlassCard(
        accentTint = if (isSelectedForAnalytics) AppleStandBlue else AppleExerciseGreen,
        onClick = onSelectForAnalytics
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelectedForAnalytics) AppleStandBlue else AppleStandBlue.copy(alpha = 0.14f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = null,
                            tint = if (isSelectedForAnalytics) Color.White else AppleStandBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${exercise.muscleGroup} • ${exercise.equipment}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row {
                    IconButton(onClick = onQuickAddToWorkout) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Workout Plan",
                            tint = AppleExerciseGreen
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Exercise",
                            tint = AppleStandBlue
                        )
                    }
                    if (exercise.isCustom) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Custom Exercise",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            if (exercise.instructions.isNotBlank()) {
                Text(
                    text = exercise.instructions,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (report != null) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val hasHistory = report.totalSessionsLogged > 0
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = if (hasHistory) AppleOverloadOrange else AppleStandBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (hasHistory) {
                                "PR: ${formatWeight(report.personalRecordWeightKg)} × ${report.personalRecordReps} • ${report.totalSessionsLogged} sessions"
                            } else {
                                "PR: ${formatWeight(0f)} × 0 • 0 sessions logged"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (hasHistory) AppleOverloadOrange else AppleStandBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OverloadBadge(
                        status = report.overloadStatus,
                        dynamicLabel = report.dynamicStatusBadge
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseEditorDialog(
    existing: ExerciseEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var muscleGroup by remember { mutableStateOf(existing?.muscleGroup ?: "Chest") }
    var equipment by remember { mutableStateOf(existing?.equipment ?: "Barbell") }
    var notes by remember { mutableStateOf(existing?.instructions ?: "") }

    val muscleGroups = listOf("Chest", "Back", "Legs", "Shoulders", "Arms", "Core", "Full Body")
    val equipments = listOf("Barbell", "Dumbbell", "Cable", "Machine", "Bodyweight", "Kettlebell")

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(AppleRadius.bottomSheet),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = if (existing != null) "Edit Exercise" else "Add Custom Exercise",
                style = AppleTypeScale.Title3,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppleLiquidGlassTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Exercise Name",
                    placeholder = "e.g., Pendlay Row, Bulgarian Split Squat",
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    fieldModifier = Modifier.testTag("custom_exercise_name_input")
                )

                AppleDropdownField(
                    label = "Primary Muscle Group",
                    selectedValue = muscleGroup,
                    options = muscleGroups,
                    onOptionSelected = { muscleGroup = it },
                    testTag = "custom_exercise_muscle_dropdown"
                )

                AppleDropdownField(
                    label = "Equipment Type",
                    selectedValue = equipment,
                    options = equipments,
                    onOptionSelected = { equipment = it },
                    testTag = "custom_exercise_equipment_dropdown"
                )

                AppleLiquidGlassTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = "Form Cues & Instructions (optional)",
                    placeholder = "Technique notes or setup reminders",
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            LiquidGlassPrimaryButton(
                text = "Save Exercise",
                onClick = {
                    if (name.trim().isNotEmpty()) {
                        onSave(name.trim(), muscleGroup, equipment, notes.trim())
                    }
                },
                accentColor = AppleSystemBlue,
                testTag = "save_custom_exercise_button"
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AppleSystemBlue)
            }
        }
    )
}
