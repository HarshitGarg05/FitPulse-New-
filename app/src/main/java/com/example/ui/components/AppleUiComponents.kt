package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import com.example.util.VivoHapticFeedback
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExerciseEntity
import com.example.ui.theme.AppleAccessoryCircleDark
import com.example.ui.theme.AppleAccessoryCircleLight
import com.example.ui.theme.AppleExerciseGreen
import com.example.ui.theme.AppleHealthDarkBg
import com.example.ui.theme.AppleHealthDarkContainer
import com.example.ui.theme.AppleHealthDarkOutline
import com.example.ui.theme.AppleHealthDarkSurface
import com.example.ui.theme.AppleHealthLightBg
import com.example.ui.theme.AppleHealthLightContainer
import com.example.ui.theme.AppleHealthLightOutline
import com.example.ui.theme.AppleHealthLightSurface
import com.example.ui.theme.AppleMindPurple
import com.example.ui.theme.AppleMoveRed
import com.example.ui.theme.AppleOverloadOrange
import com.example.ui.theme.AppleStandBlue
import com.example.ui.theme.AppleRadius
import com.example.ui.theme.AppleSpacing
import com.example.ui.theme.AppleTypeScale
import com.example.ui.theme.AppleSurfaceLight
import com.example.ui.theme.AppleSurfaceDark
import com.example.ui.theme.AppleSurfaceSecondaryLight
import com.example.ui.theme.AppleSurfaceSecondaryDark
import com.example.ui.theme.AppleSurfaceTertiaryLight
import com.example.ui.theme.AppleSeparatorLight
import com.example.ui.theme.AppleSeparatorDark
import com.example.ui.theme.AppleOutlineLight
import com.example.ui.theme.AppleTextPlaceholderLight
import java.util.Locale

/**
 * Compatible CompositionLocal so any callers referencing `LocalLiquidGlassPhase` continue to compile.
 */
val LocalLiquidGlassPhase = compositionLocalOf<State<Float>> { mutableFloatStateOf(0f) }

/**
 * Exact Apple Health / Fitness clean solid background.
 * Completely removes any liquid glass moving caustics or auroras.
 * Dark Mode: pitch black OLED (`#000000`).
 * Light Mode: iOS system grouped background (`#F2F2F7`).
 */
@Composable
fun LiquidGlassAmbientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val baseBgColor = if (isDark) AppleHealthDarkBg else AppleHealthLightBg

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(baseBgColor)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            content()
        }
    }
}

/**
 * Exact Apple Health & Fitness Modular Card.
 * - Solid `#1C1C1E` dark surface (Apple Secondary System Grouped Background) or pure white in light mode.
 * - 16dp rounded corners (`RoundedCornerShape(16.dp)`).
 * - Clean, flat, subtle border (zero liquid glass or caustic reflections).
 * - Retains smooth responsive press animation.
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    accentTint: Color = AppleStandBlue,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "appleCardScale"
    )

    val shape = RoundedCornerShape(AppleRadius.standardCard)
    val cardBg = if (isDark) AppleSurfaceDark else AppleSurfaceLight
    val cardBorder = if (isDark) {
        BorderStroke(0.5.dp, AppleSeparatorDark.copy(alpha = 0.55f))
    } else {
        BorderStroke(0.5.dp, AppleSeparatorLight.copy(alpha = 0.60f))
    }

    CompositionLocalProvider(LocalContentColor provides onSurfaceColor) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = if (isDark) 0.dp else 1.5.dp,
                    shape = shape,
                    spotColor = Color.Black.copy(alpha = 0.04f),
                    ambientColor = Color.Black.copy(alpha = 0.02f)
                )
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick
                        )
                    } else {
                        Modifier
                    }
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ),
            shape = shape,
            color = cardBg,
            contentColor = onSurfaceColor,
            border = cardBorder
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

/**
 * Circular accessory button on the top-right of Apple Health cards (e.g., chevron `>` or action icon)
 */
@Composable
fun AppleCircularAccessory(
    onClick: (() -> Unit)? = null,
    icon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val circleColor = if (isDark) AppleAccessoryCircleDark else AppleAccessoryCircleLight
    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(circleColor)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(16.dp)
        )
    }
}

/**
 * Exact Apple Health Large Page Header used at the top of pages.
 * Clean, bold typography with vibrant accent color for overline.
 */
@Composable
fun AppleLargePageHeader(
    overlineText: String,
    title: String,
    accentColor: Color = AppleMoveRed,
    actionLabel: String? = null,
    actionIcon: ImageVector? = null,
    actionTestTag: String? = null,
    onActionClick: (() -> Unit)? = null,
    trailingBadge: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = overlineText.uppercase(Locale.US),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (trailingBadge != null) {
            trailingBadge()
        } else if (actionLabel != null && onActionClick != null) {
            LiquidGlassPrimaryButton(
                text = actionLabel,
                onClick = onActionClick,
                icon = actionIcon,
                accentColor = accentColor,
                testTag = actionTestTag
            )
        }
    }
}

data class DynamicIslandNavItem<T>(
    val route: T,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val accentColor: Color,
    val testTag: String
)

/**
 * Floating iOS-Style Pill Navigation Bar.
 * - Floating capsule container inset horizontally by 18dp.
 * - Fully rounded capsule shape (999.dp).
 * - Frosted glass translucent background with subtle specular sheen and layered highlights.
 * - Thin semi-transparent white/light border and soft diffused drop shadow.
 * - Positioned safely above system gesture insets without touching screen edges.
 * - Active item highlighted by a subtle translucent pink rounded pill (#FF2D55) with pink icon & label.
 * - Inactive items in Apple system gray (#8E8E93).
 * - Equal-width navigation destinations with centered icons and accessible touch targets.
 */
@Composable
fun <T> LiquidGlassDynamicIslandBar(
    items: List<DynamicIslandNavItem<T>>,
    selectedRoute: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val context = LocalContext.current

    // Active Move accent #FF2D55
    val activeAccentColor = Color(0xFFFF2D55)
    val inactiveNavColor = Color(0xFF8E8E93)

    // Capsule background colors
    val pillBgBase = if (isDark) Color(0xD91C1C1E) else Color(0xEBFFFFFF)
    val borderColor = if (isDark) Color(0x33FFFFFF) else Color(0xB8FFFFFF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 18.dp, end = 18.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(999.dp),
                    spotColor = if (isDark) Color.Black.copy(alpha = 0.55f) else Color.Black.copy(alpha = 0.16f),
                    ambientColor = if (isDark) Color.Black.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.10f)
                ),
            shape = RoundedCornerShape(999.dp),
            color = pillBgBase,
            border = BorderStroke(1.dp, borderColor)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Layered frosted glass specular gradient highlight
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = if (isDark) {
                                    listOf(
                                        Color(0x28FFFFFF),
                                        Color(0x08FFFFFF),
                                        Color(0x18000000)
                                    )
                                } else {
                                    listOf(
                                        Color(0x99FFFFFF),
                                        Color(0x40FFFFFF),
                                        Color(0x15E5E5EA)
                                    )
                                }
                            )
                        )
                )

                // Top rim reflection line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = if (isDark) {
                                    listOf(
                                        Color.Transparent,
                                        Color(0x38FFFFFF),
                                        Color.Transparent
                                    )
                                } else {
                                    listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.90f),
                                        Color.Transparent
                                    )
                                }
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { item ->
                        val isSelected = item.route == selectedRoute

                        val pillSelectedBg by animateColorAsState(
                            targetValue = if (isSelected) {
                                activeAccentColor.copy(alpha = if (isDark) 0.20f else 0.14f)
                            } else {
                                Color.Transparent
                            },
                            animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
                            label = "navPillBg"
                        )

                        val itemContentColor by animateColorAsState(
                            targetValue = if (isSelected) activeAccentColor else inactiveNavColor,
                            animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
                            label = "navContentColor"
                        )

                        val itemScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.0f else 0.96f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            ),
                            label = "navItemScale"
                        )

                        val itemInteraction = remember { MutableInteractionSource() }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(999.dp))
                                .background(pillSelectedBg)
                                .clickable(
                                    interactionSource = itemInteraction,
                                    indication = null
                                ) {
                                    if (!isSelected) {
                                        VivoHapticFeedback.performTick(context)
                                    }
                                    onSelect(item.route)
                                }
                                .padding(vertical = 4.dp, horizontal = 2.dp)
                                .testTag(item.testTag),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = itemScale
                                    scaleY = itemScale
                                }
                            ) {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    tint = itemContentColor,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.label,
                                    fontSize = 10.sp,
                                    lineHeight = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = itemContentColor,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Solid Apple iOS Primary Action Button.
 * Crisp, vibrant, solid filled button without gradient or liquid glass effect.
 */
@Composable
fun LiquidGlassPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = AppleMoveRed,
    enabled: Boolean = true,
    testTag: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btnScale"
    )

    val shape = RoundedCornerShape(14.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color.White,
            disabledContainerColor = accentColor.copy(alpha = 0.40f),
            disabledContentColor = Color.White.copy(alpha = 0.65f)
        ),
        modifier = modifier
            .heightIn(min = 44.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Solid Apple iOS Secondary Action Button.
 * Styled in Apple's subtle dark gray container (`#2C2C2E`) or light gray in light mode.
 */
@Composable
fun LiquidGlassSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = AppleStandBlue,
    testTag: String? = null
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "secBtnScale"
    )

    val containerColor = if (isDark) AppleHealthDarkContainer else AppleHealthLightContainer
    val labelAndIconColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface

    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = labelAndIconColor
        ),
        modifier = modifier
            .heightIn(min = 44.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                fontSize = 13.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Bold,
                color = labelAndIconColor,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Exact Apple iOS Segmented Control.
 * - Solid track in `#2C2C2E` (Dark) or `#E5E5EA` (Light).
 * - Sliding active pill in `#3A3A3C` / accent with crisp white text.
 */
@Composable
fun <T> AppleSegmentedControl(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    labelProvider: (T) -> String,
    accentColor: Color = AppleMoveRed,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val selectedIndex = items.indexOf(selectedItem).coerceAtLeast(0)

    val trackColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceTertiaryLight
    val activePillColor = if (isDark) Color(0xFF3A3A3C) else Color(0xFFFFFFFF)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppleRadius.smallControl),
        color = trackColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item == selectedItem
                val itemInteraction = remember { MutableInteractionSource() }

                val textColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        if (isDark) Color.White else Color.Black
                    } else {
                        Color(0xFF8E8E93)
                    },
                    animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
                    label = "segTextColor"
                )

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .then(
                            if (isSelected && !isDark) {
                                Modifier.shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    spotColor = Color.Black.copy(alpha = 0.10f),
                                    ambientColor = Color.Black.copy(alpha = 0.04f)
                                )
                            } else Modifier
                        )
                        .clickable(
                            interactionSource = itemInteraction,
                            indication = null
                        ) { onItemSelected(item) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) activePillColor else Color.Transparent
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 7.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = labelProvider(item),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = textColor,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * Exact Apple Dynamic Island Top Capsule (for Live Workout Tracking & Rest Timers).
 * Clean solid pitch black capsule with crisp colored badges.
 */
@Composable
fun OriginIslandLiveTopCapsule(
    isVisible: Boolean,
    isResting: Boolean,
    isTimerRunning: Boolean,
    headlineText: String,
    subtitleText: String,
    timerBadgeText: String,
    accentColor: Color,
    onToggleTimer: () -> Unit,
    onAddRest15s: () -> Unit,
    onSkipRest: () -> Unit,
    onOpenWorkoutLogger: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(220)) + expandVertically(tween(260)),
        exit = fadeOut(tween(180)) + shrinkVertically(tween(220)),
        modifier = modifier
    ) {
        val capsuleCorner = if (isExpanded) 20.dp else 999.dp
        val capsuleShape = RoundedCornerShape(capsuleCorner)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(if (isExpanded) 1f else 0.94f)
                    .clip(capsuleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isExpanded = !isExpanded }
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                    .testTag("origin_island_live_capsule"),
                shape = capsuleShape,
                color = Color(0xFF000000),
                border = BorderStroke(1.dp, AppleHealthDarkOutline)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(accentColor)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = headlineText,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = subtitleText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF8E8E93),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = accentColor.copy(alpha = 0.20f)
                        ) {
                            Text(
                                text = timerBadgeText,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    if (isExpanded) {
                        HorizontalDivider(color = AppleHealthDarkOutline)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isResting) {
                                LiquidGlassPrimaryButton(
                                    text = "+15s Rest",
                                    onClick = onAddRest15s,
                                    accentColor = AppleOverloadOrange,
                                    modifier = Modifier.weight(1f)
                                )
                                LiquidGlassPrimaryButton(
                                    text = "Skip Rest",
                                    onClick = onSkipRest,
                                    accentColor = AppleMoveRed,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                LiquidGlassPrimaryButton(
                                    text = if (isTimerRunning) "Pause" else "Resume",
                                    onClick = onToggleTimer,
                                    accentColor = if (isTimerRunning) AppleOverloadOrange else AppleExerciseGreen,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            LiquidGlassPrimaryButton(
                                text = "Logger",
                                onClick = {
                                    isExpanded = false
                                    onOpenWorkoutLogger()
                                },
                                accentColor = AppleStandCyan,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Apple iOS Grouped Dropdown Field.
 * Clean solid background with subtle border.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppleDropdownField(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    subtitleProvider: ((String) -> String?)? = null,
    testTag: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember(selectedValue) { mutableStateOf(selectedValue) }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val isSearchable = options.size > 3

    val filteredOptions = remember(searchQuery, options, selectedValue) {
        val q = searchQuery.trim()
        if (q.isBlank() || q.equals(selectedValue.trim(), ignoreCase = true)) {
            options
        } else {
            options.filter { opt ->
                opt.contains(q, ignoreCase = true) ||
                (subtitleProvider?.invoke(opt)?.contains(q, ignoreCase = true) == true)
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isSearchable) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        expanded = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable)
                        .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
                    placeholder = {
                        Text(
                            text = selectedValue.ifBlank { "Search or select..." },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(AppleRadius.inputField),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                        unfocusedContainerColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                        focusedBorderColor = AppleMoveRed,
                        unfocusedBorderColor = if (isDark) AppleSeparatorDark else AppleOutlineLight
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
            } else {
                Surface(
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppleRadius.inputField))
                        .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
                    shape = RoundedCornerShape(AppleRadius.inputField),
                    color = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                    border = BorderStroke(1.dp, if (isDark) AppleSeparatorDark else AppleOutlineLight),
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedValue,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = label.ifBlank { "Dropdown" },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    searchQuery = selectedValue
                },
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                filteredOptions.forEach { option ->
                    val isSelected = option.equals(selectedValue, ignoreCase = true)
                    val sub = subtitleProvider?.invoke(option)
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AppleMoveRed else MaterialTheme.colorScheme.onSurface
                                )
                                if (!sub.isNullOrBlank()) {
                                    Text(
                                        text = sub,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        trailingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = AppleMoveRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        onClick = {
                            searchQuery = option
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Exact Apple iOS Grouped Input Field.
 */
@Composable
fun AppleLiquidGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else 4,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    accentColor: Color = AppleMoveRed,
    fieldModifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = null,
            placeholder = if (placeholder.isNotBlank()) {
                {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else null,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(AppleRadius.inputField),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                unfocusedContainerColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                focusedBorderColor = accentColor,
                unfocusedBorderColor = if (isDark) AppleSeparatorDark else AppleOutlineLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .then(fieldModifier)
        )
    }
}

/**
 * Searchable Multi-Select Exercise Dropdown.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppleMultiSelectExerciseDropdown(
    availableExercises: List<ExerciseEntity>,
    selectedExerciseNames: List<String>,
    onToggleExercise: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember(expanded) { mutableStateOf("") }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val filteredExercises = remember(searchQuery, availableExercises) {
        if (searchQuery.isBlank()) {
            availableExercises
        } else {
            val q = searchQuery.trim()
            availableExercises.filter {
                it.name.contains(q, ignoreCase = true) ||
                it.muscleGroup.contains(q, ignoreCase = true) ||
                it.equipment.contains(q, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { expanded = !expanded }
                .testTag("routine_exercises_dropdown_trigger"),
            shape = RoundedCornerShape(12.dp),
            color = if (isDark) AppleHealthDarkContainer else AppleHealthLightContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Select Exercises",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (selectedExerciseNames.isEmpty()) {
                            "Choose exercises (0 selected)"
                        } else {
                            "${selectedExerciseNames.size} exercises selected"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Exercise Dropdown",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) AppleHealthDarkSurface else AppleHealthLightSurface,
                border = BorderStroke(1.dp, AppleHealthDarkOutline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search exercise...", style = MaterialTheme.typography.bodySmall) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodySmall,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    val itemDividerColor = if (isDark) AppleHealthDarkOutline else AppleHealthLightOutline
                    HorizontalDivider(color = itemDividerColor)

                    val displayExercises = if (searchQuery.isBlank()) filteredExercises.take(15) else filteredExercises
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        displayExercises.forEachIndexed { index, exercise ->
                            val isSelected = selectedExerciseNames.any { it.equals(exercise.name, ignoreCase = true) }
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onToggleExercise(exercise.name) },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) AppleMoveRed.copy(alpha = 0.15f) else Color.Transparent
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = exercise.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) AppleMoveRed else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${exercise.muscleGroup} • ${exercise.equipment}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = AppleMoveRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            if (index < displayExercises.lastIndex) {
                                HorizontalDivider(color = itemDividerColor.copy(alpha = 0.5f))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { expanded = false }) {
                            Text("Done", style = MaterialTheme.typography.labelMedium, color = AppleMoveRed)
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = selectedExerciseNames.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedExerciseNames.forEachIndexed { idx, name ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isDark) AppleHealthDarkContainer else AppleHealthLightContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { onToggleExercise(name) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(AppleMoveRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove $name",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Exact Apple Health Section Header with optional action button or subtitle.
 */
@Composable
fun AppleSectionHeader(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    actionIcon: ImageVector? = null,
    actionColor: Color = AppleMoveRed,
    actionTestTag: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
                text = title,
                style = AppleTypeScale.Title2,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = AppleTypeScale.Subheadline,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (actionLabel != null && onActionClick != null) {
            LiquidGlassPrimaryButton(
                text = actionLabel,
                onClick = onActionClick,
                icon = actionIcon,
                accentColor = actionColor,
                testTag = actionTestTag
            )
        }
    }
}

@Composable
fun AnimatedMetricValueText(
    targetValue: Float,
    suffix: String = "",
    decimals: Int = 0,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val animatedVal by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "metricCounter"
    )
    val formatted = if (decimals <= 0 || animatedVal % 1f == 0f) {
        animatedVal.toInt().toString()
    } else {
        String.format(Locale.US, "%.${decimals}f", animatedVal)
    }
    Text(
        text = if (suffix.isBlank()) formatted else "$formatted $suffix",
        style = style,
        fontWeight = FontWeight.Bold,
        color = color,
        maxLines = 1,
        softWrap = false,
        overflow = TextOverflow.Ellipsis
    )
}
