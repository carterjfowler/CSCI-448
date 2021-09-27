package com.carterjfowler.carterjfowler_a3.api

import com.google.gson.annotations.SerializedName

class WeatherResponse {
    @SerializedName("weather")
    lateinit var description: List<descriptionItem>
}