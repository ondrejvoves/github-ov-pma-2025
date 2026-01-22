package com.example.myapp013aeducationgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,           // Text otázky
    val optionA: String,        // Možnost A
    val optionB: String,        // Možnost B
    val optionC: String,        // Možnost C
    val optionD: String,        // Možnost D
    val correctOptionIndex: Int, // Číslo 0, 1, 2 nebo 3 (podle toho, která je správně)
    val category: String
)