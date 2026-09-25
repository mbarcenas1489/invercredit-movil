package com.example.creditosappandroidx.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.example.creditosappandroidx.Adaptadores.adaptador_cliente_listview
import com.example.creditosappandroidx.Adaptadores.adaptador_cliente_mensual
import com.example.creditosappandroidx.BuildConfig
import com.example.creditosappandroidx.MainActivity
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.actividades.Cuotas.ActivityTabCuotas
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.creditocliente
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.ActivityMainBinding
import com.example.creditosappandroidx.databinding.FragmentCreditosMensualesBinding
import cswebservice.datospublicoskt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class fragment_creditos_mensuales : Fragment() {
  var listafiltrada = mutableListOf<creditocliente>()
  var text = ""
  var adap_listview: adaptador_cliente_mensual? = null
  var roott: View? = null
  private lateinit var binding: FragmentCreditosMensualesBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentCreditosMensualesBinding.inflate(layoutInflater)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    roott = view
    MainActivity.tabLayout.isVisible = true
    binding.searchViewCreditoMensual.queryHint = "Buscar Cliente"
    if (BuildConfig.BUILD_TYPE == "admin") {
      datospublicoskt.ObtenerCreditoPorCobrador(view.context, 4)
    } else {
      datospublicoskt.cargarlistacredito(view.context, 4)
    }
    adap_listview = adaptador_cliente_mensual(view.context, datospublicoskt.listacompleta)

    binding.listviewClienteMensual.adapter = adap_listview
    getcuotaporMes(adap_listview)

   binding.searchViewCreditoMensual.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return true;
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
          text = newText;
          datospublicoskt.texto = text
          filter()

        } else {
          var adap = adaptador_cliente_mensual(view.context, datospublicoskt.listacompleta)
          if (adap != null) {
            adap.notifyDataSetChanged()
          }
        }
        return true
      }

    })

    binding.listviewClienteMensual.setOnItemClickListener { parent, view, position, id ->
      var c: creditocliente
      datospublicoskt.pos_credito_select = position;

      if (listafiltrada.size > 0) {
        c = listafiltrada.get(position)
      } else {
        c = datospublicoskt.listacompleta.get(position)
      }
      datospublicos.creditocliente = c
      val intent = Intent(context, ActivityTabCuotas::class.java)
      startActivity(intent)
    }
  }

  fun filter() {
    var charText = text
    datospublicoskt.texto = text
    charText = charText.lowercase()

    if (charText.length == 0 && listafiltrada.size == 0) {
      text = ""
      listafiltrada = mutableListOf<creditocliente>()
      var adap = adaptador_cliente_mensual(context, datospublicoskt.listacompleta)
      if (adap != null) {
        adap.notifyDataSetChanged()
      }
      return

    } else {
      listafiltrada = datospublicoskt.listacompleta?.filter {
        it.nombre.lowercase().contains(charText.lowercase()) ||
        it.apellido.lowercase().contains(charText.lowercase())

      }?.toMutableList()!!

      var adap = adaptador_cliente_listview(context, listafiltrada)
      binding.listviewClienteMensual.adapter = adap
    }
  }

  fun getcuotaporMes(adap: adaptador_cliente_mensual?) {

    GlobalScope.launch(Dispatchers.IO) {
      var sqlite = crudsqlite(context)
      var cont = 0

      datospublicoskt.listacompleta.forEach({
        val ultima_cuota = sqlite.Get_Ultima_Cuota(it.prestamoid)
        var calendar = Calendar.getInstance()
        if (ultima_cuota != null) {
          if (datospublicoskt.fechaRangoMensual(ultima_cuota.fecha)) {
            it.cuota_completada = ultima_cuota.pendiente <= 0
            it.pago_cuota_dia = true
            cont += 1
          } else
            it.pago_cuota_dia = false
        }
        withContext(Dispatchers.Main)
        {
          if (binding.searchViewCreditoMensual?.query.toString().length > 0) {
            text = binding.searchViewCreditoMensual?.query.toString()
            filter()
          } else
            adap_listview?.notifyDataSetChanged()
          datospublicoskt.badge_credito_mensual!!.number = datospublicoskt.listacompleta.size - cont
        }
      })
    }
  }

  fun getfecha(dias: Int, c: Calendar): String {

    val year = c.get(Calendar.YEAR)

    val month = c.get(Calendar.MONTH) + 1
    val day = c.get(Calendar.DAY_OF_MONTH)
    var day_string = ""
    var month_string = ""
    if (day <= 9)
      day_string = "0" + day.toString()
    else
      day_string = day.toString()
    if (month <= 9)
      month_string = "0" + month.toString()
    else
      month_string = month.toString()

    return year.toString() + "-" + month_string + "-" + day_string
  }

  fun cambiar_formato(fecha: String): String {
    var f = fecha.split('-')
    var dia = f[0]
    var mes = f[1]
    var anyo = f[2]
    val meses = arrayOf(" ", "Enero", "Febrero", "Marzo", "Abril",
      "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Nobiembre", "Diciembre")

    var mes_int = mes.toInt()
    return dia + " " + meses[mes_int] + " " + anyo

  }
}

