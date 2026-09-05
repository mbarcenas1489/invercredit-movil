package com.example.creditosappandroidx.actividades.Cuotas.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.creditosappandroidx.cswebservice.datospublicos
import com.example.creditosappandroidx.databinding.FragmentDetalleCreditoBinding
import cswebservice.datospublicoskt.compareWithToday

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentDetalleCredito.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentDetalleCredito : Fragment() {
  private lateinit var binding: FragmentDetalleCreditoBinding

  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentDetalleCreditoBinding.inflate(inflater, container, false)
    viewFragment = binding.root

    llenarDatos()
    return viewFragment

  }

  fun llenarDatos() {

    binding.tvNombreDetalleCredito.setText(datospublicos.creditocliente.nombre + " " + datospublicos.creditocliente.apellido)
    binding.tvDetalleCreditoDireccion.setText(datospublicos.creditocliente.direccion)
    binding.tvDetalleCreditoTipoNegocio.setText(datospublicos.creditocliente.tipopago.toString())
    binding.tvCedulaDetalleCredito.setText(datospublicos.creditocliente.cedula)
    binding.tvDetalleCreditoTelefono.setText(datospublicos.creditocliente.telefono)
    binding.tvMontoDetalleCredito.setText("C$ " + datospublicos.creditocliente.monto.toString())
    binding.tvInteresDetalleCredito.setText(datospublicos.creditocliente.interes.toString() + " %")
    binding.tvMontoInteresDetalleCredito.setText(
      "C$ " +
        (datospublicos.creditocliente.monto *
          (datospublicos.creditocliente.interes / 100))
          .toString()
    )

    binding.tvMontoPagarDetalleCredito.setText("C$ " + datospublicos.creditocliente.monto_a_pagar.toString())
    binding.tvPendienteDetalleCredito.setText("C$ " + datospublicos.creditocliente.monto_pendiente.toString())
    binding.tvFechaFin.setText(datospublicos.creditocliente.fechafin())
    binding.tvAbonadoDetalleCredito.setText(
      "C$ " + datospublicos.creditocliente.getabonado(
        viewFragment!!.context
      ).toString()
    )
    binding.tvEstadoDetalleCredito.setText(compareWithToday(datospublicos.creditocliente.fechafin).toString())
    binding.tvFechaInicial.setText(datospublicos.creditocliente.fechainicial.toString())

  }

  companion object {
    private var viewFragment: View? = null
  }

}