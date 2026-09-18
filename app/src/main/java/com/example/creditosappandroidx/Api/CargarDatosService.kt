package com.example.creditosappandroidx.Api

import android.view.View
import android.widget.TextView
import com.example.creditosappandroidx.Api.Interfaces.InterfaceCargarDatos
import com.google.android.material.snackbar.Snackbar
import cswebservice.datospublicoskt
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CargarDatosService {
  private fun getRetrofit(): Retrofit {

    return Retrofit.Builder()
      .baseUrl(datospublicoskt.GetUrlServer())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  fun ObtenerCreditos(view: View, textViewPorcentaje: TextView,textViewCantidad: TextView ) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        try {
          val response = getRetrofit().create(InterfaceCargarDatos::class.java).ObtenerCreditos(datospublicoskt.idCobrador.toInt())
          if (response.isSuccessful) {
            val data =  response.body()!!.datos

            var pos =0
            for (item in data) {
              textViewPorcentaje.setText( (pos * 100 / data.size).toString())
              textViewCantidad.setText("$pos de ${data.size.toString()}")
              pos+=1
              item.save()
              delay(20)
            }
            textViewPorcentaje.setText("100")
            textViewCantidad.setText("${data.size.toString()} de ${data.size.toString()}")

          } else {
            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
            Snackbar.make(view, jsonObj.getString("errorMessage"), Snackbar.LENGTH_LONG)
              .setAction("Action", null).show()
          }
        } catch (e: Exception) {
        }
      }
    }
  }
  fun ObtenerCuotas(view: View, textViewPorcentaje: TextView,textViewCantidad: TextView) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        try {
          val response = getRetrofit().create(InterfaceCargarDatos::class.java).ObtenerCuotas(datospublicoskt.idCobrador.toInt())
          if (response.isSuccessful) {
            val data =  response.body()!!.datos
            var pos =0
            for (item in data) {
              textViewPorcentaje.setText( (pos * 100 / data.size).toString())
              textViewCantidad.setText("$pos de ${data.size.toString()}")
              delay(10)
              pos+=1
              item.save()
            }
            textViewPorcentaje.setText("100")
            textViewCantidad.setText("${data.size.toString()} de ${data.size.toString()}")
          } else {
            val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
            Snackbar.make(view, jsonObj.getString("errorMessage"), Snackbar.LENGTH_LONG)
              .setAction("Action", null).show()
          }
        } catch (e: Exception) {
        }
      }
    }
  }
  fun ObtenerMoras(view: View, textViewPorcentaje: TextView,textViewCantidad: TextView) {
    CoroutineScope(Dispatchers.IO).launch {
      withContext(Dispatchers.Main) {
        try {
          val response = getRetrofit().create(InterfaceCargarDatos::class.java).ObtenerMoras(datospublicoskt.idCobrador.toInt())
          if (response.isSuccessful) {
            val data =  response.body()!!.datos
            var pos =0
            for (item in data) {
              textViewPorcentaje.setText( (pos * 100 / data.size).toString())
              textViewCantidad.setText("$pos de ${data.size.toString()}")
              delay(10)
              pos+=1
              item.save()
            }
            textViewPorcentaje.setText("100")
            textViewCantidad.setText("${data.size.toString()} de ${data.size.toString()}")
          } else {
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