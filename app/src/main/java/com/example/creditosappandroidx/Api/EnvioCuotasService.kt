package com.example.creditosappandroidx.Api

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.creditosappandroidx.cssqlite.EnvioCuotasLocal
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel
import com.example.creditosappandroidx.models.response.CuotasResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

internal fun envioCuotasConfirmado(response: Response<CuotasResponse>): Boolean {
  val body = response.body()
  // El backend actual inserta el lote antes de devolver status=ok. data es toda
  // la cartera, no una correspondencia de IDs; puede estar vacía legítimamente.
  return response.isSuccessful && body != null && body.status == "ok" &&
    !body.error && body.datos != null
}

object EnvioCuotasService {
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
  private val ocupado = AtomicBoolean(false)
  private val estadoMutable = MutableLiveData("")
  val estado: LiveData<String> = estadoMutable
  val enCurso: Boolean get() = ocupado.get()

  // Se llama desde el hilo principal. El trabajo continúa aunque se recree la pantalla.
  fun enviar(context: Context): Boolean {
    if (!ocupado.compareAndSet(false, true)) return false
    val appContext = context.applicationContext
    estadoMutable.value = "Enviando cuotas..."
    scope.launch {
      var reservado = false
      val mensaje = try {
        val servicio = crudWebservice_laravel(appContext)
        val lote = EnvioCuotasLocal.reservarPendientes()
        reservado = lote.isNotEmpty()
        if (lote.isEmpty()) {
          "No hay cuotas pendientes de envío."
        } else {
          val response = servicio.crearEnvioCuotas(lote).execute()
          if (envioCuotasConfirmado(response)) {
            EnvioCuotasLocal.confirmar(lote)
            "${lote.size} cuotas enviadas correctamente."
          } else {
            response.errorBody()?.close()
            "Envío sin confirmar. Verifica las cuotas en el servidor antes de volver a enviarlas."
          }
        }
      } catch (e: Exception) {
        Log.e("EnvioCuotas", "No se pudo completar el envío", e)
        if (reservado) {
          "Envío sin confirmar. Verifica las cuotas en el servidor antes de volver a enviarlas."
        } else {
          "No se pudo iniciar el envío. Las cuotas siguen pendientes."
        }
      }
      withContext(Dispatchers.Main) {
        ocupado.set(false)
        estadoMutable.value = mensaje
      }
    }
    return true
  }
}
