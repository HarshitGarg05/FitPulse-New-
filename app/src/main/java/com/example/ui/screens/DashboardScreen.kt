package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.WorkoutSessionEntity
import com.example.data.repository.AnalyticsAndInsightsEngine
import com.example.data.repository.AnalyticsDateRange
import com.example.data.repository.ExerciseProgressionReport
import com.example.data.repository.OverloadStatus
import com.example.data.repository.PersonalRecordItem
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.ui.components.ActivityRingGoalsModal
import com.example.ui.components.AppleActivityRingsCardContent
import com.example.ui.components.AppleDropdownField
import com.example.ui.components.AppleHealthBarChart
import com.example.ui.components.AppleLargePageHeader
import com.example.ui.components.AppleScreenContainer
import com.example.ui.components.AppleSectionHeader
import com.example.ui.components.AppleSegmentedControl
import com.example.ui.components.AppleStyleMetricCard
import com.example.ui.components.ExerciseProgressionChart
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPrimaryButton
import com.example.ui.components.LiquidGlassSecondaryButton
import com.example.ui.components.MuscleVolumeDistributionGraph
import com.example.ui.components.ProgressionChartMetric
import com.example.ui.components.StrengthStandardGaugeBar
import androidx.compose.ui.graphics.luminance
import com.example.ui.theme.AppleCalories
import com.example.ui.theme.AppleCardBorderDark
import com.example.ui.theme.AppleCardBorderLight
import com.example.ui.theme.AppleExercise
import com.example.ui.theme.AppleExerciseGreen
import com.example.ui.theme.AppleGoldYellow
import com.example.ui.theme.AppleMindPurple
import com.example.ui.theme.AppleMove
import com.example.ui.theme.AppleMoveRed
import com.example.ui.theme.AppleMuscleGroups
import com.example.ui.theme.AppleNestedCardBorderDark
import com.example.ui.theme.AppleNestedCardBorderLight
import com.example.ui.theme.AppleOverloadOrange
import com.example.ui.theme.ApplePersonalRecords
import com.example.ui.theme.AppleRadius
import com.example.ui.theme.AppleSeparatorDark
import com.example.ui.theme.AppleSeparatorLight
import com.example.ui.theme.AppleSpacing
import com.example.ui.theme.AppleStand
import com.example.ui.theme.AppleStandBlue
import com.example.ui.theme.AppleStandCyan
import com.example.ui.theme.AppleStandCyanDarkText
import com.example.ui.theme.AppleStateSuccess
import com.example.ui.theme.AppleStrengthProgression
import com.example.ui.theme.AppleSurfaceSecondaryDark
import com.example.ui.theme.AppleSurfaceSecondaryLight
import com.example.ui.theme.AppleSurfaceTertiaryLight
import com.example.ui.theme.AppleSystemBlue
import com.example.ui.theme.AppleTypeScale
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.FitPulseUiState
import com.example.ui.viewmodel.FitnessViewModel
import java.util.Locale

@Composable
fun DashboardScreen(
    state: FitPulseUiState,
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val analytics = state.dashboardAnalytics
    val attendance = state.attendanceOverview
    val weeklyRings = state.weeklyActivityRings
    val todayReadable = remember(attendance.todayDateKey) {
        AnalyticsAndInsightsEngine.formatDateKeyReadable(attendance.todayDateKey).uppercase(Locale.US)
    }
    var selectedProgressionMetric by remember { mutableStateOf(ProgressionChartMetric.TOP_WEIGHT) }
    var showRingGoalsModal by rememberSaveable { mutableStateOf(false) }
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val rootCanvasBg = if (isDark) Color(0xFF000000) else Color(0xFFF2F2F7)

    if (showRingGoalsModal) {
        ActivityRingGoalsModal(
            metrics = weeklyRings,
            goalPrefs = state.activityRingGoalPrefs,
            useLbs = state.useLbs,
            unitLabel = state.unitLabel,
            onSelectMode = { mode -> viewModel.setActivityGoalMode(mode) },
            onSaveCustomGoals = { reps, exercises, volDisplay ->
                viewModel.saveCustomActivityRingGoals(
                    weeklyReps = reps,
                    weeklyExercises = exercises,
                    weeklyVolumeDisplay = volDisplay,
                    switchToCustomMode = true
                )
            },
            onDismiss = { showRingGoalsModal = false }
        )
    }

    // LEVEL 1: Main screen background (#F2F2F7 in Light Mode)
    AppleScreenContainer(
        modifier = modifier,
        testTag = "dashboard_screen",
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
                top = 16.dp,
                bottom = 140.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Unified Apple Health Summary Large Page Header
            item(key = "dashboard_header", contentType = "header") {
                AppleLargePageHeader(
                    overlineText = todayReadable,
                    title = "Summary",
                    accentColor = AppleMove,
                    trailingBadge = {
                        Surface(
                            shape = RoundedCornerShape(AppleRadius.pill),
                            color = if (attendance.attendedToday) {
                                AppleStateSuccess.copy(alpha = 0.14f)
                            } else {
                                AppleSystemBlue.copy(alpha = 0.12f)
                            }
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
                                        .background(
                                            if (attendance.attendedToday) AppleStateSuccess else AppleSystemBlue
                                        )
                                )
                                Text(
                                    text = if (attendance.attendedToday) "Checked In Today" else "Ready to Train",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (attendance.attendedToday) AppleStateSuccess else AppleSystemBlue
                                )
                            }
                        }
                    }
                )
            }

            // 2A. Weekly Activity Rings Card (Reps, Exercises, Volume — Standalone Level-2 White Card)
            item(key = "dashboard_activity_rings", contentType = "rings_card") {
                LiquidGlassCard(
                    accentTint = AppleMoveRed,
                    modifier = Modifier.testTag("todays_overview_card")
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "WEEKLY ACTIVITY RINGS",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleMoveRed
                                )
                                Text(
                                    text = "Activity Summary",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            com.example.ui.components.AppleCircularAccessory(
                                onClick = { showRingGoalsModal = true },
                                contentDescription = "Configure Weekly Activity Ring Goals"
                            )
                        }

                        AppleActivityRingsCardContent(
                            metrics = weeklyRings,
                            useLbs = state.useLbs,
                            unitLabel = state.unitLabel,
                            onConfigureGoalsClick = { showRingGoalsModal = true }
                        )
                    }
                }
            }

            // 2B. Check In & Start Workout Quick Actions Card (Standalone Level-2 White Card)
            item(key = "dashboard_quick_actions", contentType = "actions_card") {
                val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
                val nestedBorder = BorderStroke(
                    width = if (isDark) 0.5.dp else 1.dp,
                    color = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
                )
                LiquidGlassCard(accentTint = AppleStandBlue) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "QUICK ACTIONS",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleStandBlue
                                )
                                Text(
                                    text = "Check In & Start Workout",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(AppleRadius.nestedCard),
                                color = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                                shadowElevation = 0.dp,
                                border = nestedBorder
                            ) {
                                Text(
                                    text = if (attendance.attendedToday) "Checked In" else "Not Checked In",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (attendance.attendedToday) AppleStateSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }

                        // Live Workout Status Banner (when a workout is active)
                        AnimatedVisibility(
                            visible = state.activeWorkout.isWorkoutActive || state.activeWorkout.sets.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(AppleRadius.nestedCard),
                                color = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                                shadowElevation = 0.dp,
                                border = nestedBorder
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = null,
                                            tint = AppleStandBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = if (state.activeWorkout.isWorkoutActive) {
                                                    "Live Workout: ${state.activeWorkout.title.ifBlank { "Active Session" }}"
                                                } else {
                                                    "Draft Plan: ${state.activeWorkout.sets.map { it.exerciseName }.distinct().size} exercises planned"
                                                },
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = AppleStandBlue
                                            )
                                            val activeEx = state.activeWorkout.activeExerciseName
                                            Text(
                                                text = if (activeEx != null) {
                                                    "Performing: $activeEx • ${state.activeWorkout.elapsedSeconds / 60}m ${state.activeWorkout.elapsedSeconds % 60}s"
                                                } else {
                                                    "Tap Resume Workout to start exercise timer"
                                                },
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Equal-Height Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!attendance.attendedToday) {
                                LiquidGlassPrimaryButton(
                                    text = "Check In",
                                    onClick = { viewModel.recordGymAttendance() },
                                    icon = Icons.Default.CheckCircle,
                                    accentColor = AppleMoveRed,
                                    modifier = Modifier
                                        .weight(1f)
                                        .heightIn(min = 46.dp),
                                    testTag = "check_in_today_button"
                                )
                            } else {
                                LiquidGlassSecondaryButton(
                                    text = "Attendance",
                                    onClick = { viewModel.selectTab(AppDestination.ATTENDANCE) },
                                    icon = Icons.Default.CalendarMonth,
                                    accentColor = AppleMoveRed,
                                    modifier = Modifier
                                        .weight(1f)
                                        .heightIn(min = 46.dp),
                                    testTag = "open_attendance_button"
                                )
                            }

                            LiquidGlassPrimaryButton(
                                text = if (state.activeWorkout.isWorkoutActive) "Resume Workout" else "Start Workout",
                                onClick = { viewModel.openWorkoutPlanBuilder() },
                                icon = if (state.activeWorkout.isWorkoutActive) Icons.Default.PlayArrow else Icons.Default.AddCircle,
                                accentColor = AppleStandBlue,
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 46.dp),
                                testTag = "start_workout_dashboard_button"
                            )
                        }
                    }
                }
            }

            // 3. Separate Streak Card & Weekly Volume Card with Visible 16.dp Gap
            item(key = "dashboard_streak_and_volume", contentType = "dual_metric_row") {
                val cyanTextColor = if (isDark) com.example.ui.theme.AppleStand else com.example.ui.theme.AppleSystemBlue
                val cyanBarColor = if (isDark) com.example.ui.theme.AppleStand else com.example.ui.theme.AppleSystemBlue
                val inactiveBarBg = if (isDark) com.example.ui.theme.AppleSurfaceSecondaryDark else com.example.ui.theme.AppleSurfaceTertiaryLight

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Card: Separate Pure White Streak Card
                    LiquidGlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 172.dp),
                        accentTint = AppleOverloadOrange,
                        onClick = { viewModel.selectTab(AppDestination.ATTENDANCE) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = AppleOverloadOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Streak",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                com.example.ui.components.AppleCircularAccessory(
                                    onClick = { viewModel.selectTab(AppDestination.ATTENDANCE) },
                                    contentDescription = "Gym Streak details"
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val streakBadgeBg = AppleCalories.copy(alpha = if (isDark) 0.18f else 0.12f)
                                val streakTrackColor = AppleCalories.copy(alpha = if (isDark) 0.28f else 0.20f)
                                Box(
                                    modifier = Modifier.size(56.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                        val strokeW = 4.5.dp.toPx()
                                        val center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f)
                                        val r = (size.minDimension / 2f) - (strokeW / 2f)
                                        drawCircle(
                                            color = streakTrackColor,
                                            radius = r,
                                            center = center,
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeW)
                                        )
                                        val progressFraction = attendance.weeklyGoalProgress.coerceIn(0f, 1f)
                                        if (progressFraction > 0f) {
                                            drawArc(
                                                color = AppleOverloadOrange,
                                                startAngle = -90f,
                                                sweepAngle = progressFraction * 360f,
                                                useCenter = false,
                                                topLeft = androidx.compose.ui.geometry.Offset(center.x - r, center.y - r),
                                                size = androidx.compose.ui.geometry.Size(r * 2f, r * 2f),
                                                style = androidx.compose.ui.graphics.drawscope.Stroke(
                                                    strokeW,
                                                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                                                )
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(streakBadgeBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            tint = AppleOverloadOrange,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = if (attendance.currentStreakDays > 0) "${attendance.currentStreakDays} Day Streak" else "Start Streak",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "${attendance.weeklyVisitDays}/${attendance.weeklyGoalDays} Days Wk",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleOverloadOrange
                                )
                            }
                        }
                    }

                    // Right Card: Separate Pure White Weekly Volume Card
                    LiquidGlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 172.dp),
                        accentTint = AppleStandCyan,
                        onClick = { viewModel.selectTab(AppDestination.WORKOUTS) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = cyanTextColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Volume",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                com.example.ui.components.AppleCircularAccessory(
                                    onClick = { viewModel.selectTab(AppDestination.WORKOUTS) },
                                    contentDescription = "Workout volume details"
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = "Weekly Volume",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = viewModel.formatVolume(analytics.periodTotalVolumeKg),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = cyanTextColor
                                    )
                                }

                                // Dynamic 7-day volume pill bar chart directly on #FFFFFF card surface
                                val last7Days = if (analytics.dailyVolumeSeries.isNotEmpty()) {
                                    analytics.dailyVolumeSeries.takeLast(7)
                                } else emptyList()
                                val maxVol = (last7Days.maxOfOrNull { it.value } ?: 1f).coerceAtLeast(1f)
                                val defaultDays = listOf("M", "T", "W", "T", "F", "S", "S")

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    for (i in 0 until 7) {
                                        val point = last7Days.getOrNull(i)
                                        val dayLetter = point?.shortDayLabel?.take(1) ?: defaultDays[i]
                                        val dayVal = point?.value ?: 0f
                                        val isFilled = dayVal > 0f
                                        val fraction = if (isFilled) (dayVal / maxVol).coerceIn(0.2f, 1f) else 0.14f

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Bottom,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(6.dp)
                                                    .height((28f * fraction).coerceAtLeast(4f).dp)
                                                    .clip(RoundedCornerShape(3.dp))
                                                    .background(if (isFilled) cyanBarColor else inactiveBarBg)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = dayLetter,
                                                fontSize = 9.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = if (isFilled) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Today:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = viewModel.formatVolume(analytics.volumeTodayKg),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = cyanTextColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Activity & Strength Highlights (4 Clearly Separated Pure White Metric Cards on #F2F2F7 Canvas)
            item(key = "dashboard_highlights_grid", contentType = "highlights_grid") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppleSectionHeader(
                        title = "Activity & Strength Highlights",
                        subtitle = "Calculated from your saved local records"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AppleStatTile(
                            label = "GYM ATTENDANCE",
                            primaryValue = "${attendance.totalAllTimeVisits} visits",
                            secondaryValue = "${attendance.weeklyVisitDays} this wk • ${attendance.monthlyVisitDays} this mo",
                            icon = Icons.Default.CalendarMonth,
                            accentColor = AppleMoveRed,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 118.dp)
                        )
                        AppleStatTile(
                            label = "ACTIVE STREAK",
                            primaryValue = "${attendance.currentStreakDays} ${if (attendance.currentStreakDays == 1) "day" else "days"}",
                            secondaryValue = "Longest streak: ${attendance.longestStreakDays}d",
                            icon = Icons.Default.LocalFireDepartment,
                            accentColor = AppleOverloadOrange,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 118.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AppleStatTile(
                            label = "WORKOUT SESSIONS",
                            primaryValue = "${state.completedSessions.size} sessions",
                            secondaryValue = "${analytics.periodCompletedSets} sets in period",
                            icon = Icons.Default.FitnessCenter,
                            accentColor = AppleStandBlue,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 118.dp)
                        )
                        AppleStatTile(
                            label = "TRAINING VOLUME",
                            primaryValue = viewModel.formatVolume(analytics.periodTotalVolumeKg),
                            secondaryValue = "Today: ${viewModel.formatVolume(analytics.volumeTodayKg)}",
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            accentColor = AppleExerciseGreen,
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 118.dp)
                        )
                    }
                }
            }

            // 5A. Training Analytics & Daily Training Volume Card (Standalone Level-2 White Card with Clear Section Separation)
            item(key = "dashboard_volume_analytics", contentType = "bar_chart_card") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppleSectionHeader(
                        title = "Training Analytics & Volume Graphs",
                        subtitle = "Daily volume, completed sets, and muscle group distribution"
                    )

                    LiquidGlassCard(accentTint = AppleExerciseGreen) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "TRAINING VOLUME ANALYTICS",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleExerciseGreen
                                    )
                                    Text(
                                        text = "Daily Training Volume (${state.unitLabel})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = viewModel.formatVolume(analytics.periodTotalVolumeKg),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleExerciseGreen
                                )
                            }

                            AppleSegmentedControl(
                                items = AnalyticsDateRange.entries,
                                selectedItem = state.analyticsDateRange,
                                onItemSelected = { viewModel.setAnalyticsDateRange(it) },
                                labelProvider = { it.shortLabel },
                                accentColor = AppleExerciseGreen
                            )

                            AppleHealthBarChart(
                                points = analytics.dailyVolumeSeries,
                                barColor = AppleExerciseGreen,
                                unitSuffix = state.unitLabel
                            )
                        }
                    }
                }
            }

            // 5B. Daily Completed Sets Card (Standalone Level-2 White Card)
            item(key = "dashboard_sets_analytics", contentType = "bar_chart_card") {
                LiquidGlassCard(accentTint = AppleMoveRed) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "SET FREQUENCY",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleMoveRed
                                )
                                Text(
                                    text = "Daily Completed Sets",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "${analytics.periodCompletedSets} sets",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppleMoveRed
                            )
                        }

                        AppleHealthBarChart(
                            points = analytics.dailySetsSeries,
                            barColor = AppleMoveRed,
                            unitSuffix = "sets"
                        )
                    }
                }
            }

            // 5C. Muscle Group Volume & Set Breakdown Card (Standalone Level-2 White Card)
            item(key = "dashboard_muscle_distribution", contentType = "muscle_distribution_card") {
                LiquidGlassCard(accentTint = AppleMuscleGroups) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "MUSCLE DISTRIBUTION",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppleMuscleGroups
                            )
                            Text(
                                text = "Muscle Group Volume & Set Breakdown",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        MuscleVolumeDistributionGraph(
                            muscleBreakdown = analytics.muscleGroupBreakdown,
                            formatVolume = { viewModel.formatVolume(it) }
                        )
                    }
                }
            }

            // 6. Exercise-Specific Strength Progression Spotlight Card (Standalone Level-2 White Card)
            item(key = "dashboard_strength_progression", contentType = "progression_card") {
                val reports = analytics.overloadReports
                val activeReport = state.selectedExerciseReport ?: reports.firstOrNull()

                if (activeReport != null) {
                    LiquidGlassCard(accentTint = AppleStrengthProgression) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "STRENGTH PROGRESSION",
                                        style = AppleTypeScale.Caption1,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleStrengthProgression
                                    )
                                    Text(
                                        text = activeReport.exerciseName,
                                        style = AppleTypeScale.Title3,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                OverloadBadge(
                                    status = activeReport.overloadStatus,
                                    dynamicLabel = activeReport.dynamicStatusBadge
                                )
                            }

                            AppleDropdownField(
                                label = "Select Exercise to Inspect",
                                selectedValue = activeReport.exerciseName,
                                options = reports.map { it.exerciseName },
                                onOptionSelected = { viewModel.selectExerciseForDetail(it) },
                                subtitleProvider = { name ->
                                    val r = reports.firstOrNull { it.exerciseName == name }
                                    if (r != null) "${r.muscleGroup} • ${r.equipment} • ${r.dynamicStatusBadge}" else null
                                },
                                testTag = "dashboard_exercise_dropdown"
                            )

                            val displayWeight = activeReport.personalRecordWeightKg
                            val displayReps = activeReport.personalRecordReps
                            val displayVolume = activeReport.personalRecordVolumeKg
                            val display1Rm = activeReport.personalRecordOneRepMaxKg

                            // Equal-height 4-box Level-3 metric row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                EqualHeightMetricMiniBox(
                                    label = "Best Load",
                                    value = viewModel.formatWeight(displayWeight),
                                    accent = AppleMoveRed,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                                EqualHeightMetricMiniBox(
                                    label = "Best Reps",
                                    value = "$displayReps reps",
                                    accent = AppleStandBlue,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                                EqualHeightMetricMiniBox(
                                    label = "Peak Vol",
                                    value = viewModel.formatVolume(displayVolume),
                                    accent = AppleExerciseGreen,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                                EqualHeightMetricMiniBox(
                                    label = "Est. 1RM",
                                    value = viewModel.formatWeight(display1Rm),
                                    accent = AppleMindPurple,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }

                            AppleSegmentedControl(
                                items = ProgressionChartMetric.entries,
                                selectedItem = selectedProgressionMetric,
                                onItemSelected = { selectedProgressionMetric = it },
                                labelProvider = { it.shortLabel },
                                accentColor = AppleStrengthProgression
                            )

                            ExerciseProgressionChart(
                                sessions = activeReport.sessionHistory,
                                metric = selectedProgressionMetric,
                                useLbs = state.useLbs,
                                unitLabel = state.unitLabel,
                                recommendedWeightKg = activeReport.recommendedStartWeightKg,
                                recommendedReps = activeReport.recommendedTargetReps,
                                recommendedVolumeKg = activeReport.recommendedTargetVolumeKg,
                                recommended1RmKg = activeReport.recommendedTargetOneRepMaxKg,
                                accentColor = AppleStrengthProgression
                            )

                            StrengthStandardGaugeBar(
                                exerciseName = activeReport.exerciseName,
                                currentOrTargetWeightKg = displayWeight,
                                userBodyweightKg = state.profile.weightKg,
                                unitLabel = state.unitLabel,
                                useLbs = state.useLbs
                            )
                        }
                    }
                }
            }

            // 7. Recent Personal Records & Strength Improvements
            item(key = "dashboard_personal_records", contentType = "pr_card") {
                PersonalRecordsCard(
                    records = analytics.personalRecords,
                    reports = analytics.overloadReports,
                    formatWeight = { viewModel.formatWeight(it) },
                    onNavigateToExercises = { viewModel.selectTab(AppDestination.EXERCISES) }
                )
            }

            // 8. Recent Workout History
            item(key = "dashboard_recent_workouts", contentType = "recent_workouts_card") {
                RecentWorkoutHistorySection(
                    workouts = state.completedSessions.take(5),
                    formatVolume = { viewModel.formatVolume(it) },
                    onReuseWorkout = { session ->
                        viewModel.loadSessionIntoEditor(session, duplicateAsNewToday = true)
                    },
                    onStartNewWorkout = { viewModel.openWorkoutPlanBuilder() }
                )
            }
        }
    }
}

@Composable
private fun AppleStatTile(
    label: String,
    primaryValue: String,
    secondaryValue: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    AppleStyleMetricCard(
        label = label,
        primaryValue = primaryValue,
        secondaryValue = secondaryValue,
        icon = icon,
        accentColor = accentColor,
        modifier = modifier
    )
}

/**
 * LEVEL 3 Nested Metric Container (#FAFAFC in Light Mode, 16.dp radius, #EBEBF0 border, 1.dp shadow)
 */
@Composable
fun EqualHeightMetricMiniBox(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val containerBg = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val containerBorder = BorderStroke(
        width = if (isDark) 0.5.dp else 1.dp,
        color = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
    )

    Surface(
        modifier = modifier.heightIn(min = 68.dp),
        shape = RoundedCornerShape(AppleRadius.nestedCard),
        color = containerBg,
        shadowElevation = 0.dp,
        border = containerBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                style = AppleTypeScale.Caption1,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = AppleTypeScale.SmallMetric,
                fontWeight = FontWeight.Bold,
                color = accent
            )
        }
    }
}

@Composable
private fun PersonalRecordsCard(
    records: List<PersonalRecordItem>,
    reports: List<ExerciseProgressionReport>,
    formatWeight: (Float) -> String,
    onNavigateToExercises: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val nestedBg = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val nestedBorder = BorderStroke(
        width = if (isDark) 0.5.dp else 1.dp,
        color = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
    )
    val prTextColor = if (isDark) ApplePersonalRecords else AppleCalories

    LiquidGlassCard(accentTint = ApplePersonalRecords) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            AppleSectionHeader(
                title = "Personal Records",
                subtitle = if (records.isEmpty()) {
                    "0 personal records logged yet — log sets to track PRs"
                } else {
                    "${records.size} exercises with verified personal records"
                },
                actionLabel = "Inspect",
                actionIcon = Icons.Default.EmojiEvents,
                actionColor = AppleCalories,
                onActionClick = onNavigateToExercises
            )

            if (records.isNotEmpty()) {
                records.take(4).forEach { pr ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppleRadius.nestedCard),
                        color = nestedBg,
                        shadowElevation = 0.dp,
                        border = nestedBorder
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = pr.exerciseName,
                                    style = AppleTypeScale.Headline,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${pr.muscleGroup} • Achieved ${pr.achievedDateLabel}",
                                    style = AppleTypeScale.Footnote,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(AppleRadius.smallControl),
                                color = ApplePersonalRecords.copy(alpha = if (isDark) 0.18f else 0.22f)
                            ) {
                                Text(
                                    text = "${formatWeight(pr.bestWeightKg)} × ${pr.bestReps}",
                                    style = AppleTypeScale.Footnote,
                                    fontWeight = FontWeight.Bold,
                                    color = prTextColor,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                reports.take(3).forEach { report ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppleRadius.nestedCard),
                        color = nestedBg,
                        shadowElevation = 0.dp,
                        border = nestedBorder
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = report.exerciseName,
                                    style = AppleTypeScale.Headline,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${report.muscleGroup} • 0 sessions logged",
                                    style = AppleTypeScale.Footnote,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(AppleRadius.smallControl),
                                color = if (isDark) Color(0xFF3A3A3C) else AppleSurfaceTertiaryLight
                            ) {
                                Text(
                                    text = "${formatWeight(0f)} × 0",
                                    style = AppleTypeScale.Footnote,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
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
private fun RecentWorkoutHistorySection(
    workouts: List<WorkoutSessionEntity>,
    formatVolume: (Float) -> String,
    onReuseWorkout: (WorkoutSessionEntity) -> Unit,
    onStartNewWorkout: () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val nestedBg = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val nestedBorder = BorderStroke(
        width = if (isDark) 0.5.dp else 1.dp,
        color = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
    )

    LiquidGlassCard(accentTint = AppleMuscleGroups) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            AppleSectionHeader(
                title = "Recent Workouts",
                subtitle = if (workouts.isEmpty()) "No sessions recorded yet" else "Tap Repeat to load a past plan",
                actionLabel = "Build Plan",
                actionIcon = Icons.Default.FitnessCenter,
                actionColor = AppleSystemBlue,
                onActionClick = onStartNewWorkout
            )

            if (workouts.isEmpty()) {
                Surface(
                    shape = RoundedCornerShape(AppleRadius.nestedCard),
                    color = nestedBg,
                    shadowElevation = 0.dp,
                    border = nestedBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Build your first workout plan",
                            style = AppleTypeScale.Headline,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Create a custom plan in the Workouts tab, save it for next time, and tap each exercise as you perform it to start the live timer.",
                            style = AppleTypeScale.Footnote,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                workouts.forEach { workout ->
                    val dateStr = AnalyticsAndInsightsEngine.formatDateKeyReadable(workout.dateKey)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppleRadius.nestedCard),
                        color = nestedBg,
                        shadowElevation = 0.dp,
                        border = nestedBorder
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = workout.title,
                                    style = AppleTypeScale.Headline,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "$dateStr • ${workout.durationMinutes} min • ${workout.totalSets} sets • ${formatVolume(workout.totalVolumeKg)}",
                                    style = AppleTypeScale.Footnote,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            LiquidGlassSecondaryButton(
                                text = "Repeat",
                                onClick = { onReuseWorkout(workout) },
                                accentColor = AppleSystemBlue
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OverloadBadge(
    status: OverloadStatus,
    dynamicLabel: String? = null
) {
    val color = when (status) {
        OverloadStatus.NEW_PR -> ApplePersonalRecords
        OverloadStatus.INCREASED_WEIGHT -> AppleExercise
        OverloadStatus.MORE_REPS -> AppleStand
        OverloadStatus.INCREASED_VOLUME -> AppleExercise
        OverloadStatus.MAINTAINED_OR_LOWER -> AppleCalories
        OverloadStatus.BASELINE_ESTABLISHED -> AppleStrengthProgression
        OverloadStatus.INSUFFICIENT_DATA -> AppleStand
    }
    val label = dynamicLabel ?: status.badgeLabel

    Surface(
        shape = RoundedCornerShape(AppleRadius.pill),
        color = color.copy(alpha = 0.14f)
    ) {
        Text(
            text = label,
            style = AppleTypeScale.Caption1,
            fontWeight = FontWeight.Bold,
            color = if (status == OverloadStatus.NEW_PR) AppleCalories else color,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun AppleHealthCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    LiquidGlassCard(
        modifier = modifier,
        accentTint = AppleStandBlue,
        onClick = onClick,
        content = content
    )
}
