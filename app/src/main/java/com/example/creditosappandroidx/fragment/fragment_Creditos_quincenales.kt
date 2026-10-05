package com.example.creditosappandroidx.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.creditosappandroidx.Adaptadores.adaptador_cliente_quincenal
import com.example.creditosappandroidx.BuildConfig
import com.example.creditosappandroidx.MainActivity
import com.example.creditosappandroidx.actividades.Cuotas.ActivityTabCuotas
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.creditocliente
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.FragmentCreditosQuincenalBinding
import cswebservice.datospublicoskt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class fragment_Creditos_quincenales : Fragment() {
  private var _binding: FragmentCreditosQuincenalBinding? = null
  private val binding get() = _binding!!

  var listafiltrada = mutableListOf<creditocliente>()
  var text = ""
  private lateinit var adap_listview: adaptador_cliente_quincenal
  var roott: View? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentCreditosQuincenalBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    roott = view
    MainActivity.tabLayout.isVisible = true

    binding.searchViewCreditoQuincenal.queryHint = "Buscar Cliente"
    if (BuildConfig.BUILD_TYPE == "admin") {
      datospublicoskt.ObtenerCreditoPorCobrador(view.context, 3)
    } else {
      datospublicoskt.cargarlistacredito(view.context, 3)
    }

    adap_listview = adaptador_cliente_quincenal(view.context, datospublicoskt.listacompleta)

    binding.listviewClienteQuincenal.adapter = adap_listview

    binding.searchViewCreditoQuincenal.setOnQueryTextListener(object :
      SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return true;
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
          text = newText;
          datospublicoskt.texto = text
          filter()

        } else {
          text = ""
          filter()
        }
        return true
      }


    })

    binding.listviewClienteQuincenal.setOnItemClickListener { parent, view, position, id ->
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

  override fun onResume() {
    super.onResume()
    getcuotapordia()
  }

  override fun onDestroyView() {
    roott = null
    _binding = null
    super.onDestroyView()
  }

  fun getcuotapordia() {
    val appContext = requireContext().applicationContext

    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
      val sqlite = crudsqlite(appContext)

      datospublicoskt.listacompleta.forEach {
        val ultima_cuota = sqlite.Get_Ultima_Cuota(it.prestamoid)
        it.pago_cuota_dia = false
        if (ultima_cuota != null) {
          if (datospublicoskt.fecha_rango_quincenal(ultima_cuota.fecha)) {
            it.cuota_completada = ultima_cuota.pendiente <= 0
            it.pago_cuota_dia = true
          }
        }
      }

      withContext(Dispatchers.Main) {
        if (_binding == null) return@withContext

        if (binding.searchViewCreditoQuincenal.query.isNotEmpty()) {
          text = binding.searchViewCreditoQuincenal.query.toString()
          filter()
        } else {
          adap_listview.notifyDataSetChanged()
        }
      }
    }
  }

  fun filter() {
    var charText = text
    datospublicoskt.texto = text
    charText = charText.lowercase()

    if (charText.isEmpty()) {
      text = ""
      listafiltrada.clear()
      adap_listview = adaptador_cliente_quincenal(context, datospublicoskt.listacompleta)

    } else {
      listafiltrada = datospublicoskt.listacompleta.filter {
        it.nombre.lowercase().contains(charText) ||
          it.apellido.lowercase().contains(charText)


      }.toMutableList()
      adap_listview = adaptador_cliente_quincenal(context, listafiltrada)
    }

    binding.listviewClienteQuincenal.adapter = adap_listview
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

}
