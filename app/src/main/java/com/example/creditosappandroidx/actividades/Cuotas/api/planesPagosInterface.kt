package com.example.creditosappandroidx.actividades.Cuotas.api

import com.example.creditosappandroidx.models.response.PlanesPagoResponse
import retrofit2.Response
import retrofit2.http.*

interface planesPagosInterface {
  @GET("api/planPago/ObtenerPlanPagoActivoPorCobrador/{idCobrador}")
  suspend fun ObtenerPlanesPagoPorCobrador(@Path("idCobrador") idCobrador: Int) : Response<PlanesPagoResponse>
}