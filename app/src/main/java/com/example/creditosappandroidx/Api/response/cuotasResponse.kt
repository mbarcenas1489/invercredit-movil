package com.example.creditosappandroidx.Api.response

import com.example.creditosappandroidx.cswebservice.cuotas

data class cuotasResponse(
  val status: String,
  val data: ArrayList<cuotas>,
)
