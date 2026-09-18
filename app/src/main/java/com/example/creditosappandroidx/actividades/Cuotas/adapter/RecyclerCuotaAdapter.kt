package com.example.creditosappandroidx.actividades.Cuotas.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.cswebservice.cspublic
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.databinding.RowlistviewBinding
import java.text.SimpleDateFormat

class RecyclerCuotaAdapter(val context: Context, val lista: ArrayList<cuotas>) : RecyclerView.Adapter<RecyclerCuotaAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = RowlistviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val cuota = lista[position];
    val format2 = SimpleDateFormat("yyyy-MM-d")

    holder.binding.lvLbfecha.text = cuota.fecha
    holder.binding.tvNumCuota.text = cuota.getnumerocuota().toString()
    holder.binding.lvLbmonto.text = cuota.monto.toString()

    if (cuota.getPendiente() > 0)
      holder.binding!!.tvPendiente.setText(cuota.getPendiente().toString())
    else {
      holder.binding!!.tvPendiente.setText("")
    }
    if (format2.format(cuota.obtenerfecha()).equals(cspublic.getfechahoy_yyyy_MM_dd(), ignoreCase = true)) {
      holder.binding.lvLbfecha.setTextColor(Color.parseColor("#3b92ea"))
      holder.binding.lvLbmonto.setTextColor(Color.parseColor("#3b92ea"))
      holder.binding.imgEditcuota.visibility = View.GONE
    } else {
      holder.binding.imgEditcuota.visibility = View.GONE
    }
  }

  override fun getItemCount(): Int {
    return lista.size
  }

  inner class ViewHolder(val binding: RowlistviewBinding) : RecyclerView.ViewHolder(binding.root)
}
