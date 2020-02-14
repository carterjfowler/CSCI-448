package com.csci448.carterjfowler_A1

import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci448.carterjfowler_A1.R

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val questionBank: MutableList<Question> = mutableListOf()
    var score = 0
    var currentQuestionIndex = 0

    init {
        // val textResId: Int,
        // val type: questionType,
        // val tfAnswer: Boolean,
        // val frAnswer: String,
        // val mcAnswer: Char,
        // val allAnswers: ArrayList<Int>
        Log.d(TAG, "ViewModel instance created")
        questionBank.add(
            Question(
                R.string.question1,
                questionType.TF,
                false,
                "",
                'x',
                ArrayList()
            )
        )
        questionBank.add(
            Question(
                R.string.question2,
                questionType.TF,
                true,
                "",
                'x',
                ArrayList()
            )
        )
        questionBank.add(
            Question(
                R.string.question3,
                questionType.TF,
                true,
                "",
                'x',
                ArrayList()
            )
        )
        questionBank.add(
            Question(
                R.string.question4,
                questionType.TF,
                false,
                "",
                'x',
                ArrayList()
            )
        )
        questionBank.add(
            Question(
                R.string.question5,
                questionType.MC,
                false,
                "",
                'B',
                arrayListOf(R.string.q5_A, R.string.q5_B, R.string.q5_C, R.string.q5_D)
            )
        )
        questionBank.add(
            Question(
                R.string.question6,
                questionType.FR,
                false,
                "PCJ",
                'x',
                ArrayList()
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private val currentQuestion: Question
        get() = questionBank[currentQuestionIndex]

    val currentQuestionTextId: Int
        get() = currentQuestion.textResId
    val currentQuestionAnswerTF: Boolean
        get() = currentQuestion.tfAnswer
    val currentQuestionAnswerMC: Char
        get() = currentQuestion.mcAnswer
    val currentQuestionAnswerFR: String
        get() = currentQuestion.frAnswer
    val currentScore: Int
        get() = score
    val answerArray: ArrayList<Int>
        get() = currentQuestion.allAnswers

    fun getTypeAtIndex(index: Int): questionType {
        return questionBank.get(index).type
    }

    fun getAttemptBool (index: Int): Boolean {
        return questionBank.get(index).questionAttempted
    }

    fun setAttemptBool (index: Int, attempted: Boolean) {
        questionBank.get(index).questionAttempted = attempted
    }

    fun getCheated (index: Int): Boolean {
        return questionBank.get(index).cheated
    }

    fun setCheated (index: Int, cheated: Boolean) {
        questionBank.get(index).cheated = cheated
    }

    fun isAnswerCorrect(answerTF: Boolean, answerMC: Char, answerFR: String): Boolean {
        if (currentQuestion.type == questionType.TF) {
            if (answerTF == currentQuestionAnswerTF) {
                ++score
                return true
            } else {
                return false
            }
        } else if (currentQuestion.type == questionType.MC) {
            if (answerMC.toLowerCase() == currentQuestionAnswerMC.toLowerCase()) {
                ++score
                return true
            } else {
                return false
            }
        } else if (currentQuestion.type == questionType.FR) {
            if (answerFR.equals(currentQuestionAnswerFR, true)) {
                ++score
                return true
            } else {
                return false
            }
        }
        return true
    }

    fun getCurrentAnswer(): String {
        if (currentQuestion.type == questionType.TF) {
            return currentQuestionAnswerTF.toString()
        } else if (currentQuestion.type == questionType.MC) {
            return currentQuestionAnswerMC.toString()
        } else if (currentQuestion.type == questionType.FR) {
            return currentQuestionAnswerFR
        }
        return ""
    }

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