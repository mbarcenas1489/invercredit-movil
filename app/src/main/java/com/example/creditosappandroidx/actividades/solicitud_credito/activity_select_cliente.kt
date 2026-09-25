package com.example.creditosappandroidx.actividades.solicitud_credito

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.creditosappandroidx.Adaptadores.adaptador_select_cliente
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.databinding.ActivitySelectClienteBinding
import cswebservice.datospublicoskt


class activity_select_cliente : AppCompatActivity() {
  private lateinit var binding: ActivitySelectClienteBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySelectClienteBinding.inflate(layoutInflater)

    setContentView(R.layout.activity_select_cliente)
    binding.recycleSelectCliente.layoutManager =
      LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    datospublicoskt.cargarlistacredito(this, 0)
    cargardatos()

  }

  fun cargardatos() {
    datospublicoskt.cargarlistacredito(this, 0)
    var adapter = adaptador_select_cliente(datospublicoskt.listacompleta)
    binding.recycleSelectCliente.adapter = adapter
  }

  override fun onRestart() {
    datospublicoskt.cli_exitente = false
    if (datospublicoskt.solicitud_guardada) {
      datospublicoskt.solicitud_guardada = false
      finish()
    }
    super.onRestart()
  }
}