package com.example.creditosappandroidx.actividades.solicitud_credito

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.ActivityDatosCreditoBinding
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import cswebservice.datospublicoskt
import java.util.*

class activity_datos_credito : AppCompatActivity() {
  var tipopago = 0
  var fecha_fin = ""
  private lateinit var binding: ActivityDatosCreditoBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    binding = ActivityDatosCreditoBinding.inflate(layoutInflater)

    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    FlowManager.init(FlowConfig.Builder(this).build())
    if (datospublicoskt.cli_exitente) {
      binding.lbnombrecliente.setText(datospublicoskt.cli_select_new_credito?.nombre + " " + datospublicoskt.cli_select_new_credito?.apellido)
    } else {
      binding.lbnombrecliente.setText(datospublicoskt.nuevo_credito.nombre + " " + datospublicoskt.nuevo_credito.apellido)
    }

    binding.ctfecha.setText(datospublicoskt.getfecha_format())

    binding.radiogrupoFormapago.setOnCheckedChangeListener { group, checkedId ->
      calcularfecha()
      calculos()
    }

    binding.ctinteres.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {

      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        calculos()
      }
    })
    binding.ctplazo.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        calcularfecha()
        calcularinteres()
      }
    })

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_nuevo_credito, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    when (item.itemId) {
      R.id.menu_guardar_nuevoCredito -> {
        if (datospublicoskt.cli_exitente) {
          datospublicoskt.nuevo_credito.cliente_existente = 1
          datospublicoskt.nuevo_credito.cliente_clienteid =
            datospublicoskt.cli_select_new_credito!!.clienteid
          datospublicoskt.nuevo_credito.nombre =
            datospublicoskt.cli_select_new_credito!!.nombre
          datospublicoskt.nuevo_credito.apellido =
            datospublicoskt.cli_select_new_credito!!.apellido
        } else {
          datospublicoskt.nuevo_credito.cliente_existente = 0
        }

        datospublicoskt.nuevo_credito.monto = binding.ctmonto.text.toString().toFloat()
        datospublicoskt.nuevo_credito.plazo = binding.ctplazo.text.toString().toInt()
        datospublicoskt.nuevo_credito.interes = binding.ctinteres.text.toString().toInt()
        datospublicoskt.nuevo_credito.monto_pendiente = binding.lbApagar.text.toString().toFloat()
        datospublicoskt.nuevo_credito.monto_a_pagar = binding.lbApagar.text.toString().toFloat()
        datospublicoskt.nuevo_credito.activo = 1
        datospublicoskt.nuevo_credito.tipopago = 1
        datospublicoskt.nuevo_credito.fechainicial = datospublicoskt.getfecha_format_mysql()
        datospublicoskt.nuevo_credito.fechafin = fecha_fin
        datospublicoskt.nuevo_credito.cobrador_idCobrador = datospublicos.obtener_cobrador(this)
        datospublicoskt.nuevo_credito.cuenta_idCuenta = 1
        datospublicoskt.nuevo_credito.estado = 1
        datospublicoskt.nuevo_credito.moneda = "Cordoba"
        datospublicoskt.nuevo_credito.save()
        Toast.makeText(
          applicationContext,
          "Solicitud de Credito Guardada",
          Toast.LENGTH_SHORT
        ).show()

        datospublicoskt.solicitud_guardada = true;
        finish()
        return true;
      }
    }

    return super.onOptionsItemSelected(item)
  }

  fun calculos() {
    if (binding.ctmonto.text!!.isEmpty())
      return
    if (binding.ctplazo.text!!.isEmpty())
      return
    if (binding.ctinteres.text!!.isEmpty())
      return
    var monto: Float = binding.ctmonto.text.toString().toFloat()
    var plazo: Int = binding.ctplazo.text.toString().toInt()
    var interes: Float = binding.ctinteres.text.toString().toFloat()
    var monto_interes: Float = monto * (interes / 100)
    var cuota: Float = (monto + monto_interes) / plazo
    var monto_pagar: Float = monto + monto_interes
    binding.lbApagar.setText(monto_pagar.toString())
    binding.lbmontointeres.setText(monto_interes.toString())
    binding.lbcuotas.setText(cuota.toString())
  }

  fun calcularinteres() {
    if (binding.ctplazo.text!!.isEmpty()) {
      return
    }
    var plazo = binding.ctplazo.text.toString().toInt()
    if (plazo <= 20) {
      binding.ctinteres.setText("13")
    }
    if (plazo > 20 && plazo <= 40) {
      binding.ctinteres.setText("26")
    }
    if (plazo > 40) {
      binding.ctinteres.setText("39")
    }
  }

  fun calcularfecha() {
    if (binding.ctplazo.text!!.isEmpty()) {
      return
    }
    var plazo = binding.ctplazo.text.toString().toInt()
    if (binding.radioDia.isChecked) {
      tipopago = 1
      val calendar = Calendar.getInstance()
      var dias_sumar = (plazo / 5) * 2
      binding.lbfechafin.setText(datospublicoskt.getfecha((plazo + dias_sumar), calendar))
      fecha_fin = datospublicoskt.getfecha_format_mysql((plazo + dias_sumar), calendar)
    }
    if (binding.radioSemana.isChecked) {
      tipopago = 2
      val calendar = Calendar.getInstance()
      var dias_sumar = plazo * 7
      binding.lbfechafin.setText(datospublicoskt.getfecha((dias_sumar), calendar))
      fecha_fin = datospublicoskt.getfecha_format_mysql((dias_sumar), calendar)
    }
    if (binding.radioQuincena.isChecked) {
      tipopago = 3
      val calendar = Calendar.getInstance()
      var dias_sumar = plazo * 15
      binding.lbfechafin.setText(datospublicoskt.getfecha((dias_sumar), calendar))
      fecha_fin = datospublicoskt.getfecha_format_mysql((dias_sumar), calendar)
    }
    if (binding.radioMes.isChecked) {
      tipopago = 4
      val calendar = Calendar.getInstance()
      var dias_sumar = plazo * 30
      binding.lbfechafin.setText(datospublicoskt.getfecha((dias_sumar), calendar))
      fecha_fin = datospublicoskt.getfecha_format_mysql((dias_sumar), calendar)
    }
  }
}