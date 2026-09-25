package com.example.creditosappandroidx.models.response

import com.google.gson.annotations.SerializedName

data class CuotaResponse(
  @SerializedName("status") var status: String,
  @SerializedName("cuotaId") var cuotaId: Int
)


