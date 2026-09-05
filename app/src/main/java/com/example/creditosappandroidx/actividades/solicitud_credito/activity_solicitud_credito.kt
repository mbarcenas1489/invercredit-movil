//package com.example.creditosappandroidx.actividades.solicitud_credito
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.creditosappandroidx.R
//import cswebservice.datospublicoskt
//import kotlinx.android.synthetic.main.activity_solicitud_credito.*
//
//class activity_solicitud_credito : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView( R.layout.activity_solicitud_credito)
//        datospublicoskt.cli_exitente=false
//        datospublicoskt.reiniciar_nuevo_credito()
//        btnuevocliente.setOnClickListener {
//            var intent= Intent(applicationContext,activity_datoscliente::class.java)
//            startActivity(intent)
//        }
//        btclienteexistente.setOnClickListener{
//            datospublicoskt.cli_exitente=true
//            var intent= Intent(applicationContext,activity_select_cliente::class.java)
//            startActivity(intent)
//        }
//
//
//    }
//
//    /*override fun onRestart() {
//        datospublicoskt.cli_exitente=false
//        if(datospublicoskt.solicitud_guardada)
//        {
//            datospublicoskt.solicitud_guardada=false
//            finish()
//        }
//        super.onRestart()
//    }*/
//}