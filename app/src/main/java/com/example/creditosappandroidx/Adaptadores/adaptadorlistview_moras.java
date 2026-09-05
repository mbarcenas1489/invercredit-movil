package com.example.creditosappandroidx.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cswebservice.moras;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class adaptadorlistview_moras extends BaseAdapter {
    private final Context cont;
    private final ArrayList<moras> listamoras;

    public adaptadorlistview_moras(Context c, ArrayList<moras> list)
    {
        cont=c;
        listamoras=list;
    }

    @Override
    public int getCount() {
        return listamoras.size();
    }

    @Override
    public Object getItem(int arg0) {
        return listamoras.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) cont
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rowlistview_moras, parent, false);

        moras c=listamoras.get(position);
       //Log.e("Cuota",c.getMonto()+" "+c.getFecha());

        TextView textfecha = (TextView) rowView.findViewById( R.id.lv_lbfecha_mora);
        TextView textmonto = (TextView) rowView.findViewById(R.id.lv_lbmonto_mora);


        ConstraintLayout constrain=rowView.findViewById(R.id.constrains_rowcuotas);


       // textfecha.setText(c.getFecha());
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");

        if(c.obtenerfecha()==null )
        {
            textfecha.setText("No hay fecha");

        }
        else {
            textfecha.setText(format2.format(c.obtenerfecha()).toString());
        }
       // textfecha.setText(String.valueOf(c.getnumerocuota()));

        textmonto.setText(String.valueOf( c.getMonto()));

        return  rowView;
       // return super.getView(position, convertView, parent);
    }
}
