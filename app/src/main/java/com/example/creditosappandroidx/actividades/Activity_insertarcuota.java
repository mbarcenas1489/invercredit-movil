package com.example.creditosappandroidx.actividades;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.datospublicos;
import com.example.creditosappandroidx.cswebservice.moras;
import com.google.android.material.textfield.TextInputEditText;
import com.mazenrashed.printooth.Printooth;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cswebservice.datospublicoskt;


public class Activity_insertarcuota extends AppCompatActivity {

    EditText ctfecha;
    TextView textfecha;

    TextInputEditText ctmonto;
    RadioButton rbmora;
    private static final String CERO = "0";
    private static final String BARRA = "/";



    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FlowManager.init(new FlowConfig.Builder(this).build());
        setContentView( R.layout.activity_insertarcuota);
        ctfecha=(EditText) findViewById(R.id.ct_fecha);
        ctmonto=(TextInputEditText)findViewById(R.id.ctmonto1);
        rbmora=findViewById(R.id.rbmora);
        textfecha=findViewById(R.id.textfecha);

        ctfecha.setKeyListener(null);
        if(!datospublicos.creditocliente.getMoneda().equalsIgnoreCase("Cordoba")) {
            ctmonto.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        textfecha.setText(datospublicoskt.INSTANCE.getfecha_format());
        //ctfecha.setFocusable(false);

       int montocuota=(int) datospublicos.creditocliente.getMonto_a_pagar()/datospublicos.creditocliente.getPlazo();
        ctmonto.setText(String.valueOf( montocuota));
      // Toast.makeText(this,String.valueOf(datospublicos.creditocliente.getPlazo()),Toast.LENGTH_SHORT).show();
        LocalDateTime ahora;

        Calendar c1 = Calendar.getInstance();
        String mes="";
        String dia="";
        if((c1.get(Calendar.MONTH)+1)<10)
            mes="0"+(c1.get(Calendar.MONTH)+1);
        else
            mes= String.valueOf(c1.get(Calendar.MONTH)+1);


        if(c1.get(Calendar.DAY_OF_MONTH)<10)
            dia="0"+c1.get(Calendar.DAY_OF_MONTH);
        else
            dia=String.valueOf( c1.get(Calendar.DAY_OF_MONTH));

        ctfecha.setText(c1.get(Calendar.YEAR)+"-"+mes+"-"+dia);
      //  Toast.makeText(this,"instando cuota",Toast.LENGTH_LONG).show();

    }


    public void btguardarcuota_onclick(View view)
    {
        cuotas c=new cuotas();
        moras m =new moras();
        if(ctfecha.getText().length()==0 || ctmonto.getText().length()==0)
        {
            Toast.makeText(this,"Llene todos los campos",Toast.LENGTH_SHORT).show();
            return;

        }
       /* if(datospublicos.cuotahoy==true && rbmora.isChecked()==false)
        {
            Toast.makeText(this, "Ya se ha registrado una cuota el dia de hoy", Toast.LENGTH_SHORT).show();
            return;
        }*/


        if(rbmora.isChecked())
        {
            m.setMonto(Float.parseFloat(ctmonto.getText().toString()));
            m.setFecha(ctfecha.getText().toString());
            m.setIdprestamo(datospublicos.creditocliente.getPrestamoid());


            c.setSaldo(datospublicos.creditocliente.getMonto_pendiente());


            m.save();

        }
        else {
            c.setMonto(Float.parseFloat(ctmonto.getText().toString()));
            c.setFecha(ctfecha.getText().toString());
            c.setPrestamo_prestamoid(datospublicos.creditocliente.getPrestamoid());

            c.setMora(1);
            c.setSaldo(datospublicos.creditocliente.getMonto_pendiente());

            c.setMora(0);
            c.setSaldo(0);



            if( c.save())
            {
              /*  datospublicoskt.INSTANCE.imprimir_recibo(c.getFecha(),c.getMonto(),
                        datospublicos.creditocliente.getNombre()+" "+datospublicos.creditocliente.getApellido()
                c.getSaldo());*/

            }
        }
        datospublicoskt.INSTANCE.setCuota_insertada(true);


        this.finish();

    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                ctfecha.setText(year +"-" + mesFormateado + "-" + diaFormateado);

               // messelect=mesActual;
                //diaselect=(dayOfMonth < 10)? CERO + dayOfMonth;

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();

    }

    public void btcancelaronclick(View view)
    {
        this.finish();
    }
}
