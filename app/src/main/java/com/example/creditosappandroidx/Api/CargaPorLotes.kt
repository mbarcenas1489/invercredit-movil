package com.example.creditosappandroidx.Api

import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.creditosappandroidx.cssqlite.dbprestamo
import com.google.android.material.snackbar.Snackbar
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.structure.BaseModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

internal object CargaPorLotes {
  private const val TAMANO_LOTE = 500

  fun <T : BaseModel> cargar(
    nombre: String,
    view: View,
    porcentaje: TextView,
    cantidad: TextView,
    obtenerDatos: suspend () -> List<T>
  ) = CoroutineScope(Dispatchers.IO).launch {
    try {
      withContext(Dispatchers.Main) {
        porcentaje.text = "0"
        cantidad.text = "Descargando..."
      }
      val datos = obtenerDatos()
      val database = FlowManager.getDatabase(dbprestamo::class.java)
      var guardados = 0
      while (guardados < datos.size) {
        ensureActive()
        val fin = minOf(guardados + TAMANO_LOTE, datos.size)
        // No suspender dentro de la transacción: todas sus escrituras usan el mismo hilo.
        database.executeTransaction { wrapper ->
          for (indice in guardados until fin) {
            ensureActive()
            check(datos[indice].save(wrapper)) {
              "No se pudo guardar el registro ${indice + 1} de $nombre"
            }
          }
        }
        // Informar solamente registros cuya transacción ya fue confirmada.
        guardados = fin
        withContext(Dispatchers.Main) {
          porcentaje.text = (guardados.toLong() * 100 / datos.size).toString()
          cantidad.text = "$guardados de ${datos.size}"
        }
      }
      if (datos.isEmpty()) {
        withContext(Dispatchers.Main) {
          porcentaje.text = "100"
          cantidad.text = "0 de 0"
        }
      }
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      Log.e("CargaPorLotes", "Error al cargar $nombre", e)
      withContext(Dispatchers.Main) {
        cantidad.text = "Error al cargar $nombre"
        Snackbar.make(
          view, "Error al cargar $nombre: ${e.message ?: "error inesperado"}",
          Snackbar.LENGTH_LONG
        ).show()
      }
    }
  }

  fun <T : Any> cuerpo(response: Response<out T?>?): T {
    checkNotNull(response) { "El servidor no devolvió una respuesta" }
    if (!response.isSuccessful) {
      val detalle = response.errorBody()?.string().orEmpty()
      val mensaje = runCatching { JSONObject(detalle).optString("errorMessage") }
        .getOrDefault("").ifBlank { "HTTP ${response.code()}" }
      error(mensaje)
    }
    return checkNotNull(response.body()) { "El servidor devolvió una respuesta vacía" }
  }
}
