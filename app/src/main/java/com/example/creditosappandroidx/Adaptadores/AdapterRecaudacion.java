package com.example.creditosappandroidx.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.creditocliente_Table;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;


public class AdapterRecaudacion extends BaseAdapter {
    private Context context=null;
    private  ArrayList<cuotas> listacuoatas=null;

    public AdapterRecaudacion(Context c, ArrayList<cuotas> lcuotas)
    {
      this.context=c;
       this.listacuoatas=lcuotas;

    }

    @Override
    public int getCount() {
        return listacuoatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listacuoatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listacuoatas.get(position).getCuentaid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        creditocliente credito=null;

        View rowView = inflater.inflate( R.layout.rowlistview_recaudacion, parent, false);
        cuotas c=listacuoatas.get(position);
        TextView etmonto=rowView.findViewById(R.id.etmonto_recaudacion);
        TextView etnombre=rowView.findViewById(R.id.etnombre_recaudacion);
        TextView textnumcuota = (TextView) rowView.findViewById(R.id.etnum_cuota);




        ConstraintLayout constrain=rowView.findViewById(R.id.constrains_recaudacion);


        if(c.getMora()==1)
        {
            constrain.setBackgroundColor(ContextCompat.getColor(context, R.color.Azul));
        }


        //Obtener creditocliente
        ArrayList<creditocliente> listcredito=(ArrayList<creditocliente>) SQLite.select().from(creditocliente.class)
                .where(creditocliente_Table.prestamoid.is(c.getPrestamo_prestamoid())).queryList();
       if(listcredito!=null) {
           etnombre.setText(listcredito.get(0).getNombre()+" "+listcredito.get(0).getApellido());
           if(listcredito.get(0).getMoneda().equalsIgnoreCase("Cordoba"))
           {
               etmonto.setText("C$ "+String.valueOf( c.getMonto()));
           }
           else
           {
               etmonto.setText("$ "+String.valueOf( c.getMonto()));

             //  etmonto.setBackgroundResource(R.drawable.text_view_circle_dollar);
           }
       }



        //Buscar el credito que le corresponde a la cuota
       /* crudsqlite crud = new crudsqlite(context);
        try {
            credito=crud.Consulta_CreditoByPrestamoID(listacuoatas.get(position).getPrestamo_prestamoid());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(credito!=null)
        {
            etnombre.setText(credito.getNombre()+" "+credito.getApellido());
        }*/
        textnumcuota.setText(String.valueOf(position+1));
        return  rowView;
    }
}
