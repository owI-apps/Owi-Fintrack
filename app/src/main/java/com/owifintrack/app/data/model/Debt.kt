package com.owifintrack.app.data.model

enum class DebtType { PAYABLE, RECEIVABLE } // Hutang (Payable), Piutang (Receivable)
enum class DebtStatus { ONGOING, PAID, OVERDUE }

data class Debt(
    val id: Int = 0,
    val personName: String, // Nama pihak terkait
    val type: DebtType,
    val totalAmount: Double,
    val remainingAmount: Double,
    val dueDate: Long, // Jatuh tempo
    val status: DebtStatus,
    val accountId: Int = 0, // Terhubung ke akun mana uangnya keluar/masuk
    val note: String = ""
)