package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.DynamicIslandNavItem
import com.example.ui.components.LiquidGlassAmbientBackground
import com.example.ui.components.LiquidGlassDynamicIslandBar
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GoalsAndRecoveryScreen
import com.example.ui.screens.ProfileAndPrivacyScreen
import com.example.ui.screens.WorkoutLoggerScreen
import com.example.ui.theme.AppleMoveRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.FitnessViewModel
import com.example.util.OriginIslandLiveManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.decorView.setBackgroundColor(android.graphics.Color.parseColor("#F2F2F7"))
        configureHighRefreshRateDisplay()
        OriginIslandLiveManager.ensureChannelCreated(this)
        setContent {
            val fitnessViewModel: FitnessViewModel = viewModel()
            val themeMode by fitnessViewModel.themeModeFlow.collectAsStateWithLifecycle()

            MyApplicationTheme(themeMode = themeMode) {
                FitPulseApp(
                    viewModel = fitnessViewModel
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        OriginIslandLiveManager.isAppMinimized = false
        OriginIslandLiveManager.cancelOriginIsland(this)
    }

    override fun onResume() {
        super.onResume()
        configureHighRefreshRateDisplay()
    }

    override fun onStop() {
        super.onStop()
        OriginIslandLiveManager.isAppMinimized = true
    }

    /**
     * Optimizes frame pacing across 60Hz, 90Hz, 120Hz, and 144Hz ProMotion/Adaptive displays
     * by selecting the highest refresh rate display mode matching the active physical resolution.
     */
    private fun configureHighRefreshRateDisplay() {
        runCatching {
            val currentDisplay = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display
            } else {
                @Suppress("DEPRECATION")
                windowManager.defaultDisplay
            } ?: return

            val activeMode = currentDisplay.mode
            val bestHighRefreshMode = currentDisplay.supportedModes
                .filter {
                    it.physicalWidth == activeMode.physicalWidth &&
                        it.physicalHeight == activeMode.physicalHeight
                }
                .maxByOrNull { it.refreshRate }

            if (bestHighRefreshMode != null) {
                val layoutParams = window.attributes
                if (layoutParams.preferredDisplayModeId != bestHighRefreshMode.modeId ||
                    layoutParams.preferredRefreshRate != bestHighRefreshMode.refreshRate
                ) {
                    layoutParams.preferredDisplayModeId = bestHighRefreshMode.modeId
                    layoutParams.preferredRefreshRate = bestHighRefreshMode.refreshRate
                    window.attributes = layoutParams
                }
            }
        }
    }
}

@Composable
fun FitPulseApp(
    viewModel: FitnessViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentState by rememberUpdatedState(state)
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        OriginIslandLiveManager.syncOriginIslandState(
            context = context,
            activeWorkout = currentState.activeWorkout,
            unitLabel = currentState.unitLabel,
            useLbs = currentState.useLbs
        )
    }

    DisposableEffect(viewModel) {
        OriginIslandLiveManager.onToggleTimerCallback = { viewModel.toggleWorkoutTimer() }
        OriginIslandLiveManager.onSkipRestCallback = { viewModel.stopRestTimer() }
        OriginIslandLiveManager.onAddRest15sCallback = {
            val next = (currentState.activeWorkout.restTimerRemainingSeconds + 15).coerceAtLeast(15)
            viewModel.startRestTimer(next)
        }
        onDispose { }
    }

    // Track foreground vs minimized state so iQOO/vivo Origin Island only triggers when the app is minimized
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME -> {
                    OriginIslandLiveManager.isAppMinimized = false
                    OriginIslandLiveManager.cancelOriginIsland(context)
                }
                Lifecycle.Event.ON_STOP -> {
                    OriginIslandLiveManager.isAppMinimized = true
                    OriginIslandLiveManager.syncOriginIslandState(
                        context = context,
                        activeWorkout = currentState.activeWorkout,
                        unitLabel = currentState.unitLabel,
                        useLbs = currentState.useLbs
                    )
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Request notification permission when a live workout starts so Vivo/iQOO OriginOS 6 Origin Island works when minimized
    LaunchedEffect(state.activeWorkout.isWorkoutActive) {
        if (state.activeWorkout.isWorkoutActive && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Continuously sync OriginOS 6 Origin Island state ONLY when minimized
    LaunchedEffect(
        state.activeWorkout.isWorkoutActive,
        state.activeWorkout.title,
        state.activeWorkout.activeExerciseName,
        state.activeWorkout.activeExerciseElapsedSeconds,
        state.activeWorkout.elapsedSeconds,
        state.activeWorkout.isTimerRunning,
        state.activeWorkout.isRestTimerRunning,
        state.activeWorkout.restTimerRemainingSeconds,
        state.activeWorkout.restTimerTotalSeconds,
        state.activeWorkout.sets,
        state.unitLabel,
        state.useLbs
    ) {
        if (OriginIslandLiveManager.isAppMinimized) {
            OriginIslandLiveManager.syncOriginIslandState(
                context = context,
                activeWorkout = state.activeWorkout,
                unitLabel = state.unitLabel,
                useLbs = state.useLbs
            )
        }
    }

    BackHandler(enabled = state.currentTab != AppDestination.SUMMARY) {
        viewModel.selectTab(AppDestination.SUMMARY)
    }

    LaunchedEffect(state.statusMessage) {
        val message = state.statusMessage
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearStatusMessage()
        }
    }

    val navItems = remember {
        listOf(
            DynamicIslandNavItem(
                route = AppDestination.SUMMARY,
                label = "Summary",
                selectedIcon = Icons.Filled.Dashboard,
                unselectedIcon = Icons.Outlined.Dashboard,
                accentColor = AppleMoveRed,
                testTag = "nav_summary"
            ),
            DynamicIslandNavItem(
                route = AppDestination.ATTENDANCE,
                label = "Attendance",
                selectedIcon = Icons.Filled.CalendarMonth,
                unselectedIcon = Icons.Outlined.CalendarMonth,
                accentColor = AppleMoveRed,
                testTag = "nav_attendance"
            ),
            DynamicIslandNavItem(
                route = AppDestination.WORKOUTS,
                label = "Workouts",
                selectedIcon = Icons.Filled.FitnessCenter,
                unselectedIcon = Icons.Outlined.FitnessCenter,
                accentColor = AppleMoveRed,
                testTag = "nav_workouts"
            ),
            DynamicIslandNavItem(
                route = AppDestination.EXERCISES,
                label = "Exercises",
                selectedIcon = Icons.AutoMirrored.Filled.TrendingUp,
                unselectedIcon = Icons.AutoMirrored.Outlined.TrendingUp,
                accentColor = AppleMoveRed,
                testTag = "nav_exercises"
            ),
            DynamicIslandNavItem(
                route = AppDestination.SETTINGS,
                label = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
                accentColor = AppleMoveRed,
                testTag = "nav_settings"
            )
        )
    }

    val isDark = state.themeMode == com.example.ui.theme.ThemeMode.DARK
    val rootBgColor = if (isDark) Color(0xFF000000) else Color(0xFFF2F2F7)

    LiquidGlassAmbientBackground {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(rootBgColor),
            containerColor = rootBgColor,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets.safeDrawing.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Top
            ),
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 88.dp)
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(rootBgColor)
                    .padding(innerPadding)
            ) {
                // Content scrolls freely all the way to the bottom underneath the floating navbar
                AnimatedContent(
                    targetState = state.currentTab,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(rootBgColor),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                    },
                    label = "mainTabTransition"
                ) { tab ->
                    when (tab) {
                        AppDestination.SUMMARY -> DashboardScreen(
                            state = state,
                            viewModel = viewModel
                        )
                        AppDestination.ATTENDANCE -> AttendanceScreen(
                            state = state,
                            viewModel = viewModel
                        )
                        AppDestination.WORKOUTS -> WorkoutLoggerScreen(
                            state = state,
                            viewModel = viewModel
                        )
                        AppDestination.EXERCISES -> GoalsAndRecoveryScreen(
                            state = state,
                            viewModel = viewModel
                        )
                        AppDestination.SETTINGS -> ProfileAndPrivacyScreen(
                            state = state,
                            viewModel = viewModel
                        )
                    }
                }

                // Authentic Apple Health Bottom Tab Bar
                LiquidGlassDynamicIslandBar(
                    items = navItems,
                    selectedRoute = state.currentTab,
                    onSelect = { viewModel.selectTab(it) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

