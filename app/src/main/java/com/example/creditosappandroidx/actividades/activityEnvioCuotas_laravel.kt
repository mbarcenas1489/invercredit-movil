package com.example.creditosappandroidx.actividades

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.creditosappandroidx.Api.EnvioCuotasService
import com.example.creditosappandroidx.cssqlite.EnvioCuotasLocal
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel
import com.example.creditosappandroidx.cswebservice.network
import com.example.creditosappandroidx.databinding.ActivityCargaDatosLaravelBinding
import com.google.android.material.snackbar.Snackbar
import cswebservice.datospublicoskt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class activityEnvioCuotas_laravel : AppCompatActivity() {
  private lateinit var binding: ActivityCargaDatosLaravelBinding
  private var consultaEstado: Job? = null
  private var redConfigurada = true

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCargaDatosLaravelBinding.inflate(layoutInflater)
    setContentView(binding.root)
    crudsqlite(applicationContext)
    binding.btenviarCuotas.isEnabled = false

    EnvioCuotasService.estado.observe(this) { actualizarEstado() }
    binding.btenviarCuotas.setOnClickListener {
      binding.btenviarCuotas.isEnabled = false
      if (EnvioCuotasService.enviar(applicationContext)) {
        // Se conserva el envío de moras que ya acompañaba a este botón.
        lifecycleScope.launch {
          val moras = withContext(Dispatchers.IO) {
            crudsqlite(applicationContext).ConsultaTodas_Moras()
          }
          if (moras.isNotEmpty()) {
            crudWebservice_laravel(applicationContext)
              .enviarMorasServidor(moras, binding.contrainEnviarCuotas)
          }
        }
      }
      actualizarEstado()
    }
    validateNetwork()
  }

  override fun onResume() {
    super.onResume()
    actualizarEstado()
  }

  private fun actualizarEstado() {
    consultaEstado?.cancel()
    consultaEstado = lifecycleScope.launch {
      val (pendientes, sinConfirmar) = withContext(Dispatchers.IO) {
        EnvioCuotasLocal.contarPendientes() to EnvioCuotasLocal.contarSinConfirmar()
      }
      val enCurso = EnvioCuotasService.enCurso
      binding.tvCantidadCuotasNuevas.text = pendientes.toString()
      binding.llProgressBar.root.visibility = if (enCurso) View.VISIBLE else View.GONE
      binding.btenviarCuotas.isEnabled = redConfigurada && !enCurso && pendientes > 0
      binding.btenviarCuotas.text = when {
        enCurso -> "Enviando..."
        pendientes > 0 -> "Enviar cuotas"
        sinConfirmar > 0 -> "Envío sin confirmar"
        else -> "Sin cuotas pendientes"
      }
      binding.textView56.text = when {
        enCurso -> "Enviando cuotas. No es necesario volver a presionar el botón."
        sinConfirmar > 0 -> "$sinConfirmar cuotas sin confirmar. No se reenviarán. Verifica su recepción en el servidor antes de realizar otro intento."
        else -> EnvioCuotasService.estado.value?.takeIf { it.isNotBlank() }
          ?: "Solo se enviarán las cuotas pendientes. Las confirmadas no se vuelven a enviar."
      }
    }
  }

  fun validateNetwork() {
    if (datospublicoskt.conexion_server == "Local" && !network.validIPServer(applicationContext)) {
      redConfigurada = false
      Snackbar.make(binding.contrainEnviarCuotas, "La dirección del servidor no es correcta",
        Snackbar.LENGTH_LONG).show()
      binding.btenviarCuotas.visibility = View.GONE
      return
    }
    crudWebservice_laravel(this).isConnectServer(binding.contrainEnviarCuotas, "", "")
  }
}
