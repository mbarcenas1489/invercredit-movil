package com.example.creditosappandroidx.cswebservice;



import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.creditosappandroidx.cssqlite.crudsqlite;

import java.util.ArrayList;



public final class datospublicos
{
    public static creditocliente creditocliente;
    public static ArrayList<cuotas> listacuotas;
    public static ArrayList<moras> lista_mora_nueva;
    public  static String fechaultimacuota;
    public  static boolean cuotahoy=false;
    public  static boolean MORA_HOY=false;
    public static cuotas ultimaCuota;

    public  static  ConstraintLayout constrain_vencidos =null;
    public  static int obtener_cobrador(Context context)
    {
        SharedPreferences pref ;
        int idcobrador=0;
        pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        String cobrador= pref.getString("idcobrador","1");
        idcobrador=Integer.parseInt(cobrador);
        return idcobrador;

    }
    public static void CargarCuotas(Context context, View view)
    {
         //Toast.makeText(context,"Cargando Cuotas",Toast.LENGTH_SHORT).show();

        final crudsqlite crud=new crudsqlite(context);
        final crudWebservice_laravel crudweb=new crudWebservice_laravel(context);

        AlertDialog.Builder dialog=new AlertDialog.Builder(context);
        dialog.setTitle("Cargando Cuotas del Servidor");
        dialog.setMessage("Esta accion eliminara todas las cuotas almacenadas en el telefono,");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                crud.Eliminar_Todas_cuotas();
                crud.Eliminar_Todas_Moras();

                //crudweb.consultarCuotasAServidor(view,progressDialog);
//                crudweb.consultarCuotasAServidor(view);
                crudweb.obtenerMoras(view);

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
