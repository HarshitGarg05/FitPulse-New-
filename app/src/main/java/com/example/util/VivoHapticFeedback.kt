package com.example.util

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Native Android Tactile Haptics & Motion Accessibility Engine for FitPulse.
 *
 * Provides calibrated haptic feedback tiers matching Apple's Taptic Engine philosophy:
 * - Light: Button taps, navigation selection, and minor interactions.
 * - Medium: Completing a set, adding an exercise, or changing important selections.
 * - Success: Completing a workout or reaching a weekly activity target.
 * - Warning: Invalid actions, destructive operations, or validation errors.
 *
 * Respects device system haptic settings, checks hardware capability, and debounces rapid calls
 * so haptics never trigger repeatedly during animations or continuous interactions.
 */
object VivoHapticFeedback {

    @Volatile
    private var lastHapticElapsedMillis: Long = 0L

    private const val MIN_LIGHT_INTERVAL_MS = 35L
    private const val MIN_MEDIUM_INTERVAL_MS = 55L
    private const val MIN_STATUS_INTERVAL_MS = 120L

    private fun getVibrator(context: Context): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                manager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Checks whether system haptic feedback is enabled and the hardware vibrator is supported.
     */
    fun isHapticsEnabled(context: Context): Boolean {
        return try {
            @Suppress("DEPRECATION")
            val systemHapticEnabled = Settings.System.getInt(
                context.contentResolver,
                Settings.System.HAPTIC_FEEDBACK_ENABLED,
                1
            ) != 0
            if (!systemHapticEnabled) return false
            val vibrator = getVibrator(context) ?: return false
            vibrator.hasVibrator()
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Checks whether the user has enabled Reduced Motion / disabled animator duration scale in system settings.
     */
    fun isReducedMotionEnabled(context: Context): Boolean {
        return try {
            val scale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            )
            scale <= 0.05f
        } catch (_: Exception) {
            false
        }
    }

    private fun canTriggerHaptic(context: Context, minIntervalMs: Long): Vibrator? {
        if (!isHapticsEnabled(context)) return null
        val now = SystemClock.elapsedRealtime()
        if (now - lastHapticElapsedMillis < minIntervalMs) return null
        val vibrator = getVibrator(context) ?: return null
        if (!vibrator.hasVibrator()) return null
        lastHapticElapsedMillis = now
        return vibrator
    }

    /**
     * Light feedback: Button taps, navigation selection, and minor interactions.
     */
    fun performLight(context: Context) {
        val vibrator = canTriggerHaptic(context, MIN_LIGHT_INTERVAL_MS) ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_TICK)
            ) {
                vibrator.vibrate(
                    VibrationEffect.startComposition()
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 0.65f)
                        .compose()
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(10, 90))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(10)
            }
        } catch (_: Exception) {}
    }

    /**
     * Medium feedback: Completing a set, adding an exercise, or changing important selections.
     */
    fun performMedium(context: Context) {
        val vibrator = canTriggerHaptic(context, MIN_MEDIUM_INTERVAL_MS) ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_CLICK)
            ) {
                vibrator.vibrate(
                    VibrationEffect.startComposition()
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 0.85f)
                        .compose()
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(14, 150))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(14)
            }
        } catch (_: Exception) {}
    }

    /**
     * Success feedback: Completing a workout or reaching a weekly activity target.
     */
    fun performSuccess(context: Context) {
        val vibrator = canTriggerHaptic(context, MIN_STATUS_INTERVAL_MS) ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator.areAllPrimitivesSupported(
                    VibrationEffect.Composition.PRIMITIVE_TICK,
                    VibrationEffect.Composition.PRIMITIVE_CLICK
                )
            ) {
                vibrator.vibrate(
                    VibrationEffect.startComposition()
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_TICK, 0.6f, 0)
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 0.95f, 40)
                        .compose()
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 12, 35, 18)
                val amplitudes = intArrayOf(0, 110, 0, 200)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(30)
            }
        } catch (_: Exception) {}
    }

    /**
     * Warning feedback: Invalid actions, destructive operations, or validation errors.
     */
    fun performWarning(context: Context) {
        val vibrator = canTriggerHaptic(context, MIN_STATUS_INTERVAL_MS) ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                vibrator.areAllPrimitivesSupported(VibrationEffect.Composition.PRIMITIVE_CLICK)
            ) {
                vibrator.vibrate(
                    VibrationEffect.startComposition()
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 0.9f, 0)
                        .addPrimitive(VibrationEffect.Composition.PRIMITIVE_CLICK, 0.65f, 55)
                        .compose()
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 16, 45, 16)
                val amplitudes = intArrayOf(0, 180, 0, 130)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(35)
            }
        } catch (_: Exception) {}
    }

    /**
     * Backwards-compatible aliases mapped to calibrated semantic haptic levels.
     */
    fun performClick(context: Context) {
        performLight(context)
    }

    fun performTick(context: Context) {
        performLight(context)
    }

    fun performLaunch(context: Context) {
        performMedium(context)
    }

    fun performAccordionToggle(context: Context, isExpanding: Boolean) {
        performLight(context)
    }
}

@Composable
fun rememberReducedMotionEnabled(): Boolean {
    val context = LocalContext.current
    return remember(context) {
        VivoHapticFeedback.isReducedMotionEnabled(context)
    }
}

