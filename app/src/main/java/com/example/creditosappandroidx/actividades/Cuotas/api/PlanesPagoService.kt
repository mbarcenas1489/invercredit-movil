package com.example.creditosappandroidx.actividades.Cuotas.api

import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.creditosappandroidx.models.planPago
import com.google.android.material.snackbar.Snackbar
import cswebservice.datospublicoskt
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlanesPagoService {
  private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl(datospublicoskt.GetUrlServer())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  fun ObtenerPlanesPagoPorCobrador(view: View, textViewPorcentaje: TextView,textViewCantidad: TextView) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        try {
            val response =
            getRetrofit().create(planesPagosInterface::class.java)
              .ObtenerPlanesPagoPorCobrador(datospublicoskt.idCobrador.toInt())
          if (response.isSuccessful && response.code() == 200) {
            val data =  response.body()!!.datos

            var pos =0
            for (plan in response.body()!!.datos) {
              var pago = planPago()
              pago.id = plan.id
              pago.fecha_pago = plan.fecha_pago
              pago.monto = plan.monto
              pago.pagado = plan.pagado
              pago.pendiente = plan.pendiente
              pago.prestamoid = plan.prestamoid
              pago.save();
              textViewPorcentaje.setText( (pos * 100 / data.size).toString())
              textViewCantidad.setText("$pos de ${data.size.toString()}")
              delay(10)
              pos+=1
            }
            textViewPorcentaje.setText("100")
            textViewCantidad.setText("${data.size.toString()} de ${data.size.toString()}")
          }
          else {
            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
            Snackbar.make(view, jsonObj.getString("errorMessage"), Snackbar.LENGTH_LONG)
              .setAction("Action", null).show()
          }
        } catch (e: Exception) {
        }
      }
    }

  }


}