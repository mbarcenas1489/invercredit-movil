package com.example.creditosappandroidx.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.cuotas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adaplistview_creditocli extends BaseAdapter
{

    private final Context cont;
    private final ArrayList<creditocliente> listacredito;
    crudsqlite sqlite;

    public Adaplistview_creditocli(Context cont, ArrayList<creditocliente> listacredito) {
        this.cont = cont;
        this.listacredito = listacredito;
        sqlite=new crudsqlite(cont);
    }

    @Override
    public int getCount() {
        return listacredito.size();
    }

    @Override
    public Object getItem(int i) {
        return listacredito.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) cont
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate( R.layout.rowlistview_creditocli, parent, false);


        creditocliente c= listacredito.get(i);
        cuotas ultima_cuota=sqlite.Get_Ultima_Cuota(c.getPrestamoid());

        TextView textnombre = (TextView) rowView.findViewById(R.id.et_listcri_nombre);
        TextView textmonto = (TextView) rowView.findViewById(R.id.et_listcri_monto);
        TextView textabonado = (TextView) rowView.findViewById(R.id.et_listcri_abonado);
        TextView textpendiente = (TextView) rowView.findViewById(R.id.et_listcri_pendiente);
        TextView textorden=(TextView)rowView.findViewById(R.id.etorden);
        TextView textimg=(TextView)rowView.findViewById(R.id.et_ico_cuotadia);
        TextView textcuotadia=(TextView)rowView.findViewById(R.id.et_cuotadeldia);
        TextView textfechacredito=(TextView)rowView.findViewById(R.id.et_fechacredito);
        TextView text_estado=(TextView)rowView.findViewById(R.id.et_estado_credito);

      //  ImageView btexpandir= rowView.findViewById(R.id.btexpandir);

        TextView textfechafincredito=(TextView)rowView.findViewById(R.id.et_fechafincredito);
        TextView textnumerocuotas=(TextView)rowView.findViewById(R.id.et_num_cuotas);
        Date fechahoy=new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        textnombre.setText(c.getNombre() + " " + c.getApellido());

        if(ultima_cuota!=null)
        {
            if(c.obtener_dias_dias_atrasados()>0 && c.obtener_dias_dias_atrasados()<=10)
            {
                text_estado.setText("P-"+c.obtener_dias_dias_atrasados());

            }
            else
                if(c.getMalo()==1)
                {
                    text_estado.setText("M");
                }
                else
                {
                    text_estado.setVisibility(View.GONE);
                    int dias_atrasados = ultima_cuota.obtener_dias_dias_atrasados();

                    if (dias_atrasados > 0 && dias_atrasados <= 10)
                        {
                            textnombre.setText(c.getNombre() + " " + c.getApellido() + " <" + dias_atrasados+">");

                        }


                }

        }

        /*//Codigo para cambiar de color cuando no haya dado cuotas
         if(ultima_cuota!=null) {
            int dias_atrasados = ultima_cuota.obtener_dias_dias_atrasados();
            textnombre.setText(c.getNombre() + " " + c.getApellido() + " <" + dias_atrasados+">");


            if (dias_atrasados >= 5 && dias_atrasados <= 10) {

                textnombre.setTextColor(textnombre.getContext().getResources().getColor(R.color.Naranja));
            }
             if (dias_atrasados > 10 || c.getMalo()==1) {

                textnombre.setTextColor(textnombre.getContext().getResources().getColor(R.color.Rojo));
            }
        }
        else//Cuando no hay cuotas en el credito
        { textnombre.setText(c.getNombre() + " " + c.getApellido());}
        */


        float abonado=sqlite.totalcuota_by_idprestamo(c.getPrestamoid());
        float moras=sqlite.total_moras_by_idprestamo(c.getPrestamoid());

        textmonto.setText(String.valueOf(c.getMonto_a_pagar()));
        textpendiente.setText(String.valueOf((c.getMonto_a_pagar()-abonado)+moras));
        textabonado.setText(String.valueOf(c.getabonado(cont)));
        textorden.setText(String.valueOf(c.getOrden()));

       // textfechafincredito.setVisibility(View.GONE);
        //textcuotadia.setVisibility(View.GONE);
        //textfechacredito.setVisibility(View.GONE);
        //textnumerocuotas.setVisibility(View.GONE);


            textfechacredito.setText(c.getFechainicial());


            textfechafincredito.setText(c.fechafin());



        textnumerocuotas.setText("# Cuotas: "+String.valueOf(c.cuotas_Abonadas(rowView.getContext())));
        textimg.setText("");



        //comprobar si hay una couta el dia de hoy
        crudsqlite crudsqlite=new crudsqlite(rowView.getContext());
        ArrayList<cuotas> listacuotas=null;

            listacuotas=crudsqlite.consultacuotaByPrestamoID(c.getPrestamoid());
        if(listacuotas!=null) {
            if(listacuotas.size()>0) {

                // if (dateFormat.format(fechahoy).equalsIgnoreCase(listacuotas.get(listacuotas.size() - 1).getFecha()))
                if (dateFormat.format(fechahoy).equalsIgnoreCase(listacuotas.get(listacuotas.size() - 1).getfechaFormat()))

                {
                   // textimg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check, 0, 0, 0);
                    textimg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ok, 0, 0, 0);


                    textcuotadia.setText("Cuota:"+String.valueOf(listacuotas.get(listacuotas.size() - 1).getMonto()));

                } else {
                   //textimg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.close, 0, 0, 0);

                    textimg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);

                    textcuotadia.setText("Cuota:0");

                }
            }
            else {
                textcuotadia.setText("Cuota:0");
                textimg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cancel, 0, 0, 0);

            }
        }




        //Evento cllick del boton expandir
      /*  btexpandir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textfechafincredito.setVisibility(View.VISIBLE);
                //textcuotadia.setVisibility(View.VISIBLE);
                textfechacredito.setVisibility(View.VISIBLE);
                textnumerocuotas.setVisibility(View.VISIBLE);
                if(cspublic.fechafin!=null)
                {
                    cspublic.fechafin.setVisibility(View.GONE);
                    cspublic.fechacredito.setVisibility(View.GONE);
                   // cspublic.cuotadia.setVisibility(View.GONE);
                    cspublic.numerocuota.setVisibility(View.GONE);


                }
                cspublic.fechafin=textfechafincredito;
             //   cspublic.cuotadia=textcuotadia;
                cspublic.fechacredito=textfechacredito;
                cspublic.numerocuota=textnumerocuotas;

            }
        });*/




        return rowView;
    }


}
