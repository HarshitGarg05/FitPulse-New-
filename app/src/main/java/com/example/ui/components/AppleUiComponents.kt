package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExerciseEntity
import com.example.ui.theme.AppleAccessoryCircleDark
import com.example.ui.theme.AppleAccessoryCircleLight
import com.example.ui.theme.AppleBgDark
import com.example.ui.theme.AppleBgLight
import com.example.ui.theme.AppleCardBorderDark
import com.example.ui.theme.AppleCardBorderLight
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
import com.example.ui.theme.AppleNestedCardBorderDark
import com.example.ui.theme.AppleNestedCardBorderLight
import com.example.ui.theme.AppleOutlineDark
import com.example.ui.theme.AppleOutlineLight
import com.example.ui.theme.AppleOverloadOrange
import com.example.ui.theme.AppleRadius
import com.example.ui.theme.AppleSeparatorDark
import com.example.ui.theme.AppleSeparatorLight
import com.example.ui.theme.AppleSpacing
import com.example.ui.theme.AppleStandBlue
import com.example.ui.theme.AppleStandCyan
import com.example.ui.theme.AppleStateDisabled
import com.example.ui.theme.AppleSurfaceDark
import com.example.ui.theme.AppleSurfaceLight
import com.example.ui.theme.AppleSurfaceSecondaryDark
import com.example.ui.theme.AppleSurfaceSecondaryLight
import com.example.ui.theme.AppleSurfaceTertiaryDark
import com.example.ui.theme.AppleSurfaceTertiaryLight
import com.example.ui.theme.AppleSystemBlue
import com.example.ui.theme.AppleSystemPink
import com.example.ui.theme.AppleTextInverseLight
import com.example.ui.theme.AppleTextPlaceholderLight
import com.example.ui.theme.AppleTextPrimaryDark
import com.example.ui.theme.AppleTextPrimaryLight
import com.example.ui.theme.AppleTextSecondaryDark
import com.example.ui.theme.AppleTextSecondaryLight
import com.example.ui.theme.AppleTypeScale
import java.util.Locale

private val PrimaryCardShape = RoundedCornerShape(20.dp)
private val NestedCardShape = RoundedCornerShape(16.dp)
private val NavPillShape = RoundedCornerShape(100.dp)
private val LightPrimaryCardBorder = BorderStroke(1.dp, Color(0xFFE8E8ED))
private val DarkPrimaryCardBorder = BorderStroke(1.dp, AppleCardBorderDark)
private val LightNestedCardBorder = BorderStroke(1.dp, Color(0xFFEBEBF0))
private val DarkNestedCardBorder = BorderStroke(0.5.dp, AppleNestedCardBorderDark)

/**
 * Compatible CompositionLocal so any callers referencing `LocalLiquidGlassPhase` continue to compile.
 */
val LocalLiquidGlassPhase = compositionLocalOf<State<Float>> { mutableFloatStateOf(0f) }

/**
 * Reusable Root Screen Background Component (`AppleScreenContainer`).
 * Explicitly forces the root canvas background to `#F2F2F7` (`Color(0xFFF2F2F7)`) in Light Mode
 * and `#000000` in Dark Mode so child `#FFFFFF` cards never sit on a white parent surface.
 */
@Composable
fun AppleScreenContainer(
    modifier: Modifier = Modifier,
    testTag: String? = null,
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val rootCanvasColor = if (isDark) Color(0xFF000000) else Color(0xFFF2F2F7)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(rootCanvasColor)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        contentAlignment = contentAlignment
    ) {
        CompositionLocalProvider(
            LocalContentColor provides if (isDark) AppleTextPrimaryDark else Color(0xFF000000)
        ) {
            content()
        }
    }
}

/**
 * Exact Apple Health / Fitness clean solid background (`#F2F2F7` in Light Mode).
 */
@Composable
fun LiquidGlassAmbientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AppleScreenContainer(
        modifier = modifier,
        content = content
    )
}

/**
 * Reusable Primary Content Card (`AppleStyleCard`).
 * Explicitly renders:
 * - Background: `#FFFFFF` (`Color(0xFFFFFFFF)`) in Light Mode
 * - Border: `1.dp` solid `#E8E8ED` (`Color(0xFFE8E8ED)`)
 * - Shape: `RoundedCornerShape(20.dp)`
 * - Elevation: `3.dp` soft diffused shadow (single hardware shadow layer for 120Hz scroll smoothness)
 * - Internal padding: `16.dp`
 */
@Composable
fun AppleStyleCard(
    modifier: Modifier = Modifier,
    accentTint: Color = AppleStandBlue,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val onSurfaceColor = if (isDark) AppleTextPrimaryDark else Color(0xFF000000)
    val cardBg = if (isDark) AppleSurfaceDark else Color(0xFFFFFFFF)
    val cardBorder = if (isDark) DarkPrimaryCardBorder else LightPrimaryCardBorder
    val cardElevation = if (isDark) 0.dp else 3.dp

    val clickableAndScaleModifier = if (onClick != null) {
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
        Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    } else {
        Modifier
    }

    CompositionLocalProvider(LocalContentColor provides onSurfaceColor) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .then(clickableAndScaleModifier),
            shape = PrimaryCardShape,
            color = cardBg,
            contentColor = onSurfaceColor,
            shadowElevation = cardElevation,
            tonalElevation = 0.dp,
            border = cardBorder
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
            ) {
                content()
            }
        }
    }
}

/**
 * Primary Content Card alias (`LiquidGlassCard`) delegating to `AppleStyleCard`.
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    accentTint: Color = AppleStandBlue,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AppleStyleCard(
        modifier = modifier,
        accentTint = accentTint,
        onClick = onClick,
        content = content
    )
}

/**
 * Reusable Secondary / Nested Card (`AppleNestedCard`).
 * - Light Mode:
 *   - Color: `#FAFAFC` (`AppleSurfaceSecondaryLight`)
 *   - Border: `#EBEBF0` (`AppleNestedCardBorderLight`, 1.dp)
 *   - Corner radius: `16.dp` (`AppleRadius.nestedCard`)
 *   - Elevation: `1.dp`
 */
@Composable
fun AppleNestedCard(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(12.dp),
    content: @Composable () -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val nestedBg = if (isDark) AppleSurfaceSecondaryDark else Color(0xFFFAFAFC)
    val nestedBorder = if (isDark) DarkNestedCardBorder else LightNestedCardBorder
    Surface(
        modifier = modifier,
        shape = NestedCardShape,
        color = nestedBg,
        contentColor = if (isDark) AppleTextPrimaryDark else Color(0xFF000000),
        shadowElevation = if (isDark) 0.dp else 1.dp,
        tonalElevation = 0.dp,
        border = nestedBorder
    ) {
        Box(modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

/**
 * Reusable Modular Metric Card (`AppleStyleMetricCard`).
 * Pure white card (`#FFFFFF`, `20.dp` radius, `3.dp` elevation, `1.dp` `#E8E8ED` border, `16.dp` internal padding)
 * sitting directly on the `#F2F2F7` screen canvas.
 */
@Composable
fun AppleStyleMetricCard(
    label: String,
    primaryValue: String,
    secondaryValue: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    AppleStyleCard(
        modifier = modifier,
        accentTint = accentColor,
        contentPadding = PaddingValues(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = primaryValue,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = secondaryValue,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Reusable Section Container (`AppleStyleSection`) with outside section heading and spaced child content.
 */
@Composable
fun AppleStyleSection(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    actionIcon: ImageVector? = null,
    actionColor: Color = AppleMoveRed,
    actionTestTag: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AppleSectionHeader(
            title = title,
            subtitle = subtitle,
            actionLabel = actionLabel,
            actionIcon = actionIcon,
            actionColor = actionColor,
            actionTestTag = actionTestTag,
            onActionClick = onActionClick
        )
        content()
    }
}

/**
 * Reusable Grouped Primary Card (`AppleStyleGroupedCard`) for grouping related rows or metrics
 * inside a `#FFFFFF` card (`20.dp` radius, `3.dp` elevation, `#E8E8ED` border).
 */
@Composable
fun AppleStyleGroupedCard(
    modifier: Modifier = Modifier,
    accentTint: Color = AppleStandBlue,
    title: String? = null,
    subtitle: String? = null,
    actionLabel: String? = null,
    actionIcon: ImageVector? = null,
    actionColor: Color = AppleMoveRed,
    actionTestTag: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AppleStyleCard(
        modifier = modifier,
        accentTint = accentTint
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (!title.isNullOrBlank()) {
                AppleSectionHeader(
                    title = title,
                    subtitle = subtitle,
                    actionLabel = actionLabel,
                    actionIcon = actionIcon,
                    actionColor = actionColor,
                    actionTestTag = actionTestTag,
                    onActionClick = onActionClick
                )
            }
            content()
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

private val SelectedNavContentColor = Color(0xFFFF2D55)
private val UnselectedNavContentColor = Color(0xFF8E8E93)
private val SelectedNavHighlightShape = RoundedCornerShape(14.dp)

/**
 * Floating Apple-Inspired Navigation Bar.
 *
 * SELECTED STATE:
 * - Translucent rounded-rectangle highlight behind BOTH icon and label
 * - Icon tint: #FF2D55
 * - Label color: #FF2D55
 * - Label font weight: Semibold
 * - Shape: RoundedCornerShape(14.dp) (rectangle-shaped with smooth rounded corners)
 * - Full non-cropped single-line label layout
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

    val pillBgBase = if (isDark) {
        AppleSurfaceDark.copy(alpha = 0.94f)
    } else {
        AppleSurfaceLight.copy(alpha = 0.96f)
    }
    val borderColor = if (isDark) {
        AppleCardBorderDark
    } else {
        AppleSeparatorLight
    }

    val selectedHighlightFill = if (isDark) {
        SelectedNavContentColor.copy(alpha = 0.22f)
    } else {
        SelectedNavContentColor.copy(alpha = 0.14f)
    }
    val selectedHighlightBorder = if (isDark) {
        SelectedNavContentColor.copy(alpha = 0.34f)
    } else {
        SelectedNavContentColor.copy(alpha = 0.24f)
    }

    val glassSheenBrush = remember(isDark) {
        Brush.verticalGradient(
            colors = if (isDark) {
                listOf(
                    AppleTextInverseLight.copy(alpha = 0.10f),
                    AppleTextInverseLight.copy(alpha = 0.02f),
                    AppleTextPrimaryLight.copy(alpha = 0.10f)
                )
            } else {
                listOf(
                    AppleSurfaceLight.copy(alpha = 0.98f),
                    AppleSurfaceSecondaryLight.copy(alpha = 0.88f),
                    AppleSurfaceLight.copy(alpha = 0.95f)
                )
            }
        )
    }

    val topRimBrush = remember(isDark) {
        Brush.horizontalGradient(
            colors = if (isDark) {
                listOf(
                    Color.Transparent,
                    AppleTextInverseLight.copy(alpha = 0.22f),
                    Color.Transparent
                )
            } else {
                listOf(
                    Color.Transparent,
                    AppleTextInverseLight.copy(alpha = 0.92f),
                    Color.Transparent
                )
            }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            shape = RoundedCornerShape(24.dp),
            color = pillBgBase,
            shadowElevation = 8.dp,
            tonalElevation = 0.dp,
            border = BorderStroke(1.dp, borderColor)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Frosted glass sheen layer (without expensive per-frame RenderEffect blur)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(glassSheenBrush)
                )

                // Top rim specular highlight
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(topRimBrush)
                )

                // Navigation content area
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    val tabCount = items.size.coerceAtLeast(1)
                    val selectedIndex = items.indexOfFirst { it.route == selectedRoute }.coerceAtLeast(0)
                    val tabWidth = maxWidth / tabCount
                    val selectedRectWidth = (tabWidth - 4.dp).coerceAtLeast(48.dp)
                    val selectedRectHeight = (maxHeight - 4.dp).coerceAtLeast(48.dp)
                    val targetOffsetX = (tabWidth * selectedIndex) + ((tabWidth - selectedRectWidth) / 2f)

                    val animatedPillOffsetX by animateDpAsState(
                        targetValue = targetOffsetX,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        ),
                        label = "selectedPillOffsetX"
                    )

                    // Smoothly sliding translucent rounded-rectangle highlight behind icon + label
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = animatedPillOffsetX)
                            .width(selectedRectWidth)
                            .height(selectedRectHeight),
                        shape = SelectedNavHighlightShape,
                        color = selectedHighlightFill,
                        border = BorderStroke(1.dp, selectedHighlightBorder),
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {}

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items.forEach { item ->
                            val isSelected = item.route == selectedRoute

                            val itemContentColor by animateColorAsState(
                                targetValue = if (isSelected) SelectedNavContentColor else UnselectedNavContentColor,
                                animationSpec = tween(
                                    durationMillis = 250,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "navItemContentColor"
                            )

                            val itemInteraction = remember { MutableInteractionSource() }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable(
                                        interactionSource = itemInteraction,
                                        indication = null
                                    ) {
                                        if (!isSelected) {
                                            VivoHapticFeedback.performTick(context)
                                        }
                                        onSelect(item.route)
                                    }
                                    .testTag(item.testTag),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 1.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label,
                                        tint = itemContentColor,
                                        modifier = Modifier.size(21.dp)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = item.label,
                                        fontSize = 10.sp,
                                        lineHeight = 12.sp,
                                        letterSpacing = (-0.25).sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = itemContentColor,
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Visible,
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
    val activePillColor = if (isDark) AppleSurfaceTertiaryDark else AppleSurfaceLight

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
                        if (isDark) AppleTextPrimaryDark else AppleTextPrimaryLight
                    } else {
                        if (isDark) AppleTextSecondaryDark else AppleTextSecondaryLight
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
 * Apple iOS Grouped Dropdown Field.
 * - Uses a uniform 56.dp height closed box across all option counts so side-by-side columns are always the exact same size.
 * - Always expands downwards directly below the field (never pops upwards over the screen header).
 */
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
    var searchQuery by remember(expanded) { mutableStateOf("") }
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val showSearchInsideMenu = options.size > 8

    val filteredOptions = remember(searchQuery, options) {
        val q = searchQuery.trim()
        if (q.isBlank()) {
            options
        } else {
            options.filter { opt ->
                opt.contains(q, ignoreCase = true) ||
                    (subtitleProvider?.invoke(opt)?.contains(q, ignoreCase = true) == true)
            }
        }
    }

    val fieldShape = RoundedCornerShape(AppleRadius.inputField)
    val triggerBorderColor = if (expanded) {
        AppleMoveRed
    } else if (isDark) {
        AppleNestedCardBorderDark
    } else {
        AppleNestedCardBorderLight
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

        // Uniform 56.dp height closed trigger box so every column in a 2-column row has the exact same size
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(fieldShape)
                .clickable { expanded = !expanded }
                .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
            shape = fieldShape,
            color = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
            shadowElevation = if (isDark) 0.dp else 1.dp,
            border = BorderStroke(if (expanded) 1.5.dp else 1.dp, triggerBorderColor),
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValue,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = label.ifBlank { "Dropdown" },
                    tint = if (expanded) AppleMoveRed else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Always opens DOWNWARDS directly below the trigger box
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(180)) + expandVertically(
                animationSpec = tween(220, easing = FastOutSlowInEasing),
                expandFrom = Alignment.Top
            ),
            exit = fadeOut(tween(140)) + shrinkVertically(
                animationSpec = tween(180, easing = FastOutSlowInEasing),
                shrinkTowards = Alignment.Top
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (isDark) AppleSurfaceDark else Color(0xFFFFFFFF),
                shadowElevation = if (isDark) 2.dp else 4.dp,
                tonalElevation = 0.dp,
                border = BorderStroke(1.dp, if (isDark) AppleCardBorderDark else Color(0xFFE8E8ED))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    if (showSearchInsideMenu) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = "Search...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
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
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                                unfocusedContainerColor = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                                focusedBorderColor = AppleMoveRed,
                                unfocusedBorderColor = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    val displayedOptions = if (showSearchInsideMenu && searchQuery.isBlank()) {
                        filteredOptions.take(12)
                    } else {
                        filteredOptions
                    }

                    displayedOptions.forEachIndexed { index, option ->
                        val isSelected = option.equals(selectedValue, ignoreCase = true)
                        val sub = subtitleProvider?.invoke(option)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOptionSelected(option)
                                    expanded = false
                                    searchQuery = ""
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp)
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) AppleMoveRed else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (!sub.isNullOrBlank()) {
                                    Text(
                                        text = sub,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = AppleMoveRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        if (index < displayedOptions.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                thickness = 0.5.dp,
                                color = if (isDark) AppleSeparatorDark else AppleSeparatorLight
                            )
                        }
                    }
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
                unfocusedBorderColor = if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight
            ),
            modifier = Modifier
                .fillMaxWidth()
                .then(if (singleLine) Modifier.height(56.dp) else Modifier.heightIn(min = 56.dp))
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
                .clip(RoundedCornerShape(AppleRadius.nestedCard))
                .clickable { expanded = !expanded }
                .testTag("routine_exercises_dropdown_trigger"),
            shape = RoundedCornerShape(AppleRadius.nestedCard),
            color = if (isDark) AppleHealthDarkContainer else AppleHealthLightContainer,
            shadowElevation = if (isDark) 0.dp else 1.dp,
            border = BorderStroke(1.dp, if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight),
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
                shape = RoundedCornerShape(AppleRadius.inputField),
                color = if (isDark) AppleSurfaceSecondaryDark else AppleSurfaceSecondaryLight,
                shadowElevation = if (isDark) 0.dp else 1.dp,
                border = BorderStroke(1.dp, if (isDark) AppleNestedCardBorderDark else AppleNestedCardBorderLight)
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
