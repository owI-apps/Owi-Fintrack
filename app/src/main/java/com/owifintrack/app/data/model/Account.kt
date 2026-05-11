package com.owifintrack.app.data.model

enum class AccountType { CASH, BANK, E_WALLET, CRYPTO, INVESTMENT, INSURANCE, OTHER_ASSET }

data class Account(
    val id: Int = 0,
    val name: String, // Contoh: "BCA", "GoPay", "Tunai"
    val type: AccountType,
    val initialBalance: Double = 0.0, // Saldo awal saat akun dibuat
    val currency: String = "IDR"
)