package com.csci448.carterjfowler

object QuizMaster {
    private val questionBank: MutableList<Question> = mutableListOf()
    private var score = 0
    private var currentQuestionIndex = 0;

    init {
        questionBank.add( Question(R.string.question1, false) )
        questionBank.add( Question(R.string.question2, true) )
        questionBank.add( Question(R.string.question3, true) )
        questionBank.add( Question(R.string.question4, false) )
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