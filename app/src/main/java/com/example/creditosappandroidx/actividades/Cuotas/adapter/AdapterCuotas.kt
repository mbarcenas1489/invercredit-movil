package com.example.creditosappandroidx.actividades.Cuotas.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.creditosappandroidx.cswebservice.cspublic
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.databinding.RowlistviewBinding
import java.text.SimpleDateFormat

class AdapterCuotas(val context: Context, val lista: ArrayList<cuotas>) : BaseAdapter() {

  var inflater: LayoutInflater = LayoutInflater.from(context)
  val listaCuotas = lista

  override fun getCount(): Int {
    return listaCuotas.size
  }

  override fun getItem(position: Int): Any {
    return listaCuotas.get(position)
  }

  override fun getItemId(position: Int): Long {
    return listaCuotas.get(position).id.toLong()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val binding: RowlistviewBinding
    var vh: ListRowHolder? = null
    if (convertView == null) {
      binding = RowlistviewBinding.inflate(inflater, parent, false)
      vh = ListRowHolder(binding)
      binding.root.tag = binding
    } else {
      binding = convertView.tag as RowlistviewBinding
    }
    val format2 = SimpleDateFormat("yyyy-MM-d")

    val cuota = listaCuotas[position];



    if (vh != null) {
      vh.textFecha.text = cuota.fecha
      vh.textNumCuota.text = cuota.getnumerocuota().toString()
      vh.textMonto.text = cuota.monto.toString()
      if (cuota.getPendiente() > 0)
        vh!!.textPendiente.setText(cuota.getPendiente().toString())
      else vh!!.textPendiente.setText("")

      if (format2.format(cuota.obtenerfecha())
          .equals(cspublic.getfechahoy_yyyy_MM_dd(), ignoreCase = true)
      ) {
        vh.textFecha.setTextColor(Color.parseColor("#3b92ea"))
        vh.textMonto.setTextColor(Color.parseColor("#3b92ea"))
        vh.imgEditar.visibility = View.GONE
      } else {
        vh.imgEditar.visibility = View.GONE
      }
    }



    return binding.root
  }

  private class ListRowHolder(binding: RowlistviewBinding) {
    val textFecha: TextView
    var textNumCuota: TextView
    var textMonto: TextView
    var textPendiente: TextView
    var imgEditar: ImageView


    init {
      this.textFecha = binding.lvLbfecha
      this.textNumCuota = binding.tvNumCuota
      this.textMonto = binding.lvLbmonto
      this.textPendiente = binding.tvPendiente
      this.imgEditar = binding.imgEditcuota
    }
  }
}