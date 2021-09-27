package com.carterjfowler.carterjfowler_a3.api

import com.google.gson.annotations.SerializedName

class MainResponse {
    @SerializedName("main")
    lateinit var temp: tempItem
}