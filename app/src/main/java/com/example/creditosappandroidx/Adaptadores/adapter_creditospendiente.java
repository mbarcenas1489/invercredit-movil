package com.example.creditosappandroidx.Adaptadores;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.actividades.Activity_detallecredito;
import com.example.creditosappandroidx.cswebservice.creditocliente;
import com.example.creditosappandroidx.cswebservice.cspublic;
import com.example.creditosappandroidx.cswebservice.cuotas;
import com.example.creditosappandroidx.cswebservice.datospublicos;
import com.example.creditosappandroidx.cswebservice.interfacesMetodos;
import com.example.creditosappandroidx.cswebservice.moras;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class adapter_creditospendiente extends RecyclerView.Adapter<adapter_creditospendiente.MyViewHolder> {
    private List<creditocliente> lista;

    public adapter_creditospendiente(List<creditocliente> l)
    {
        lista=l;
    }
    @NonNull
    @Override
    public adapter_creditospendiente.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate( R.layout.rowlistview_creditopendiente, viewGroup, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull adapter_creditospendiente.MyViewHolder myViewHolder, int i) {

        creditocliente c = lista.get(i);
        myViewHolder.tvnombre.setText(c.getNombre()+" "+c.getApellido());
        //myViewHolder.tvnombre.setTextColor(Color.parseColor("#0aad3f"));
      if(c.getMalo()==1)
      {
         // myViewHolder.tv_setmalo.setText("Bueno");
          myViewHolder.tv_setmalo.setVisibility(View.GONE);
          myViewHolder.tvnombre.setTextColor(myViewHolder.tvnombre.getContext().getResources().getColor(R.color.Rojo));

      }
      else
      {
          myViewHolder.tvnombre.setTextColor(myViewHolder.tvnombre.getContext().getResources().getColor(R.color.Azul));


      }



        // myViewHolder.tv_setmalo.setVisibility(View.GONE);
       // myViewHolder.tv_monto_pendiente.setVisibility(View.GONE);


        myViewHolder.constrain_vencidos.setVisibility(View.GONE);
        myViewHolder.tv_pos.setText(String.valueOf(c.obtener_dias_dias_atrasados()));

        if(c.getPendiente()>0)
            myViewHolder.tv_monto_pendiente.setText("C$"+String.valueOf(c.getMonto_pendiente()));
        else
            myViewHolder.tv_monto_pendiente.setText("");
        myViewHolder.tv_fechafin.setText(cspublic.cambiar_formato( c.getFechafin()));

        myViewHolder.image_detallecredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                datospublicos.creditocliente =c;
                Intent intent = new Intent(myViewHolder.itemView.getContext(), Activity_detallecredito.class);
                myViewHolder.itemView.getContext().startActivity(intent);


            }
        });
        myViewHolder.tv_setmalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( final View v) {


                //PREGUNTAR SI QUIERE PASARLO A MALO
                new AlertDialog.Builder(myViewHolder.itemView.getContext())
                        .setTitle("Poner Credito como Malo")
                        .setMessage("¿Usted Seguro que quiere poner este credito como Malo?")
                        //.setIcon(R.drawable.ninja)
                        .setPositiveButton("SI",
                                new DialogInterface.OnClickListener() {
                                   // @TargetApi(11)
                                    public void onClick(DialogInterface dialog, int id) {
                                      // Toast.makeText(myViewHolder.itemView.getContext(),"Malo",Toast.LENGTH_SHORT).show();

                                        View vv=v;
                                        c.setMalo(1);
                                        c.save();


                                        //PEDIR LA MORA
                                        LayoutInflater inflater = LayoutInflater.from(myViewHolder.itemView.getContext());

                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(myViewHolder.itemView.getContext());
                                         vv= inflater.inflate(R.layout.dialog_montomora,null);
                                        mBuilder.setTitle("Agregando Mora");
                                        TextInputEditText textimput_montomora=vv.findViewById(R.id.textimput_montomora);

                                        mBuilder.setView( vv );
                                        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Toast.makeText(myViewHolder.itemView.getContext(),"Mora Agregada="+textimput_montomora.getText().toString(),Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(myViewHolder.itemView.getContext(),myViewHolder.dia+"-"+myViewHolder.mes+1+"-"+myViewHolder.anio,Toast.LENGTH_SHORT).show();


                                                cuotas cuot=new cuotas();
                                                moras m=new moras();
                                                m.setMonto(Integer.parseInt(textimput_montomora.getText().toString()));
                                                m.setIdprestamo(c.getPrestamoid());
                                                m.setSaldo(c.getMonto_pendiente());
                                                m.setFecha(myViewHolder.anio+"-"+(myViewHolder.mes+1)+"-"+myViewHolder.dia);
                                                m.save();
                                                ((interfacesMetodos)myViewHolder.itemView.getContext()).Actualizarlista();


                                            }
                                        });
                                        mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        });
                                        mBuilder.setCancelable(false);
                                        AlertDialog dialog1 = mBuilder.create();
                                        dialog1.show();

                                       // myViewHolder.notify();

                                     // notifyDataSetChanged();

                                    adapter_creditospendiente.this.notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                               // Toast.makeText(myViewHolder.itemView.getContext(),"No Malo",Toast.LENGTH_SHORT).show();

                                dialog.cancel();
                            }
                        }).show();






            }
        });



        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent=new Intent(myViewHolder.itemView.getContext(), activity_cuotas.class);
                //datospublicos.creditocliente = c;
                //myViewHolder.itemView.getContext().startActivity(intent);

                if(datospublicos.constrain_vencidos!=null)
                {
                    datospublicos.constrain_vencidos.setVisibility(View.GONE);
                    //datospublicos.tv_montopendiente.setVisibility(View.GONE);
                   // Toast.makeText(myViewHolder.itemView.getContext(),"Ocultando elementos",Toast.LENGTH_SHORT).show();

                }
                //myViewHolder.tv_monto_pendiente.setVisibility(View.VISIBLE);
                myViewHolder.constrain_vencidos.setVisibility(View.VISIBLE);

                datospublicos.constrain_vencidos=myViewHolder.constrain_vencidos;

            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        public TextView tvnombre;
        public TextView tv_monto_pendiente;
        public TextView tv_pos;
        public TextView tv_fechafin;
        public TextView tv_setmalo;
        public ConstraintLayout constrain_vencidos;
        ImageView image_detallecredito;

        public TextView tv_montopendiente;

        AlertDialog.Builder builder ;
        public final Calendar calendar = Calendar.getInstance();

        //Variables para obtener la fecha
        final int mes = calendar.get(Calendar.MONTH);
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);
        final int anio = calendar.get(Calendar.YEAR);

        public MyViewHolder(View view) {
            super(view);
            //tv_pos_cred_p

          tvnombre=view.findViewById(R.id.tv_nombre_creP);
            tv_monto_pendiente=view.findViewById(R.id.tv_monto_pendiente);
            tv_pos=view.findViewById(R.id.tv_pos_cred_p);
            tv_fechafin=view.findViewById(R.id.tv_fechafin);
            tv_monto_pendiente=view.findViewById(R.id.tv_monto_pendiente);
            tv_setmalo=view.findViewById(R.id.tv_setmalo);
            constrain_vencidos=view.findViewById(R.id.constrain_vencidos);
            image_detallecredito=view.findViewById(R.id.bt_detalle_credito);

           // tv_setmalo=view.findViewById(R.id.tv_setmalo);
         //builder=   android.app.AlertDialog.Builder();

        }
    }
}
