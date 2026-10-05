package com.example.creditosappandroidx.models.response

import com.example.creditosappandroidx.cswebservice.cuotas
import com.google.gson.annotations.SerializedName

data class CuotasResponse(
  @SerializedName("status") var status: String,
  @SerializedName("error") var error: Boolean,
  @SerializedName("errorMessage") var errorMessage: String,
  @SerializedName("data") var datos: ArrayList<cuotas>
)


