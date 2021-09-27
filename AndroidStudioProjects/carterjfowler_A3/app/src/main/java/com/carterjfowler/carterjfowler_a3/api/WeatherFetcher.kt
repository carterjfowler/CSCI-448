package com.carterjfowler.carterjfowler_a3.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class WeatherFetcher {

    val logTag = "448.WeatherFetcher"
    private val weatherApi: WeatherApi
    private val retrofit: Retrofit
    val apiKey = "c90fff8cc2e6479305e8934628d04190"

    init {
        retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(GsonConverterFactory.create()).build()
        weatherApi = retrofit.create(WeatherApi::class.java)
    }

//    fun fetchContents(longitude: Double, latitude: Double): LiveData<String> {
//        val responseLiveData: MutableLiveData<String> = MutableLiveData()
//        val weatherPageRequest: Call<String> = weatherApi.fetchContents(latitude, longitude, apiKey)
//
//        weatherPageRequest.enqueue(object: Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.e(logTag, "Failed to fetch weather", t)
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                Log.d(logTag, "Response received")
//                responseLiveData.value = response.body()
//            }
//        })
//
//        return responseLiveData
//    }
    fun fetchTemp(longitude: Double, latitude: Double): LiveData<tempItem> {
        val responseLiveData: MutableLiveData<tempItem> = MutableLiveData()
        val weatherPageRequest: Call<MainResponse> = weatherApi.fetchTemp(latitude, longitude, apiKey)

        weatherPageRequest.enqueue(object: Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                Log.e(logTag, "Failed to fetch weather", t)
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                Log.d(logTag, "Response received")
                val mainResponse: MainResponse? = response.body()
                val temp: tempItem? = mainResponse?.temp ?: null
                responseLiveData.value = temp
            }
        })

        return responseLiveData
    }

    fun fetchDescription(longitude: Double, latitude: Double): LiveData<List<descriptionItem>> {
        val responseLiveData: MutableLiveData<List<descriptionItem>> = MutableLiveData()
        val weatherPageRequest: Call<WeatherResponse> = weatherApi.fetchDescription(latitude, longitude, apiKey)

        weatherPageRequest.enqueue(object: Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e(logTag, "Failed to fetch weather", t)
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d(logTag, "Response received")
                val weatherResponse: WeatherResponse? = response.body()
                var description: List<descriptionItem>? = weatherResponse?.description ?: mutableListOf()
                description = description?.filterNot {
                    it.description.isBlank()
                }
                responseLiveData.value = description
            }
        })

        return responseLiveData
    }

}
