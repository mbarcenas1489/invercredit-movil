package com.example.creditosappandroidx.actividades.AddCuota

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.creditosappandroidx.actividades.Cuotas.fragment.FragmentTabCuotas
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel
import com.example.creditosappandroidx.cswebservice.cuotas
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.cswebservice.moras
import com.example.creditosappandroidx.databinding.ActivityInsertarcuotaBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import cswebservice.datospublicoskt
import cswebservice.datospublicoskt.cuota_insertada
import cswebservice.datospublicoskt.getfecha_format

import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [fragment_add_cuota.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_add_cuota() : BottomSheetDialogFragment() {

    private lateinit var binding: ActivityInsertarcuotaBinding
    var montocuota = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var mCallback: CallbackAddCuota? = null
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
//    mCallback = activity as CallbackAddCuota
    }

    interface CallbackAddCuota {
        fun insertSlider(texto: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ActivityInsertarcuotaBinding.inflate(inflater, container, false)


        if (datospublicoskt.monto_pendiente > 0) {

            binding.ctmontoPendiente.setText("Pendiente " + datospublicoskt.monto_pendiente)
        } else {
            binding.ctmontoPendiente.visibility = View.GONE
        }
        binding.textfecha.setText(getfecha_format())
        montocuota = Math.ceil(
            datospublicos.creditocliente.monto_a_pagar.toDouble() / datospublicos.creditocliente.plazo
        )
            .toInt()

        binding.ctmonto1.setText(montocuota.toString())


        val c1 = Calendar.getInstance()
        var mes = ""
        var dia = ""
        mes =
            if (c1[Calendar.MONTH] + 1 < 10) "0" + (c1[Calendar.MONTH] + 1) else (c1[Calendar.MONTH] + 1).toString()


        dia = if (c1[Calendar.DAY_OF_MONTH] < 10) "0" + c1[Calendar.DAY_OF_MONTH] else c1[Calendar.DAY_OF_MONTH].toString()

        binding.ctFecha.setText(c1[Calendar.YEAR].toString() + "-" + mes + "-" + dia)

        binding.imgGuardar.setOnClickListener {
            val c = cuotas()
            val m = moras()
            if (binding?.ctFecha?.getText()?.length == 0 || binding?.ctmonto1?.getText()?.length == 0) {
                Toast.makeText(binding?.root?.context, "Llene todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (validarUnaCuotaPorDia() || validarUnaMoraPorDia()) {
                return@setOnClickListener
            }

            if (binding?.rbmora?.isChecked()!!) {
                m.monto = binding?.ctmonto1?.getText().toString().toFloat()
                m.fecha = binding?.ctFecha?.getText().toString()
                m.idprestamo = datospublicos.creditocliente.prestamoid
                c.saldo = datospublicos.creditocliente.monto_pendiente
                c.fechahora = datospublicoskt.getDateHoursNow();
                m.save()
            } else {
                c.monto = binding?.ctmonto1?.getText().toString().toFloat()
                c.fecha = binding?.ctFecha?.getText().toString()
                c.prestamo_prestamoid = datospublicos.creditocliente.prestamoid
                c.mora = 1
                c.saldo = datospublicos.creditocliente.monto_pendiente - c.monto
                c.mora = 0
                c.fechahora = datospublicoskt.getDateHoursNow();
                if (montocuota > c.monto) {
                    c.pendiente = montocuota - c.monto;
                } else {
                    datospublicoskt.actualizar_pendiente(c.monto - montocuota)
                }
                val webservice = crudWebservice_laravel(binding!!.root.context)



                if (c.save()) {
                    var saldo = 0f;

                    if (datospublicos.ultimaCuota == null) {
                        saldo = 0f;
                    } else {
                        saldo = datospublicos.ultimaCuota.saldo
                    }
                    datospublicoskt.imprimir_recibo(
                        c.fechahora,
                        c.monto,
                        datospublicos.creditocliente.nombre + " " + datospublicos.creditocliente.apellido,
                        c.saldo,
                        saldo,
                        datospublicos.creditocliente.moneda,
                        datospublicos.creditocliente.fechafin
                    )
                }
                datospublicos.creditocliente.pendiente = c.pendiente
                datospublicos.creditocliente.save()

            }
            cuota_insertada = true
            FragmentTabCuotas.LlenarListaCuotas()
            dismiss()
        }
        return binding.root
    }

    fun validarUnaCuotaPorDia(): Boolean {
        if (datospublicos.cuotahoy && !binding.rbmora.isChecked) {
            Toast.makeText(
              binding?.root?.context,
                "Ya se ha registrado una cuota el dia de hoy",
                Toast.LENGTH_SHORT
            ).show();
            return true
        }
        return false

    }

    fun validarUnaMoraPorDia(): Boolean {
        if (datospublicos.MORA_HOY && binding.rbmora.isChecked) {
            Toast.makeText(
              binding.root.context,
                "Ya se ha registrado una MORA el dia de hoy",
                Toast.LENGTH_SHORT
            ).show();
            return true
        }
        return false

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}