package com.example.creditosappandroidx.Api.Interfaces


import com.example.creditosappandroidx.models.response.CreditoResponse
import com.example.creditosappandroidx.models.response.CuotasResponse
import com.example.creditosappandroidx.models.response.MorasResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InterfaceCargarDatos {
  @GET("api/credito_servicio/{idcobrador}/getPrestamoCliente")
  suspend fun ObtenerCreditos(@Path("idcobrador") cobrador: Int?): Response<CreditoResponse>

  @GET("api/credito_servicio/{idcobrador}/getCuotasPorCobrador")
  suspend fun ObtenerCuotas(@Path("idcobrador") cobrador: Int?): Response<CuotasResponse>

  @GET("api/credito_servicio/{idcobrador}/getMorasPorCobrador")
  suspend fun ObtenerMoras(@Path("idcobrador") cobrador: Int?): Response<MorasResponse>
}