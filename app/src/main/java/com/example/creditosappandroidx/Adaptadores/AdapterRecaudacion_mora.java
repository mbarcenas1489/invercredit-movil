package com.example.creditosappandroidx.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.creditocliente_Table;
import com.example.creditosappandroidx.cswebservice.moras;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;


public class AdapterRecaudacion_mora extends BaseAdapter {
  private Context context = null;
  private ArrayList<moras> listamora = null;

  public AdapterRecaudacion_mora(Context c, ArrayList<moras> lcuotas) {
    this.context = c;
    this.listamora = lcuotas;

  }

  @Override
  public int getCount() {
    return listamora.size();
  }

  @Override
  public Object getItem(int position) {
    return listamora.get(position);
  }

  @Override
  public long getItemId(int position) {
    return listamora.get(position).getIdmora();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    creditocliente credito = null;

    View rowView = inflater.inflate(R.layout.rowlistview_recaudacion, parent, false);
    moras c = listamora.get(position);
    TextView etmonto = rowView.findViewById(R.id.etmonto_recaudacion);
    TextView etnombre = rowView.findViewById(R.id.etnombre_recaudacion);
    TextView textnumcuota = (TextView) rowView.findViewById(R.id.etnum_cuota);

    ConstraintLayout constrain = rowView.findViewById(R.id.constrains_recaudacion);

    etmonto.setText(String.valueOf(c.getMonto()));

    ArrayList<creditocliente> listcredito = (ArrayList<creditocliente>) SQLite.select().from(creditocliente.class)
      .where(creditocliente_Table.prestamoid.is(c.getIdprestamo())).queryList();
    if (listcredito != null) {
      etnombre.setText(listcredito.get(0).getNombre() + " " + listcredito.get(0).getApellido());
    }

    textnumcuota.setText(String.valueOf(position + 1));
    return rowView;
  }
}
