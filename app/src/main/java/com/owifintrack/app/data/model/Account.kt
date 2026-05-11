package com.owifintrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AccountType { CASH, BANK, E_WALLET, CRYPTO, INVESTMENT, INSURANCE, OTHER_ASSET }

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: AccountType,
    val initialBalance: Double = 0.0,
    val currency: String = "IDR"
)