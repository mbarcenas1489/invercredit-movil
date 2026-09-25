package com.example.creditosappandroidx.models.response
import com.example.creditosappandroidx.models.cobrador
import com.google.gson.annotations.SerializedName

 data class CobradorResponse(
   @SerializedName("status") var status: String,
   @SerializedName("error") var error: Boolean,
   @SerializedName("errorMessage") var errorMessage: Boolean,
   @SerializedName("data") var datos: ArrayList<cobrador>
)


