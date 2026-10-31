package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.FitnessDatabase
import com.example.data.repository.ActivityGoalMode
import com.example.data.repository.ActivityRingGoalPrefs
import com.example.data.repository.CalendarAttendanceStatus
import com.example.data.repository.CalendarDayCell
import com.example.data.repository.DailyChartPoint
import com.example.data.repository.DraftSetEntry
import com.example.data.repository.ExerciseSessionSummary
import com.example.data.repository.MuscleGroupStat
import com.example.data.repository.WeeklyActivityRingMetrics
import com.example.ui.theme.AppleCalories
import com.example.ui.theme.AppleCardBorderDark
import com.example.ui.theme.AppleCardBorderLight
import com.example.ui.theme.AppleExercise
import com.example.ui.theme.AppleExerciseGreen
import com.example.ui.theme.AppleGoldYellow
import com.example.ui.theme.AppleMindPurple
import com.example.ui.theme.AppleMobility
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
import com.example.ui.theme.AppleStand
import com.example.ui.theme.AppleStandBlue
import com.example.ui.theme.AppleStandCyan
import com.example.ui.theme.AppleStateSuccess
import com.example.ui.theme.AppleStrengthProgression
import com.example.ui.theme.AppleSurfaceSecondaryDark
import com.example.ui.theme.AppleSurfaceSecondaryLight
import com.example.ui.theme.AppleSurfaceTertiaryDark
import com.example.ui.theme.AppleSurfaceTertiaryLight
import com.example.ui.theme.AppleSystemBlue
import com.example.ui.theme.AppleTextInverseLight
import com.example.ui.theme.AppleTextPrimaryDark
import com.example.ui.theme.AppleTextPrimaryLight
import com.example.ui.theme.AppleTextSecondaryDark
import com.example.ui.theme.AppleTextSecondaryLight
import com.example.ui.theme.AppleTypeScale
import com.example.util.VivoHapticFeedback
import com.example.util.rememberReducedMotionEnabled
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Redesigned FitPulse 3-Ring Weekly Activity Rings:
 * 1. Outer Ring (Crimson-Red #FA114F): Reps — Total repetitions completed across all workout sets this week.
 * 2. Middle Ring (Neon Lime #A1FF00): Exercises — Total completed exercises across all workout sessions this week.
 * 3. Inner Ring (Electric Cyan #00E5FF): Volume — Total weight lifted (Weight × Reps across completed weighted sets) this week.
 *
 * Includes smooth entry and value-change animations, subtle completion pulse + success haptic when a weekly target
 * is reached, numerical progress formatting, and a weekly progress indicator bar.
 */
@Composable
fun AppleActivityRingsCardContent(
    metrics: WeeklyActivityRingMetrics,
    useLbs: Boolean,
    unitLabel: String,
    onConfigureGoalsClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reducedMotion = rememberReducedMotionEnabled()
    val intFormatter = remember { NumberFormat.getIntegerInstance(Locale.US) }

    val repsTargetProgress = metrics.repsProgress.coerceIn(0f, 1f)
    val exercisesTargetProgress = metrics.exercisesProgress.coerceIn(0f, 1f)
    val volumeTargetProgress = metrics.volumeProgress.coerceIn(0f, 1f)

    // Smooth Animatable rings that animate from 0 on initial dashboard open and from previous -> updated value on change
    var hasAnimatedEntry by rememberSaveable { mutableStateOf(false) }
    val repsAnim = remember { Animatable(if (reducedMotion || hasAnimatedEntry) repsTargetProgress else 0f) }
    val exercisesAnim = remember { Animatable(if (reducedMotion || hasAnimatedEntry) exercisesTargetProgress else 0f) }
    val volumeAnim = remember { Animatable(if (reducedMotion || hasAnimatedEntry) volumeTargetProgress else 0f) }
    val arrowPath = remember { Path() }

    // Subtle completion pulse when a weekly target is newly completed
    val completionPulseScale = remember { Animatable(1f) }
    var previousCompletedCount by rememberSaveable { mutableIntStateOf(metrics.completedRingsCount) }

    LaunchedEffect(repsTargetProgress, reducedMotion) {
        if (reducedMotion) {
            repsAnim.snapTo(repsTargetProgress)
        } else {
            repsAnim.animateTo(
                targetValue = repsTargetProgress,
                animationSpec = tween(durationMillis = 680, easing = FastOutSlowInEasing)
            )
        }
        hasAnimatedEntry = true
    }

    LaunchedEffect(exercisesTargetProgress, reducedMotion) {
        if (reducedMotion) {
            exercisesAnim.snapTo(exercisesTargetProgress)
        } else {
            exercisesAnim.animateTo(
                targetValue = exercisesTargetProgress,
                animationSpec = tween(durationMillis = 740, easing = FastOutSlowInEasing)
            )
        }
        hasAnimatedEntry = true
    }

    LaunchedEffect(volumeTargetProgress, reducedMotion) {
        if (reducedMotion) {
            volumeAnim.snapTo(volumeTargetProgress)
        } else {
            volumeAnim.animateTo(
                targetValue = volumeTargetProgress,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        hasAnimatedEntry = true
    }

    LaunchedEffect(metrics.completedRingsCount) {
        if (metrics.completedRingsCount > previousCompletedCount && metrics.completedRingsCount > 0) {
            VivoHapticFeedback.performSuccess(context)
            if (!reducedMotion) {
                completionPulseScale.animateTo(
                    targetValue = 1.045f,
                    animationSpec = tween(durationMillis = 160, easing = FastOutSlowInEasing)
                )
                completionPulseScale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing)
                )
            }
        }
        previousCompletedCount = metrics.completedRingsCount
    }

    val animatedReps by animateIntAsState(
        targetValue = metrics.repsCompleted,
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 450, easing = FastOutSlowInEasing),
        label = "animatedRepsCount"
    )
    val animatedExercises by animateIntAsState(
        targetValue = metrics.exercisesCompleted,
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 450, easing = FastOutSlowInEasing),
        label = "animatedExercisesCount"
    )

    val displayVolumeCompleted = remember(metrics.volumeCompletedKg, useLbs) {
        val v = if (useLbs) FitnessDatabase.kgToLbs(metrics.volumeCompletedKg) else metrics.volumeCompletedKg
        v.roundToInt()
    }
    val displayVolumeTarget = remember(metrics.volumeTargetKg, useLbs) {
        val v = if (useLbs) FitnessDatabase.kgToLbs(metrics.volumeTargetKg) else metrics.volumeTargetKg
        v.roundToInt().coerceAtLeast(1)
    }
    val animatedVolume by animateIntAsState(
        targetValue = displayVolumeCompleted,
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 500, easing = FastOutSlowInEasing),
        label = "animatedVolumeCount"
    )
    val animatedWeeklyPct by animateFloatAsState(
        targetValue = (metrics.overallWeeklyCompletionPct / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = if (reducedMotion) 0 else 550, easing = FastOutSlowInEasing),
        label = "animatedWeeklyPct"
    )

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val moveTrack = if (isDark) com.example.ui.theme.AppleMoveRedTrackDark else com.example.ui.theme.AppleMoveRedTrackLight
    val exerciseTrack = if (isDark) com.example.ui.theme.AppleExerciseGreenTrackDark else com.example.ui.theme.AppleExerciseGreenTrackLight
    val standTrack = if (isDark) com.example.ui.theme.AppleStandCyanTrackDark else com.example.ui.theme.AppleStandCyanTrackLight

    val exerciseTextColor = if (isDark) AppleExerciseGreen else com.example.ui.theme.AppleExerciseGreenDarkText
    val standTextColor = if (isDark) AppleStandCyan else com.example.ui.theme.AppleStandCyanDarkText
    val separatorColor = if (isDark) AppleSeparatorDark else AppleSeparatorLight

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Concentric 3-ring Activity Rings
            Box(
                modifier = Modifier
                    .size(152.dp)
                    .scale(completionPulseScale.value)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.5.dp.toPx()
                    val gap = 4.dp.toPx()
                    val center = Offset(size.width / 2f, size.height / 2f)

                    val r1 = (size.minDimension / 2f) - (strokeWidth / 2f)
                    val r2 = r1 - strokeWidth - gap
                    val r3 = r2 - strokeWidth - gap

                    fun drawAppleFitnessRing(
                        radius: Float,
                        progress: Float,
                        trackColor: Color,
                        ringColor: Color,
                        gradientEndColor: Color
                    ) {
                        val topLeft = Offset(center.x - radius, center.y - radius)
                        val ringSize = Size(radius * 2f, radius * 2f)

                        // 1. Background track
                        drawArc(
                            color = trackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = ringSize,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )

                        // 2. Active Progress Ring with rounded caps
                        val sweep = (progress * 360f).coerceIn(0f, 360f)
                        if (sweep > 0f) {
                            drawArc(
                                brush = Brush.sweepGradient(
                                    colors = listOf(ringColor, gradientEndColor, ringColor),
                                    center = center
                                ),
                                startAngle = -90f,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = topLeft,
                                size = ringSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // 3. Round cap at 12 o'clock when near/at completion
                            if (sweep > 340f) {
                                val startAngleRad = Math.toRadians(-90.0)
                                val startX = center.x + radius * cos(startAngleRad).toFloat()
                                val startY = center.y + radius * sin(startAngleRad).toFloat()
                                drawCircle(
                                    color = ringColor,
                                    radius = strokeWidth / 2f,
                                    center = Offset(startX, startY)
                                )
                            }
                        }
                    }

                    // 1. Outer Ring: Reps (Crimson-Red)
                    drawAppleFitnessRing(
                        radius = r1,
                        progress = repsAnim.value,
                        trackColor = moveTrack,
                        ringColor = AppleMoveRed,
                        gradientEndColor = com.example.ui.theme.AppleMoveRedLight
                    )

                    // 2. Middle Ring: Exercises (Chartreuse/Neon Lime)
                    drawAppleFitnessRing(
                        radius = r2,
                        progress = exercisesAnim.value,
                        trackColor = exerciseTrack,
                        ringColor = AppleExerciseGreen,
                        gradientEndColor = com.example.ui.theme.AppleExerciseGreenLight
                    )

                    // 3. Inner Ring: Volume (Electric Cyan)
                    drawAppleFitnessRing(
                        radius = r3,
                        progress = volumeAnim.value,
                        trackColor = standTrack,
                        ringColor = AppleStandCyan,
                        gradientEndColor = com.example.ui.theme.AppleStandCyanLight
                    )

                    // Directional indicator arrows at 12 o'clock
                    val arrowMarkerColor = if (isDark) AppleTextPrimaryLight.copy(alpha = 0.78f) else AppleTextInverseLight.copy(alpha = 0.92f)
                    fun drawAppleArrowMarker(radius: Float, type: Int) {
                        val mx = center.x
                        val my = center.y - radius
                        val arrowSize = strokeWidth * 0.36f
                        arrowPath.rewind()
                        when (type) {
                            0 -> {
                                arrowPath.apply {
                                    moveTo(mx - arrowSize, my)
                                    lineTo(mx + arrowSize * 0.8f, my)
                                    moveTo(mx + arrowSize * 0.15f, my - arrowSize * 0.65f)
                                    lineTo(mx + arrowSize * 0.8f, my)
                                    lineTo(mx + arrowSize * 0.15f, my + arrowSize * 0.65f)
                                }
                                drawPath(
                                    path = arrowPath,
                                    color = arrowMarkerColor,
                                    style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            1 -> {
                                arrowPath.apply {
                                    moveTo(mx - arrowSize * 0.6f, my - arrowSize * 0.6f)
                                    lineTo(mx - arrowSize * 0.05f, my)
                                    lineTo(mx - arrowSize * 0.6f, my + arrowSize * 0.6f)
                                    moveTo(mx + arrowSize * 0.05f, my - arrowSize * 0.6f)
                                    lineTo(mx + arrowSize * 0.6f, my)
                                    lineTo(mx + arrowSize * 0.05f, my + arrowSize * 0.6f)
                                }
                                drawPath(
                                    path = arrowPath,
                                    color = arrowMarkerColor,
                                    style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            2 -> {
                                arrowPath.apply {
                                    moveTo(mx, my + arrowSize)
                                    lineTo(mx, my - arrowSize * 0.8f)
                                    moveTo(mx - arrowSize * 0.65f, my - arrowSize * 0.15f)
                                    lineTo(mx, my - arrowSize * 0.8f)
                                    lineTo(mx + arrowSize * 0.65f, my - arrowSize * 0.15f)
                                }
                                drawPath(
                                    path = arrowPath,
                                    color = arrowMarkerColor,
                                    style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                        }
                    }

                    drawAppleArrowMarker(r1, 0)
                    drawAppleArrowMarker(r2, 1)
                    drawAppleArrowMarker(r3, 2)
                }
            }

            // Right-Hand Stacked Legend (Reps, Exercises, Volume)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp, top = 2.dp, bottom = 2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Outer Ring: Reps
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(AppleMoveRed)
                            )
                            Text(
                                text = "Reps",
                                style = AppleTypeScale.Caption1,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${(metrics.repsProgress * 100f).roundToInt()}%",
                            style = AppleTypeScale.Caption2,
                            fontWeight = FontWeight.Bold,
                            color = if (metrics.repsGoalMet) AppleStateSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${intFormatter.format(animatedReps)} / ${intFormatter.format(metrics.repsTarget)} reps",
                        style = AppleTypeScale.Subheadline,
                        fontWeight = FontWeight.Bold,
                        color = AppleMove
                    )
                }

                HorizontalDivider(thickness = 0.5.dp, color = separatorColor)

                // 2. Middle Ring: Exercises
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(AppleExerciseGreen)
                            )
                            Text(
                                text = "Exercises",
                                style = AppleTypeScale.Caption1,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${(metrics.exercisesProgress * 100f).roundToInt()}%",
                            style = AppleTypeScale.Caption2,
                            fontWeight = FontWeight.Bold,
                            color = if (metrics.exercisesGoalMet) AppleStateSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${intFormatter.format(animatedExercises)} / ${intFormatter.format(metrics.exercisesTarget)} exercises",
                        style = AppleTypeScale.Subheadline,
                        fontWeight = FontWeight.Bold,
                        color = exerciseTextColor
                    )
                }

                HorizontalDivider(thickness = 0.5.dp, color = separatorColor)

                // 3. Inner Ring: Volume
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(AppleStandCyan)
                            )
                            Text(
                                text = "Volume",
                                style = AppleTypeScale.Caption1,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${(metrics.volumeProgress * 100f).roundToInt()}%",
                            style = AppleTypeScale.Caption2,
                            fontWeight = FontWeight.Bold,
                            color = if (metrics.volumeGoalMet) AppleStateSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${intFormatter.format(animatedVolume)} / ${intFormatter.format(displayVolumeTarget)} $unitLabel",
                        style = AppleTypeScale.Subheadline,
                        fontWeight = FontWeight.Bold,
                        color = standTextColor
                    )
                }
            }
        }

        // Redesigned Weekly Progress & Adaptive Goal Summary Box
        val nestedBg = if (isDark) AppleSurfaceSecondaryDark else Color(0xFFF2F2F7)
        val nestedBorder = if (isDark) AppleNestedCardBorderDark else Color(0xFFE2E2EA)

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = nestedBg,
            tonalElevation = 0.dp,
            border = BorderStroke(1.dp, nestedBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = if (metrics.weekRangeLabel.isNotBlank()) {
                                "This Week (${metrics.weekRangeLabel})"
                            } else {
                                "Weekly Ring Progress"
                            },
                            style = AppleTypeScale.Subheadline,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${metrics.completedRingsCount} of 3 Rings Closed • ${metrics.overallWeeklyCompletionPct}% Complete",
                            style = AppleTypeScale.Caption1,
                            fontWeight = FontWeight.SemiBold,
                            color = if (metrics.completedRingsCount == 3) AppleStateSuccess else AppleMoveRed
                        )
                    }

                    if (onConfigureGoalsClick != null) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = AppleMoveRed.copy(alpha = 0.14f),
                            border = BorderStroke(1.dp, AppleMoveRed.copy(alpha = 0.28f)),
                            modifier = Modifier
                                .heightIn(min = 34.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .clickable {
                                    VivoHapticFeedback.performLight(context)
                                    onConfigureGoalsClick()
                                }
                                .testTag("configure_ring_goals_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Configure Weekly Ring Goals",
                                    tint = AppleMoveRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = if (metrics.goalMode == ActivityGoalMode.CUSTOM) "Custom" else "Auto Goal",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleMoveRed
                                )
                            }
                        }
                    }
                }

                // Clean custom progress track without M3 stop-indicator dot
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(if (isDark) Color(0xFF3A3A3C) else Color(0xFFE0E0E6))
                ) {
                    val clampedFill = animatedWeeklyPct.coerceIn(0f, 1f)
                    if (clampedFill > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(clampedFill)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (metrics.completedRingsCount == 3) {
                                        Brush.horizontalGradient(listOf(AppleStateSuccess, AppleExerciseGreen))
                                    } else {
                                        Brush.horizontalGradient(listOf(AppleMoveRed, Color(0xFFFF6482)))
                                    }
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = metrics.adaptiveProgressionDetail,
                        style = AppleTypeScale.Caption2,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Mon–Sun Reset",
                        style = AppleTypeScale.Caption2,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Modal dialog allowing users to switch between Automatic Goals (3-week rolling average + adaptive progression)
 * and Custom Goals (manual weekly Reps, Exercises, and Volume targets).
 */
@Composable
fun ActivityRingGoalsModal(
    metrics: WeeklyActivityRingMetrics,
    goalPrefs: ActivityRingGoalPrefs,
    useLbs: Boolean,
    unitLabel: String,
    onSelectMode: (ActivityGoalMode) -> Unit,
    onSaveCustomGoals: (weeklyReps: Int, weeklyExercises: Int, weeklyVolumeDisplay: Float) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val intFormatter = remember { NumberFormat.getIntegerInstance(Locale.US) }
    var selectedMode by remember(goalPrefs.mode) { mutableStateOf(goalPrefs.mode) }

    val initialDisplayVolume = remember(goalPrefs.customWeeklyVolumeKg, useLbs) {
        val raw = if (useLbs) FitnessDatabase.kgToLbs(goalPrefs.customWeeklyVolumeKg) else goalPrefs.customWeeklyVolumeKg
        raw.roundToInt().toString()
    }
    var repsText by remember(goalPrefs.customWeeklyReps) {
        mutableStateOf(goalPrefs.customWeeklyReps.toString())
    }
    var exercisesText by remember(goalPrefs.customWeeklyExercises) {
        mutableStateOf(goalPrefs.customWeeklyExercises.toString())
    }
    var volumeText by remember(initialDisplayVolume) {
        mutableStateOf(initialDisplayVolume)
    }
    var validationError by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(24.dp),
            color = if (isDark) com.example.ui.theme.AppleSurfaceDark else Color(0xFFFFFFFF),
            shadowElevation = if (isDark) 4.dp else 10.dp,
            tonalElevation = 0.dp,
            border = BorderStroke(1.dp, if (isDark) AppleCardBorderDark else AppleCardBorderLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "WEEKLY ACTIVITY RINGS",
                            style = AppleTypeScale.Caption1,
                            fontWeight = FontWeight.Bold,
                            color = AppleMoveRed
                        )
                        Text(
                            text = "Weekly Goal Configuration",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            VivoHapticFeedback.performLight(context)
                            onDismiss()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Goal Settings"
                        )
                    }
                }

                AppleSegmentedControl(
                    items = ActivityGoalMode.entries.toList(),
                    selectedItem = selectedMode,
                    labelProvider = { it.label },
                    onItemSelected = { mode ->
                        VivoHapticFeedback.performMedium(context)
                        selectedMode = mode
                        validationError = null
                        if (mode == ActivityGoalMode.AUTOMATIC) {
                            onSelectMode(ActivityGoalMode.AUTOMATIC)
                        }
                    },
                    modifier = Modifier.testTag("activity_goal_mode_selector")
                )

                if (selectedMode == ActivityGoalMode.AUTOMATIC) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDark) AppleSurfaceSecondaryDark else Color(0xFFF2F2F7),
                        tonalElevation = 0.dp,
                        border = BorderStroke(1.dp, if (isDark) AppleNestedCardBorderDark else AppleSeparatorLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = metrics.adaptiveProgressionBadge,
                                    style = AppleTypeScale.Subheadline,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleMoveRed
                                )
                                Text(
                                    text = if (metrics.completedWeeksUsedForBaseline == 0) {
                                        "New User Starter"
                                    } else {
                                        "${metrics.completedWeeksUsedForBaseline}/3 Weeks Avg"
                                    },
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Text(
                                text = metrics.adaptiveProgressionDetail,
                                style = AppleTypeScale.Footnote,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            HorizontalDivider(
                                thickness = 0.5.dp,
                                color = if (isDark) AppleSeparatorDark else AppleSeparatorLight
                            )

                            val autoDisplayVol = if (useLbs) {
                                FitnessDatabase.kgToLbs(metrics.autoTargetVolumeKg).roundToInt()
                            } else {
                                metrics.autoTargetVolumeKg.roundToInt()
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Text("Reps Target", style = AppleTypeScale.Caption2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        "${intFormatter.format(metrics.autoTargetReps)} reps",
                                        style = AppleTypeScale.Subheadline,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleMoveRed
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Exercises Target", style = AppleTypeScale.Caption2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        "${intFormatter.format(metrics.autoTargetExercises)} ex",
                                        style = AppleTypeScale.Subheadline,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleExercise
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Volume Target", style = AppleTypeScale.Caption2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(
                                        "${intFormatter.format(autoDisplayVol)} $unitLabel",
                                        style = AppleTypeScale.Subheadline,
                                        fontWeight = FontWeight.Bold,
                                        color = AppleStand
                                    )
                                }
                            }

                            Text(
                                text = "Adaptive Progression Rules:\n" +
                                    "• < 70% last week: Keep target unchanged (+0%)\n" +
                                    "• 70%–89% last week: +2% gradual increase\n" +
                                    "• 90%–109% last week: +5% progressive increase\n" +
                                    "• 110%+ last week: Capped at +5% max increase",
                                style = AppleTypeScale.Caption2,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    LiquidGlassPrimaryButton(
                        text = "Use Automatic Weekly Goals",
                        onClick = {
                            VivoHapticFeedback.performMedium(context)
                            onSelectMode(ActivityGoalMode.AUTOMATIC)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Manually configure your weekly targets for Reps (Outer Ring), Exercises (Middle Ring), and Weighted Volume (Inner Ring).",
                            style = AppleTypeScale.Footnote,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        AppleLiquidGlassTextField(
                            value = repsText,
                            onValueChange = {
                                repsText = it.filter { ch -> ch.isDigit() }
                                validationError = null
                            },
                            label = "Weekly Reps Target (Outer Ring)",
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            accentColor = AppleMoveRed,
                            modifier = Modifier.fillMaxWidth(),
                            fieldModifier = Modifier.testTag("custom_reps_goal_input")
                        )

                        AppleLiquidGlassTextField(
                            value = exercisesText,
                            onValueChange = {
                                exercisesText = it.filter { ch -> ch.isDigit() }
                                validationError = null
                            },
                            label = "Weekly Exercises Target (Middle Ring)",
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            accentColor = AppleExerciseGreen,
                            modifier = Modifier.fillMaxWidth(),
                            fieldModifier = Modifier.testTag("custom_exercises_goal_input")
                        )

                        AppleLiquidGlassTextField(
                            value = volumeText,
                            onValueChange = {
                                volumeText = it.filter { ch -> ch.isDigit() || ch == '.' }
                                validationError = null
                            },
                            label = "Weekly Volume Target ($unitLabel - Inner Ring)",
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            accentColor = AppleStandBlue,
                            modifier = Modifier.fillMaxWidth(),
                            fieldModifier = Modifier.testTag("custom_volume_goal_input")
                        )

                        if (validationError != null) {
                            Text(
                                text = validationError!!,
                                style = AppleTypeScale.Caption1,
                                color = AppleMoveRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            LiquidGlassSecondaryButton(
                                text = "Reset to Auto",
                                onClick = {
                                    VivoHapticFeedback.performLight(context)
                                    repsText = metrics.autoTargetReps.toString()
                                    exercisesText = metrics.autoTargetExercises.toString()
                                    val vol = if (useLbs) {
                                        FitnessDatabase.kgToLbs(metrics.autoTargetVolumeKg).roundToInt()
                                    } else {
                                        metrics.autoTargetVolumeKg.roundToInt()
                                    }
                                    volumeText = vol.toString()
                                },
                                modifier = Modifier.weight(1f)
                            )
                            LiquidGlassPrimaryButton(
                                text = "Save Goals",
                                onClick = {
                                    val r = repsText.toIntOrNull()
                                    val e = exercisesText.toIntOrNull()
                                    val v = volumeText.toFloatOrNull()
                                    if (r == null || r < 50 || e == null || e < 1 || v == null || v < 100f) {
                                        VivoHapticFeedback.performWarning(context)
                                        validationError = "Enter valid targets (min 50 reps, 1 exercise, 100 $unitLabel)."
                                    } else {
                                        VivoHapticFeedback.performSuccess(context)
                                        onSaveCustomGoals(r, e, v)
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("save_custom_goals_button")
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
private fun RingMetricLegendRow(
    color: Color,
    secondaryColor: Color,
    label: String,
    value: String,
    subLabel: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.14f else 0f,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "legendBgAlpha"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = bgAlpha))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(color, secondaryColor)))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subLabel,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Interactive Apple Health style bar chart for 7-day volume, sets, or attendance
 * with animated bar growth and multi-stop gradient bars.
 */
@Composable
fun AppleHealthBarChart(
    points: List<DailyChartPoint>,
    barColor: Color,
    unitSuffix: String,
    emptyMessage: String = "No activity recorded for this range yet.",
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val displayPoints = remember(points) {
        if (points.size <= 14) {
            points
        } else {
            val chunkSize = ((points.size + 11) / 12).coerceAtLeast(1)
            points.chunked(chunkSize).map { chunk ->
                val last = chunk.last()
                DailyChartPoint(
                    dateKey = last.dateKey,
                    shortDayLabel = last.shortDayLabel,
                    fullDateLabel = if (chunk.size > 1) "${chunk.first().fullDateLabel} – ${last.fullDateLabel}" else last.fullDateLabel,
                    value = chunk.sumOf { it.value.toDouble() }.toFloat()
                )
            }
        }
    }

    var selectedIndex by remember(displayPoints) {
        val lastNonZero = displayPoints.indexOfLast { it.value > 0f }
        mutableIntStateOf(if (lastNonZero >= 0) lastNonZero else displayPoints.lastIndex)
    }
    val safeIdx = selectedIndex.coerceIn(0, displayPoints.lastIndex)
    val selectedPoint = displayPoints[safeIdx]
    val maxVal = (displayPoints.maxOfOrNull { it.value } ?: 1f).coerceAtLeast(1f)
    val allZero = displayPoints.all { it.value <= 0f }

    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val headerSurface = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val nestedBorder = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            color = headerSurface,
            shape = RoundedCornerShape(AppleRadius.nestedCard),
            shadowElevation = 0.dp,
            border = BorderStroke(1.dp, nestedBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedPoint.shortDayLabel} • ${selectedPoint.fullDateLabel}",
                    style = AppleTypeScale.Footnote,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val formattedVal = if (selectedPoint.value % 1f == 0f) {
                    selectedPoint.value.roundToInt().toString()
                } else {
                    String.format(Locale.US, "%.1f", selectedPoint.value)
                }
                Text(
                    text = "$formattedVal $unitSuffix".trim(),
                    style = AppleTypeScale.SmallMetric,
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .height(124.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            val emptyTrackColor = if (isDark) AppleSurfaceTertiaryDark else AppleSurfaceTertiaryLight
            displayPoints.forEachIndexed { idx, pt ->
                val isSelected = idx == safeIdx
                val barFraction = if (pt.value <= 0f) 0.06f else (pt.value / maxVal).coerceIn(0.14f, 1f)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedIndex = idx }
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.58f)
                                .fillMaxSize(barFraction)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                                .background(
                                    color = when {
                                        pt.value <= 0f -> emptyTrackColor
                                        isSelected -> barColor
                                        else -> barColor.copy(alpha = 0.62f)
                                    }
                                )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = pt.shortDayLabel,
                        style = AppleTypeScale.Caption2,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }

        if (allZero) {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

enum class ProgressionChartMetric(val label: String, val shortLabel: String) {
    TOP_WEIGHT("Top Weight", "Weight"),
    BEST_REPS("Best Reps", "Reps"),
    SESSION_VOLUME("Session Volume", "Volume"),
    EST_1RM("Est. 1RM", "Est. 1RM")
}

/**
 * Interactive Exercise Progression Line + Area chart for Progressive Overload tracking.
 */
@Composable
fun ExerciseProgressionChart(
    sessions: List<ExerciseSessionSummary>,
    metric: ProgressionChartMetric,
    useLbs: Boolean,
    unitLabel: String,
    recommendedWeightKg: Float = 40f,
    recommendedReps: Int = 8,
    recommendedVolumeKg: Float = 960f,
    recommended1RmKg: Float = 50f,
    accentColor: Color = AppleOverloadOrange,
    modifier: Modifier = Modifier
) {
    val factor = if (useLbs) 2.20462f else 1f
    val suffix = when (metric) {
        ProgressionChartMetric.BEST_REPS -> "reps"
        else -> unitLabel
    }

    val displaySessions = remember(sessions) {
        if (sessions.isNotEmpty()) {
            sessions
        } else {
            listOf(
                ExerciseSessionSummary(
                    sessionId = -1L,
                    sessionTitle = "No Sessions Logged",
                    dateKey = "Week",
                    dateLabel = "Week",
                    timestampMillis = 1L,
                    topWeightKg = 0f,
                    maxRepsAtTopWeight = 0,
                    bestSetReps = 0,
                    totalReps = 0,
                    completedSetCount = 0,
                    sessionVolumeKg = 0f,
                    bestEstimatedOneRepMaxKg = 0f,
                    sets = emptyList()
                ),
                ExerciseSessionSummary(
                    sessionId = -2L,
                    sessionTitle = "No Sessions Logged",
                    dateKey = "Month",
                    dateLabel = "Month",
                    timestampMillis = 2L,
                    topWeightKg = 0f,
                    maxRepsAtTopWeight = 0,
                    bestSetReps = 0,
                    totalReps = 0,
                    completedSetCount = 0,
                    sessionVolumeKg = 0f,
                    bestEstimatedOneRepMaxKg = 0f,
                    sets = emptyList()
                ),
                ExerciseSessionSummary(
                    sessionId = -3L,
                    sessionTitle = "No Sessions Logged",
                    dateKey = "Year",
                    dateLabel = "Year",
                    timestampMillis = 3L,
                    topWeightKg = 0f,
                    maxRepsAtTopWeight = 0,
                    bestSetReps = 0,
                    totalReps = 0,
                    completedSetCount = 0,
                    sessionVolumeKg = 0f,
                    bestEstimatedOneRepMaxKg = 0f,
                    sets = emptyList()
                )
            )
        }
    }

    val isEmptyHistory = sessions.isEmpty()

    val values = displaySessions.map { s ->
        when (metric) {
            ProgressionChartMetric.TOP_WEIGHT -> s.topWeightKg * factor
            ProgressionChartMetric.BEST_REPS -> s.bestSetReps.toFloat()
            ProgressionChartMetric.SESSION_VOLUME -> s.sessionVolumeKg * factor
            ProgressionChartMetric.EST_1RM -> s.bestEstimatedOneRepMaxKg * factor
        }
    }

    var selectedIdx by remember(displaySessions, metric) { mutableIntStateOf(displaySessions.lastIndex) }
    val safeIdx = selectedIdx.coerceIn(0, displaySessions.lastIndex)
    val selectedSession = displaySessions[safeIdx]
    val selectedValue = values[safeIdx]

    val minVal = (values.minOrNull() ?: 0f)
    val maxVal = (values.maxOrNull() ?: 1f)
    val valueSpan = (maxVal - minVal).coerceAtLeast(1f)
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val gridColor = if (isDark) AppleSeparatorDark else AppleSeparatorLight
    val headerSurface = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val nestedBorder = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight

    val areaPath = remember { Path() }
    val linePath = remember { Path() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            color = headerSurface,
            shape = RoundedCornerShape(AppleRadius.nestedCard),
            shadowElevation = 0.dp,
            border = BorderStroke(1.dp, nestedBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isEmptyHistory) {
                            "0 Sessions Logged"
                        } else {
                            "${selectedSession.dateLabel} • ${selectedSession.sessionTitle}"
                        },
                        style = AppleTypeScale.Footnote,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isEmptyHistory) {
                            "Log workouts to track progression across weeks, months & years"
                        } else {
                            "${selectedSession.completedSetCount} sets • ${selectedSession.totalReps} total reps"
                        },
                        style = AppleTypeScale.Caption1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${selectedValue.roundToInt()} $suffix",
                    style = AppleTypeScale.SmallMetric,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            val w = size.width
            val h = size.height

            for (i in 0..2) {
                val y = h * (i / 2f)
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 0.6.dp.toPx()
                )
            }

            val stepX = if (values.size > 1) w / (values.size - 1).toFloat() else 0f
            val pointsOffsets = values.mapIndexed { idx, v ->
                val x = if (values.size > 1) idx * stepX else w / 2f
                val normalized = if (values.size == 1 || maxVal == minVal) {
                    0.5f
                } else {
                    (v - minVal) / valueSpan
                }
                val y = h - (normalized * (h * 0.76f) + h * 0.12f)
                Offset(x, y)
            }

            if (pointsOffsets.size > 1) {
                areaPath.rewind()
                areaPath.apply {
                    moveTo(pointsOffsets.first().x, h)
                    pointsOffsets.forEach { pt -> lineTo(pt.x, pt.y) }
                    lineTo(pointsOffsets.last().x, h)
                    close()
                }
                drawPath(
                    path = areaPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(accentColor.copy(alpha = 0.26f), Color.Transparent)
                    )
                )

                linePath.rewind()
                linePath.apply {
                    moveTo(pointsOffsets.first().x, pointsOffsets.first().y)
                    for (i in 1 until pointsOffsets.size) {
                        lineTo(pointsOffsets[i].x, pointsOffsets[i].y)
                    }
                }
                drawPath(
                    path = linePath,
                    color = accentColor,
                    style = Stroke(width = 3.2.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            pointsOffsets.forEachIndexed { idx, offset ->
                val isSelected = idx == safeIdx
                drawCircle(
                    color = if (isSelected) accentColor else accentColor.copy(alpha = 0.78f),
                    radius = if (isSelected) 6.5.dp.toPx() else 4.5.dp.toPx(),
                    center = offset
                )
                if (isSelected) {
                    drawCircle(
                        color = Color.White,
                        radius = 2.8.dp.toPx(),
                        center = offset
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            displaySessions.takeLast(5).forEach { sess ->
                val actualIdx = displaySessions.indexOf(sess)
                val isSelected = actualIdx == safeIdx
                Text(
                    text = sess.dateLabel,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) accentColor.copy(alpha = 0.16f) else Color.Transparent
                        )
                        .clickable { selectedIdx = actualIdx }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Measurable Strength-to-Bodyweight & Overload Standard Gauge Bar.
 * Shows where an exercise's current or target lift sits relative to bodyweight standards.
 */
@Composable
fun StrengthStandardGaugeBar(
    exerciseName: String,
    currentOrTargetWeightKg: Float,
    userBodyweightKg: Float,
    unitLabel: String,
    useLbs: Boolean,
    modifier: Modifier = Modifier
) {
    val safeBw = userBodyweightKg.coerceAtLeast(45f)
    val hasWeight = currentOrTargetWeightKg > 0f
    val ratio = if (hasWeight) (currentOrTargetWeightKg / safeBw).coerceIn(0f, 2.5f) else 0f
    val progressTarget = if (hasWeight) (ratio / 2.0f).coerceIn(0.05f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "strengthGauge"
    )

    val tierLabel = when {
        !hasWeight -> "0 $unitLabel Logged"
        ratio >= 1.5f -> "Advanced / Elite"
        ratio >= 1.0f -> "Intermediate+"
        ratio >= 0.65f -> "Developing"
        else -> "Foundation"
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Strength-to-Bodyweight Index (${String.format(Locale.US, "%.2f", ratio)}× BW)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = tierLabel,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AppleOverloadOrange
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(AppleStandBlue, AppleExerciseGreen, AppleOverloadOrange, AppleMoveRed)
                        )
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0.5× Novice", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("1.0× Interm.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("1.5× Advanced", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("2.0× Elite", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Live Set-by-Set Volume & Load Mini Graph shown inside the Active Workout Logger.
 */
@Composable
fun LiveWorkoutSetVolumeGraph(
    sets: List<DraftSetEntry>,
    useLbs: Boolean,
    unitLabel: String,
    modifier: Modifier = Modifier
) {
    if (sets.isEmpty()) return
    val factor = if (useLbs) 2.20462f else 1f
    val volumes = sets.map { s ->
        if (s.status == "COMPLETED") (s.weightKg * factor * s.reps).coerceAtLeast(10f) else 5f
    }
    val maxVol = (volumes.maxOrNull() ?: 100f).coerceAtLeast(10f)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Volume (${sets.size} sets)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = AppleExerciseGreen.copy(alpha = 0.12f)
            ) {
                Text(
                    text = "Peak: ${volumes.maxOrNull()?.roundToInt() ?: 0} $unitLabel",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppleExerciseGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    softWrap = false
                )
            }
        }

        val displayedSets = sets.take(14)
        val barSpacing = if (displayedSets.size > 8) 4.dp else 6.dp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(barSpacing),
            verticalAlignment = Alignment.Bottom
        ) {
            displayedSets.forEachIndexed { idx, set ->
                val v = volumes[idx]
                val frac by animateFloatAsState(
                    targetValue = (v / maxVol).coerceIn(0.15f, 1f),
                    animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy),
                    label = "liveSetBar_$idx"
                )
                val barColor = when (set.status) {
                    "COMPLETED" -> AppleExerciseGreen
                    "WARMUP" -> AppleOverloadOrange
                    else -> MaterialTheme.colorScheme.outline
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.80f)
                                .fillMaxSize(frac)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 2.dp, bottomEnd = 2.dp))
                                .background(barColor)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (displayedSets.size > 8) "${idx + 1}" else "S${idx + 1}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        softWrap = false
                    )
                }
            }
        }
    }
}

/**
 * Multi-Color Muscle Group Volume & Set Distribution Graph.
 */
@Composable
fun MuscleVolumeDistributionGraph(
    muscleBreakdown: List<MuscleGroupStat>,
    formatVolume: (Float) -> String,
    modifier: Modifier = Modifier
) {
    val defaultMuscles = if (muscleBreakdown.isNotEmpty()) {
        muscleBreakdown
    } else {
        listOf(
            MuscleGroupStat("Chest", 0, 0f, 0f),
            MuscleGroupStat("Back", 0, 0f, 0f),
            MuscleGroupStat("Legs", 0, 0f, 0f),
            MuscleGroupStat("Shoulders", 0, 0f, 0f),
            MuscleGroupStat("Arms", 0, 0f, 0f)
        )
    }

    val palette = listOf(
        AppleMuscleGroups,
        AppleStrengthProgression,
        AppleMove,
        AppleExercise,
        AppleStand,
        AppleMobility
    )
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val trackBg = if (isDark) AppleSurfaceTertiaryDark else AppleSurfaceTertiaryLight
    val nestedBg = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
    val nestedBorder = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        defaultMuscles.take(6).forEachIndexed { idx, mg ->
            val color = palette[idx % palette.size]
            val animProgress = if (mg.completedSets > 0) {
                (mg.percentageOfSets / 100f).coerceIn(0.06f, 1f)
            } else {
                0f
            }

            Surface(
                shape = RoundedCornerShape(AppleRadius.nestedCard),
                color = nestedBg,
                shadowElevation = 0.dp,
                border = BorderStroke(1.dp, nestedBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Text(
                                text = mg.muscleGroup,
                                style = AppleTypeScale.Footnote,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${mg.completedSets} sets (${mg.percentageOfSets.roundToInt()}%) • ${formatVolume(mg.totalVolumeKg)}",
                            style = AppleTypeScale.Caption1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(trackBg)
                    ) {
                        if (animProgress > 0f) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animProgress)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive Monthly Gym Attendance Calendar Grid.
 */
@Composable
fun AttendanceCalendarGrid(
    cells: List<CalendarDayCell>,
    selectedDateKey: String?,
    onSelectDate: (CalendarDayCell) -> Unit,
    modifier: Modifier = Modifier
) {
    val dayHeaders = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            dayHeaders.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
        val restDayBg = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight
        val restDayBorder = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
        val restDayText = if (isDark) AppleTextPrimaryDark else AppleTextPrimaryLight

        val rows = cells.chunked(7)
        rows.forEach { weekRow ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (col in 0 until 7) {
                    val cell = weekRow.getOrNull(col)
                    if (cell == null || !cell.isCurrentMonth) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    } else {
                        val isSelected = cell.dateKey == selectedDateKey
                        val isAttended = cell.status == CalendarAttendanceStatus.ATTENDED
                        val bgColor = if (isAttended) {
                            AppleMove
                        } else {
                            restDayBg
                        }
                        val textColor = if (isAttended) {
                            AppleTextInverseLight
                        } else if (cell.isToday) {
                            AppleSystemBlue
                        } else {
                            restDayText
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(bgColor)
                                .then(
                                    if (isSelected) {
                                        Modifier.border(2.dp, AppleSystemBlue, CircleShape)
                                    } else if (cell.isToday && !isAttended) {
                                        Modifier.border(1.5.dp, AppleSystemBlue, CircleShape)
                                    } else if (!isAttended) {
                                        Modifier.border(1.dp, restDayBorder, CircleShape)
                                    } else {
                                        Modifier
                                    }
                                )
                                .clickable { onSelectDate(cell) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = cell.dayOfMonth.toString(),
                                    style = AppleTypeScale.Footnote,
                                    fontWeight = if (isAttended || cell.isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = textColor
                                )
                                if (cell.visitCount > 1) {
                                    Text(
                                        text = "×${cell.visitCount}",
                                        style = AppleTypeScale.Caption2,
                                        color = textColor.copy(alpha = 0.90f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CalendarLegendItem(color = AppleMove, label = "Attended")
            CalendarLegendItem(color = AppleSystemBlue, label = "Today", outlined = true)
        }
    }
}

@Composable
private fun CalendarLegendItem(
    color: Color,
    label: String,
    outlined: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(if (outlined) color.copy(alpha = 0.15f) else color)
                .then(
                    if (outlined) Modifier.border(1.dp, color, CircleShape) else Modifier
                )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
