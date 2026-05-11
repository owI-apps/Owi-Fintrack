package com.owifintrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DebtType { PAYABLE, RECEIVABLE }
enum class DebtStatus { ONGOING, PAID, OVERDUE }

@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val personName: String,
    val type: DebtType,
    val totalAmount: Double,
    val remainingAmount: Double,
    val dueDate: Long,
    val status: DebtStatus,
    val accountId: Int = 0,
    val note: String = ""
)