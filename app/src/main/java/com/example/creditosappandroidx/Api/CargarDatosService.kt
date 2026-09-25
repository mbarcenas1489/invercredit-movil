package com.example.creditosappandroidx.Api

import android.view.View
import android.widget.TextView
import com.example.creditosappandroidx.Api.Interfaces.InterfaceCargarDatos
import cswebservice.datospublicoskt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CargarDatosService {
  private fun getApi(): InterfaceCargarDatos = Retrofit.Builder()
    .baseUrl(datospublicoskt.GetUrlServer())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(InterfaceCargarDatos::class.java)

  fun ObtenerCreditos(view: View, textViewPorcentaje: TextView, textViewCantidad: TextView) {
    CargaPorLotes.cargar("créditos", view, textViewPorcentaje, textViewCantidad) {
      val body = CargaPorLotes.cuerpo(getApi().ObtenerCreditos(datospublicoskt.idCobrador.toInt()))
      check(!body.error) { body.errorMessage }
      body.datos
    }
  }

  fun ObtenerCuotas(view: View, textViewPorcentaje: TextView, textViewCantidad: TextView) {
    CargaPorLotes.cargar("cuotas", view, textViewPorcentaje, textViewCantidad) {
      val body = CargaPorLotes.cuerpo(getApi().ObtenerCuotas(datospublicoskt.idCobrador.toInt()))
      check(!body.error) { body.errorMessage }
      body.datos
    }
  }

  fun ObtenerMoras(view: View, textViewPorcentaje: TextView, textViewCantidad: TextView) {
    CargaPorLotes.cargar("moras", view, textViewPorcentaje, textViewCantidad) {
      val body = CargaPorLotes.cuerpo(getApi().ObtenerMoras(datospublicoskt.idCobrador.toInt()))
      check(!body.error) { body.errorMessage }
      body.datos
    }
  }
}
