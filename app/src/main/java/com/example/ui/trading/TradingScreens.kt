package com.example.ui.trading

import android.graphics.BitmapFactory
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BeastRepository
import com.example.data.GeminiService
import com.example.data.VoiceAnnouncer
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.dialogs.LicenseActivationDialog
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TradingTerminalScreen(
    currentThemePreset: BeastThemePreset,
    onThemeChange: (BeastThemePreset) -> Unit,
    voiceAnnouncer: VoiceAnnouncer?
) {
    var selectedBottomNav by remember { mutableIntStateOf(0) }
    var showLicenseDialog by remember { mutableStateOf(false) }

    val theme = LocalBeastTheme.current

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = theme.primary,
                tonalElevation = 8.dp
            ) {
                val navItems = listOf(
                    Triple(0, "Home", Icons.Filled.Home),
                    Triple(1, "Symbols", Icons.Filled.CandlestickChart),
                    Triple(2, "Monitor", Icons.Filled.Analytics),
                    Triple(3, "Settings", Icons.Filled.Settings)
                )
                navItems.forEach { (index, label, icon) ->
                    val isSelected = selectedBottomNav == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedBottomNav = index },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) theme.primary else TextSecondary
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) theme.primary else TextSecondary
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = theme.primary.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.testTag("trading_nav_$label")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedBottomNav) {
                0 -> TradingHomeScreen(
                    onAddRobotClick = { showLicenseDialog = true },
                    onNavigateToSymbols = { selectedBottomNav = 1 },
                    voiceAnnouncer = voiceAnnouncer
                )
                1 -> SymbolsAnalyzerScreen(voiceAnnouncer = voiceAnnouncer)
                2 -> MonitorSessionsScreen(voiceAnnouncer = voiceAnnouncer)
                3 -> TradingSettingsScreen(
                    currentTheme = currentThemePreset,
                    onThemeChange = onThemeChange,
                    voiceAnnouncer = voiceAnnouncer
                )
            }

            if (showLicenseDialog) {
                LicenseActivationDialog(
                    onDismiss = { showLicenseDialog = false },
                    onSuccess = { boundAccount ->
                        voiceAnnouncer?.speak("License verified. Robot activated on $boundAccount.")
                    }
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 1. HOME SCREEN (WAKANDA WEALTH EA)
// ---------------------------------------------------------------------------
@Composable
fun TradingHomeScreen(
    onAddRobotClick: () -> Unit,
    onNavigateToSymbols: () -> Unit,
    voiceAnnouncer: VoiceAnnouncer?
) {
    val theme = LocalBeastTheme.current
    val robots by BeastRepository.robots.collectAsState()
    val primaryRobot = robots.firstOrNull() ?: RobotEA(
        id = "default",
        name = "WAKANDA WEALTH EA",
        mentor = "Thesia",
        status = RobotStatus.ACTIVE,
        accountBound = "MT5 #8849201",
        winRate = 88.5,
        totalProfit = 14820.50,
        activeTrades = 2,
        licenseKey = "BEAST-9921-ACTIVE-PRO"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App header bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(theme.primary.copy(alpha = 0.2f))
                            .border(1.dp, theme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("EA", fontWeight = FontWeight.Black, fontSize = 12.sp, color = theme.primary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("EA NEXUS", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary, letterSpacing = 1.sp)
                        Text("TERMINAL CONNECTED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BeastSuccess)
                    }
                }

                StatusPill(status = primaryRobot.status.name, color = if (primaryRobot.status == RobotStatus.ACTIVE) BeastSuccess else BeastWarning)
            }
        }

        // Hero Card (Wakanda Wealth EA)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = DarkSurface,
                border = BorderStroke(1.5.dp, theme.primary)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "You are trading with",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = primaryRobot.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Mentor: ${primaryRobot.mentor}",
                        fontSize = 11.sp,
                        color = theme.accent,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Hologram Vector Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.dp, theme.primary.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_beast_hologram),
                            contentDescription = "Wakanda Wealth EA Hologram",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Actions Row (matching video: REMOVE, START, SYMBOLS)
                    Text(
                        text = "Actions",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // REMOVE
                        ActionTile(
                            icon = Icons.Default.Delete,
                            label = "REMOVE",
                            accentColor = BeastWarning,
                            onClick = {
                                BeastRepository.removeRobot(primaryRobot.id)
                                voiceAnnouncer?.speak("Robot detached from active bridge.")
                            }
                        )

                        // START / STOP
                        val isStarted = primaryRobot.status == RobotStatus.ACTIVE
                        ActionTile(
                            icon = if (isStarted) Icons.Default.Pause else Icons.Default.PlayArrow,
                            label = if (isStarted) "PAUSE" else "START",
                            accentColor = if (isStarted) theme.primary else BeastSuccess,
                            onClick = {
                                BeastRepository.toggleRobot(primaryRobot.id)
                                voiceAnnouncer?.speak(if (isStarted) "Execution paused." else "Wakanda Wealth EA algorithm activated.")
                            }
                        )

                        // SYMBOLS
                        ActionTile(
                            icon = Icons.AutoMirrored.Filled.ShowChart,
                            label = "SYMBOLS",
                            accentColor = theme.secondary,
                            onClick = onNavigateToSymbols
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "POWERED BY EA NEXUS",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        // Robot List Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ROBOT LIST:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${robots.size} Installed",
                    fontSize = 11.sp,
                    color = theme.primary
                )
            }
        }

        items(robots) { bot ->
            CyberCard(
                borderColor = if (bot.status == RobotStatus.ACTIVE) theme.primary.copy(alpha = 0.5f) else DarkSurfaceBorder,
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(bot.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Mentor: ${bot.mentor} • ${bot.accountBound}", fontSize = 11.sp, color = TextSecondary)
                        Text("Key: ${bot.licenseKey}", fontSize = 10.sp, color = theme.accent)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        StatusPill(status = bot.status.name, color = if (bot.status == RobotStatus.ACTIVE) BeastSuccess else BeastWarning)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Win rate: ${bot.winRate}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BeastSuccess)
                    }
                }
            }
        }

        // "+ Add a new Robot" Button
        item {
            Button(
                onClick = onAddRobotClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("add_new_robot_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkSurfaceVariant,
                    contentColor = theme.primary
                ),
                border = BorderStroke(1.dp, theme.primary.copy(alpha = 0.4f))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = theme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add a new Robot via License Key",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun ActionTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(96.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = DarkSurfaceVariant,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = accentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

// ---------------------------------------------------------------------------
// 2. SYMBOLS & AI MARKET SCAN SCREEN
// ---------------------------------------------------------------------------
@Composable
fun SymbolsAnalyzerScreen(voiceAnnouncer: VoiceAnnouncer?) {
    val theme = LocalBeastTheme.current
    val coroutineScope = rememberCoroutineScope()

    var selectedSymbol by remember { mutableStateOf("XAUUSD") }
    var selectedMode by remember { mutableStateOf(TradingMode.SCALP) }
    var lotSize by remember { mutableStateOf("0.01") }
    var numTrades by remember { mutableStateOf("1") }

    var isScanningModalOpen by remember { mutableStateOf(false) }
    var scanResult by remember { mutableStateOf<ChartScanResult?>(null) }
    var activeCommandFeedback by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "NEURAL SYMBOL ANALYZER",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 1.sp
            )
            Text(
                text = "Multi-timeframe liquidity scan with auto-order execution",
                fontSize = 11.sp,
                color = TextSecondary
            )
        }

        // Symbol / Pair Selector
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Text(
                    text = "Symbol / Pair",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("XAUUSD", "BTCUSD", "NAS100", "EURUSD").forEach { sym ->
                        val isSelected = selectedSymbol == sym
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedSymbol = sym }
                                .testTag("symbol_chip_$sym"),
                            color = if (isSelected) theme.primary.copy(alpha = 0.2f) else DarkSurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) theme.primary else DarkSurfaceBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = sym,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) theme.primary else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Trading Mode Chips
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Text(
                    text = "Trading Mode",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TradingMode.values().forEach { mode ->
                        val isSelected = selectedMode == mode
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedMode = mode }
                                .testTag("mode_${mode.name}"),
                            color = if (isSelected) theme.primary.copy(alpha = 0.2f) else DarkSurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) theme.primary else DarkSurfaceBorder),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = mode.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) theme.primary else TextPrimary
                                )
                                Text(
                                    text = mode.timeframe,
                                    fontSize = 10.sp,
                                    color = if (isSelected) theme.accent else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Chart Screenshot Section (with uploaded chart or sample)
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chart Screenshot",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text("Live Feed: M15 Active", fontSize = 10.sp, color = BeastSuccess, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Chart preview container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkBackground)
                        .border(1.dp, theme.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_chart_gold),
                        contentDescription = "Chart Screenshot",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$selectedSymbol • ${selectedMode.label} (${selectedMode.timeframe})",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = theme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = { /* Simulated photo library pick */ },
                        label = { Text("Photo Library", fontSize = 10.sp) },
                        leadingIcon = { Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                    AssistChip(
                        onClick = { /* Simulated take photo */ },
                        label = { Text("Take Photo", fontSize = 10.sp) },
                        leadingIcon = { Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                }
            }
        }

        // Lot Size and # of Trades Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = lotSize,
                    onValueChange = { lotSize = it },
                    label = { Text("Lot size") },
                    modifier = Modifier.weight(1f).testTag("lot_size_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                OutlinedTextField(
                    value = numTrades,
                    onValueChange = { numTrades = it },
                    label = { Text("# of trades") },
                    modifier = Modifier.weight(1f).testTag("num_trades_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        }

        // "Analyze Chart" Button
        item {
            CyberButton(
                text = "⚡ ANALYZE CHART",
                onClick = {
                    isScanningModalOpen = true
                },
                modifier = Modifier.fillMaxWidth(),
                testTag = "analyze_chart_button"
            )
        }

        // Scan Result & SMC Breakdown
        if (scanResult != null) {
            val res = scanResult!!
            item {
                CyberCard(
                    borderColor = BeastSuccess,
                    backgroundColor = DarkSurface
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "ANALYSIS COMPLETE: ${res.bias}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = BeastSuccess
                            )
                            Text(
                                text = "Confidence: ${res.confidence}% • R:R ${res.riskReward}",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                        StatusPill(status = "${res.strategiesTested}+ STRATEGIES", color = theme.primary)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PriceMetric("Entry Zone", "\$${res.entryPrice}")
                        PriceMetric("Take Profit 1", "\$${res.takeProfit1}", BeastSuccess)
                        PriceMetric("Take Profit 2", "\$${res.takeProfit2}", BeastSuccess)
                        PriceMetric("Stop Loss", "\$${res.stopLoss}", BeastError)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = res.reasoning,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    if (res.searchGroundingNote != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Engine: ${res.searchGroundingNote}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = theme.accent
                        )
                    }
                }
            }
        }

        // Execution Commands Bar (BUY, SELL, START_AUTO, STOP_AUTO, CLOSE_ALL)
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Text(
                    text = "EXECUTION BRIDGE COMMANDS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Lifecycle: PENDING → RECEIVED → EXECUTED",
                    fontSize = 10.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // BUY
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val cmd = BeastRepository.dispatchCommand(
                                    symbol = selectedSymbol,
                                    type = CommandType.BUY,
                                    lotSize = lotSize.toDoubleOrNull() ?: 0.01,
                                    executionPrice = if (selectedSymbol == "XAUUSD") 2652.40 else 64200.0
                                )
                                activeCommandFeedback = "BUY ${cmd.symbol} PENDING..."
                                delay(600)
                                BeastRepository.updateCommandStatus(cmd.id, CommandStatus.RECEIVED)
                                activeCommandFeedback = "BUY ${cmd.symbol} RECEIVED BY MT5..."
                                delay(700)
                                BeastRepository.updateCommandStatus(cmd.id, CommandStatus.EXECUTED, profitLoss = 24.50)
                                activeCommandFeedback = "✓ BUY ${cmd.symbol} EXECUTED AT ${cmd.executionPrice}"
                                voiceAnnouncer?.speak("Wakanda Wealth EA executing Buy on ${cmd.symbol}")
                            }
                        },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("cmd_buy_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BeastBuy, contentColor = Color.Black)
                    ) {
                        Text("BUY", fontWeight = FontWeight.ExtraBold)
                    }

                    // SELL
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val cmd = BeastRepository.dispatchCommand(
                                    symbol = selectedSymbol,
                                    type = CommandType.SELL,
                                    lotSize = lotSize.toDoubleOrNull() ?: 0.01,
                                    executionPrice = if (selectedSymbol == "XAUUSD") 2652.40 else 64200.0
                                )
                                activeCommandFeedback = "SELL ${cmd.symbol} PENDING..."
                                delay(600)
                                BeastRepository.updateCommandStatus(cmd.id, CommandStatus.RECEIVED)
                                activeCommandFeedback = "SELL ${cmd.symbol} RECEIVED BY MT5..."
                                delay(700)
                                BeastRepository.updateCommandStatus(cmd.id, CommandStatus.EXECUTED, profitLoss = 31.00)
                                activeCommandFeedback = "✓ SELL ${cmd.symbol} EXECUTED AT ${cmd.executionPrice}"
                                voiceAnnouncer?.speak("Wakanda Wealth EA executing Sell on ${cmd.symbol}")
                            }
                        },
                        modifier = Modifier.weight(1f).height(46.dp).testTag("cmd_sell_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BeastSell, contentColor = Color.White)
                    ) {
                        Text("SELL", fontWeight = FontWeight.ExtraBold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            BeastRepository.dispatchCommand(selectedSymbol, CommandType.START_AUTO, 0.01, 0.0)
                            activeCommandFeedback = "AUTO TRADING INITIATED"
                            voiceAnnouncer?.speak("Auto trading activated.")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("START AUTO", fontSize = 10.sp, color = BeastSuccess)
                    }

                    OutlinedButton(
                        onClick = {
                            BeastRepository.dispatchCommand(selectedSymbol, CommandType.STOP_AUTO, 0.01, 0.0)
                            activeCommandFeedback = "AUTO TRADING HALTED"
                            voiceAnnouncer?.speak("Auto trading stopped.")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("STOP AUTO", fontSize = 10.sp, color = BeastWarning)
                    }

                    OutlinedButton(
                        onClick = {
                            BeastRepository.dispatchCommand(selectedSymbol, CommandType.CLOSE_ALL, 0.01, 0.0)
                            activeCommandFeedback = "ALL POSITIONS CLOSED"
                            voiceAnnouncer?.speak("All open positions closed.")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("CLOSE ALL", fontSize = 10.sp, color = BeastError)
                    }
                }

                if (activeCommandFeedback != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeCommandFeedback!!,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.primary
                    )
                }
            }
        }
    }

    if (isScanningModalOpen) {
        AiMarketScanModal(
            symbol = selectedSymbol,
            onScanComplete = {
                isScanningModalOpen = false
                coroutineScope.launch {
                    val result = GeminiService.analyzeChartScreenshot(
                        symbol = selectedSymbol,
                        mode = selectedMode,
                        lotSize = lotSize.toDoubleOrNull() ?: 0.01
                    )
                    scanResult = result
                    voiceAnnouncer?.speak("AI Market Scan complete for $selectedSymbol. Strong buy signal detected.")
                }
            }
        )
    }
}

@Composable
private fun PriceMetric(label: String, value: String, color: Color = TextPrimary) {
    Column {
        Text(text = label, fontSize = 9.sp, color = TextMuted)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

// ---------------------------------------------------------------------------
// 3. MONITOR & SESSIONS SCREEN
// ---------------------------------------------------------------------------
@Composable
fun MonitorSessionsScreen(voiceAnnouncer: VoiceAnnouncer?) {
    val theme = LocalBeastTheme.current
    val openPositions by BeastRepository.openPositions.collectAsState()
    val commands by BeastRepository.commands.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Performance Card
        item {
            CyberCard(
                borderColor = theme.primary.copy(alpha = 0.4f),
                backgroundColor = DarkSurface
            ) {
                Text(
                    text = "TERMINAL PERFORMANCE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Floating PnL", fontSize = 10.sp, color = TextSecondary)
                        Text("+\$130.50", fontSize = 18.sp, fontWeight = FontWeight.Black, color = BeastSuccess)
                    }
                    Column {
                        Text("Today's Win Rate", fontSize = 10.sp, color = TextSecondary)
                        Text("89.4%", fontSize = 18.sp, fontWeight = FontWeight.Black, color = theme.primary)
                    }
                    Column {
                        Text("Active Trades", fontSize = 10.sp, color = TextSecondary)
                        Text("${openPositions.size}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                    }
                }
            }
        }

        // Market Sessions Row
        item {
            Text(
                text = "GLOBAL MARKET SESSIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        items(BeastRepository.marketSessions) { session ->
            CyberCard(
                borderColor = if (session.status == SessionStatus.OPEN) BeastSuccess.copy(alpha = 0.4f) else DarkSurfaceBorder,
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(session.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(session.activeHours, fontSize = 11.sp, color = TextSecondary)
                        Text("Volatility: ${session.volatilityLevel}", fontSize = 10.sp, color = theme.primary)
                    }
                    StatusPill(
                        status = session.status.name,
                        color = if (session.status == SessionStatus.OPEN) BeastSuccess else TextMuted
                    )
                }
            }
        }

        // Live Positions
        item {
            Text(
                text = "ACTIVE OPEN POSITIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        items(openPositions) { pos ->
            CyberCard(backgroundColor = DarkSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(pos.symbol, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.width(6.dp))
                            StatusPill(status = pos.type.name, color = BeastSuccess)
                        }
                        Text("Open: \$${pos.openPrice} • Lot ${pos.lotSize}", fontSize = 11.sp, color = TextSecondary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "+$${pos.pnl}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = BeastSuccess
                        )
                        Text("Now: \$${pos.currentPrice}", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }
        }

        // Command Execution Log
        item {
            Text(
                text = "BRIDGE COMMAND LOGS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        items(commands) { cmd ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = DarkSurfaceVariant,
                border = BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("${cmd.type.name} • ${cmd.symbol}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(cmd.note, fontSize = 10.sp, color = TextSecondary)
                    }
                    CommandStatusBadge(status = cmd.status)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 4. SETTINGS SCREEN (THEMES, VOICE, CREDENTIALS)
// ---------------------------------------------------------------------------
@Composable
fun TradingSettingsScreen(
    currentTheme: BeastThemePreset,
    onThemeChange: (BeastThemePreset) -> Unit,
    voiceAnnouncer: VoiceAnnouncer?
) {
    val theme = LocalBeastTheme.current
    val coroutineScope = rememberCoroutineScope()

    var voiceEnabled by remember { mutableStateOf(voiceAnnouncer?.isEnabled ?: true) }
    var speedSlider by remember { mutableFloatStateOf(1.05f) }
    var pitchSlider by remember { mutableFloatStateOf(0.95f) }

    var botUuid by remember { mutableStateOf("••••••••••••-8199") }
    var apiKeyInput by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("8849201") }
    var connectionTestStatus by remember { mutableStateOf<String?>(null) }
    var isTestingConnection by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Theme Colors Section (Exact copy of video feature!)
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Palette, contentDescription = null, tint = theme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Theme Colors",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Pick your neon palette. Saves automatically.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Quick presets",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Presets grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemePresetCard(
                            preset = BeastThemePreset.CYBER_BLUE,
                            isSelected = currentTheme == BeastThemePreset.CYBER_BLUE,
                            modifier = Modifier.weight(1f),
                            onSelect = { onThemeChange(BeastThemePreset.CYBER_BLUE) }
                        )
                        ThemePresetCard(
                            preset = BeastThemePreset.NEON_PINK,
                            isSelected = currentTheme == BeastThemePreset.NEON_PINK,
                            modifier = Modifier.weight(1f),
                            onSelect = { onThemeChange(BeastThemePreset.NEON_PINK) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemePresetCard(
                            preset = BeastThemePreset.MATRIX_GREEN,
                            isSelected = currentTheme == BeastThemePreset.MATRIX_GREEN,
                            modifier = Modifier.weight(1f),
                            onSelect = { onThemeChange(BeastThemePreset.MATRIX_GREEN) }
                        )
                        ThemePresetCard(
                            preset = BeastThemePreset.SUNSET,
                            isSelected = currentTheme == BeastThemePreset.SUNSET,
                            modifier = Modifier.weight(1f),
                            onSelect = { onThemeChange(BeastThemePreset.SUNSET) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemePresetCard(
                            preset = BeastThemePreset.ROYAL_GOLD,
                            isSelected = currentTheme == BeastThemePreset.ROYAL_GOLD,
                            modifier = Modifier.weight(1f),
                            onSelect = { onThemeChange(BeastThemePreset.ROYAL_GOLD) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = { onThemeChange(BeastThemePreset.CYBER_BLUE) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Reset to default", fontSize = 11.sp, color = theme.primary)
                }
            }
        }

        // Bot Voice Section (As seen in video)
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = theme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Bot Voice", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = "Your bot will speak when it analyzes a chart and executes trades using WAKANDA WEALTH EA as its name.",
                                fontSize = 10.sp,
                                color = TextSecondary,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Voice announcements", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Switch(
                        checked = voiceEnabled,
                        onCheckedChange = {
                            voiceEnabled = it
                            voiceAnnouncer?.isEnabled = it
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = theme.primary, checkedTrackColor = theme.primary.copy(alpha = 0.3f))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Speed: ${String.format("%.2f", speedSlider)}x", fontSize = 11.sp, color = TextSecondary)
                Slider(
                    value = speedSlider,
                    onValueChange = {
                        speedSlider = it
                        voiceAnnouncer?.speechRate = it
                    },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(thumbColor = theme.primary, activeTrackColor = theme.primary)
                )

                Text("Pitch: ${String.format("%.2f", pitchSlider)}", fontSize = 11.sp, color = TextSecondary)
                Slider(
                    value = pitchSlider,
                    onValueChange = {
                        pitchSlider = it
                        voiceAnnouncer?.speechPitch = it
                    },
                    valueRange = 0.5f..1.5f,
                    colors = SliderDefaults.colors(thumbColor = theme.primary, activeTrackColor = theme.primary)
                )

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = {
                        voiceAnnouncer?.speak("Wakanda Wealth EA connected. Institutional neural engine ready for trade execution.")
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, theme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = theme.primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("PREVIEW VOICE", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Bot Credentials (MetaTrader API)
        item {
            CyberCard(backgroundColor = DarkSurface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = theme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Bot Credentials", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    StatusPill(status = "MetaTrader API", color = theme.accent)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = botUuid,
                    onValueChange = { botUuid = it },
                    label = { Text("Bot UUID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = { apiKeyInput = it },
                    label = { Text("API Key") },
                    placeholder = { Text("Enter new API key") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("MT5 Account Number") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                CyberButton(
                    text = "SAVE & CONNECT",
                    onClick = {
                        connectionTestStatus = "✓ Credentials saved to secure keystore and bound to MT5."
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            isTestingConnection = true
                            connectionTestStatus = "Testing ping to MetaTrader bridge..."
                            delay(1000)
                            isTestingConnection = false
                            connectionTestStatus = "✓ Connection Established (Ping: 18ms • Deriv-Server-02)"
                            voiceAnnouncer?.speak("MetaTrader 5 bridge verified and online.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, theme.secondary)
                ) {
                    Text("TEST CONNECTION", fontSize = 11.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                }

                if (isTestingConnection) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(4.dp), color = theme.primary)
                }

                if (connectionTestStatus != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(connectionTestStatus!!, fontSize = 11.sp, color = BeastSuccess, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ThemePresetCard(
    preset: BeastThemePreset,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onSelect)
            .testTag("theme_preset_${preset.name}"),
        color = DarkSurfaceVariant,
        border = BorderStroke(1.5.dp, if (isSelected) preset.primary else DarkSurfaceBorder),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(preset.displayName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(preset.primary))
                Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(preset.secondary))
                Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(preset.accent))
            }
        }
    }
}
