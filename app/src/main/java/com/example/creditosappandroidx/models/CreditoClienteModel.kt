package com.example.creditosappandroidx.models

import com.google.gson.annotations.SerializedName

data class CreditoClienteModel(
  @SerializedName("id") val id: Int,
  @SerializedName("prestamoid") var prestamoid: Int,
  @SerializedName("dia_pago1") var dia_pago1: Int,
  @SerializedName("dia_pago2") var dia_pago2: Int,
  @SerializedName("cantidadCuotasAtrasadas") var cantidadCuotasAtrasadas: Int,
  @SerializedName("monto") var monto: Float,
  @SerializedName("motipopagonto") var tipopago: Int,
  @SerializedName("dia_pago") var dia_pago: Int,
  @SerializedName("moneda") var moneda: Int,
  @SerializedName("interes") var interes: Int,
  @SerializedName("plazo") var plazo: Int,
  @SerializedName("fechainicial") var fechainicial: String,
  @SerializedName("fechafin") var fechafin: String,
  @SerializedName("cobrador_nombre") var cobrador_nombre: String,
  @SerializedName("monto_a_pagar") var monto_a_pagar: Float,
  @SerializedName("monto_cuota") var monto_cuota: Float,
  @SerializedName("monto_pendiente") var monto_pendiente: Float,
  @SerializedName("activo") var activo: Int,
  @SerializedName("cliente_clienteid") var cliente_clienteid: Int,
  @SerializedName("cobrador_idCobrador") var cobrador_idCobrador: Int,
  @SerializedName("malo") var malo: Int,
  @SerializedName("clienteid") var clienteid: Int,
  @SerializedName("nombre") var nombre: String,
  @SerializedName("cedula") var cedula: String,
  @SerializedName("telefono") var telefono: String,
  @SerializedName("direccion") var direccion: String,
  @SerializedName("orden") var orden: String,
  @SerializedName("correo") var correo: String,
  @SerializedName("pendiente") var pendiente: Float,

  )
