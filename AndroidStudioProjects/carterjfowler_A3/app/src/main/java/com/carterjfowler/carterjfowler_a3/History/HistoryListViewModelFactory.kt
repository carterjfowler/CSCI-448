package com.carterjfowler.carterjfowler_a3.History

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carterjfowler.carterjfowler_a3.Data.LocationRepository

class HistoryListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LocationRepository::class.java)
            .newInstance(LocationRepository.getInstance(context))
    }

}