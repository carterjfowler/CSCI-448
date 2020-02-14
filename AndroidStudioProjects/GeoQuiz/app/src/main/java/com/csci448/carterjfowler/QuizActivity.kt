package com.csci448.carterjfowler

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val LOG_TAG = "448.QuizActivity"
private const val KEY_INDEX = "index"
private const val KEY_SCORE = "score"
private const val REQUEST_CODE_CHEAT = 0
private const val KEY_CHEAT = "cheat"

class QuizActivity : AppCompatActivity() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView

    private var didCheat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")
        setContentView(R.layout.activity_quiz_tf)

        val factory = QuizViewModelFactory()
        quizViewModel = ViewModelProvider(this@QuizActivity, factory)
            .get(QuizViewModel::class.java)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentQuestionIndex = currentIndex

        val currentScore = savedInstanceState?.getInt(KEY_SCORE, 0) ?: 0
        quizViewModel.score = currentScore

        didCheat = savedInstanceState?.getBoolean(KEY_CHEAT, false) ?: false

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

        val cheatButton: Button = findViewById( R.id.cheat_button )
        cheatButton.setOnClickListener { launchCheat() }

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
        if (didCheat) {
            Toast.makeText(baseContext, R.string.judgement_toast, Toast.LENGTH_SHORT).show()
        } else if (quizViewModel.isAnswerCorrect(userAnswer)) {
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
        didCheat = false
        updateQuestion()
    }

    private fun launchCheat() {
        val intent = CheatActivity.createIntent(baseContext, quizViewModel.getCurrentAnswer() )
        startActivityForResult(intent, REQUEST_CODE_CHEAT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHEAT) {
                if (data != null) {
                    didCheat = CheatActivity.didUserCheat(data)
                }
            }
        }
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(LOG_TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentQuestionIndex)
        savedInstanceState.putInt(KEY_SCORE, quizViewModel.score)
        savedInstanceState.putBoolean(KEY_CHEAT, didCheat)
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }


}

