package com.example.creditosappandroidx.models

import com.google.gson.annotations.SerializedName


data class _PlanesPago
  (
  @SerializedName("id") val id: Int,
  @SerializedName("fecha_pago") var fecha_pago: String,
  @SerializedName("monto") var monto: Float,
  @SerializedName("pagado") var pagado: Float,
  @SerializedName("pendiente") var pendiente: Float,
  @SerializedName("prestamoid") var prestamoid: Int = 0,
  @SerializedName("Cancelado") var Cancelado: Int = 0,
  @SerializedName("Atrasado") var Atrasado: Int = 0
)
