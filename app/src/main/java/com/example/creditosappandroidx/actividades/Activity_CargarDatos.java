package com.example.creditosappandroidx.actividades;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.crudWebservice;
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

import cswebservice.datospublicoskt;


public class Activity_CargarDatos extends AppCompatActivity {

  ImageView img_actualizarcredito;
  Button btinsertar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity__cargar_datos);
    img_actualizarcredito = findViewById(R.id.img_actualizar_credito_cliente);
    btinsertar = findViewById(R.id.btinsertar_cuotasPruebas);
    if (!datospublicoskt.INSTANCE.getEntorno_prueba()) {
      btinsertar.setVisibility(View.GONE);
    }

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    datospublicoskt.INSTANCE.obtenerNombreCobrador(getApplicationContext());

    img_actualizarcredito.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        crudsqlite crud = new crudsqlite(getApplicationContext());

        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
        dialog.setTitle("Actualizando creditos");
        dialog.setMessage("Esta seguro que desea actualizar datos de los creditos?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            SQLite.delete().from(creditocliente.class).execute();
            crud.DeleteAllCobradores();
            crudWebservice_laravel crudweb = new crudWebservice_laravel(getApplicationContext());
//                        crudweb.consultarCreditoCliente(v);
//                        CobradorService.INSTANCE.ObtenerCobradores(v);

          }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

          }
        });
        dialog.show();
      }
    });

    btinsertar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        crudsqlite crud = new crudsqlite(getApplicationContext());
        ArrayList<creditocliente> listacliente = crud.consultaTodo();

        for (int j = 0; j < 10; j++) {
          for (int i = 0; i < listacliente.size() - 1; i++) {
            cuotas c = new cuotas();
            c.setFecha("2021-02-08");
            c.setFechahora("2021-02-08 16:13:59");
            c.setMonto(i);
            c.setPrestamo_prestamoid(listacliente.get(i).getPrestamoid());
            c.setPendiente(1);
            c.setCuentaid(0);
            c.setMora(0);
            c.save();
          }
        }
        Toast.makeText(getApplicationContext(), "Cuotas Insertadas", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home: //hago un case por si en un futuro agrego mas opciones

        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }


  }

  public void iv_limpiarbd(View view) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Eliminando datos de la Base de datos");
    dialog.setMessage("Esta accion eliminara todos los datos almacenados en el telefono,Esta seguro de continuar?");
    dialog.setCancelable(false);
    dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

        SQLite.delete().from(cuotas.class).execute();
        SQLite.delete().from(creditocliente.class).execute();
        if (SQLite.select().from(cuotas.class).count() == 0) {
          Toast.makeText(getApplicationContext(), "Cuotas Eliminadas Correctamente", Toast.LENGTH_SHORT).show();
        }
        if (SQLite.select().from(creditocliente.class).count() == 0) {
          Toast.makeText(getApplicationContext(), "Creditos Eliminados Correctamente", Toast.LENGTH_SHORT).show();
        }

      }
    });
    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    dialog.show();
  }

  public void update_credito_cliente_onclick(View view) {
    crudsqlite crud = new crudsqlite(getApplicationContext());

    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Actualizando creditos");
    dialog.setMessage("Esta seguro que desea actualizar datos de los creditos?");
    dialog.setCancelable(false);
    dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        SQLite.delete().from(creditocliente.class).execute();
        crud.DeleteAllCobradores();
        crudWebservice_laravel crudLaravel = new crudWebservice_laravel(getApplicationContext());
//                crudLaravel.consultarCreditoCliente(view);
//                CobradorService.INSTANCE.ObtenerCobradores(view);
      }
    });
    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    dialog.show();
  }

  public void im_cargarcuotas_onclick(View view) {
    //CustomProgressDialog progressDialog = new CustomProgressDialog();

    final crudsqlite crud = new crudsqlite(this);
    final crudWebservice crudweb = new crudWebservice(this);
    final crudWebservice_laravel
      crudLaravel = new crudWebservice_laravel(this);

    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle("Cargando Cuotas del Servidor");
    dialog.setMessage("Esta accion eliminara todas las cuotas almacenadas en el telefono,");
    dialog.setCancelable(false);
    dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        crud.Eliminar_Todas_cuotas();
        crud.Eliminar_Todas_Moras();
//                crudLaravel.consultarCuotasAServidor(view);
        crudLaravel.obtenerMoras(view);
//                PlanesPagoService.INSTANCE.ObtenerPlanesPagoPorCobrador();

      }
    });
    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    dialog.show();

  }
}
