package com.example.creditosappandroidx.actividades.Cuotas.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.actividades.AddCuota.fragmentAddCuotaAbono
import com.example.creditosappandroidx.actividades.AddCuota.fragment_add_cuota
import com.example.creditosappandroidx.actividades.Cuotas.adapter.RecyclerCuotaAdapter
import com.example.creditosappandroidx.actividades.activity_VerMora
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.FragmentTabCuotasBinding
import cswebservice.datospublicoskt.compareWithToday
import cswebservice.datospublicoskt.fecha_en_rango_semanal
import cswebservice.datospublicoskt.fecha_rango_quincenal
import cswebservice.datospublicoskt.monto_pendiente

class FragmentTabCuotas : Fragment() {
  lateinit var binding: FragmentTabCuotasBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentTabCuotasBinding.inflate(inflater, container, false)
    viewFragment = binding.root
    FragmentTabCuotas.listViewCuotas = binding.recyclerCuotas

    binding.tvTabCuotasNombrecliente.text =
      datospublicos.creditocliente.nombre + " " + datospublicos.creditocliente.apellido

    binding.recyclerCuotas.layoutManager =
      LinearLayoutManager(binding.root.context, LinearLayout.VERTICAL, false)
    LlenarListaCuotas()
//    registerForContextMenu(binding.listViewCuotas!!)

    binding.tabCuotasAgregarNueva.setOnClickListener {
      AgregarNuevaCuota()
    }
    CalcularMontos()
    binding.tvMoras.setOnClickListener {
      val intent = Intent(binding.root.context, activity_VerMora::class.java)
      startActivity(intent)
    }
    return viewFragment
  }

  fun CalcularMontos() {
    var abonado = 0f
    var pendiente = 0f
    for (cuota in listaCuotas!!) {
      if (cuota.getMora() == 0) {
        abonado += cuota.getMonto()
        pendiente += cuota.getPendiente()
      }
    }
    datospublicos.creditocliente.monto_pendiente =
      datospublicos.creditocliente.monto_a_pagar - abonado
    datospublicos.creditocliente.save()
    monto_pendiente = pendiente
    binding.tvAbonado.setText(abonado.toString())
    binding.tvPendiente.setText(
      (datospublicos.creditocliente.monto_a_pagar - abonado + crudsqlite!!.totalMora_by_idprestamo(
        datospublicos.creditocliente.prestamoid
      )).toString()
    )
    binding.tvTotal.setText(datospublicos.creditocliente.monto_a_pagar.toString())
    binding.tvMora.setText(
      crudsqlite!!.totalMora_by_idprestamo(datospublicos.creditocliente.prestamoid).toString()
    )
    listaCuotas = crudsqlite!!.consultacuotaByPrestamoID(datospublicos.creditocliente.prestamoid)

//    adap = AdapterCuotas(viewFragment!!.context, listaCuotas!!)
    adap = RecyclerCuotaAdapter(viewFragment!!.context, listaCuotas!!)

//    listViewCuotas.setAdapter(adap)
  }

  fun AgregarNuevaCuota() {

    if (datospublicos.ultimaCuota == null || datospublicos.ultimaCuota.pendiente == 0f) {
      fragment_add_cuota().show(childFragmentManager, "Add Nueva Cuota")
      return
    }
    when (datospublicos.creditocliente.getTipopago()) {
      1 ->
        if (compareWithToday(datospublicos.ultimaCuota.fecha)) {
          Toast.makeText(viewFragment!!.context, "Abono a Cuota", Toast.LENGTH_SHORT)
            .show()
        } else {
          fragment_add_cuota().show(childFragmentManager, "Add Nueva Cuota")
        }

      2 ->
        if (fecha_en_rango_semanal(datospublicos.ultimaCuota.fecha)) {
          Toast.makeText(viewFragment!!.context, "Abono a Cuota", Toast.LENGTH_SHORT)
            .show()
          fragmentAddCuotaAbono().show(childFragmentManager, "Add Nueva Cuota")
        } else {
          fragment_add_cuota().show(childFragmentManager, "Add Nueva Cuota")
        }

      3 ->
        if (fecha_rango_quincenal(datospublicos.ultimaCuota.fecha)) {
          fragmentAddCuotaAbono().show(childFragmentManager, "Add Nueva Cuota")
        } else {
          fragment_add_cuota().show(childFragmentManager, "Add Nueva Cuota")
        }
    }
  }

  companion object {
    var listViewCuotas: RecyclerView? = null
    var crudsqlite: crudsqlite? = null
    var adap: RecyclerCuotaAdapter? = null
    var listaCuotas: ArrayList<cuotas>? = null
    private var viewFragment: View? = null

    fun LlenarListaCuotas() {
      crudsqlite = crudsqlite(viewFragment!!.context)
      listaCuotas =
        crudsqlite!!.consultacuotaByPrestamoID(datospublicos.creditocliente.prestamoid)
      datospublicos.listacuotas = listaCuotas
      if (listaCuotas!!.size > 0) {
        datospublicos.ultimaCuota = listaCuotas!![0]
      }
//      adap = AdapterCuotas(viewFragment!!.context, listaCuotas!!)
      adap = RecyclerCuotaAdapter(viewFragment!!.context, listaCuotas!!)

      listViewCuotas!!.adapter = adap
    }
  }
}