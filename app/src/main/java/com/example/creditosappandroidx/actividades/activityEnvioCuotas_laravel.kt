package com.example.creditosappandroidx.actividades

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.creditosappandroidx.R
import com.example.creditosappandroidx.cssqlite.crudsqlite
import com.example.creditosappandroidx.cswebservice.*
import com.example.creditosappandroidx.databinding.ActivityCargaDatosLaravelBinding
import com.google.android.material.snackbar.Snackbar
import cswebservice.datospublicoskt
import java.util.*

class activityEnvioCuotas_laravel : AppCompatActivity() {
    private lateinit var binding: ActivityCargaDatosLaravelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCargaDatosLaravelBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_carga_datos_laravel)


        //datospublicoskt.progressbar = binding.llProgressBar
        val crudsqlite = crudsqlite(applicationContext)

        var cuotasBD: ArrayList<cuotas?>? = null
        var morasBD: ArrayList<moras?>? = null

        cuotasBD = crudsqlite.consultaCuotasNuevas()
        morasBD = crudsqlite.ConsultaTodas_Moras()

        binding.tvCantidadCuotasNuevas.setText(cuotasBD.size.toString())
//    tv_cantidad_moras_nuevas.setText(morasBD.size.toString())

        binding.btenviarCuotas.setOnClickListener {
            binding.llProgressBar.root.visibility = View.VISIBLE
            Toast.makeText(applicationContext, "Inicinado envio de cuotas", Toast.LENGTH_SHORT)
                .show()

            val crudweb_laravel = crudWebservice_laravel(application)

            if (cuotasBD.size == 0) {
                val snack = Snackbar.make(it, "No hay cuotas Nuevas", Snackbar.LENGTH_LONG)
                snack.show()
                return@setOnClickListener
            }
            if (cuotasBD.size > 0) {
                crudweb_laravel.enviarCuotasServidor_mejorada(
                    cuotasBD,
                    binding.contrainEnviarCuotas
                )
            }
            if (morasBD.size > 0) {
                crudweb_laravel.enviarMorasServidor(morasBD, binding.contrainEnviarCuotas)
            }
            binding.btenviarCuotas.isEnabled = false
        }
        validateNetwork()
    }

    fun validateNetwork() {
        if (datospublicoskt.conexion_server == "Local" && network.validIPServer(applicationContext) == false) {
            Snackbar.make(
                binding.contrainEnviarCuotas,
                "La Direccion del servidor no es correcta",
                Snackbar.LENGTH_LONG
            ).show();
            binding.btenviarCuotas.visibility = View.GONE
            return;
        }
        val webservice = crudWebservice_laravel(this)
        webservice.isConnectServer(binding.contrainEnviarCuotas, "", "")
    }
}