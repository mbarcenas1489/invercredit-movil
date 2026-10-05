package com.example.creditosappandroidx.actividades.Cuotas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.databinding.PlanPagoItemBinding
import com.example.creditosappandroidx.models.planPago

class AdapterPlan(val context: Context, val lista: ArrayList<planPago>) :
  RecyclerView.Adapter<AdapterPlan.ViewHolder>() {
  val listaPlanes = lista

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
      PlanPagoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun getItemCount(): Int = listaPlanes.size

  inner class ViewHolder(val binding: PlanPagoItemBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    with(holder) {
      val plan = listaPlanes[position]
      holder.binding.tvFechaPago.text = plan.fecha_pago
      holder.binding.tvMontoPago.text = plan.monto.toString()
      holder.binding.tvPagadoItem.text = plan.pagado.toString()
      holder.binding.tvPendiente.text = plan.pendiente.toString()
      if (plan.pendiente == 0.0f) {
        binding.imgEstadoPago.setBackgroundResource(R.drawable.ic_ok)
      } else {
        binding.imgEstadoPago.setBackgroundResource(R.drawable.ic_cancel);
      }
    }
  }
}



