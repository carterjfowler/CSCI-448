package com.csci448.carterjfowler_A1

class Question(val textResId: Int,
               val type: questionType,
               val tfAnswer: Boolean,
               val frAnswer: String,
               val mcAnswer: Char,
               val allAnswers: ArrayList<Int>) {
    var questionAttempted: Boolean = false
    var cheated: Boolean = false
}