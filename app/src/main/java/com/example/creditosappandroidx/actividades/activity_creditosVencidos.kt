package com.example.root.myapplication


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.creditosappandroidx.Adaptadores.adapter_creditospendiente
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.creditocliente
import com.example.creditosappandroidx.cswebservice.crudWebservice
import com.example.creditosappandroidx.cswebservice.interfacesMetodos

class activity_creditosVencidos : AppCompatActivity(), interfacesMetodos {
  private var lista: MutableList<creditocliente> = mutableListOf()
  private val listafiltrada: MutableList<creditocliente> = mutableListOf()
  private var recyclerView: RecyclerView? = null
  private var mAdapter: adapter_creditospendiente? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_creditospendientes)





    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setDisplayShowHomeEnabled(true)
    val crud = crudsqlite(this)
    lista = crud.consultaTodo()
    recyclerView = findViewById<View>(R.id.recycle_creditopen) as RecyclerView
    mAdapter = adapter_creditospendiente(listafiltrada)

    prepareMovieData()
    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
    recyclerView!!.layoutManager = mLayoutManager
    recyclerView!!.itemAnimator = DefaultItemAnimator()
    recyclerView!!.adapter = mAdapter


  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_creditos_vencidos, menu)
    return true
  }


  /* override fun onContextItemSelected(item: MenuItem?): Boolean {
       if(item?.itemId  == R.id.menu_enviar_creditos_malos)
       {
           Toast.makeText(this,"enviando",Toast.LENGTH_SHORT).show()


       }
       return super.onContextItemSelected(item)
   }*/


  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      android.R.id.home -> {
        finish()
        true
      }

      R.id.menu_enviar_creditos_malos -> {
        // showHelp()
        val crud = crudsqlite(this)
        // Toast.makeText(this,"enviando",Toast.LENGTH_SHORT).show()
        var lista_todos: MutableList<creditocliente> = mutableListOf()
        lista_todos = crud.consultaTodo()
        val crudweb = crudWebservice(application)

        for (i in lista_todos.indices) {

          crudweb.setMalo(lista_todos.get(i).prestamoid, lista_todos.get(i).malo)

        }
        true
      }

      else -> super.onOptionsItemSelected(item)
    }

  }

  fun actualizardatos() {
    val crud = crudsqlite(this)
    lista = crud.consultaTodo()
    recyclerView = findViewById<View>(R.id.recycle_creditopen) as RecyclerView
    prepareMovieData()
    mAdapter = adapter_creditospendiente(listafiltrada)
    val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
    recyclerView!!.layoutManager = mLayoutManager
    recyclerView!!.itemAnimator = DefaultItemAnimator()
    recyclerView!!.adapter = mAdapter
  }

  private fun prepareMovieData() {
    listafiltrada.clear()
    for (i in lista.indices) {
      if (lista.get(i).obtener_dias_dias_atrasados() > 10) {
        val pendiente: Float = lista.get(i).getMonto_pendiente()
        val montocredito: Float = lista.get(i).getMonto_a_pagar()
        val porcentaje = (pendiente * 100 / montocredito).toInt()
        if (porcentaje >= 50) {
          listafiltrada.add(lista.get(i))
        }
      }
    }
    listafiltrada.sortedBy { creditocliente -> creditocliente.obtener_dias_dias_atrasados() }
    mAdapter!!.notifyDataSetChanged()
  }

  override fun Actualizarlista() {
    actualizardatos()
    mAdapter!!.notifyDataSetChanged()
  }
}