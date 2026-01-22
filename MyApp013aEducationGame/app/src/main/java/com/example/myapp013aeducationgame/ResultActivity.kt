package com.example.myapp013aeducationgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("SCORE_KEY", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_KEY", 0)

        val tvFinalScore = findViewById<TextView>(R.id.tvFinalScore)
        tvFinalScore.text = "Správně: $score z $totalQuestions"

        val btnBack = findViewById<Button>(R.id.btnBackToMenu)
        btnBack.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}