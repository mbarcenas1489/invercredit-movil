package com.example.creditosappandroidx.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.creditosappandroidx.Adaptadores.adaptadorlistview;
import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.actividades.AddCuota.fragmentAddCuotaAbono;
import com.example.creditosappandroidx.actividades.AddCuota.fragment_add_cuota;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.crudWebservice;
import com.example.creditosappandroidx.cswebservice.crudWebservice_laravel;
import com.example.creditosappandroidx.cswebservice.cspublic;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.datospublicos;
import com.example.creditosappandroidx.cswebservice.moras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import cswebservice.datospublicoskt;


public class activity_cuotas extends AppCompatActivity implements fragment_add_cuota.CallbackAddCuota {

    TextView etabonado;
    TextView etmora;
    TextView etpendiente;
    TextView etmontototal;
    TextView lbnombr;
    ListView listviewcuotas;
    ArrayList<cuotas> listacuotas;
    ArrayList<moras> lista_moras;

    FloatingActionButton floating_add_cuota;
    ConstraintLayout constraincuotas;
    Toolbar myToolbar;
    com.example.creditosappandroidx.cssqlite.crudsqlite crudsqlite;
    adaptadorlistview adap;
    Date fechahoy = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuotas2);
        lbnombr = (TextView) findViewById(R.id.lbnombre);
        datospublicoskt.INSTANCE.setContext_cuotas_por_cliente(this);
        datospublicos.cuotahoy = false;
        datospublicos.MORA_HOY = false;
        constraincuotas = (ConstraintLayout) findViewById(R.id.contrain_cuotas);

        etabonado = (TextView) findViewById(R.id.et_abonado);
        etmora = (TextView) findViewById(R.id.et_mora);
        etpendiente = (TextView) findViewById(R.id.et_pendiente);
        etmontototal = (TextView) findViewById(R.id.et_montototal);

        floating_add_cuota = findViewById(R.id.add_cuota_flotante);

        lbnombr.setText(datospublicos.creditocliente.getNombre() + " " + datospublicos.creditocliente.getApellido());
        listviewcuotas = (ListView) findViewById(R.id.listviewcuotas);
        llenarlistview();

        registerForContextMenu(listviewcuotas);
        calcularmontos();

        crudWebservice_laravel webservice = new crudWebservice_laravel(this);
        webservice.isConnectServer(constraincuotas,
                "Error al conectar al servidor",
                "Las cuotas se guardaran en el telefono, posteriormente se enviaran al servidor");

    }


    void existeCuotasYMoraHoy() {
        String fecha_ultima_cuota = listacuotas.size() > 0 ? listacuotas.get(0).getFecha() : "";
        String fecha_ultima_mora = lista_moras.size() > 0 ? lista_moras.get(0).getFecha() : "";

        if (fecha_ultima_cuota.equalsIgnoreCase(cspublic.getfechahoy_yyyy_MM_dd())) {
            datospublicos.cuotahoy = true;
        }


        if (fecha_ultima_mora.equalsIgnoreCase(cspublic.getfechahoy_yyyy_MM_dd())) {
            datospublicos.MORA_HOY = true;
        }
        if (datospublicos.cuotahoy && datospublicos.MORA_HOY)
            floating_add_cuota.setVisibility(View.GONE);
    }

     void llenarlistview() {
        crudsqlite = new crudsqlite(this);

        listacuotas = crudsqlite.consultacuotaByPrestamoID(datospublicos.creditocliente.getPrestamoid());

        //LLENAR LISTA DE MORAS
        lista_moras = crudsqlite.consultaMORAByPrestamoID(datospublicos.creditocliente.getPrestamoid());
        datospublicos.listacuotas = listacuotas;
        if (listacuotas.size() > 0)
            datospublicos.ultimaCuota = listacuotas.get(0);


        adap = new adaptadorlistview(this, listacuotas);

        listviewcuotas.setAdapter(adap);
        existeCuotasYMoraHoy();

    }

    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(getBaseContext(), "On Resume", Toast.LENGTH_SHORT).show();

    }

    void calcularmontos() {

        //  listacuotas = crudsqlite.consultacuotaByPrestamoID(datospublicos.creditocliente.getPrestamoid());

        datospublicoskt.INSTANCE.actualizar_Badge(this);
        float abonado = 0;
        float pendiente = 0f;
        for (int i = 0; i < listacuotas.size(); i++) {
            if (listacuotas.get(i).getMora() == 0) {
                abonado += listacuotas.get(i).getMonto();
                pendiente += listacuotas.get(i).getPendiente();
            }
        }
        datospublicos.creditocliente.setMonto_pendiente(datospublicos.creditocliente.getMonto_a_pagar() - abonado);
        datospublicos.creditocliente.save();
        datospublicoskt.INSTANCE.setMonto_pendiente(pendiente);
        etabonado.setText(String.valueOf(abonado));

        etpendiente.setText(String.valueOf((datospublicos.creditocliente.getMonto_a_pagar() - abonado) + crudsqlite.totalMora_by_idprestamo(datospublicos.creditocliente.getPrestamoid())));
        etmontototal.setText(String.valueOf(datospublicos.creditocliente.getMonto_a_pagar()));
        etmora.setText(String.valueOf(crudsqlite.totalMora_by_idprestamo(datospublicos.creditocliente.getPrestamoid())));
        listacuotas = crudsqlite.consultacuotaByPrestamoID(datospublicos.creditocliente.getPrestamoid());

        adap = new adaptadorlistview(this, listacuotas);
        listviewcuotas.setAdapter(adap);
    }

    @Override
    protected void onRestart() {

        if (datospublicoskt.INSTANCE.getCuota_insertada()) {
            datospublicoskt.INSTANCE.setCuota_insertada(false);
            finish();
        }
        calcularmontos();
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cuotas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home)
        {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.menu_vermoras) {
            Intent intent = new Intent(this, activity_VerMora.class);
            startActivity(intent);
        }


        if (item.getItemId() == R.id.menu_buscarcuotasServidor) {
            final crudWebservice crud = new crudWebservice(this);

            //Si no hay cuotas en la base de datos local, pedir todas las cuotas de ese credito al servidor
            if (crudsqlite.consultacuotaByPrestamoID(datospublicos.creditocliente.getPrestamoid()).size() == 0) {
                //Toast.makeText(getBaseContext(), datospublicos.creditocliente.getNombre(), Toast.LENGTH_SHORT).show();

                crud.consultarcoutasByIdprestamo(datospublicos.creditocliente.getPrestamoid());


            } else {

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Obteniendo Cuotas del Servidor");
                dialog.setMessage("Esta accion eliminara las cuotas almacenadas en el Telefono,Desea Continuar?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Toast.makeText(getBaseContext(), datospublicos.creditocliente.getNombre(), Toast.LENGTH_SHORT).show();

                        crud.consultarcoutasByIdprestamo(datospublicos.creditocliente.getPrestamoid());
                        crud.consultarMora_by_Idprestamo(datospublicos.creditocliente.getPrestamoid());
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
        if (item.getItemId() == R.id.menu_ve_informacion_credito) {
            ver_informacion_credito();
        }

        if (item.getItemId() == R.id.menu_EnviarcuotasServidor) {
            final crudWebservice crud = new crudWebservice(this);

            //Si hay cuotas en la base de datos local, enviarla al servidor
            ArrayList<cuotas> lcuotas = crudsqlite.consultacuotaByPrestamoID(datospublicos.creditocliente.getPrestamoid());

            if (lcuotas.size() > 0) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                dialog.setTitle("Enviando Cuotas al servidor");
                dialog.setMessage("Asegurese de eliminar las cuotas existentes en servidor, Esta accion puede crear cuotas duplicadas,Esta seguro de continuar?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j = 0; j < lcuotas.size(); j++) {
                            cuotas c = lcuotas.get(j);
                            crud.InsertarCuota_mora(c.getFecha(), c.getMonto(), c.getPrestamo_prestamoid(), c.getMora(), c.getSaldo());
                        }


                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();


            } else {


                Toast.makeText(getBaseContext(), "No hay cuotas Guardadas", Toast.LENGTH_SHORT).show();

            }


        }

        return super.onOptionsItemSelected(item);
    }


    public void bt_floating_addCuota(View view) {

        //Si no existe la ultima cuota es por que no se ha registrado ninguan cuota
        //Si el pendiente de la ultima cuota es 0, se va a insertar una nueva cuota
        fragment_add_cuota dialog = new fragment_add_cuota();


        if (datospublicos.ultimaCuota == null || datospublicos.ultimaCuota.getPendiente() == 0) {
            // Toast.makeText(this, "Nueva cuota", Toast.LENGTH_SHORT).show();

            new fragment_add_cuota().show(getSupportFragmentManager(), "Add Nueva Cuota");

            return;
        }

        //Si el tipo de pago es diario validar que la cuota anterior sea del mismo dia
        switch (datospublicos.creditocliente.getTipopago()) {
            case 1: {
                new fragment_add_cuota().show(getSupportFragmentManager(), "Add Nueva Cuota");

                /*if (datospublicoskt.INSTANCE.compareWithToday(datospublicos.ultimaCuota.getFecha())) {
                    Toast.makeText(this, "Abono a Cuota", Toast.LENGTH_SHORT).show();

                } else {
                     new fragment_add_cuota().show(getSupportFragmentManager(), "Add Nueva Cuota");

                }*/
                break;

            }
            case 2: {
                if (datospublicoskt.INSTANCE.fecha_en_rango_semanal(datospublicos.ultimaCuota.getFecha())) {
                    Toast.makeText(this, "Abono a Cuota", Toast.LENGTH_SHORT).show();
                    new fragmentAddCuotaAbono().show(getSupportFragmentManager(), "Add Nueva Cuota");

                } else {
                    // Toast.makeText(this, "Nueva cuota Tipo Pago 1", Toast.LENGTH_SHORT).show();
                    new fragment_add_cuota().show(getSupportFragmentManager(), "Add Nueva Cuota");

                }
                break;

            }
            case 3: {
                if (datospublicoskt.INSTANCE.fecha_rango_quincenal(datospublicos.ultimaCuota.getFecha())) {
                    // Toast.makeText(this, "Abono a Cuota", Toast.LENGTH_SHORT).show();
                    new fragmentAddCuotaAbono().show(getSupportFragmentManager(), "Add Nueva Cuota");

                } else {
                    //Toast.makeText(this, "Nueva cuota Tipo Pago 1", Toast.LENGTH_SHORT).show();
                    new fragment_add_cuota().show(getSupportFragmentManager(), "Add Nueva Cuota");

                }
                break;
            }
        }


    }


    void ver_informacion_credito() {

        Intent intent = new Intent(this, Activity_detallecredito.class);
        startActivity(intent);
    }

    @Override
    public void insertSlider(@NonNull String texto) {
        llenarlistview();
    }
    public static void ActualizarCuotas(){
      //  llenarlistview();
    }
}


class cuotaDateComparator implements Comparator<cuotas> {
    @Override
    public int compare(cuotas shop1, cuotas shop2) {
        return new Integer(shop1.getnumerocuota()).
                compareTo(new Integer(shop2.getnumerocuota()));

    }
}

