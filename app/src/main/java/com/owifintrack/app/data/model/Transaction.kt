package com.owifintrack.app.data.model

import java.util.Date

data class Transaction(
    val id: Int = 0,
    val title: String = "",
    val amount: Double = 0.0,
    val type: String = "EXPENSE", 
    val category: String = "",
    val date: Date = Date()
)
