package com.example.creditosappandroidx.cswebservice;

import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class cspublic {

    public static int can_cuotas_enviadas=0;
    public static int can_moras_enviadas=0;
    public static TextView fechafin=null;
    public static TextView fechacredito=null;
    public static TextView cuotadia=null;
    public static TextView numerocuota=null;
    public static TextView tv_cuotas_enviadas=null;
    public static TextView tv_moras_enviadas=null;
    public static  cuotas cuota_a_editar;

    public static void setTv_cuotas_enviadas()
    {
        if(tv_cuotas_enviadas!=null)
        tv_cuotas_enviadas.setText(String.valueOf(can_cuotas_enviadas)+" Cuotas enviadas correctamente");
    }
    public static void setTv_moras_enviadas()
    {
        if(tv_moras_enviadas!=null)
            tv_moras_enviadas.setText(String.valueOf(can_moras_enviadas)+" Moras enviadas correctamente");
    }
    public  static String getfechahoy()
    {
        Date fecha = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fechahoy = dateFormat.format(fecha);
        return fechahoy;
    }
    public  static String getfechahoy_yyyy_MM_dd()
    {
        Date fecha = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechahoy = dateFormat.format(fecha);
        return fechahoy;
    }
    public static String cambiar_formato(String fecha)
    {
        String[] solofeca=fecha.split(" ");
        String[] f=solofeca[0].split("-");
        String dia=f[2];
        String mes=f[1];
        String anyo=f[0];
        String meses[] =  {" ","Ene", "Febr", "Mar", "Abr",
                "May", "Jun", "Jul","Agos", "Sept", "Oct","Nob", "Dic"};

        Integer mes_int=Integer.parseInt(mes);
        return dia+" "+meses[mes_int]+" "+anyo;

    }
}
