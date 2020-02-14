package com.csci448.carterjfowler_A1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import android.widget.LinearLayout


private const val LOG_TAG = "448.QuizActivity"
private const val KEY_INDEX = "index"
private const val KEY_SCORE = "score"
private const val REQUEST_CODE_CHEAT = 0
private const val KEY_CHEAT = "cheat"
private const val KEY_NUM_CHEAT = "numCheat"

class QuizActivity : AppCompatActivity() {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var scoreTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var buttonD: Button
    private var numCheat = 0

    private var didCheat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")
        setContentView(R.layout.activity_quiz)

        val factory = QuizViewModelFactory()
        quizViewModel = ViewModelProvider(this@QuizActivity, factory)
            .get(QuizViewModel::class.java)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentQuestionIndex = currentIndex

        val currentScore = savedInstanceState?.getInt(KEY_SCORE, 0) ?: 0
        quizViewModel.score = currentScore

        didCheat = savedInstanceState?.getBoolean(KEY_CHEAT, false) ?: false
        numCheat = savedInstanceState?.getInt(KEY_NUM_CHEAT, 0) ?: 0

        scoreTextView = findViewById(R.id.score_text_view)
        questionTextView = findViewById(R.id.question_text_view)

        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)
        trueButton.setOnClickListener { checkAnswer(true, 'x', -1) }
        falseButton.setOnClickListener { checkAnswer(false, 'x', -1) }

        buttonA = findViewById(R.id.button_a)
        buttonB = findViewById(R.id.button_b)
        buttonC = findViewById(R.id.button_c)
        buttonD = findViewById(R.id.button_d)
        buttonA.setOnClickListener { checkAnswer(false, 'A', -1)}
        buttonB.setOnClickListener { checkAnswer(false, 'B', -1)}
        buttonC.setOnClickListener { checkAnswer(false, 'C', -1)}
        buttonD.setOnClickListener { checkAnswer(false, 'D', -1)}

        val submitButton: Button = findViewById(R.id.submit_button)
        submitButton.setOnClickListener { checkAnswer(false, 'x', R.id.fr_answer) }

        val previousButton: Button = findViewById(R.id.previous_button)
        val nextButton: Button = findViewById(R.id.next_button)
        previousButton.setOnClickListener { moveToQuestion(1)}
        nextButton.setOnClickListener { moveToQuestion(-1) }

        val cheatButton: Button = findViewById(R.id.cheat_button)
        cheatButton.setOnClickListener { launchCheat() }

        updateQuestion()
        updateView()
    }

    fun updateView() {
        val currentType = quizViewModel.getTypeAtIndex(quizViewModel.currentQuestionIndex)
        val tfView: LinearLayout = findViewById(R.id.tf_buttons)
        val mcView: LinearLayout = findViewById(R.id.mc_buttons)
        val frView: LinearLayout = findViewById(R.id.fr_button)

        if (currentType == questionType.TF) {
            tfView.visibility = View.VISIBLE
            mcView.visibility = View.GONE
            frView.visibility = View.GONE
        } else if (currentType == questionType.MC) {
            tfView.visibility = View.GONE
            mcView.visibility = View.VISIBLE
            frView.visibility = View.GONE
        } else if (currentType == questionType.FR) {
            tfView.visibility = View.GONE
            mcView.visibility = View.GONE
            frView.visibility = View.VISIBLE
        }
    }

    fun updateQuestion() {
        setCurrentScoreText()
        questionTextView.text = resources.getString( quizViewModel.currentQuestionTextId )
        if (quizViewModel.getTypeAtIndex(quizViewModel.currentQuestionIndex) == questionType.MC) {
            buttonA.text = resources.getString(quizViewModel.answerArray.get(0))
            buttonB.text = resources.getString(quizViewModel.answerArray.get(1))
            buttonC.text = resources.getString(quizViewModel.answerArray.get(2))
            buttonD.text = resources.getString(quizViewModel.answerArray.get(3))
        }
    }

    private fun setCurrentScoreText() {
        scoreTextView.text = quizViewModel.currentScore.toString()
    }

    private fun checkAnswer(userAnswerBool: Boolean, userAnswerChar: Char, userAnswerStringID: Int) {
        var userString: String = ""
        if (userAnswerStringID != -1) {
            val userEditText: EditText = findViewById(userAnswerStringID)
            userString = userEditText.text.toString()
        }
        if(quizViewModel.getAttemptBool(quizViewModel.currentQuestionIndex)) {
            Toast.makeText(baseContext, R.string.attemptedAlready, Toast.LENGTH_SHORT).show()
        } else {
            if (didCheat) {
                Toast.makeText(baseContext, R.string.judgement_toast, Toast.LENGTH_SHORT).show()
            } else if (quizViewModel.isAnswerCorrect(userAnswerBool, userAnswerChar, userString)) {
                Toast.makeText(baseContext, R.string.correct_toast, Toast.LENGTH_SHORT).show()
                setCurrentScoreText()
                quizViewModel.setAttemptBool(quizViewModel.currentQuestionIndex, true)
            } else {
                Toast.makeText(baseContext, R.string.incorrect_toast, Toast.LENGTH_SHORT).show()
                quizViewModel.setAttemptBool(quizViewModel.currentQuestionIndex, true)
            }
        }
    }

    private fun moveToQuestion(direction: Int) {
        if (direction > 0) {
            quizViewModel.moveToNextQuestion()
        } else if (direction < 0) {
            quizViewModel.moveToPreviousQuestion()
        }
        didCheat = quizViewModel.getCheated(quizViewModel.currentQuestionIndex)
        updateView()
        updateQuestion()
    }

    private fun launchCheat() {
        if(numCheat < 3) {
            val intent = CheatActivity.createIntent(
                baseContext,
                quizViewModel.getCurrentAnswer()
            )
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        } else {
            Toast.makeText(baseContext, R.string.cheatLimit, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHEAT) {
                if (data != null) {
                    didCheat =
                        CheatActivity.didUserCheat(data)
                    quizViewModel.setCheated(quizViewModel.currentQuestionIndex, didCheat)
                    if(didCheat) {
                        ++numCheat
                    }
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
        savedInstanceState.putInt(KEY_NUM_CHEAT, numCheat)
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }


}

