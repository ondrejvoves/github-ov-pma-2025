package com.example.myapp013aeducationgame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {
    // Načtení otázek
    @Query("SELECT * FROM questions WHERE category = :categoryName")
    suspend fun getQuestionsByCategory(categoryName: String): List<Question>

    @Insert
    suspend fun insertQuestions(questions: List<Question>)




    @Query("SELECT * FROM category_scores WHERE categoryId = :id LIMIT 1")
    suspend fun getCategoryScore(id: String): CategoryScore?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateScore(score: CategoryScore)
}