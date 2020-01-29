package com.csci448.carterjfowler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        scoreTextView = findViewById( R.id.score_text_view )
        questionTextView = findViewById( R.id.question_text_view)

        val trueButton: Button = findViewById( R.id.true_button )
        val falseButton: Button = findViewById( R.id.false_button )
        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }

        val previousButton: Button = findViewById( R.id.previous_button )
        val nextButton: Button = findViewById( R.id.next_button )
        previousButton.setOnClickListener { moveToQuestion(1)}
        nextButton.setOnClickListener { moveToQuestion(-1) }

        updateQuestion();
    }

    fun updateQuestion() {
        setCurrentScoreText()
        questionTextView.text = resources.getString( QuizMaster.currentQuestionTextId )
    }

    private fun setCurrentScoreText() {
        scoreTextView.text = QuizMaster.currentScore.toString()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (QuizMaster.isAnswerCorrect(userAnswer)) {
            Toast.makeText(baseContext, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            setCurrentScoreText()
        } else {
            Toast.makeText(baseContext, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveToQuestion(direction: Int) {
        if (direction > 0) {
            QuizMaster.moveToNextQuestion()
        } else if (direction < 0) {
            QuizMaster.moveToPreviousQuestion()
        }
        updateQuestion()
    }

}

