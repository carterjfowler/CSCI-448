package com.carterjfowler.carterjfowler_a3.api

import com.google.gson.annotations.SerializedName

data class descriptionItem(
    @SerializedName("description") var description: String = ""
)