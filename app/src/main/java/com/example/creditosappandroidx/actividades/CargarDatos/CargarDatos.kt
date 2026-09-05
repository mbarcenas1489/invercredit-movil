package com.example.creditosappandroidx.actividades.CargarDatos

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.creditosappandroidx.Api.CargarDatosService
import com.example.creditosappandroidx.Api.CobradorService
import com.example.creditosappandroidx.BuildConfig
import com.example.creditosappandroidx.actividades.Cuotas.api.PlanesPagoService
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.creditocliente
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel
import com.example.creditosappandroidx.databinding.ActivityCargarDatosV2Binding
import com.raizlabs.android.dbflow.sql.language.SQLite

class CargarDatos : AppCompatActivity() {
  private lateinit var binding: ActivityCargarDatosV2Binding
  private lateinit var context : Context

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityCargarDatosV2Binding.inflate(layoutInflater)
    setContentView(binding.root)
    context = binding.root.context
    binding.btnCargarDatosv2.setOnClickListener {
      actualizarDatos()
    }
    if (BuildConfig.BUILD_TYPE == "admin") {
      binding.cardViewCobradores.visibility = View.VISIBLE
    } else {
      binding.cardViewCobradores.visibility = View.GONE
    }
  }
  fun actualizarDatos(){
    Toast.makeText(this, "Cargando Datos", Toast.LENGTH_SHORT).show()
    clearDataBase()
    CargarDatosService.ObtenerCreditos(binding.constracionCargarDatos, binding.tvCreditosPorcentaje, binding.tvCreditosCantidad )
    CargarDatosService.ObtenerCuotas(binding.constracionCargarDatos, binding.tvCuotasPorcentaje, binding.tvCuotasCantidad)
    PlanesPagoService.ObtenerPlanesPagoPorCobrador(binding.constracionCargarDatos, binding.tvPlanesPagoPorcentaje, binding.tvPlanesPagoCantidad)
    CargarDatosService.ObtenerMoras(binding.constracionCargarDatos,  binding.tvMorasPorcentaje, binding.tvMorasCantidad)
    CobradorService.ObtenerCobradores(binding.constracionCargarDatos,  binding.tvCobradoresPorcentaje, binding.tvCobradoresCantidad)

  }
  fun clearDataBase(){
    val crud = crudsqlite(context)
    crud.DeleteAllCobradores()
    SQLite.delete().from(creditocliente::class.java).execute()
    crud.Eliminar_Todas_cuotas()
    crud.EliminarPlanesPago()
    crud.EliminarMoras()
  }
}