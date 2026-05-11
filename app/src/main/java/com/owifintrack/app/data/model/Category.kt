package com.owifintrack.app.data.model

enum class CategoryType { INCOME, EXPENSE }

data class Category(
    val id: Int = 0,
    val name: String, // Contoh: "Gaji", "Makanan"
    val type: CategoryType,
    val iconName: String = "" // Nanti untuk ikon di UI
)