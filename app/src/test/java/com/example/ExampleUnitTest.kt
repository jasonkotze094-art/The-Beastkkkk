package com.example

import com.example.data.BeastRepository
import com.example.model.CommandStatus
import com.example.model.CommandType
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testLicenseValidationFlow() {
        // 1. Valid Active Key
        val successResult = BeastRepository.validateAndBindLicense(
            keyInput = "BEAST-9921-ACTIVE-PRO",
            targetAccount = "MT5 #123456"
        )
        assertTrue(successResult is BeastRepository.LicenseValidationResult.Success)

        // 2. Expired Key
        val expiredResult = BeastRepository.validateAndBindLicense(
            keyInput = "BEAST-EXPIRED-TEST",
            targetAccount = "MT5 #123456"
        )
        assertTrue(expiredResult is BeastRepository.LicenseValidationResult.Rejected)
        assertTrue((expiredResult as BeastRepository.LicenseValidationResult.Rejected).reason.contains("EXPIRED"))

        // 3. Inactive Key
        val inactiveResult = BeastRepository.validateAndBindLicense(
            keyInput = "BEAST-INACTIVE-TEST",
            targetAccount = "MT5 #123456"
        )
        assertTrue(inactiveResult is BeastRepository.LicenseValidationResult.Rejected)
        assertTrue((inactiveResult as BeastRepository.LicenseValidationResult.Rejected).reason.contains("INACTIVE"))

        // 4. Invalid Key
        val invalidResult = BeastRepository.validateAndBindLicense(
            keyInput = "RANDOM-KEY-0000",
            targetAccount = "MT5 #123456"
        )
        assertTrue(invalidResult is BeastRepository.LicenseValidationResult.Rejected)
    }

    @Test
    fun testCurriculumQuestionBankCompleteness() {
        val questions = BeastRepository.questions.value
        assertEquals(42, questions.size)

        val mathCount = questions.count { it.subject == "Mathematics" }
        val physicsCount = questions.count { it.subject == "Physical Sciences" }
        val englishCount = questions.count { it.subject == "English" }

        assertEquals(18, mathCount)
        assertEquals(14, physicsCount)
        assertEquals(10, englishCount)
    }

    @Test
    fun testTradeCommandLifecycle() {
        val cmd = BeastRepository.dispatchCommand(
            symbol = "XAUUSD",
            type = CommandType.BUY,
            lotSize = 0.01,
            executionPrice = 2652.40
        )
        assertEquals(CommandStatus.PENDING, cmd.status)

        BeastRepository.updateCommandStatus(cmd.id, CommandStatus.RECEIVED)
        val receivedCmd = BeastRepository.commands.value.first { it.id == cmd.id }
        assertEquals(CommandStatus.RECEIVED, receivedCmd.status)

        BeastRepository.updateCommandStatus(cmd.id, CommandStatus.EXECUTED, profitLoss = 45.0)
        val executedCmd = BeastRepository.commands.value.first { it.id == cmd.id }
        assertEquals(CommandStatus.EXECUTED, executedCmd.status)
        assertEquals(45.0, executedCmd.profitLoss, 0.001)
    }
}
