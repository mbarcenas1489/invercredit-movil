package com.example.creditosappandroidx.actividades.Cuotas.api

import android.view.View
import android.widget.TextView
import com.example.creditosappandroidx.Api.CargaPorLotes
import com.example.creditosappandroidx.models.planPago
import cswebservice.datospublicoskt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlanesPagoService {
  private fun getRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(datospublicoskt.GetUrlServer())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  fun ObtenerPlanesPagoPorCobrador(view: View, textViewPorcentaje: TextView, textViewCantidad: TextView) {
    CargaPorLotes.cargar("planes de pago", view, textViewPorcentaje, textViewCantidad) {
      val body = CargaPorLotes.cuerpo(getRetrofit().create(planesPagosInterface::class.java)
        .ObtenerPlanesPagoPorCobrador(datospublicoskt.idCobrador.toInt()))
      check(!body.error) { "El servidor reportó un error al obtener los planes de pago" }
      body.datos.map { plan ->
        planPago().apply {
          id = plan.id
          fecha_pago = plan.fecha_pago
          monto = plan.monto
          pagado = plan.pagado
          pendiente = plan.pendiente
          prestamoid = plan.prestamoid
        }
      }
    }
  }
}
