package com.carterjfowler.carterjfowler_a3.api

import com.google.gson.annotations.SerializedName

data class tempItem (
    @SerializedName("temp") var temp: Double = 0.0
)