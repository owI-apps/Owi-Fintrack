package com.owifintrack.app.data.model

data class DebtPayment(
    val id: Int = 0,
    val debtId: Int, // Terhubung ke Debt mana
    val amount: Double, // Nominal pembayaran kali ini
    val date: Long,
    val note: String = ""
)