package com.example.creditosappandroidx.Api

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.creditosappandroidx.Api.Interfaces.InterfaceCobrador
import com.google.android.material.snackbar.Snackbar
import cswebservice.datospublicoskt
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import org.jetbrains.anko.toast

object CobradorService {
  private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
      .baseUrl(datospublicoskt.GetUrlServer())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  fun ObtenerCobradores(view: View, textViewPorcentaje: TextView, textViewCantidad: TextView) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        try {
          val response = getRetrofit().create(InterfaceCobrador::class.java).ObtenerCobradores()

          if (response!!.isSuccessful && response.code() == 200) {
            var pos =0
            val data =  response.body()!!.datos

            for (item in data) {
              item.save()
              textViewPorcentaje.setText( (pos * 100 / data.size).toString())
              textViewCantidad.setText("$pos de ${data.size.toString()}")
              delay(10)
              pos+=1
            }
            textViewPorcentaje.setText("100")
            textViewCantidad.setText("${data.size.toString()} de ${data.size.toString()}")

          }
        } catch (e: Exception) {
        }
      }
    }
  }
}