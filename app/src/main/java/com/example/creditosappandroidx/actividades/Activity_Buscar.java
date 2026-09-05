package com.example.creditosappandroidx.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.creditosappandroidx.Adaptadores.Adaplistview_creditocli;
import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.datospublicos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class Activity_Buscar extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ArrayList<creditocliente> listacompleta;
    ArrayList<creditocliente> listafiltrada;
    ArrayList<creditocliente> listamalos=null;
    ArrayList<String> listanombre;
    ListView lista;
    int ultimaposicion=0;
    Adaplistview_creditocli adap;
    boolean busqueda=false;
    SearchView mSearchView;
    int index ;
    crudsqlite crud=null;
    boolean creditosmalos=false;

    int top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity__buscar);
        listanombre = new ArrayList<String>();
        ArrayAdapter<String> adaptador;
        lista = (ListView) findViewById(R.id.listviewdatos);
        mSearchView = (SearchView) findViewById(R.id.searchView1);

        listafiltrada=new ArrayList<creditocliente>();

         crud = new crudsqlite(this);
        listacompleta = crud.consultaTodo();


        for (int i = 0; i < listacompleta.size() - 1; i++) {
            listanombre.add(listacompleta.get(i).getNombre() + " " + listacompleta.get(i).getApellido());
        }

        // adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listanombre);
file:///home/miguel/Descargas/Android/2010-11/enunciado_android.htmlwww.g
        Collections.sort(listacompleta, new Comparator<creditocliente>() {
            @Override
            public int compare(creditocliente o1, creditocliente o2) {
                return new Integer(o1.getOrden()).compareTo(new Integer(o2.getOrden()));
            }
        });
        adap = new Adaplistview_creditocli(this, listacompleta);

        lista.setAdapter(adap);
        lista.setTextFilterEnabled(true);
        registerForContextMenu(lista);
        //Actibar Boton Atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(getApplicationContext(), activity_cuotas.class);
                creditocliente c;
                index = lista.getFirstVisiblePosition();
                View v = lista.getChildAt(0);
                top = (v == null) ? 0 : v.getTop();

                if(busqueda==false)
                {
                     c = listacompleta.get(pos);
                }
                else
                     c = listafiltrada.get(pos);


                datospublicos.creditocliente = c;
                startActivity(intent);


            }
        });

        setupSearchView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_credito, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
            {
                finish();
                return true;
            }

            case R.id.menu_credito_mostrarmalos:
            {
                if(item.isChecked())
                {
                    item.setChecked(false);
                    creditosmalos=false;
                    listamalos=null;
                    cargartodos();

                }
                else
                {
                    item.setChecked(true);
                    creditosmalos=true;
                    buscarmalos();
                }
                Toast.makeText(getApplicationContext(),"Cargando malos",Toast.LENGTH_SHORT).show();

                return true;
            }


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Buscar Cliente");
    }


    @Override
    protected void onRestart() {

        if(listafiltrada.size()>0)
        {
            adap = new Adaplistview_creditocli(this, listafiltrada);

        }
        else
        {
            adap = new Adaplistview_creditocli(this, listacompleta);
        }


        lista.setAdapter(adap);
        lista.setSelectionFromTop(index, top);

        mSearchView.requestFocus();
        super.onRestart();


    }
    void actualizarlista()
    {
        crudsqlite crud=new crudsqlite(this);
        listacompleta = crud.consultaTodo();
        Collections.sort(listacompleta, new Comparator<creditocliente>() {
            @Override
            public int compare(creditocliente o1, creditocliente o2) {
                return new Integer(o1.getOrden()).compareTo(new Integer(o2.getOrden()));
            }
        });

        adap = new Adaplistview_creditocli(this, listacompleta);
        lista.setAdapter(adap);
        Toast.makeText(this,"Registro Actualizado Correctamente",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final crudsqlite crd = new crudsqlite(this);
        final AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menulistview_eliminar:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Eliminando Credito/Cliente");
                dialog.setMessage("Esta seguro que desea eliminar este Credito");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ultimaposicion = info.position;


                        if (listafiltrada.size() > 0) {
                           crd.Eliminar_cuota_por_credito(listafiltrada.get(info.position));

                            crd.Eliminarcreditoclinte(listafiltrada.get(info.position));
                                    }
                        else {
                            crud.Eliminar_cuota_por_credito(listacompleta.get(info.position));

                            crd.Eliminarcreditoclinte(listacompleta.get(info.position));
                               }

                        actualizarlista();


                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();


                return true;
      /*      case R.id.menulistview_setmalo:
                AlertDialog.Builder dialog_setmalo = new AlertDialog.Builder(this);
                dialog_setmalo.setTitle("Estableciendo Credito como Malo");
                dialog_setmalo.setMessage("Esta seguro que desea establecer este credito como Malo?");
                dialog_setmalo.setCancelable(false);
                dialog_setmalo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ultimaposicion = info.position;
                        creditocliente cre;
                        if(busqueda)
                            cre= listafiltrada.get(ultimaposicion);
                        else
                            cre= listacompleta.get(ultimaposicion);

                      //  Toast.makeText(getApplicationContext(), cre.getNombre(), Toast.LENGTH_SHORT).show();

                        cre.setMalo(true);
                        cre.save();

                        Toast.makeText(getApplicationContext(),String.valueOf( crud.get_creditos_malos().size()), Toast.LENGTH_SHORT).show();

                    }
                });
                dialog_setmalo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog_setmalo.show();
                break;
                */

         /*   case R.id.menulistview_setbueno:
                AlertDialog.Builder dialog_setbueno= new AlertDialog.Builder(this);
                dialog_setbueno.setTitle("Quitar como Malo");
                dialog_setbueno.setMessage("Esta seguro que desea quitar ester credito como malo?");
                dialog_setbueno.setCancelable(false);
                dialog_setbueno.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ultimaposicion = info.position;
                        creditocliente cre;
                        if(busqueda)
                            cre= listafiltrada.get(ultimaposicion);
                        else
                            cre= listacompleta.get(ultimaposicion);

                        //  Toast.makeText(getApplicationContext(), cre.getNombre(), Toast.LENGTH_SHORT).show();

                        cre.setMalo(false);
                        cre.save();
                        Toast.makeText(getApplicationContext(),String.valueOf( crud.get_creditos_malos().size()), Toast.LENGTH_SHORT).show();


                    }
                });
                dialog_setbueno.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog_setbueno.show();
                break;
                */





            case R.id.menulistview_verDetalle:
                int fila = info.position;
                ultimaposicion = info.position;

                index = lista.getFirstVisiblePosition();
                View v = lista.getChildAt(0);
                top = (v == null) ? 0 : v.getTop();

                Fuente:
                https:
//www.iteramos.com/pregunta/4952/posicion-del-desplazamiento-de-mantenerguardarrestaurar-cuando-se-vuelva-a-un-control-listview
                if (busqueda) {

                    datospublicos.creditocliente = listafiltrada.get(fila);

                } else {
                    datospublicos.creditocliente = listacompleta.get(fila);

                }
                Intent intent = new Intent(this, Activity_detallecredito.class);
                startActivity(intent);


                return true;

            default:
                return super.onContextItemSelected(item);
        }
        //return super.onContextItemSelected(item);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulistiview, menu);

       // MenuItem searchitem = menu.findItem(R.id.menu_bar_search);

    }

    public void btcargardatos_onclick(View view) {


    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        int index,index1;
        ArrayList<creditocliente> listaabuscar=new ArrayList<creditocliente>();

        if (TextUtils.isEmpty(s)) {
            busqueda=false;
            lista.clearTextFilter();
            listafiltrada.clear();

            if(creditosmalos){
                adap = new Adaplistview_creditocli(this, listamalos);
                lista.setAdapter(adap);
            }
            else{
                adap = new Adaplistview_creditocli(this, listacompleta);

                lista.setAdapter(adap);
            }

        } else {
            busqueda=true;
          //  Toast.makeText(this,"Filtrando lista",Toast.LENGTH_SHORT).show();
            listafiltrada.clear();



            //si creditos malos es true
            if(creditosmalos){listaabuscar=listamalos;}
            else{listaabuscar=listacompleta;}


            for(int i=0;i<listaabuscar.size();i++)
            {
                //Buqueda por Apellido
                index=listaabuscar.get(i).getApellido().toLowerCase().indexOf(s.toLowerCase());
                if(index!=-1)
                {
                    listafiltrada.add(listaabuscar.get(i));
                }
                else {//Busqueda por nombre

                    index1 = listaabuscar.get(i).getNombre().toLowerCase().indexOf(s.toLowerCase());
                    if (index1 != -1) {
                        listafiltrada.add(listaabuscar.get(i));
                    }

                }


            }
            adap = new Adaplistview_creditocli(this, listafiltrada);
            lista.setAdapter(adap);
            //lista.setFilterText(s);

        }

        return true;
    }
    void buscarmalos()
    {
        listamalos=new ArrayList<creditocliente>();
        for (creditocliente c:listacompleta)
        {
            if(c.getMalo()==1)
            {
                listamalos.add(c);
            }
            
        }
        adap = new Adaplistview_creditocli(this, listamalos);
        lista.setAdapter(adap);

    }
    void cargartodos()
    {
        adap = new Adaplistview_creditocli(this, listacompleta);

        lista.setAdapter(adap);

    }


}
