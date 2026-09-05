package com.example.creditosappandroidx.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.creditosappandroidx.MainActivity
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.databinding.FragmentHomeBinding
import cswebservice.datospublicoskt
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
  private lateinit var binding: FragmentHomeBinding

  var vista: View? = null
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel =
      ViewModelProviders.of(this).get(HomeViewModel::class.java)
//    val root = inflater.inflate(R.layout.fragment_home, container, false)
    binding = FragmentHomeBinding.inflate(layoutInflater)

    MainActivity.tabLayout.isVisible = false

    val date = Calendar.getInstance().time
    val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
    val formatedDate = formatter.format(date)
    binding.tvHoyFecha.setText(formatedDate)

    var crud = crudsqlite(binding.root.context)
    var listacuotasdeldia = crud.ConsultaCuotasByFecha(getfecha())

    if (listacuotasdeldia != null && listacuotasdeldia.size > 0) {
      calcularRecaudado(listacuotasdeldia)
    }

    return binding.root
  }

  fun calcularRecaudado(lcuotas: ArrayList<cuotas>) {

    var total_cordobas = 0f
    var total_dollar = 0f
    var ncuotas_cordoba = 0
    var ncuotas_dolar = 0
    if (lcuotas.size <= 0) {
      // Toast.makeText(context,"Total de Cuotas="+lcuotas.toString(),Toast.LENGTH_SHORT).show()
      return
    }
    for (i in lcuotas.indices) {
      if (lcuotas[i].getprestamo().moneda.equals("Cordoba", ignoreCase = true)) {
        Log.e("Monto #=" + i.toString(), lcuotas[i].monto.toString())
        total_cordobas += lcuotas[i].monto
        ncuotas_cordoba++
      } else {
        total_dollar += lcuotas[i].monto
        ncuotas_dolar++
      }


    }
    binding.tvHoyMontorecaudado.setText("C$ " + total_cordobas)
    binding.tvHoyMontodollar.setText("$ " + total_dollar.toString())
    binding.tvHoyNumCordoba.setText("# " + ncuotas_cordoba.toString())
    binding.tvHoyNumDollar.setText("# " + ncuotas_dolar.toString())
    datospublicoskt.badge?.number = ncuotas_cordoba + ncuotas_dolar

  }

  fun getfecha(): String {
    val c = Calendar.getInstance()
    // c.add(Calendar.DAY_OF_YEAR, dias)
    var mm = ""
    var dd = ""

    val year = c.get(Calendar.YEAR)

    val month = c.get(Calendar.MONTH) + 1

    if (month <= 9)
      mm = "0" + month.toShort();
    else
      mm = month.toString()

    val day = c.get(Calendar.DAY_OF_MONTH)

    if (day <= 9)
      dd = "0" + day.toString()
    else
      dd = day.toString()

    return (year.toString() + "/" + mm + "/" + dd)
  }

}
