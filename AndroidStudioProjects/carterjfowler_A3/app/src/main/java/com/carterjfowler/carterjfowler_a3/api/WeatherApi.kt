package com.carterjfowler.carterjfowler_a3.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    //weather?lat=${latString}&lon=${lonString}&appid=${apiKey}/
//    @GET ("/")
//    fun fetchContents() : Call<String>

    @GET ("weather?")
    fun fetchTemp(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") apiKey: String): Call<MainResponse>
    @GET ("weather?")
    fun fetchDescription(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") apiKey: String): Call<WeatherResponse>
}