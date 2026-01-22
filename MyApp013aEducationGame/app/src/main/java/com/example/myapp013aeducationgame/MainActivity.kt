package com.example.myapp013aeducationgame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupCategoryCard(R.id.cardSpiders, "Pavouci 🕷️", "pavouci")
        setupCategoryCard(R.id.cardSpace, "Vesmír 🚀", "vesmir")
        setupCategoryCard(R.id.cardHistory, "Dějepis ⚔️", "dejepis")
        setupCategoryCard(R.id.cardGeo, "Zeměpis 🌍", "zemepis")
        setupCategoryCard(R.id.cardBody, "Lidské tělo 🫀", "telo")
    }


    override fun onResume() {
        super.onResume()
        updateScores()
    }

    private fun setupCategoryCard(cardId: Int, title: String, categoryKey: String) {
        val card = findViewById<View>(cardId)
        val tvTitle = card.findViewById<TextView>(R.id.tvCategoryTitle)

        tvTitle.text = title

        card.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("CATEGORY_KEY", categoryKey)
            startActivity(intent)
        }
    }

    private fun updateScores() {
        val db = (application as MyApplication).database.quizDao()


        val cards = listOf(
            Triple(R.id.cardSpiders, "pavouci", 20),
            Triple(R.id.cardSpace, "vesmir", 20),
            Triple(R.id.cardHistory, "dejepis", 20),
            Triple(R.id.cardGeo, "zemepis", 20),
            Triple(R.id.cardBody, "telo", 20)
        )

        lifecycleScope.launch(Dispatchers.IO) {
            for ((cardId, key, maxScore) in cards) {

                val scoreObj = db.getCategoryScore(key)
                val currentScore = scoreObj?.bestScore ?: 0 // Pokud v DB není, je to 0


                withContext(Dispatchers.Main) {
                    val card = findViewById<View>(cardId)
                    val progressBar = card.findViewById<ProgressBar>(R.id.progressBar)
                    val tvProgress = card.findViewById<TextView>(R.id.tvProgressText)

                    progressBar.max = maxScore
                    progressBar.progress = currentScore
                    tvProgress.text = "Rekord: $currentScore / $maxScore"
                }
            }
        }
    }
}