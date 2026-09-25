package com.example.creditosappandroidx.Adaptadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.cswebservice.creditocliente

//import kotlinx.android.synthetic.main.item_select_cliente.view.*
//import kotlinx.android.synthetic.main.item_solicitud.view.*

class adaptador_select_cliente(val lista_credito: MutableList<creditocliente>) :
  RecyclerView.Adapter<adaptador_select_cliente.Viewholder>() {
  class Viewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
//        val tvnombre: TextView = itemview.activity_select_cliente_nombre
//        val textSiguiente: TextView = itemview.textview_siguiente


  }

  override fun onCreateViewHolder(
    p0: ViewGroup,
    viewType: Int
  ): adaptador_select_cliente.Viewholder {

    val v = LayoutInflater.from(p0.context).inflate(R.layout.item_select_cliente, p0, false)
    return Viewholder(v)
  }

  override fun getItemCount(): Int {
    return lista_credito.size
  }

  override fun onBindViewHolder(p0: adaptador_select_cliente.Viewholder, pos: Int) {
    var solicitud = lista_credito.get(pos)
//
//        p0.tvnombre.setText(solicitud.nombre + " " + solicitud.apellido)
//
//        p0.textSiguiente.setOnClickListener {
//            datospublicoskt.cli_select_new_credito = solicitud
//            var intent = Intent(p0.itemView.context, activity_datos_credito::class.java)
//            p0.itemView.context.startActivity(intent)
//        }


  }
}