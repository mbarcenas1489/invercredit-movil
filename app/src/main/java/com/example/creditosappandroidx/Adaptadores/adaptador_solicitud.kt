package com.example.creditosappandroidx.Adaptadores


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.solicitud_credito
//import kotlinx.android.synthetic.main.item_solicitud.view.*
//import org.jetbrains.anko.alert

class adaptador_solicitud(val lista_credito: MutableList<solicitud_credito>) :
    RecyclerView.Adapter<adaptador_solicitud.Viewholder>() {
    class Viewholder(itemview: View) : RecyclerView.ViewHolder(itemview) {
//        val tvnombre: TextView = itemview.tv_solicitud_nombre
//        val tvmonto: TextView = itemview.tv_solicitud_monto
//        val img_delete: ImageView = itemview.img_delete_solicitud

    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): adaptador_solicitud.Viewholder {

        val v = LayoutInflater.from(p0.context).inflate(R.layout.item_solicitud, p0, false)
        return Viewholder(v)
    }

    override fun getItemCount(): Int {
        return lista_credito.size
    }

    override fun onBindViewHolder(p0: adaptador_solicitud.Viewholder, pos: Int) {

//        var solicitud = lista_credito.get(pos)
//
//        p0.tvnombre.setText(solicitud.nombre + " " + solicitud.apellido)
//        p0.tvmonto.setText(solicitud.monto.toString())
//        p0.img_delete.setOnClickListener {
//
//            it.context.alert {
//                title = "Eliminando"
//                message = "Quiere eliminar este Desembolso?"
//
//                positiveButton("Si") {
//                    val crud = crudsqlite(p0.itemView.context)
//                    crud.Eliminar_una_solicitud(solicitud);
//
//                    Toast.makeText(p0.itemView.context, "Desembolso Eliminado", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                negativeButton("No") {
//                    //Do something
//                }
//            }.show()
        }
    }
