package com.csci448.carterjfowler_A1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// QuizViewModelFactory.kt
class QuizViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor().newInstance()
    }
}