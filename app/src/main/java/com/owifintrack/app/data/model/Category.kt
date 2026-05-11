package com.owifintrack.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CategoryType { INCOME, EXPENSE }

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: CategoryType,
    val iconName: String = ""
)