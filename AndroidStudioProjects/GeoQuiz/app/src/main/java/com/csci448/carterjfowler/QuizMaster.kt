package com.csci448.carterjfowler

object QuizMaster {
    private val theQuestion = Question(R.string.question1, false)
    private var score = 0

    private val currentQuestion: Question
        get() = theQuestion

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
}