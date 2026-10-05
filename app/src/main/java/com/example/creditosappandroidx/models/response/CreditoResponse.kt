package com.example.creditosappandroidx.models.response

import com.example.creditosappandroidx.cswebservice.creditocliente
import com.google.gson.annotations.SerializedName

data class CreditoResponse(
  @SerializedName("status") var status: String,
  @SerializedName("error") var error: Boolean,
  @SerializedName("errorMessage") var errorMessage: String,
  @SerializedName("data") var datos: ArrayList<creditocliente>
)


