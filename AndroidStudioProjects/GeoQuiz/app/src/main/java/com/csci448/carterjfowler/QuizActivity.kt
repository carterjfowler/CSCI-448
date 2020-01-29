package com.csci448.carterjfowler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class QuizActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        scoreTextView = findViewById( R.id.score_text_view )

        updateQuestion();
    }

    fun updateQuestion() {
        scoreTextView.text = QuizMaster.currentScore.toString()
    }
}
