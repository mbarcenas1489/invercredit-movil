package com.example.creditosappandroidx.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.example.creditosappandroidx.Adaptadores.adaptador_cliente_listview
import com.example.creditosappandroidx.BuildConfig
import com.example.creditosappandroidx.MainActivity
import com.example.creditosappandroidx.actividades.Cuotas.ActivityTabCuotas
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.creditocliente
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.FragmentCreditosDiariosBinding
import cswebservice.datospublicoskt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class fragment_Creditos_diarios : Fragment() {
  private lateinit var binding: FragmentCreditosDiariosBinding

  var listafiltrada = mutableListOf<creditocliente>()
  var text = ""

  var adap_listview: adaptador_cliente_listview? = null
  var roott: View? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentCreditosDiariosBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)


    MainActivity.tabLayout.isVisible = true
    binding.searchView.queryHint = "Buscar Cliente"

    if (BuildConfig.BUILD_TYPE == "admin") {
      datospublicoskt.ObtenerCreditoPorCobrador(view.context, 1)
    } else {
      datospublicoskt.cargarlistacredito(view.context, 1)
    }

    adap_listview = adaptador_cliente_listview(view.context, datospublicoskt.listacompleta)
    binding.listviewCliente.adapter = adap_listview
    getcuotapordia(adap_listview)

    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
          text = newText
          datospublicoskt.texto = text
          filter()
        } else {
          var adap =
            adaptador_cliente_listview(view.context, datospublicoskt.listacompleta)
          if (adap != null) {
            adap.notifyDataSetChanged()
          }
        }
        return true
      }
    })

    binding.listviewCliente.setOnItemClickListener { parent, view, position, id ->
      var c: creditocliente
      datospublicoskt.pos_credito_select = position

      if (listafiltrada.size > 0)
        c = listafiltrada.get(position)
      else
        c = datospublicoskt.listacompleta.get(position)

      datospublicos.creditocliente = c

      val intent = Intent(context, ActivityTabCuotas::class.java)
      startActivity(intent)
    }

  }

  override fun onResume() {
    getcuotapordia(adap_listview)
    super.onResume()
  }

  fun getcuotapordia(adap: adaptador_cliente_listview?) {
    GlobalScope.launch(Dispatchers.IO) {
      var sqlite = crudsqlite(context)
      var cont = 0

      datospublicoskt.listacompleta.forEach {
        val ultima_cuota = sqlite.Get_Ultima_Cuota(it.prestamoid)
        var calendar = Calendar.getInstance()
        if (ultima_cuota != null) {
          if (ultima_cuota.fecha == getfecha(0, calendar)) {
            it.cuota_completada = ultima_cuota.pendiente <= 0
            it.pago_cuota_dia = true
            cont += 1
          } else
            it.pago_cuota_dia = false
        }
        withContext(Dispatchers.Main)
        {
          if (binding?.searchView?.query.toString().length > 0) {
            text = binding?.searchView?.query.toString()
            filter()
          } else
            adap_listview?.notifyDataSetChanged()
          datospublicoskt.badge_credito_diario!!.number =
            datospublicoskt.listacompleta.size - cont
        }
      }
    }
  }

  fun filter() {
    var charText = text
    datospublicoskt.texto = text
    charText = charText.lowercase()

    if (charText.length == 0 && listafiltrada.size == 0) {
      text = ""
      listafiltrada = mutableListOf<creditocliente>()
      var adap = adaptador_cliente_listview(context, datospublicoskt.listacompleta)
      if (adap != null) {
        adap.notifyDataSetChanged()
      }
      return

    } else {
      listafiltrada = datospublicoskt.listacompleta?.filter {
        it.nombre.lowercase().contains(charText.lowercase()) ||
          it.apellido.lowercase().contains(charText.lowercase())

      }!!.toMutableList()

      var adap = adaptador_cliente_listview(context, listafiltrada)
      binding.listviewCliente.adapter = adap
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
    if (month <= 9) {
      month_string = "0" + month.toString()
    } else {
      month_string = month.toString()
    }

    return year.toString() + "-" + month_string + "-" + day_string
  }

}