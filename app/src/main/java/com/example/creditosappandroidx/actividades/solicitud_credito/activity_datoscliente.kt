package com.example.creditosappandroidx.actividades.solicitud_credito

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.databinding.ActivityDatosclienteBinding
import cswebservice.datospublicoskt

class activity_datoscliente : AppCompatActivity() {
  private lateinit var binding: ActivityDatosclienteBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityDatosclienteBinding.inflate(layoutInflater)


    setContentView(R.layout.activity_datoscliente)
    binding.btsiguiente.setOnClickListener {
      if (binding.ctnombre.text?.length == 0 || binding.ctapellido.text?.length == 0 || binding.ctdireccion.text?.length == 0) {
        Toast.makeText(this, "Debe llenar los datos", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }
      datospublicoskt.nuevo_credito.nombre = binding.ctnombre.text.toString()
      datospublicoskt.nuevo_credito.apellido = binding.ctapellido.text.toString()
      datospublicoskt.nuevo_credito.direccion = binding.ctdireccion.text.toString()
      datospublicoskt.nuevo_credito.telefono = binding.cttelefono.text.toString()
      datospublicoskt.nuevo_credito.correo = binding.ctcorreo.text.toString()
      datospublicoskt.nuevo_credito.cedula = binding.ctcedula.text.toString()

      var intent = Intent(applicationContext, activity_datos_credito::class.java)
      startActivity(intent)
    }

  }

  override fun onRestart() {
    if (datospublicoskt.solicitud_guardada) {
      finish()
    }
    super.onRestart()
  }
}