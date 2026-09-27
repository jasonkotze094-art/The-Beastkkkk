package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.BeastRepository
import com.example.ui.components.CyberButton
import com.example.ui.theme.*

@Composable
fun LicenseActivationDialog(
    onDismiss: () -> Unit,
    onSuccess: (accountBound: String) -> Unit
) {
    val theme = LocalBeastTheme.current

    var licenseKeyInput by remember { mutableStateOf("BEAST-9921-ACTIVE-PRO") }
    var accountInput by remember { mutableStateOf("MT5 #991042 - Deriv SVG") }
    var validationStep by remember { mutableStateOf("IDLE") } // IDLE, VALIDATING, ACTIVE_CHECK, EXPIRED_CHECK, BOUND, REJECTED
    var failureReason by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            color = DarkSurface,
            border = BorderStroke(1.5.dp, theme.primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "License",
                            tint = theme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add Robot via License Key",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "LICENSE KEY VALIDATION PIPELINE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = theme.accent,
                    letterSpacing = 1.sp
                )

                // Visual Workflow Flowchart
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkflowNode("Validate", validationStep != "IDLE")
                    Text("→", color = TextMuted, fontSize = 12.sp)
                    WorkflowNode("Active?", validationStep in listOf("ACTIVE_CHECK", "EXPIRED_CHECK", "BOUND", "REJECTED"))
                    Text("→", color = TextMuted, fontSize = 12.sp)
                    WorkflowNode("Expired?", validationStep in listOf("EXPIRED_CHECK", "BOUND", "REJECTED"))
                    Text("→", color = TextMuted, fontSize = 12.sp)
                    WorkflowNode("EA Bound", validationStep == "BOUND")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = licenseKeyInput,
                    onValueChange = {
                        licenseKeyInput = it
                        validationStep = "IDLE"
                    },
                    label = { Text("License Key (Format: BEAST-XXXX-...)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("license_key_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = accountInput,
                    onValueChange = { accountInput = it },
                    label = { Text("Target MT4 / MT5 Account") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("account_number_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primary,
                        unfocusedBorderColor = DarkSurfaceBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick test key chips
                Text(
                    text = "Presets for quick validation test:",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssistChip(
                        onClick = { licenseKeyInput = "BEAST-9921-ACTIVE-PRO" },
                        label = { Text("Active Key", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { licenseKeyInput = "BEAST-EXPIRED-TEST" },
                        label = { Text("Expired Key", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { licenseKeyInput = "BEAST-INACTIVE-TEST" },
                        label = { Text("Inactive Key", fontSize = 10.sp) }
                    )
                }

                if (validationStep == "BOUND") {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = BeastSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BeastSuccess)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BeastSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("EA ACTIVATED & BOUND", color = BeastSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Bound to: $accountInput", color = TextPrimary, fontSize = 11.sp)
                            }
                        }
                    }
                } else if (validationStep == "REJECTED") {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = BeastError.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BeastError)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = BeastError)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(failureReason, color = BeastError, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    CyberButton(
                        text = if (validationStep == "BOUND") "DONE" else "VALIDATE & BIND",
                        onClick = {
                            if (validationStep == "BOUND") {
                                onSuccess(accountInput)
                                onDismiss()
                            } else {
                                val result = BeastRepository.validateAndBindLicense(licenseKeyInput, accountInput)
                                when (result) {
                                    is BeastRepository.LicenseValidationResult.Success -> {
                                        validationStep = "BOUND"
                                    }
                                    is BeastRepository.LicenseValidationResult.Rejected -> {
                                        validationStep = "REJECTED"
                                        failureReason = result.reason
                                    }
                                }
                            }
                        },
                        testTag = "validate_bind_button"
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkflowNode(label: String, isActive: Boolean) {
    val theme = LocalBeastTheme.current
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isActive) theme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .border(1.dp, if (isActive) theme.primary else TextMuted, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) theme.primary else TextMuted
        )
    }
}
