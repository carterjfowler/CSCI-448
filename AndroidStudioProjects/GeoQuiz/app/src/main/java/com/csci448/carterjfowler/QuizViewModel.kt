package com.csci448.carterjfowler

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val questionBank: MutableList<Question> = mutableListOf()
    public var score = 0
    public var currentQuestionIndex = 0

    init {
        Log.d(TAG, "ViewModel instance created")
        questionBank.add( Question(R.string.question1, false) )
        questionBank.add( Question(R.string.question2, true) )
        questionBank.add( Question(R.string.question3, true) )
        questionBank.add( Question(R.string.question4, false) )
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private val currentQuestion: Question
        get() = questionBank[currentQuestionIndex]

    val currentQuestionTextId: Int
        get() = currentQuestion.textResId
    val currentQuestionAnswer: Boolean
        get() = currentQuestion.isAnswerTrue
    val currentScore: Int
        get() = score

    fun isAnswerCorrect(answer: Boolean): Boolean {
        if (answer == currentQuestionAnswer) {
            ++score
            return true
        } else {
            return false
        }
    }

    fun getCurrentAnswer() = currentQuestion.isAnswerTrue

    fun moveToNextQuestion() {
        if (currentQuestionIndex < questionBank.size - 1) {
            ++currentQuestionIndex
        } else {
            currentQuestionIndex = 0
        }
    }
    fun moveToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            --currentQuestionIndex
        } else {
            currentQuestionIndex = questionBank.size - 1
        }
    }
}