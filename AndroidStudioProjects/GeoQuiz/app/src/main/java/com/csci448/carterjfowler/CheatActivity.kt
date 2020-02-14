package com.csci448.carterjfowler

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

private const val LOG_TAG = "448.CheatActivity"
private const val EXTRA_ANSWER_IS_TRUE = "CORRECT_ANSWER_KEY"
const val EXTRA_ANSWER_SHOWN = "com.CSCI448.android.geoquiz.answer_shown"
private const val KEY_CHEAT = "cheat"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        Log.d(LOG_TAG, "onCreate() called")
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        val showAnswerButton: Button = findViewById( R.id.show_answer_button )
        showAnswerButton.setOnClickListener { showAnswer() }
    }

    companion object {
        fun createIntent(packageContext: Context, isAnswerTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE, isAnswerTrue)
            }
        }
        fun didUserCheat(intent: Intent): Boolean {
            return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }

    private fun showAnswer() {
        val answer: TextView = findViewById( R.id.answer_text_view )
        val answerText = when {
            answerIsTrue -> R.string.True
            else -> R.string.False
        }
        answer.setText(answerText)
        answer.visibility = TextView.VISIBLE
        setCheated()
    }

    private fun setCheated() {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data)
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

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }
}
