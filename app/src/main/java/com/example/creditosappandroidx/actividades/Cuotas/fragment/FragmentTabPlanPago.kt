package com.example.creditosappandroidx.actividades.Cuotas.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.FragmentTabPlanPagoBinding
import com.example.creditosappandroidx.models.planPago
import com.example.creditosappandroidx.actividades.Cuotas.adapter.AdapterPlan


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTabPlanPago.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTabPlanPago : Fragment() {
  private lateinit var binding: FragmentTabPlanPagoBinding
  private lateinit var adapterPago: AdapterPlan
  private lateinit var listaPlan: ArrayList<planPago>
  var crudsqlite: crudsqlite? = null
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentTabPlanPagoBinding.inflate(inflater, container, false)
    val view = binding.root

    crudsqlite = crudsqlite(view!!.context)
    listaPlan = crudsqlite!!.obtenerPlanesPagoPorPrestamId(datospublicos.creditocliente.prestamoid)
    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(view.context)

    binding.recyclerPlanPago.setLayoutManager(layoutManager)

    adapterPago = AdapterPlan(view.context, listaPlan)

    binding.recyclerPlanPago.adapter = adapterPago

    binding.textviewNombreCliente.setText(datospublicos.creditocliente.nombre + " " + datospublicos.creditocliente.apellido)
    binding.tvMonto.setText(datospublicos.creditocliente.monto.toString())
    binding.tvPlanPagoPagado.setText(datospublicos.creditocliente.getabonado(view.context).toString())
    binding.tvInteres.setText(datospublicos.creditocliente.interes.toString())
    binding.tvPlanPagoPendiente.setText(datospublicos.creditocliente.pendiente.toString())
    return view
  }


}