package com.example.creditosappandroidx

import com.example.creditosappandroidx.Api.envioCuotasConfirmado
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.models.response.CuotasResponse
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test
import retrofit2.Response

class EnvioCuotasTest {
  @Test
  fun marcasLocalesNoSeEnvianNiSeAceptanDelServidor() {
    val cuota = cuotas().apply { enviada = true; envioSinConfirmar = true }
    val json = Gson().toJsonTree(cuota).asJsonObject
    assertFalse(json.has("enviada"))
    assertFalse(json.has("envioSinConfirmar"))
    assertTrue(json.has("cuentaid"))
    val recibida = Gson().fromJson(
      """{"enviada":true,"envioSinConfirmar":true,"cuentaid":123}""", cuotas::class.java)
    assertFalse(recibida.enviada)
    assertFalse(recibida.envioSinConfirmar)
    assertEquals(123, recibida.cuentaid)
  }

  @Test
  fun exigeConfirmacionExplicitaDelBackend() {
    val gson = Gson()
    fun response(json: String) = Response.success(gson.fromJson(json, CuotasResponse::class.java))
    assertTrue(envioCuotasConfirmado(response("""{"status":"ok","data":[]}""")))
    assertFalse(envioCuotasConfirmado(response("""{"status":"error","data":[]}""")))
    assertFalse(envioCuotasConfirmado(response("""{"status":"ok","error":true,"data":[]}""")))
    assertFalse(envioCuotasConfirmado(response("""{"data":[]}""")))
    assertFalse(envioCuotasConfirmado(response("""{"status":"ok"}""")))
    assertFalse(envioCuotasConfirmado(Response.success(null)))
  }
}
