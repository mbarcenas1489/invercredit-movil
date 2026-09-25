package com.example.creditosappandroidx.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cswebservice.cspublic;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.datospublicos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class adaptadorlistview extends BaseAdapter {
  private final Context cont;
  private final ArrayList<cuotas> listacuotas;

  public adaptadorlistview(Context c, ArrayList<cuotas> list) {
    cont = c;
    listacuotas = list;
  }

  @Override
  public int getCount() {
    return listacuotas.size();
  }

  @Override
  public Object getItem(int arg0) {
    return listacuotas.get(arg0);
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

    View rowView = inflater.inflate(R.layout.rowlistview, parent, false);

    cuotas c = listacuotas.get(position);

    TextView textfecha = (TextView) rowView.findViewById(R.id.lv_lbfecha);
    TextView text_num_cuota = (TextView) rowView.findViewById(R.id.tv_num_cuota);
    TextView textmonto = (TextView) rowView.findViewById(R.id.lv_lbmonto);
    TextView textPendiente = (TextView) rowView.findViewById(R.id.tvPendiente);
    ImageView img_editar = (ImageView) rowView.findViewById(R.id.img_editcuota);
    //  ImageView imgAddcuota = (ImageView) rowView.findViewById(R.id.imgAddCuota);
    // SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-d");

    if (c.getPendiente() > 0) {
      textPendiente.setText(String.valueOf(c.getPendiente()));
    } else {
      textPendiente.setText("");
    }

    if (format2.format(c.obtenerfecha()).equalsIgnoreCase(cspublic.getfechahoy_yyyy_MM_dd()))

      if (format2.format(c.obtenerfecha()).equalsIgnoreCase(cspublic.getfechahoy_yyyy_MM_dd())) {
        textfecha.setTextColor(Color.parseColor("#3b92ea"));
        textmonto.setTextColor(Color.parseColor("#3b92ea"));
        img_editar.setVisibility(View.GONE);
      } else {
        img_editar.setVisibility(View.GONE);
      }

      /*  } else
            img_editar.setVisibility(View.GONE);*/


    img_editar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //   Toast.makeText(cont,"Editando",Toast.LENGTH_SHORT).show();
        cspublic.cuota_a_editar = c;
        // Intent intent = new Intent(cont, activity_editarcuota.class);
        //cont.startActivity(intent);
      }
    });

    ConstraintLayout constrain = rowView.findViewById(R.id.constrains_rowcuotas);
    if (c.getMora() == 1) {
      constrain.setBackgroundColor(ContextCompat.getColor(cont, R.color.Azul));
    }

    // textfecha.setText(c.getFecha());

    if (c.obtenerfecha() == null) {
      textfecha.setText("No hay fecha");


    } else {
      // textfecha.setText(format2.format(c.obtenerfecha()).toString());
      textfecha.setText(cspublic.cambiar_formato(format2.format(c.obtenerfecha())));
    }
    // textfecha.setText(String.valueOf(c.getnumerocuota()));

    if (datospublicos.creditocliente.getMoneda().equalsIgnoreCase("Cordoba")) {
      textmonto.setText("C$ " + String.valueOf(c.getMonto()));
    } else {
      textmonto.setText("$ " + String.valueOf(c.getMonto()));
    }

    // textmonto.setText(String.valueOf( c.getMonto()));
    int p = listacuotas.size() - position;
    text_num_cuota.setText(String.valueOf(p));

    return rowView;
    // return super.getView(position, convertView, parent);
  }
}
