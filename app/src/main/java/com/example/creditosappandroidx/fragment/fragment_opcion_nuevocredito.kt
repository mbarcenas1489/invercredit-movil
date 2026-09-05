//package com.example.creditosappandroidx.fragment
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.example.creditosappandroidx.R
//import com.example.creditosappandroidx.actividades.solicitud_credito.activity_datoscliente
//import com.example.creditosappandroidx.actividades.solicitud_credito.activity_select_cliente
//import com.example.creditosappandroidx.databinding.ActivityMainBinding
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//import cswebservice.datospublicoskt
//
//
//class fragment_opcion_nuevocredito : BottomSheetDialogFragment() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = ActivityMainBinding.inflate(layoutInflater)
//
//        val root = inflater.inflate(R.layout.fragment_opcion_nuevocredito, container, false)
//        datospublicoskt.cli_exitente=false
//        datospublicoskt.reiniciar_nuevo_credito()
//        root.img_cliente_existente.setOnClickListener {
//            datospublicoskt.cli_exitente=true
//            var intent= Intent(root.context, activity_select_cliente::class.java)
//            startActivity(intent)
//        }
//        root.img_cliente_nuevo.setOnClickListener {
//            var intent= Intent(root.context, activity_datoscliente::class.java)
//            startActivity(intent)
//        }
//        return root
//    }
//
//
//}