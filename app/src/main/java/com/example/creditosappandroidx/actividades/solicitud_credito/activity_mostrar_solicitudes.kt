//package com.example.creditosappandroidx.actividades.solicitud_credito
//
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.LinearLayout
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.creditosappandroidx.Adaptadores.adaptador_solicitud
//import com.example.creditosappandroidx.R
//import com.example.creditosappandroidx.actividades.AddCuota.fragment_add_cuota
//import com.example.creditosappandroidx.cssqlite.crudsqlite
//import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel
//import com.example.creditosappandroidx.fragment.fragment_opcion_nuevocredito
//import cswebservice.datospublicoskt
//import kotlinx.android.synthetic.main.activity_mostrar_solicitudes.*
//import org.jetbrains.anko.alert
//import org.jetbrains.anko.toast
//
//class activity_mostrar_solicitudes : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_mostrar_solicitudes)
//        recycle_solicitudes.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL, false)
//
//        datospublicoskt.cargar_todas_solicitudes(this)
//
//
//       cargardatos()
//
//        add_nueva_solicitud.setOnClickListener {
//           // var intent=Intent(applicationContext, activity_solicitud_credito::class.java)
//            //startActivity(intent)
//            fragment_opcion_nuevocredito().show(supportFragmentManager, "Add Nueva Cuota")
//
//        }
//    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_dessembolso, menu)
//        //menu.findItem(R.id.add_cliente).setVisible(false)
//        //menu_addcliente=menu.findItem(R.id.add_cliente)
//        return true
//    }
//
//    override fun onRestart() {
//        cargardatos()
//        /*datospublicoskt.cli_exitente=false
//        if(datospublicoskt.solicitud_guardada)
//        {
//            datospublicoskt.solicitud_guardada=false
//            finish()
//        }*/
//        super.onRestart()
//    }
//
//    fun cargardatos()
//    {
//        datospublicoskt.cargar_todas_solicitudes(this)
//        var adapter = adaptador_solicitud(datospublicoskt.lista_solicitudes)
//        recycle_solicitudes.adapter=adapter
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//
//
//            R.id.menu_desembolso_eliminar_todo -> {
//                val crud = crudsqlite(applicationContext)
//                alert {
//                    title = "Eliminando"
//                    message = "Quiere eliminar todas los desembolsos?"
//                    title = "Eliminando"
//                    positiveButton("Si") {
//                        crud.Eliminar_Todas_solicitudes();
//                        cargardatos()
//
//                    }
//                    negativeButton("No") {
//                        //Do something
//                    }
//
//
//                }.show()
//
//                return true;
//
//            }
//            R.id.menu_desembolso_enviar -> {
//
//                alert {
//
//                    message = "Desea enviar los Desembolsos al servidor?"
//                    title = "Eliminando"
//                    positiveButton("Si") {
//
//                        var webservice = crudWebservice_laravel(applicationContext);
//                        if (datospublicoskt.lista_solicitudes.size == 0) {
//                            toast("No hay solicitudes de Creditos")
//                            return@positiveButton
//                        }
//                        webservice.guardar_solicitud(datospublicoskt.lista_solicitudes,recycle_solicitudes);
//
//                    }
//                    negativeButton("No") {
//                        //Do something
//                    }
//
//
//                }.show()
//
//
//
//
//
//
//                return true;
//
//            }
//
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//}