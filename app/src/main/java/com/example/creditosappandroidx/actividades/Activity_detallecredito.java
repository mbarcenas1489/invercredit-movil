package com.example.creditosappandroidx.actividades;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cswebservice.datospublicos;

import cswebservice.datospublicoskt;

public class Activity_detallecredito extends AppCompatActivity {

  TextView etpagosabonados,
    etcuotasabonadas,
    etcuotaspendientes,
    etMontoInteres,
    etnombre,
    ettiponegocio,
    etcedula,
    etdireccion,
    etfechainicial,
    etplazo,
    ettelefono,
    etmonto,
    etinteres,
    ettotalpagar,
    etpendiente,
    tvEstado,
    etfechafin,
    tvDireccion,
    tvTipoNegocio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detallecredito);
    etnombre = (TextView) findViewById(R.id.tv_nombre_detalle_credito);
    etcedula = (TextView) findViewById(R.id.tv_cedula_detalle_credito);
    ettelefono = (TextView) findViewById(R.id.tvDetalleCredito_Telefono);
    ettelefono = (TextView) findViewById(R.id.tvDetalleCredito_Telefono);
    etmonto = (TextView) findViewById(R.id.tv_monto_detalle_credito);
    etinteres = (TextView) findViewById(R.id.tv_interes_detalle_credito);
    etMontoInteres = (TextView) findViewById(R.id.tv_monto_interes_detalle_credito);
    ettotalpagar = (TextView) findViewById(R.id.tv_monto_pagar_detalle_credito);
    etpendiente = (TextView) findViewById(R.id.tv_pendiente_detalle_credito);
    etfechainicial = (TextView) findViewById(R.id.tvFechaInicial);
    etfechafin = (TextView) findViewById(R.id.tvFechaFin);
    etpagosabonados = findViewById(R.id.tv_abonado_detalle_credito);
    tvEstado = findViewById(R.id.tv_estado_detalle_credito);
    tvDireccion = findViewById(R.id.tvDetalleCredito_Direccion);
    tvTipoNegocio = findViewById(R.id.tvDetalleCredito_TipoNegocio);


    etnombre.setText(datospublicos.creditocliente.getNombre() + " " + datospublicos.creditocliente.getApellido());
    tvDireccion.setText(datospublicos.creditocliente.getDireccion());
    tvTipoNegocio.setText(String.valueOf(datospublicos.creditocliente.getTipopago()));
    etcedula.setText(datospublicos.creditocliente.getCedula());
    ettelefono.setText(datospublicos.creditocliente.getTelefono());
    etmonto.setText("C$ " + String.valueOf(datospublicos.creditocliente.getMonto()));
    etinteres.setText(String.valueOf(datospublicos.creditocliente.getInteres()) + " %");
    etMontoInteres.setText("C$ " + String.valueOf(
      (datospublicos.creditocliente.getMonto() *
        (datospublicos.creditocliente.getInteres() / 100)
      )

    ));

    ettotalpagar.setText("C$ " + String.valueOf(datospublicos.creditocliente.getMonto_a_pagar()));
    etpendiente.setText("C$ " + String.valueOf(datospublicos.creditocliente.getMonto_pendiente()));
    etfechafin.setText(datospublicos.creditocliente.fechafin());
    etpagosabonados.setText("C$ " + String.valueOf(datospublicos.creditocliente.getabonado(this)));
    tvEstado.setText(String.valueOf(datospublicoskt.INSTANCE.compareWithToday(datospublicos.creditocliente.getFechafin())));
    etfechainicial.setText(datospublicos.creditocliente.getFechainicial().toString());

  }
}
