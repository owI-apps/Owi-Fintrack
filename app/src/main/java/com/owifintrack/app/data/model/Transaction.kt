package com.owifintrack.app.data.model

enum class TransactionType { INCOME, EXPENSE, TRANSFER }

data class Transaction(
    val id: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val date: Long = System.currentTimeMillis(), // Gunakan Long untuk akurasi waktu
    val note: String = "",
    
    // Relasi untuk Income/Expense
    val accountId: Int = 0, // Dari akun mana uang keluar/masuk
    val categoryId: Int = 0, // Untuk kategori apa
    
    // Relasi khusus untuk Transfer
    val fromAccountId: Int = 0, // Dari akun sumber
    val toAccountId: Int = 0    // Ke akun tujuan
)