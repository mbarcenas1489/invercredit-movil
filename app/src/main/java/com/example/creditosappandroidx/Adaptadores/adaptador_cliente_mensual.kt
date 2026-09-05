package com.example.creditosappandroidx.Adaptadores

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.cswebservice.creditocliente
import cswebservice.datospublicoskt
import java.util.*

class adaptador_cliente_mensual(var context: Context?, var listacompleta: MutableList<creditocliente>) : BaseAdapter() {
  private val mInflator: LayoutInflater

  init {
    this.mInflator = LayoutInflater.from(context)
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
    val view: View?
    val vh: ListRowHolder
    if (convertView == null) {
      view = this.mInflator.inflate(R.layout.row_cliente, parent, false)
      vh = ListRowHolder(view)
      view.tag = vh
    } else {
      view = convertView
      vh = view.tag as ListRowHolder
    }

    var c = listacompleta?.get(position)
    /*  vh.contrain.setOnClickListener {
          datospublicos.creditocliente = c
          val intent = Intent(context, activity_cuotas::class.java)
          context.startActivity(intent)

      }*/
    vh.tvnum.setText(position.toString())
    vh.tvnombre.setText(listacompleta.get(position).nombre + " " + listacompleta.get(position).apellido)
    vh.tv_forma_pago.setText(listacompleta.get(position).tipopago.toString())
    if (listacompleta.get(position).pendiente >= 0) {
      vh.tvpendiente.setText(listacompleta.get(position).pendiente.toString())
    } else {
      vh.tvpendiente.setText("")
    }

    vh.tvnombre.setText(c?.nombre + " " + c?.apellido)

    if (c != null) {
      if (c.pago_cuota_dia) {
        if (c.cuota_completada)
          vh.image.setBackgroundResource(R.drawable.ic_ok);
        else
          vh.image.setBackgroundResource(R.drawable.ic_ok_pendiente);
      } else {
        vh.image.setBackgroundResource(R.drawable.ic_cancel);

      }
    }
    vh.tvdiaPago.setText(c?.dia_pago1.toString())
    if (datospublicoskt.getDayOfTMonth() == c?.dia_pago1) {
      vh.tvdiaPago.setTextColor(Color.parseColor("#088A08"))
    } else {
      vh.tvdiaPago.setTextColor(Color.parseColor("#050505"))
    }

    return view
  }

  override fun getItem(position: Int): Any {
    return listacompleta[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return listacompleta.size
  }

  private class ListRowHolder(row: View?) {
    public val tvnombre: TextView
    public val tvnum: TextView
    public val tv_forma_pago: TextView
    public val image: ImageView
    public val contrain: ConstraintLayout
    public val tvpendiente: TextView
    public val tvdiaPago: TextView


    init {
      this.tvnombre = row?.findViewById(R.id.vistacliente_tv_nombre) as TextView
      this.image = row?.findViewById(R.id.vistacliente_img) as ImageView
      this.contrain = row?.findViewById(R.id.contrain_cliente) as ConstraintLayout
      this.tvnum = row?.findViewById(R.id.textView53) as TextView
      this.tv_forma_pago = row?.findViewById(R.id.tv_formapago) as TextView
      this.tvpendiente = row?.findViewById(R.id.tv_pendiente_credito) as TextView
      this.tvdiaPago = row?.findViewById(R.id.tv_diapago) as TextView

    }
  }
}