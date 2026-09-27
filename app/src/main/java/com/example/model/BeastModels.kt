package com.example.model

enum class EngineMode {
    EDUCATION,
    TRADING
}

// ---------------------------------------------------------------------------
// EDUCATION ENGINE MODELS
// ---------------------------------------------------------------------------

enum class ConceptPriority {
    HIGH,
    MEDIUM,
    LOW
}

enum class PlanStatus {
    TODO,
    IN_PROGRESS,
    COMPLETED
}

data class SubjectOverview(
    val name: String,
    val questionCount: Int,
    val conceptCount: Int,
    val highPriority: List<String>,
    val reviewAreas: List<String>,
    val averageScore: Int
)

data class Question(
    val id: String,
    val subject: String,
    val topic: String,
    val concept: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val priority: ConceptPriority = ConceptPriority.HIGH,
    var userSelectedOption: Int? = null
)

data class ExamScan(
    val id: String,
    val title: String,
    val subject: String,
    val dateScanned: String,
    val detectedQuestionsCount: Int,
    val detectedConceptsCount: Int,
    val score: Int,
    val status: String,
    val summary: String,
    val highPriorityFocus: List<String>,
    val reviewAreas: List<String>
)

data class ChatSource(
    val id: String,
    val name: String,
    val scorePercent: Int,
    val verifiedConcepts: Int,
    val sourceTitle: String
)

data class StudyPlanItem(
    val id: String,
    val subject: String,
    val topic: String,
    val concept: String,
    val priority: ConceptPriority,
    val hoursRecommended: Double,
    val status: PlanStatus,
    val targetDate: String,
    val notionPageId: String? = null
)

data class MistakeReviewItem(
    val id: String,
    val questionId: String,
    val subject: String,
    val topic: String,
    val concept: String,
    val questionText: String,
    val yourAnswer: String,
    val correctAnswer: String,
    val explanation: String,
    val priority: ConceptPriority
)

// ---------------------------------------------------------------------------
// TRADING ENGINE (EA NEXUS) MODELS
// ---------------------------------------------------------------------------

enum class RobotStatus {
    ACTIVE,
    PAUSED,
    STANDBY
}

enum class TradingMode(val label: String, val timeframe: String) {
    SCALP("Scalp", "M1-M15"),
    DAY("Day", "M15-H1"),
    SWING("Swing", "H4-D1")
}

enum class CommandType {
    BUY,
    SELL,
    START_AUTO,
    STOP_AUTO,
    CLOSE_ALL
}

enum class CommandStatus {
    PENDING,
    RECEIVED,
    EXECUTED,
    REJECTED
}

data class RobotEA(
    val id: String,
    val name: String,
    val mentor: String,
    val status: RobotStatus,
    val accountBound: String,
    val winRate: Double,
    val totalProfit: Double,
    val activeTrades: Int,
    val licenseKey: String
)

data class LicenseKey(
    val key: String,
    val isActive: Boolean,
    val isExpired: Boolean,
    val planName: String,
    val expirationDate: String,
    val boundAccounts: List<String>
)

data class TradeCommand(
    val id: String,
    val symbol: String,
    val type: CommandType,
    val lotSize: Double,
    val status: CommandStatus,
    val timestamp: Long,
    val executionPrice: Double,
    val profitLoss: Double = 0.0,
    val note: String = ""
)

data class ChartScanResult(
    val symbol: String,
    val mode: TradingMode,
    val bias: String,
    val confidence: Int,
    val entryPrice: Double,
    val takeProfit1: Double,
    val takeProfit2: Double,
    val stopLoss: Double,
    val riskReward: String,
    val marketStructure: String,
    val liquidityZone: String,
    val strategiesTested: Int,
    val reasoning: String,
    val searchGroundingNote: String? = null
)

enum class SessionStatus {
    OPEN,
    CLOSED,
    CLOSING_SOON
}

data class MarketSession(
    val name: String,
    val timezone: String,
    val status: SessionStatus,
    val activeHours: String,
    val volatilityLevel: String
)

data class OpenPosition(
    val id: String,
    val symbol: String,
    val type: CommandType,
    val lotSize: Double,
    val openPrice: Double,
    val currentPrice: Double,
    val pnl: Double,
    val openTime: String
)
