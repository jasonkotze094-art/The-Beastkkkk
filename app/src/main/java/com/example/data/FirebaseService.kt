package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.ExamScan
import com.example.model.TradeCommand
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FirebaseService {
    private const val TAG = "BeastFirebase"

    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val currentUser: FirebaseUser?
        get() = try {
            auth.currentUser
        } catch (_: Exception) {
            null
        }

    suspend fun signInAnonymouslyOrCheck(): FirebaseUser? {
        return try {
            if (auth.currentUser != null) {
                auth.currentUser
            } else {
                val result = auth.signInAnonymously().await()
                result.user
            }
        } catch (e: Exception) {
            Log.w(TAG, "Anonymous sign in skipped or offline: ${e.message}")
            null
        }
    }

    suspend fun syncExamScanToFirestore(scan: ExamScan) {
        val uid = currentUser?.uid ?: "guest_beast_user"
        try {
            val docData = hashMapOf(
                "id" to scan.id,
                "title" to scan.title,
                "subject" to scan.subject,
                "dateScanned" to scan.dateScanned,
                "detectedQuestionsCount" to scan.detectedQuestionsCount,
                "detectedConceptsCount" to scan.detectedConceptsCount,
                "score" to scan.score,
                "status" to scan.status,
                "summary" to scan.summary,
                "highPriorityFocus" to scan.highPriorityFocus,
                "reviewAreas" to scan.reviewAreas,
                "syncedAt" to System.currentTimeMillis()
            )
            firestore.collection("users")
                .document(uid)
                .collection("exam_scans")
                .document(scan.id)
                .set(docData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.d(TAG, "Firestore sync note: ${e.message}")
        }
    }

    suspend fun syncTradeCommandToFirestore(command: TradeCommand) {
        val uid = currentUser?.uid ?: "guest_beast_user"
        try {
            val docData = hashMapOf(
                "id" to command.id,
                "symbol" to command.symbol,
                "type" to command.type.name,
                "lotSize" to command.lotSize,
                "status" to command.status.name,
                "executionPrice" to command.executionPrice,
                "profitLoss" to command.profitLoss,
                "timestamp" to command.timestamp,
                "note" to command.note
            )
            firestore.collection("users")
                .document(uid)
                .collection("commands")
                .document(command.id)
                .set(docData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            Log.d(TAG, "Firestore command sync note: ${e.message}")
        }
    }
}
