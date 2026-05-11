package com.owifintrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType { INCOME, EXPENSE, TRANSFER }

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val accountId: Int = 0,
    val categoryId: Int = 0,
    val fromAccountId: Int = 0,
    val toAccountId: Int = 0
)