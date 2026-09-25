package com.example.creditosappandroidx.Api.Interfaces

import com.example.creditosappandroidx.models.response.CobradorResponse
import retrofit2.Response
import retrofit2.http.GET

interface InterfaceCobrador {
  @GET("api/cobrador/GetAll")
  suspend fun ObtenerCobradores(): Response<CobradorResponse?>?
}