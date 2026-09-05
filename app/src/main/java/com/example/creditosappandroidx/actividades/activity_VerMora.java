package com.example.creditosappandroidx.actividades;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditosappandroidx.Adaptadores.adaptadorlistview_moras;
import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.datospublicos;
import com.example.creditosappandroidx.cswebservice.moras;

import java.util.ArrayList;


public class activity_VerMora extends AppCompatActivity {
    TextView nombre;
    TextView totalmoras;
    ListView listViewmoras;
    Toolbar myToolbar;
    adaptadorlistview_moras adap;
    ArrayList<moras> listamoras;
    com.example.creditosappandroidx.cssqlite.crudsqlite crudsqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity__ver_mora);
        crudsqlite=new crudsqlite(this);
        nombre=(TextView) findViewById(R.id.tv_nombre_moras);
        totalmoras=findViewById(R.id.tv_totalmora);
        totalmoras.setText(String.valueOf(crudsqlite.total_moras_by_idprestamo(datospublicos.creditocliente.getPrestamoid())));

        nombre.setText(datospublicos.creditocliente.getNombre()+" "+ datospublicos.creditocliente.getApellido());

       // myToolbar = (Toolbar) findViewById(R.id.toolbarmora);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //myToolbar.setSubtitle("Moras");
        //myToolbar.setTitle("Moras Registradas");

        listViewmoras=(ListView) findViewById(R.id.listviewmoras);
        llenarlistview();
    }
    private void llenarlistview() {
        crudsqlite=new crudsqlite(this);

        listamoras=crudsqlite.consultaMORAByPrestamoID(datospublicos.creditocliente.getPrestamoid());
        // datospublicos.listacuotas=lis;
        adap=new adaptadorlistview_moras(this,listamoras);

        listViewmoras.setAdapter(adap);
    }
}
