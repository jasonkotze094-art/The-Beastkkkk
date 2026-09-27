package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CommandStatus
import com.example.model.ConceptPriority
import com.example.model.EngineMode
import com.example.model.RobotStatus
import com.example.ui.theme.*

@Composable
fun BeastEngineSwitcher(
    currentMode: EngineMode,
    onModeChange: (EngineMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalBeastTheme.current
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        color = DarkSurface,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, theme.primary.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val eduSelected = currentMode == EngineMode.EDUCATION
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (eduSelected) {
                            Modifier.background(Brush.horizontalGradient(listOf(theme.primary, theme.secondary)))
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
                    .clickable { onModeChange(EngineMode.EDUCATION) }
                    .padding(vertical = 10.dp)
                    .testTag("engine_switch_education"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "EDUCATION ENGINE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (eduSelected) Color.Black else TextSecondary,
                    letterSpacing = 0.5.sp
                )
            }

            val tradeSelected = currentMode == EngineMode.TRADING
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .then(
                        if (tradeSelected) {
                            Modifier.background(Brush.horizontalGradient(listOf(theme.primary, theme.secondary)))
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
                    .clickable { onModeChange(EngineMode.TRADING) }
                    .padding(vertical = 10.dp)
                    .testTag("engine_switch_trading"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TRADING TERMINAL",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (tradeSelected) Color.Black else TextSecondary,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    backgroundColor: Color = DarkSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalBeastTheme.current
    val strokeColor = borderColor ?: theme.primary.copy(alpha = 0.25f)
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, strokeColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            content = content
        )
    }
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accentColor: Color? = null,
    testTag: String = "cyber_button"
) {
    val theme = LocalBeastTheme.current
    val color = accentColor ?: theme.primary
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTag)
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.Black,
            disabledContainerColor = DarkSurfaceVariant,
            disabledContentColor = TextMuted
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun StatusPill(status: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = status,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        }
    }
}

@Composable
fun CommandStatusBadge(status: CommandStatus) {
    val (color, label) = when (status) {
        CommandStatus.PENDING -> BeastWarning to "PENDING"
        CommandStatus.RECEIVED -> Color(0xFF00B0FF) to "RECEIVED"
        CommandStatus.EXECUTED -> BeastSuccess to "EXECUTED"
        CommandStatus.REJECTED -> BeastError to "REJECTED"
    }
    StatusPill(status = label, color = color)
}

@Composable
fun PriorityBadge(priority: ConceptPriority) {
    val (color, label) = when (priority) {
        ConceptPriority.HIGH -> BeastError to "HIGH PRIORITY"
        ConceptPriority.MEDIUM -> BeastWarning to "MEDIUM"
        ConceptPriority.LOW -> Color(0xFF00B0FF) to "NORMAL"
    }
    StatusPill(status = label, color = color)
}

// Full Cyber Market Scan Radar Modal (matching video sequence)
@Composable
fun AiMarketScanModal(
    symbol: String,
    onScanComplete: () -> Unit
) {
    val theme = LocalBeastTheme.current

    var currentStep by remember { mutableIntStateOf(0) }
    var progress by remember { mutableFloatStateOf(0.15f) }

    val steps = listOf(
        "Scanning chart structure...",
        "Mapping liquidity & order blocks...",
        "Running 100+ strategies...",
        "Calculating TP / SL zones...",
        "Validating risk / reward..."
    )

    // Animated rotation for cyber radar ring
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        for (i in steps.indices) {
            currentStep = i
            progress = ((i + 1).toFloat() / steps.size.toFloat())
            kotlinx.coroutines.delay(650)
        }
        kotlinx.coroutines.delay(400)
        onScanComplete()
    }

    Dialog(onDismissRequest = { /* Modal in progress */ }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = DarkBackground,
            border = BorderStroke(2.dp, theme.primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Radar / Hologram scanner circle
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(theme.primary.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                        .border(2.dp, theme.primary.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.9f)
                            .rotate(angle)
                            .border(
                                3.dp,
                                Brush.sweepGradient(
                                    listOf(Color.Transparent, theme.primary, theme.accent)
                                ),
                                CircleShape
                            )
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = theme.primary
                        )
                        Text(
                            text = "NEURAL SCAN",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "AI MARKET SCAN",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "$symbol - NEURAL ENGINE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Steps list
                Column(modifier = Modifier.fillMaxWidth()) {
                    steps.forEachIndexed { index, step ->
                        val isDone = index < currentStep
                        val isCurrent = index == currentStep
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isDone -> BeastSuccess.copy(alpha = 0.2f)
                                            isCurrent -> theme.primary.copy(alpha = 0.2f)
                                            else -> DarkSurfaceVariant
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            isDone -> BeastSuccess
                                            isCurrent -> theme.primary
                                            else -> TextMuted
                                        },
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Text("✓", fontSize = 11.sp, color = BeastSuccess, fontWeight = FontWeight.Bold)
                                } else if (isCurrent) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(theme.primary)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step,
                                fontSize = 12.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isDone || isCurrent) TextPrimary else TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = theme.primary,
                    trackColor = DarkSurfaceVariant
                )
            }
        }
    }
}
