package com.example.creditosappandroidx.actividades;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.creditosappandroidx.Adaptadores.AdapterRecaudacion;
import com.example.creditosappandroidx.Adaptadores.AdapterRecaudacion_mora;
import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.moras;


public class Activity_Recaudacionpordia extends AppCompatActivity implements TextWatcher {
  crudsqlite crud;
  ArrayList<cuotas> listacuotasdeldia;
  ArrayList<moras> moras_del_dia;
  ListView listview;
  EditText etfecha;
  TextView ettotalrecaudado;
  TextView etcant_cuota_recaudado;
  TextView tv_vermora;
  TextView tv_vercuotas_creditosmalos;
  TextView tv_creditosbuenos;
  TextView tv_num, tv_num_valor, tv_total_valor, tvtodas_cuotas;


  private static final String CERO = "0";
  private static final String BARRA = "/";


  public final Calendar c = Calendar.getInstance();

  //Variables para obtener la fecha
  final int mes = c.get(Calendar.MONTH);
  final int dia = c.get(Calendar.DAY_OF_MONTH);
  final int anio = c.get(Calendar.YEAR);


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity__recaudacionpordia);
    tv_num = findViewById(R.id.tv_num);
    tv_num_valor = findViewById(R.id.tv_num_valor);
    tv_total_valor = findViewById(R.id.tv_total_valor);

    listview = (ListView) findViewById(R.id.listaRecaudacion);
    etfecha = (EditText) findViewById(R.id.et_fechaRecaudacion);
    ettotalrecaudado = (TextView) findViewById(R.id.et_montorecaudado);
    etcant_cuota_recaudado = (TextView) findViewById(R.id.et_cantidadcuotasrecaudadas);
    tv_vermora = findViewById(R.id.tv_vermoras);
    tv_vercuotas_creditosmalos = findViewById(R.id.tv_vercuotas_creditosmalos);
    tv_creditosbuenos = findViewById(R.id.tv_creditos_buenos);
    tvtodas_cuotas = findViewById(R.id.tv_cuotas111);

    final DialogFragment dialogfragment = new DatePickerDialogTheme1();

    etfecha.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        dialogfragment.show(getFragmentManager(), "Theme 1");
      }
    });

    tvtodas_cuotas.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        crud = new crudsqlite(getApplication());
        if (etfecha.getText().toString().trim().length() > 0) {

          listacuotasdeldia = crud.ConsultaCuotasByFecha(etfecha.getText().toString());
          //moras_del_dia = crud.ConsultaMorasByFecha(etfecha.getText().toString());
          //  ArrayList<cuotas> cuotas_malos = crud.ConsultaCuotasByFecha_malos(etfecha.getText().toString());
          //ArrayList<cuotas> cuotas_buenos=crud

          Toast.makeText(getApplicationContext(), String.valueOf(listacuotasdeldia.size()), Toast.LENGTH_SHORT).show();


          AdapterRecaudacion adapter = new AdapterRecaudacion(getApplication(), listacuotasdeldia);
          listview.setAdapter(adapter);
          calculo_buenos(listacuotasdeldia);
          //mostrartotales(listacuotasdeldia);
          //mostrartotales_moras(moras_del_dia);
          //calculo_malos(cuotas_malos);
        }

      }
    });

    tv_creditosbuenos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        crud = new crudsqlite(getApplication());
        if (etfecha.getText().toString().trim().length() > 0) {

          listacuotasdeldia = crud.ConsultaCuotasByFecha_buenos(etfecha.getText().toString());
          //moras_del_dia = crud.ConsultaMorasByFecha(etfecha.getText().toString());
          ArrayList<cuotas> cuotas_malos = crud.ConsultaCuotasByFecha_malos(etfecha.getText().toString());
          //ArrayList<cuotas> cuotas_buenos=crud

          Toast.makeText(getApplicationContext(), String.valueOf(listacuotasdeldia.size()), Toast.LENGTH_SHORT).show();


          AdapterRecaudacion adapter = new AdapterRecaudacion(getApplication(), listacuotasdeldia);
          listview.setAdapter(adapter);
          calculo_buenos(listacuotasdeldia);
          //mostrartotales(listacuotasdeldia);
          //mostrartotales_moras(moras_del_dia);
          //calculo_malos(cuotas_malos);
        }

      }
    });

    tv_vercuotas_creditosmalos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (etfecha.getText().toString().trim().length() > 0) {
          crud = new crudsqlite(getApplication());

          //listacuotasdeldia = crud.ConsultaCuotasByFecha(etfecha.getText().toString());
          //moras_del_dia = crud.ConsultaMorasByFecha(etfecha.getText().toString());
          ArrayList<cuotas> cuotas_malos = crud.ConsultaCuotasByFecha_malos(etfecha.getText().toString());


          Toast.makeText(getApplicationContext(), String.valueOf(listacuotasdeldia.size()), Toast.LENGTH_SHORT).show();


          AdapterRecaudacion adapter = new AdapterRecaudacion(getApplication(), cuotas_malos);
          listview.setAdapter(adapter);
          //mostrartotales(listacuotasdeldia);
          //mostrartotales_moras(moras_del_dia);
          calculo_malos(cuotas_malos);
        }
      }
    });


    tv_vermora.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (etfecha.getText().toString().trim().length() > 0) {
          crud = new crudsqlite(getApplication());

          //listacuotasdeldia = crud.ConsultaCuotasByFecha(etfecha.getText().toString());
          moras_del_dia = crud.ConsultaMorasByFecha(etfecha.getText().toString());

          //ArrayList<cuotas> cuotas_malos = crud.ConsultaCuotasByFecha_malos(etfecha.getText().toString());

          Toast.makeText(getApplicationContext(), String.valueOf(listacuotasdeldia.size()), Toast.LENGTH_SHORT).show();


          AdapterRecaudacion_mora adapter = new AdapterRecaudacion_mora(getApplication(), moras_del_dia);
          listview.setAdapter(adapter);
          //mostrartotales(listacuotasdeldia);
          mostrartotales_moras(moras_del_dia);
          //calculo_malos(cuotas_malos);
        }

      }
    });

    etfecha.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

        crud = new crudsqlite(getApplication());
        if (etfecha.getText().toString().trim().length() > 0) {

          listacuotasdeldia = crud.ConsultaCuotasByFecha(etfecha.getText().toString());
          //moras_del_dia = crud.ConsultaMorasByFecha(etfecha.getText().toString());
          //ArrayList<cuotas> cuotas_malos = crud.ConsultaCuotasByFecha_malos(etfecha.getText().toString());


          Toast.makeText(getApplicationContext(), String.valueOf(listacuotasdeldia.size()), Toast.LENGTH_SHORT).show();


          AdapterRecaudacion adapter = new AdapterRecaudacion(getApplication(), listacuotasdeldia);
          listview.setAdapter(adapter);
          mostrartotales(listacuotasdeldia);
          //mostrartotales_moras(moras_del_dia);
          //calculo_malos(cuotas_malos);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  void mostrartotales(ArrayList<cuotas> lcuotas) {
    float total = 0;
    int ncuotas = 0;
    float total_cordobas = 0;
    float total_dollar = 0;
    for (int i = 0; i < lcuotas.size(); i++) {

      if (lcuotas.get(i).getprestamo().getMoneda().equalsIgnoreCase("Cordoba")) {
        total_cordobas += lcuotas.get(i).getMonto();

      } else
        total_dollar += lcuotas.get(i).getMonto();


      total += lcuotas.get(i).getMonto();
      ncuotas++;

    }
    ettotalrecaudado.setText("C$" + String.valueOf(total_cordobas) + " / $" + String.valueOf(total_dollar));
    etcant_cuota_recaudado.setText(String.valueOf(ncuotas));

    tv_num.setText("# Cuotas:");
    tv_num_valor.setText(String.valueOf(ncuotas));
    tv_total_valor.setText("C$" + String.valueOf(total_cordobas) + "/ $" + String.valueOf(total_dollar));
  }

  void calculo_malos(ArrayList<cuotas> lcuotas) {
    float total_cordobas = 0;
    float total_dollar = 0;
    float total = 0;
    int ncuotas = 0;
    for (int i = 0; i < lcuotas.size(); i++) {
      if (lcuotas.get(i).getprestamo().getMoneda().equalsIgnoreCase("Cordoba")) {
        total_cordobas += lcuotas.get(i).getMonto();

      } else
        total_dollar += lcuotas.get(i).getMonto();


      total += lcuotas.get(i).getMonto();
      ncuotas++;

    }
    tv_num.setText("# Cuotas:");
    tv_num_valor.setText(String.valueOf(ncuotas));
    tv_total_valor.setText("C$" + String.valueOf(total_cordobas) + "/ $" + String.valueOf(total_dollar));

    // tv_total_cuotas_malos.setText(String.valueOf(total));
    //tv_num_cuotas_malos.setText(String.valueOf(ncuotas));

  }

  void calculo_buenos(ArrayList<cuotas> lcuotas) {
    float total = 0;
    float total_cordobas = 0;
    float total_dollar = 0;

    int ncuotas = 0;
    for (int i = 0; i < lcuotas.size(); i++) {

      if (lcuotas.get(i).getprestamo().getMoneda().equalsIgnoreCase("Cordoba")) {
        total_cordobas += lcuotas.get(i).getMonto();

      } else
        total_dollar += lcuotas.get(i).getMonto();

      total += lcuotas.get(i).getMonto();
      ncuotas++;

    }
    tv_num.setText("# Cuotas:");
    tv_num_valor.setText(String.valueOf(ncuotas));
    tv_total_valor.setText("C$" + String.valueOf(total_cordobas) + "/ $" + String.valueOf(total_dollar));

    // tv_total_cuotas_malos.setText(String.valueOf(total));
    //tv_num_cuotas_malos.setText(String.valueOf(ncuotas));

  }

  void mostrartotales_moras(ArrayList<moras> lmoras) {
    float totalmoras = 0;
    float total_cordobas = 0;
    float total_dollar = 0;
    int nmoras = 0;
    for (int i = 0; i < lmoras.size(); i++) {
      if (lmoras.get(i).getprestamo().getMoneda().equalsIgnoreCase("Cordoba")) {
        total_cordobas += lmoras.get(i).getMonto();

      } else
        total_dollar += lmoras.get(i).getMonto();

      totalmoras += lmoras.get(i).getMonto();
      nmoras++;

    }
    tv_num.setText("# Moras:");
    tv_num_valor.setText(String.valueOf(nmoras));
    tv_total_valor.setText("C$" + String.valueOf(total_cordobas) + "/ $" + String.valueOf(total_dollar));

    //etnumeromoras.setText(String.valueOf(nmoras));
    //etmontomoras.setText(String.valueOf(totalmoras));

  }

  private void obtenerFecha() {
    DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
        final int mesActual = month + 1;
        //Formateo el día obtenido: antepone el 0 si son menores de 10
        String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
        //Formateo el mes obtenido: antepone el 0 si son menores de 10
        String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
        //Muestro la fecha con el formato deseado
        etfecha.setText(year + "-" + mesFormateado + "-" + diaFormateado);


      }
      //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
      /**
       *También puede cargar los valores que usted desee
       */
    }, anio, mes, dia);
    //Muestro el widget
    recogerFecha.show();

  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {

  }

  @Override
  public void afterTextChanged(Editable s) {

  }

  @Override
  public void onPointerCaptureChanged(boolean hasCapture) {

  }
}


