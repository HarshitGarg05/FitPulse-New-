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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.local.GymAttendanceEntity
import com.example.data.repository.AnalyticsAndInsightsEngine
import com.example.data.repository.AnalyticsDateRange
import com.example.ui.components.AppleDatePickerField
import com.example.ui.components.AppleDropdownField
import com.example.ui.components.AppleHealthBarChart
import com.example.ui.components.AppleLargePageHeader
import com.example.ui.components.AppleLiquidGlassTextField
import com.example.ui.components.AppleSectionHeader
import com.example.ui.components.AppleSegmentedControl
import com.example.ui.components.AttendanceCalendarGrid
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPrimaryButton
import com.example.ui.components.LiquidGlassSecondaryButton
import androidx.compose.ui.graphics.luminance
import com.example.ui.theme.AppleCalories
import com.example.ui.theme.AppleCardBorderDark
import com.example.ui.theme.AppleCardBorderLight
import com.example.ui.theme.AppleExercise
import com.example.ui.theme.AppleExerciseGreen
import com.example.ui.theme.AppleMove
import com.example.ui.theme.AppleMoveRed
import com.example.ui.theme.AppleNestedCardBorderDark
import com.example.ui.theme.AppleNestedCardBorderLight
import com.example.ui.theme.AppleOverloadOrange
import com.example.ui.theme.AppleRadius
import com.example.ui.theme.AppleStand
import com.example.ui.theme.AppleStandBlue
import com.example.ui.theme.AppleStateError
import com.example.ui.theme.AppleStateSuccess
import com.example.ui.theme.AppleSurfaceSecondaryDark
import com.example.ui.theme.AppleSurfaceSecondaryLight
import com.example.ui.theme.AppleSystemBlue
import com.example.ui.theme.AppleTypeScale
import com.example.ui.viewmodel.FitPulseUiState
import com.example.ui.viewmodel.FitnessViewModel
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AttendanceScreen(
    state: FitPulseUiState,
    viewModel: FitnessViewModel,
    modifier: Modifier = Modifier
) {
    val overview = state.attendanceOverview
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    var selectedDateKey by remember { mutableStateOf<String?>(null) }
    var showLogModalForDateKey by remember { mutableStateOf<String?>(null) }
    var editingRecord by remember { mutableStateOf<GymAttendanceEntity?>(null) }
    var editingNoteText by remember { mutableStateOf("") }
    var editingDurationText by remember { mutableStateOf("60") }
    var showGoalEditor by remember { mutableStateOf(false) }

    val recentDateOptions = remember {
        (0..14).map { daysAgo ->
            val key = AnalyticsAndInsightsEngine.dateKeyForDaysAgo(daysAgo)
            val label = AnalyticsAndInsightsEngine.formatDateKeyReadable(key)
            key to "$label ($key)"
        }
    }

    val isDarkRoot = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val rootCanvasBg = if (isDarkRoot) androidx.compose.ui.graphics.Color(0xFF000000) else androidx.compose.ui.graphics.Color(0xFFF2F2F7)

    // LEVEL 1: Main screen background (#F2F2F7 in Light Mode)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(rootCanvasBg)
            .testTag("attendance_screen"),
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
            item(key = "attendance_header", contentType = "header") {
                AppleLargePageHeader(
                    overlineText = "GYM CHECK-INS & STREAKS",
                    title = "Attendance",
                    accentColor = if (overview.attendedToday) AppleStateSuccess else AppleMove,
                    actionLabel = if (overview.attendedToday) "Log Visit" else "Check In",
                    actionIcon = Icons.Default.CheckCircle,
                    actionTestTag = "attendance_primary_checkin_button",
                    onActionClick = {
                        editingRecord = overview.todayRecords.firstOrNull()
                        editingNoteText = editingRecord?.note ?: "Gym Training Session"
                        editingDurationText = (editingRecord?.durationMinutes ?: 60).toString()
                        showLogModalForDateKey = overview.todayDateKey
                    },
                    trailingBadge = {
                        Surface(
                            shape = RoundedCornerShape(AppleRadius.pill),
                            color = AppleCalories.copy(alpha = 0.14f)
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
                                        .background(AppleCalories)
                                )
                                Text(
                                    text = if (overview.currentStreakDays > 0) "${overview.currentStreakDays}d Streak" else "No Streak",
                                    style = AppleTypeScale.Caption1,
                                    fontWeight = FontWeight.SemiBold,
                                    color = AppleCalories
                                )
                            }
                        }
                    }
                )
            }

            // 1. Weekly Goal Progress & Equal-Height Streak Metrics
            item(key = "attendance_weekly_goal", contentType = "weekly_goal_card") {
                LiquidGlassCard(accentTint = AppleMoveRed) {
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
                                    text = "WEEKLY ATTENDANCE GOAL",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AppleMoveRed
                                )
                                Text(
                                    text = "${overview.weeklyVisitDays} of ${overview.weeklyGoalDays} gym days this week",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            LiquidGlassSecondaryButton(
                                text = "Edit Goal",
                                onClick = { showGoalEditor = !showGoalEditor },
                                accentColor = AppleStandBlue,
                                testTag = "edit_weekly_goal_button"
                            )
                        }

                        val goalProgress = overview.weeklyGoalProgress.coerceIn(0f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(AppleMoveRed.copy(alpha = 0.15f))
                        ) {
                            if (goalProgress > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(goalProgress)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(AppleMoveRed)
                                )
                            }
                        }

                        if (showGoalEditor) {
                            val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
                            var sliderVal by remember(overview.weeklyGoalDays) {
                                mutableFloatStateOf(overview.weeklyGoalDays.toFloat())
                            }
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
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Target Days Per Week: ${sliderVal.toInt()} days",
                                        style = AppleTypeScale.Subheadline,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Slider(
                                        value = sliderVal,
                                        onValueChange = { sliderVal = it },
                                        onValueChangeFinished = {
                                            val p = state.profile
                                            viewModel.updateProfileSettings(
                                                displayName = p.displayName,
                                                weightDisplay = viewModel.toDisplayWeightValue(p.weightKg),
                                                heightCm = p.heightCm,
                                                fitnessLevel = p.fitnessLevel,
                                                primaryObjective = p.primaryObjective,
                                                weeklyAttendanceGoal = sliderVal.toInt(),
                                                trainingDaysCsv = p.trainingDaysCsv,
                                                allowMultipleVisitsPerDay = p.allowMultipleVisitsPerDay
                                            )
                                        },
                                        valueRange = 1f..7f,
                                        steps = 5,
                                        colors = SliderDefaults.colors(
                                            thumbColor = AppleMove,
                                            activeTrackColor = AppleMove
                                        )
                                    )
                                }
                            }
                        }

                        // Equal-Height 4-Metric Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EqualHeightMetricMiniBox(
                                label = "Active Streak",
                                value = "${overview.currentStreakDays}d",
                                accent = AppleOverloadOrange,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            EqualHeightMetricMiniBox(
                                label = "Best Streak",
                                value = "${overview.longestStreakDays}d",
                                accent = AppleStandBlue,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            EqualHeightMetricMiniBox(
                                label = "This Month",
                                value = "${overview.monthlyVisitDays}d",
                                accent = AppleExerciseGreen,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                            EqualHeightMetricMiniBox(
                                label = "Total Visits",
                                value = "${overview.totalAllTimeVisits}",
                                accent = AppleMoveRed,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                            )
                        }
                    }
                }
            }

            // 2. 7-Day Attendance Consistency Bar Graph
            item(key = "attendance_bar_chart", contentType = "bar_chart_card") {
                LiquidGlassCard(accentTint = AppleExerciseGreen) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "7-DAY CHECK-IN CONSISTENCY GRAPH",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = AppleExerciseGreen
                            )
                            Text(
                                text = "Daily Gym Visits This Week",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        AppleHealthBarChart(
                            points = overview.weeklyAttendanceSeries,
                            barColor = AppleMoveRed,
                            unitSuffix = "visits"
                        )
                    }
                }
            }

            // 3. Interactive Monthly Attendance Calendar
            item(key = "attendance_calendar", contentType = "calendar_card") {
                LiquidGlassCard(accentTint = AppleStandBlue) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.navigateCalendarMonth(-1) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous Month"
                                )
                            }
                            Text(
                                text = overview.calendarMonthTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { viewModel.navigateCalendarMonth(1) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next Month"
                                )
                            }
                        }

                        AttendanceCalendarGrid(
                            cells = overview.calendarDays,
                            selectedDateKey = selectedDateKey,
                            onSelectDate = { cell ->
                                selectedDateKey = cell.dateKey
                                if (cell.dateKey <= overview.todayDateKey) {
                                    editingRecord = cell.records.firstOrNull()
                                    editingNoteText = editingRecord?.note ?: "Gym Training Session"
                                    editingDurationText = (editingRecord?.durationMinutes ?: 60).toString()
                                    showLogModalForDateKey = cell.dateKey
                                }
                            }
                        )
                    }
                }
            }

            // 4. Filterable Chronological Attendance History
            item(key = "attendance_history_header", contentType = "section_header") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppleSectionHeader(
                        title = "Attendance History",
                        subtitle = "${state.filteredAttendanceRecords.size} check-ins in selected period",
                        actionLabel = "Log Past Date",
                        actionIcon = Icons.Default.Add,
                        actionColor = AppleStandBlue,
                        onActionClick = {
                            val yesterday = AnalyticsAndInsightsEngine.dateKeyForDaysAgo(1)
                            editingRecord = state.allAttendanceRecords.firstOrNull { it.dateKey == yesterday }
                            editingNoteText = editingRecord?.note ?: ""
                            editingDurationText = (editingRecord?.durationMinutes ?: 60).toString()
                            showLogModalForDateKey = yesterday
                        }
                    )

                    AppleSegmentedControl(
                        items = AnalyticsDateRange.entries,
                        selectedItem = state.attendanceFilterRange,
                        onItemSelected = { viewModel.setAttendanceFilterRange(it) },
                        labelProvider = { it.shortLabel },
                        accentColor = AppleMoveRed
                    )
                }
            }

            if (state.filteredAttendanceRecords.isEmpty()) {
                item(key = "attendance_empty_state", contentType = "empty_state") {
                    LiquidGlassCard(accentTint = AppleStandBlue) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "No gym visits recorded for this filter",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Tap any date on the calendar above or tap 'Check In' to record your gym attendance.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    items = state.filteredAttendanceRecords,
                    key = { it.id },
                    contentType = { "attendance_record_row" }
                ) { record ->
                    AttendanceRecordRow(
                        record = record,
                        onEdit = {
                            editingRecord = record
                            editingNoteText = record.note
                            editingDurationText = record.durationMinutes.toString()
                            showLogModalForDateKey = record.dateKey
                        },
                        onDelete = { viewModel.deleteGymAttendance(record.id) }
                    )
                }
            }
        }
    }

    val modalDateKey = showLogModalForDateKey
    if (modalDateKey != null) {
        AlertDialog(
            onDismissRequest = {
                showLogModalForDateKey = null
                editingRecord = null
            },
            shape = RoundedCornerShape(AppleRadius.bottomSheet),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            title = {
                Text(
                    text = if (editingRecord != null) "Edit Gym Check-In" else "Log Gym Attendance",
                    style = AppleTypeScale.Title3,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AppleDatePickerField(
                        label = "Select Check-In Date",
                        selectedDateKey = modalDateKey,
                        onDateKeySelected = { matchedKey ->
                            showLogModalForDateKey = matchedKey
                            val existing = state.allAttendanceRecords.firstOrNull { it.dateKey == matchedKey }
                            editingRecord = existing
                            editingNoteText = existing?.note ?: ""
                            editingDurationText = (existing?.durationMinutes ?: 60).toString()
                        },
                        accentColor = AppleSystemBlue,
                        testTag = "attendance_date_picker_field"
                    )

                    AppleLiquidGlassTextField(
                        value = editingNoteText,
                        onValueChange = { editingNoteText = it },
                        label = "Session focus or note",
                        placeholder = "e.g., Upper Body Strength, Leg Day",
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        fieldModifier = Modifier.testTag("attendance_note_input")
                    )

                    AppleLiquidGlassTextField(
                        value = editingDurationText,
                        onValueChange = { editingDurationText = it },
                        label = "Session Duration (minutes)",
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                LiquidGlassPrimaryButton(
                    text = if (editingRecord != null) "Save Changes" else "Confirm Check-In",
                    onClick = {
                        val dur = editingDurationText.toIntOrNull() ?: 60
                        val existing = editingRecord
                        if (existing != null) {
                            viewModel.updateGymAttendance(existing, modalDateKey, editingNoteText, dur)
                        } else {
                            viewModel.recordGymAttendance(modalDateKey, editingNoteText, dur)
                        }
                        showLogModalForDateKey = null
                        editingRecord = null
                    },
                    accentColor = AppleSystemBlue,
                    testTag = "confirm_attendance_modal_button"
                )
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val existing = editingRecord
                    if (existing != null) {
                        TextButton(
                            onClick = {
                                viewModel.deleteGymAttendance(existing.id)
                                showLogModalForDateKey = null
                                editingRecord = null
                            }
                        ) {
                            Text("Delete", color = AppleStateError, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    TextButton(
                        onClick = {
                            showLogModalForDateKey = null
                            editingRecord = null
                        }
                    ) {
                        Text("Cancel", color = AppleSystemBlue)
                    }
                }
            }
        )
    }
}

@Composable
private fun AttendanceRecordRow(
    record: GymAttendanceEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatted = remember(record.dateKey) {
        AnalyticsAndInsightsEngine.formatDateKeyReadable(record.dateKey)
    }

    LiquidGlassCard(accentTint = AppleStateSuccess) {
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
                        .background(AppleStateSuccess.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AppleStateSuccess
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = dateFormatted,
                        style = AppleTypeScale.Headline,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${record.note.ifBlank { "Gym attendance verified" }} • ${record.durationMinutes} min",
                        style = AppleTypeScale.Footnote,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Check-In",
                        tint = AppleSystemBlue
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Check-In",
                        tint = AppleStateError
                    )
                }
            }
        }
    }
}
