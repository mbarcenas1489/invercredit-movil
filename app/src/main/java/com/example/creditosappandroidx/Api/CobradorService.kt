package com.example.creditosappandroidx.Api

import android.view.View
import android.widget.TextView
import com.example.creditosappandroidx.Api.Interfaces.InterfaceCobrador
import cswebservice.datospublicoskt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CobradorService {
  private fun getRetrofit(): Retrofit = Retrofit.Builder()
    .baseUrl(datospublicoskt.GetUrlServer())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  fun ObtenerCobradores(view: View, textViewPorcentaje: TextView, textViewCantidad: TextView) {
    CargaPorLotes.cargar("cobradores", view, textViewPorcentaje, textViewCantidad) {
      val body = CargaPorLotes.cuerpo(
        getRetrofit().create(InterfaceCobrador::class.java)
          .ObtenerCobradores()
      )
      check(!body.error) { "El servidor reportó un error al obtener los cobradores" }
      body.datos
    }
  }
}
