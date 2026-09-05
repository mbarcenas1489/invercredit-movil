package com.example.creditosappandroidx.actividades;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.crudWebservice;
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel;
import com.example.creditosappandroidx.cswebservice.cspublic;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.datospublicos;
import com.example.creditosappandroidx.cswebservice.moras;
import com.example.creditosappandroidx.cswebservice.network;

import java.util.ArrayList;


public class Activity_enviarcuotas extends AppCompatActivity {

  ImageView btbuscarcuotas;
  ImageView btenviar;
  TextView etnumerocuotas;
  TextView etnumero_moras;
  ConstraintLayout constraintLayout;

  TextView et_cant_cuotas_enviadas;
  TextView et_cant_moras_enviadas;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_enviarcuotas);
    btbuscarcuotas = (ImageView) findViewById(R.id.iv_buscarcuotas);
    btenviar = (ImageView) findViewById(R.id.iv_enviaralservidor);
    etnumerocuotas = (TextView) findViewById(R.id.et_num_cuotasnuevas);
    etnumero_moras = (TextView) findViewById(R.id.et_moras_nuevas);
    et_cant_cuotas_enviadas = (TextView) findViewById(R.id.et_cant_cuotas_enviadas);
    et_cant_moras_enviadas = (TextView) findViewById(R.id.et_cant_moras_enviadas);
    constraintLayout = (ConstraintLayout) findViewById(R.id.constrain_enviocuota);

    et_cant_cuotas_enviadas.setVisibility(View.GONE);
    et_cant_moras_enviadas.setVisibility(View.GONE);
    cspublic.tv_cuotas_enviadas = et_cant_cuotas_enviadas;
    cspublic.tv_moras_enviadas = et_cant_moras_enviadas;

    btbuscarcuotas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        crudWebservice crudweb = new crudWebservice(getApplication());

        cspublic.can_cuotas_enviadas = 0;
        cspublic.can_moras_enviadas = 0;
        crudweb.BuscarCuotasNuevas(etnumerocuotas);
        crudweb.BuscarMorasNuevas(etnumero_moras);
      }
    });

    btenviar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(getApplicationContext(), "Inicinado envio de cuotas", Toast.LENGTH_SHORT).show();

        final crudWebservice_laravel crudweb_laravel = new crudWebservice_laravel(getApplication());
        final crudsqlite crudsqlite = new crudsqlite(getApplicationContext());
        ArrayList<cuotas> cuotasBD = null;

        cuotasBD = crudsqlite.ConsultaTodas_CUOTAS();
        Toast.makeText(getApplicationContext(), "Enviando " + String.valueOf(cuotasBD.size()), Toast.LENGTH_SHORT).show();

        crudweb_laravel.enviarCuotasServidor(cuotasBD, constraintLayout);
      }
    });


  }


}
