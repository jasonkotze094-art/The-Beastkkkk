package com.example.ui.education

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeastRepository
import com.example.data.GeminiService
import com.example.data.VoiceAnnouncer
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun EducationEngineScreen(
    voiceAnnouncer: VoiceAnnouncer? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Exam Scan", "Simulator", "Mistakes", "Study Plan")

    val theme = LocalBeastTheme.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Sub-tabs row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = theme.primary,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = theme.primary
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("edu_tab_$index")
                )
            }
        }

        when (selectedTab) {
            0 -> ExamScanTab(voiceAnnouncer)
            1 -> ExamSimulatorTab(voiceAnnouncer)
            2 -> MistakeReviewTab(voiceAnnouncer)
            3 -> StudyPlanTab()
        }
    }
}

// ---------------------------------------------------------------------------
// 1. EXAM SCAN TAB
// ---------------------------------------------------------------------------
@Composable
fun ExamScanTab(voiceAnnouncer: VoiceAnnouncer?) {
    val theme = LocalBeastTheme.current
    val coroutineScope = rememberCoroutineScope()
    val scans by BeastRepository.examScans.collectAsState()

    var isScanning by remember { mutableStateOf(false) }
    var scanCompletedMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Scanner action card
            CyberCard(
                borderColor = theme.primary.copy(alpha = 0.4f),
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AI EXAM SCANNER",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "OCR + Neural Extraction: Questions & Concepts",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(theme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = theme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Subject overview chips
                Text(
                    text = "SUPPORTED CURRICULA & SUBJECTS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BeastRepository.subjectOverviews.forEach { subj ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, DarkSurfaceBorder),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(subj.name, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                                Text("${subj.questionCount} Questions", fontSize = 10.sp, color = theme.primary)
                                Text("${subj.conceptCount} Concepts", fontSize = 9.sp, color = TextSecondary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CyberButton(
                        text = if (isScanning) "ANALYZING PAPER..." else "SCAN EXAM PAPER",
                        onClick = {
                            coroutineScope.launch {
                                isScanning = true
                                kotlinx.coroutines.delay(1800)
                                val newScan = ExamScan(
                                    id = "scan-${UUID.randomUUID().toString().take(5)}",
                                    title = "Matric Final Simulation Paper",
                                    subject = "Mathematics, Physical Sciences, English",
                                    dateScanned = "Just now",
                                    detectedQuestionsCount = 42,
                                    detectedConceptsCount = 27,
                                    score = 88,
                                    status = "COMPLETED",
                                    summary = "Extracted 42 questions across 27 concepts. High priority: Algebra, Trigonometry, Chemical reactions, Forces & motion.",
                                    highPriorityFocus = listOf("Algebra", "Trigonometry", "Chemical reactions", "Forces & motion"),
                                    reviewAreas = listOf("Quadratics", "Newton's laws", "Organic chemistry")
                                )
                                BeastRepository.addExamScan(newScan)
                                isScanning = false
                                scanCompletedMessage = "Exam scanned! Detected 42 questions, 27 concepts."
                                voiceAnnouncer?.speak("Exam paper scanned successfully. 42 questions indexed.")
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isScanning,
                        testTag = "scan_exam_button"
                    )

                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                isScanning = true
                                kotlinx.coroutines.delay(1200)
                                isScanning = false
                                scanCompletedMessage = "Physical Sciences test paper imported."
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, theme.primary),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, tint = theme.primary)
                    }
                }

                if (isScanning) {
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = theme.primary,
                        trackColor = DarkSurfaceVariant
                    )
                }

                if (scanCompletedMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = scanCompletedMessage!!,
                        color = BeastSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item {
            Text(
                text = "EXAM SCAN HISTORY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        items(scans) { scan ->
            CyberCard(
                borderColor = DarkSurfaceBorder,
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(scan.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("${scan.subject} • ${scan.dateScanned}", fontSize = 11.sp, color = TextSecondary)
                    }
                    StatusPill(status = "${scan.score}%", color = if (scan.score >= 80) BeastSuccess else BeastWarning)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(scan.summary, fontSize = 11.sp, color = TextSecondary)

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Questions: ${scan.detectedQuestionsCount}", fontSize = 10.sp, color = theme.primary, fontWeight = FontWeight.Bold)
                    Text("Concepts: ${scan.detectedConceptsCount}", fontSize = 10.sp, color = theme.accent, fontWeight = FontWeight.Bold)
                    Text("Status: ${scan.status}", fontSize = 10.sp, color = BeastSuccess, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 2. EXAM SIMULATOR TAB
// ---------------------------------------------------------------------------
@Composable
fun ExamSimulatorTab(voiceAnnouncer: VoiceAnnouncer?) {
    val theme = LocalBeastTheme.current
    val questions by BeastRepository.questions.collectAsState()

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedSubjectFilter by remember { mutableStateOf("All") }
    var showExplanation by remember { mutableStateOf(false) }

    val filteredQuestions = remember(selectedSubjectFilter, questions) {
        if (selectedSubjectFilter == "All") questions
        else questions.filter { it.subject == selectedSubjectFilter }
    }

    val currentQ = filteredQuestions.getOrNull(currentIndex) ?: questions.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Filter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Mathematics", "Physical Sciences", "English").forEach { subj ->
                FilterChip(
                    selected = selectedSubjectFilter == subj,
                    onClick = {
                        selectedSubjectFilter = subj
                        currentIndex = 0
                        showExplanation = false
                    },
                    label = { Text(subj, fontSize = 11.sp) }
                )
            }
        }

        // Test Simulation Header
        CyberCard(
            borderColor = theme.primary.copy(alpha = 0.3f),
            backgroundColor = DarkSurface
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EXAM SIMULATOR",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Question ${currentIndex + 1} of ${filteredQuestions.size} (${currentQ.subject})",
                        fontSize = 11.sp,
                        color = theme.primary
                    )
                }
                PriorityBadge(priority = currentQ.priority)
            }

            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / filteredQuestions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = theme.primary,
                trackColor = DarkSurfaceVariant
            )
        }

        // Question Card
        CyberCard(
            borderColor = DarkSurfaceBorder,
            backgroundColor = DarkSurface
        ) {
            Text(
                text = "${currentQ.topic} • ${currentQ.concept}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = theme.accent
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentQ.questionText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Options
            currentQ.options.forEachIndexed { optIndex, optionText ->
                val isSelected = currentQ.userSelectedOption == optIndex
                val isCorrect = optIndex == currentQ.correctAnswerIndex
                val optBg = when {
                    showExplanation && isCorrect -> BeastSuccess.copy(alpha = 0.2f)
                    showExplanation && isSelected && !isCorrect -> BeastError.copy(alpha = 0.2f)
                    isSelected -> theme.primary.copy(alpha = 0.2f)
                    else -> DarkSurfaceVariant
                }
                val optBorder = when {
                    showExplanation && isCorrect -> BeastSuccess
                    showExplanation && isSelected && !isCorrect -> BeastError
                    isSelected -> theme.primary
                    else -> DarkSurfaceBorder
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            BeastRepository.updateQuestionAnswer(currentQ.id, optIndex)
                            showExplanation = true
                            if (optIndex == currentQ.correctAnswerIndex) {
                                voiceAnnouncer?.speak("Correct answer.")
                            }
                        }
                        .testTag("option_$optIndex"),
                    color = optBg,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, optBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) theme.primary else DarkBackground)
                                .border(1.dp, if (isSelected) theme.primary else TextMuted, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = listOf("A", "B", "C", "D").getOrElse(optIndex) { "?" },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else TextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = optionText,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (showExplanation) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkBackground,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, theme.primary.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = theme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("BEAST CONCEPT EXPLANATION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primary)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(currentQ.explanation, fontSize = 12.sp, color = TextPrimary, lineHeight = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            showExplanation = false
                        }
                    },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Previous", fontSize = 12.sp)
                }

                CyberButton(
                    text = if (currentIndex < filteredQuestions.size - 1) "NEXT QUESTION" else "COMPLETE TEST",
                    onClick = {
                        if (currentIndex < filteredQuestions.size - 1) {
                            currentIndex++
                            showExplanation = false
                        }
                    },
                    modifier = Modifier.widthIn(min = 140.dp),
                    testTag = "next_question_button"
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 3. MISTAKE REVIEW & ANALYTICS TAB
// ---------------------------------------------------------------------------
@Composable
fun MistakeReviewTab(voiceAnnouncer: VoiceAnnouncer?) {
    val theme = LocalBeastTheme.current
    val mistakes by BeastRepository.mistakes.collectAsState()
    val chatSources = BeastRepository.initialChatSources
    val coroutineScope = rememberCoroutineScope()

    var tutorResponse by remember { mutableStateOf<String?>(null) }
    var isTutorLoading by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Chat Sources Performance Breakdown (Exact values from user prompt)
        item {
            CyberCard(
                borderColor = theme.primary.copy(alpha = 0.35f),
                backgroundColor = DarkSurface
            ) {
                Text(
                    text = "CHAT SOURCES ACCURACY BREAKDOWN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                chatSources.forEach { chat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(chat.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextPrimary, modifier = Modifier.width(70.dp))
                        LinearProgressIndicator(
                            progress = { chat.scorePercent / 100f },
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = when {
                                chat.scorePercent >= 90 -> BeastSuccess
                                chat.scorePercent >= 75 -> theme.primary
                                else -> BeastWarning
                            },
                            trackColor = DarkSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${chat.scorePercent}%",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = when {
                                chat.scorePercent >= 90 -> BeastSuccess
                                chat.scorePercent >= 75 -> theme.primary
                                else -> BeastWarning
                            }
                        )
                    }
                }
            }
        }

        // Priority breakdown summary
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // High Priority Card
                Surface(
                    modifier = Modifier.weight(1f),
                    color = DarkSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BeastError.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BeastError))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("HIGH PRIORITY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BeastError)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Algebra", fontSize = 11.sp, color = TextPrimary)
                        Text("• Trigonometry", fontSize = 11.sp, color = TextPrimary)
                        Text("• Chemical reactions", fontSize = 11.sp, color = TextPrimary)
                        Text("• Forces & motion", fontSize = 11.sp, color = TextPrimary)
                    }
                }

                // Review Areas Card
                Surface(
                    modifier = Modifier.weight(1f),
                    color = DarkSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BeastWarning.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(BeastWarning))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("REVIEW AREAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BeastWarning)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Quadratics", fontSize = 11.sp, color = TextPrimary)
                        Text("• Newton's laws", fontSize = 11.sp, color = TextPrimary)
                        Text("• Organic chemistry", fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }
        }

        // Low-latency Gemini Concept Drill Card
        item {
            CyberCard(
                borderColor = theme.primary.copy(alpha = 0.5f),
                backgroundColor = DarkSurface
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = theme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LOW-LATENCY GEMINI 3.1 FLASH-LITE TUTOR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = theme.primary,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Request immediate deep-dive drilldown on any prioritized mistake concept.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Quadratics", "Newton's Laws", "Organic Chemistry").forEach { topic ->
                        AssistChip(
                            onClick = {
                                coroutineScope.launch {
                                    isTutorLoading = true
                                    tutorResponse = GeminiService.getLowLatencyAnswer("Explain high-frequency exam traps for: $topic")
                                    isTutorLoading = false
                                }
                            },
                            label = { Text(topic, fontSize = 10.sp) }
                        )
                    }
                }

                if (isTutorLoading) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(4.dp), color = theme.primary)
                }

                if (tutorResponse != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkBackground,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, theme.primary.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = tutorResponse!!,
                            fontSize = 11.sp,
                            color = TextPrimary,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "INDIVIDUAL MISTAKE LOG",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        items(mistakes) { item ->
            CyberCard(
                borderColor = BeastError.copy(alpha = 0.3f),
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${item.subject} • ${item.concept}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primary)
                    PriorityBadge(priority = item.priority)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(item.questionText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)

                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text("Your answer: ", fontSize = 11.sp, color = BeastError, fontWeight = FontWeight.Bold)
                    Text(item.yourAnswer, fontSize = 11.sp, color = TextPrimary)
                }
                Row {
                    Text("Correct answer: ", fontSize = 11.sp, color = BeastSuccess, fontWeight = FontWeight.Bold)
                    Text(item.correctAnswer, fontSize = 11.sp, color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Explanation: ${item.explanation}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// 4. STUDY PLAN TAB
// ---------------------------------------------------------------------------
@Composable
fun StudyPlanTab() {
    val theme = LocalBeastTheme.current
    val plans by BeastRepository.studyPlans.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var notionSyncSuccess by remember { mutableStateOf<String?>(null) }
    var searchGroundingResult by remember { mutableStateOf<Pair<String, List<String>>?>(null) }
    var isSearchLoading by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Notion Knowledge Sync Card
        item {
            CyberCard(
                borderColor = theme.primary.copy(alpha = 0.4f),
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NOTION STUDY KNOWLEDGE",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Automated sync to your Notion Study Workspace",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("N", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                CyberButton(
                    text = "EXPORT STUDY PLAN TO NOTION",
                    onClick = {
                        coroutineScope.launch {
                            notionSyncSuccess = "Syncing 4 modules to Notion workspace..."
                            kotlinx.coroutines.delay(1000)
                            notionSyncSuccess = "✓ Study plan & formula sheets synced to notion.so/thebeast/2026-matric"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "export_notion_button"
                )

                if (notionSyncSuccess != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(notionSyncSuccess!!, fontSize = 11.sp, color = BeastSuccess, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Google Search Grounding Card
        item {
            CyberCard(
                borderColor = Color(0xFF4285F4).copy(alpha = 0.5f),
                backgroundColor = DarkSurface
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TravelExplore, contentDescription = null, tint = Color(0xFF4285F4))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GEMINI 3.5 FLASH WITH GOOGLE SEARCH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4),
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Live grounding with current South African & Cambridge 2026 exam guidelines.",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            isSearchLoading = true
                            searchGroundingResult = GeminiService.getGroundedMarketNews("2026 Matric Exam Physics and Mathematics curriculum updates")
                            isSearchLoading = false
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFF4285F4)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("FETCH LATEST CURRICULUM DATA", color = TextPrimary, fontSize = 11.sp)
                }

                if (isSearchLoading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(4.dp), color = Color(0xFF4285F4))
                }

                if (searchGroundingResult != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkBackground,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF4285F4).copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(searchGroundingResult!!.first, fontSize = 11.sp, color = TextPrimary, lineHeight = 16.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Sources: " + searchGroundingResult!!.second.joinToString(", "), fontSize = 9.sp, color = Color(0xFF4285F4))
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "ACTIONABLE STUDY MILESTONES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )
        }

        items(plans) { plan ->
            CyberCard(
                borderColor = if (plan.status == PlanStatus.COMPLETED) BeastSuccess.copy(alpha = 0.3f) else DarkSurfaceBorder,
                backgroundColor = DarkSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${plan.subject} • ${plan.topic}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primary)
                        Text(plan.concept, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    StatusPill(
                        status = plan.status.name,
                        color = when (plan.status) {
                            PlanStatus.COMPLETED -> BeastSuccess
                            PlanStatus.IN_PROGRESS -> theme.primary
                            PlanStatus.TODO -> BeastWarning
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Target: ${plan.targetDate} (${plan.hoursRecommended} hrs)", fontSize = 11.sp, color = TextSecondary)
                    if (plan.status != PlanStatus.COMPLETED) {
                        TextButton(onClick = { BeastRepository.markPlanCompleted(plan.id) }) {
                            Text("Mark Done", fontSize = 11.sp, color = theme.primary)
                        }
                    }
                }
            }
        }
    }
}
