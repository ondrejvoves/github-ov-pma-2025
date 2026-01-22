package com.example.myapp013aeducationgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_scores")
data class CategoryScore(
    @PrimaryKey val categoryId: String,
    val bestScore: Int
)