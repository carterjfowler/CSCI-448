package edu.mines.csci448.lab.samodelkin.util

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.net.URL

private const val CHARACTER_DATA_API = "https://chargen-api.herokuapp.com/"
private const val CHARACTER_API_KEY = "apiCharacterData"

// TODO Step II.3
class CharacterWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val logTag = "448.CharWorker"

    override fun doWork(): Result {
        Log.d(logTag, "Work request was triggered")

        val urlResult = URL(CHARACTER_DATA_API).readText()

        Log.d(logTag, "Received result: " + urlResult)

        val outputData = workDataOf(CHARACTER_API_KEY to urlResult)
        return Result.success(outputData)
    }

    companion object {
        fun getApiData(outputData: Data) =
            outputData.getString(CHARACTER_API_KEY)
    }

}