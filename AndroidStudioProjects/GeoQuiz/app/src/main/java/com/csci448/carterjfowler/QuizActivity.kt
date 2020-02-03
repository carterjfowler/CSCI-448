package com.csci448.carterjfowler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val LOG_TAG = "448.QuizActivity"
private const val KEY_INDEX = "index"

class QuizActivity : AppCompatActivity() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")
        setContentView(R.layout.activity_quiz)

//        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
//        quizViewModel.currentQuestionIndex = currentIndex

        val factory = QuizViewModelFactory()
        quizViewModel = ViewModelProvider(this@QuizActivity, factory)
            .get(QuizViewModel::class.java)

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
        questionTextView.text = resources.getString( quizViewModel.currentQuestionTextId )
    }

    private fun setCurrentScoreText() {
        scoreTextView.text = quizViewModel.currentScore.toString()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (quizViewModel.isAnswerCorrect(userAnswer)) {
            Toast.makeText(baseContext, R.string.correct_toast, Toast.LENGTH_SHORT).show()
            setCurrentScoreText()
        } else {
            Toast.makeText(baseContext, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveToQuestion(direction: Int) {
        if (direction > 0) {
            quizViewModel.moveToNextQuestion()
        } else if (direction < 0) {
            quizViewModel.moveToPreviousQuestion()
        }
        updateQuestion()
    }


    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

//    override fun onSaveInstanceState(savedInstanceState: Bundle) {
//        super.onSaveInstanceState(savedInstanceState)
//        Log.i(LOG_TAG, "onSaveInstanceState")
//        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentQuestionIndex)
//    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }


}

