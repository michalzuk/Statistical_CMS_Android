package io.michalzuk.horton.models

import com.google.gson.annotations.SerializedName

data class TotalOrders(
        @SerializedName("slug")
        val slug : String,
        @SerializedName("name")
        val name : String,
        @SerializedName("total")
        val total : Int)