package com.example.creditosappandroidx.cswebservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.google.android.material.snackbar.Snackbar;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class crudWebservice {
  Context context;
  Retrofit retrofit;
  SharedPreferences pref;
  int idcobrador = 0;

  public crudWebservice(Context c) {
    context = c;

    pref = PreferenceManager.getDefaultSharedPreferences(context);
    String ip = pref.getString("ip", "192.168.1.1");
    String cobrador = pref.getString("idcobrador", "1");
    idcobrador = Integer.parseInt(cobrador);
    retrofit = new Retrofit.Builder().baseUrl("http://" + ip + "/invercreditWeb/public/").addConverterFactory(GsonConverterFactory.create()).build();

  }

  public void consultarCreditoCliente(View view) {
    final ArrayList<creditocliente> lista = new ArrayList<creditocliente>();
    final crudsqlite crud = new crudsqlite(context);

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ArrayList<creditocliente>> listacredito = interfasproducto.obtenercredito_cobrador(idcobrador);

    listacredito.enqueue(new Callback<ArrayList<creditocliente>>() {
      @Override
      public void onResponse(Call<ArrayList<creditocliente>> call, Response<ArrayList<creditocliente>> response) {
        if (response.code() == 200) {
          Toast.makeText(context, "Obteniendo datos:" + String.valueOf(response.body().size()), Toast.LENGTH_LONG).show();

          for (int i = 0; i < response.body().size(); i++) {
            creditocliente cc = new creditocliente();
            cc = response.body().get(i);
            lista.add(cc);
            cc.save();
          }
          crudsqlite crud = new crudsqlite(view.getContext());
          ArrayList<cuotas> cuotas1 = new ArrayList<cuotas>();
          ArrayList<cuotas> cuotas2 = new ArrayList<cuotas>();
          cuotas1 = (ArrayList<cuotas>) SQLite.select().from(cuotas.class).queryList();
          crud.Eliminar_Cuotas_sin_Credito();
          cuotas2 = (ArrayList<cuotas>) SQLite.select().from(cuotas.class).queryList();
          Snackbar snackbar = Snackbar.make(view, String.valueOf(response.body().size()) + " Creditos Cargados Correctamente", Snackbar.LENGTH_LONG);
          snackbar.show();
        }
      }
      @Override
      public void onFailure(Call<ArrayList<creditocliente>> call, Throwable t) {
      }
    });
  }

  public void consultarCreditoCliente_Asyn(final ArrayList<creditocliente> listaBD) {
    final crudsqlite crud = new crudsqlite(context);

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ArrayList<creditocliente>> listacredito = interfasproducto.obtenercredito();
    listacredito.enqueue(new Callback<ArrayList<creditocliente>>() {
      @Override
      public void onResponse(Call<ArrayList<creditocliente>> call, Response<ArrayList<creditocliente>> response) {
        int cant = 0;
        for (int i = 0; i < response.body().size(); i++) {
          for (int j = 0; j < listaBD.size(); j++) {
            if (response.body().get(i).getPrestamoid() == listaBD.get(j).getPrestamoid()) {
              if (response.body().get(i).getMonto_pendiente() > listaBD.get(j).getMonto_pendiente()) {
                cant++;
              }
            }
          }
        }
      }
      @Override
      public void onFailure(Call<ArrayList<creditocliente>> call, Throwable t) {
      }
    });
  }
  public void consultarMora_by_Idprestamo(int idprestamo) {
    final crudsqlite crud = new crudsqlite(context);
    interfacecredito interfazcuota = retrofit.create(interfacecredito.class);
    Call<ArrayList<moras>> listamora = interfazcuota.ObtenerMora_by_idprestamo(idprestamo);
    listamora.enqueue(new Callback<ArrayList<moras>>() {
      @Override
      public void onResponse(Call<ArrayList<moras>> call, Response<ArrayList<moras>> response) {
        if (response.code() == 200) {
          crud.Eliminar_Moras_por_credito(datospublicos.creditocliente);
          for (int i = 0; i < response.body().size(); i++) {
            moras c = response.body().get(i);
            c.save();
          }
        }
      }
      @Override
      public void onFailure(Call<ArrayList<moras>> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void consultarcoutasByIdprestamo(int idprestamo) {
    final crudsqlite crud = new crudsqlite(context);
    interfacecredito interfazcuota = retrofit.create(interfacecredito.class);
    Call<ArrayList<cuotas>> listacredito = interfazcuota.ObtenercuotasByidprestamo(idprestamo);
    listacredito.enqueue(new Callback<ArrayList<cuotas>>() {
      @Override
      public void onResponse(Call<ArrayList<cuotas>> call, Response<ArrayList<cuotas>> response) {

        if (response.code() == 200) {
          crud.Eliminar_cuota_por_credito(datospublicos.creditocliente);
          for (int i = 0; i < response.body().size(); i++) {
            cuotas c = response.body().get(i);
            c.save();
          }
        }
      }
      @Override
      public void onFailure(Call<ArrayList<cuotas>> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }
  public void BuscarCuotasNuevas(final TextView etcuotasnuevas) {
    final crudsqlite crudsqlite = new crudsqlite(context);
    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ArrayList<cuotas>> callcuotas = interfasproducto.ObtenerTodasCuotas_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<cuotas>>() {
      @Override
      public void onResponse(Call<ArrayList<cuotas>> call, Response<ArrayList<cuotas>> response) {

        ArrayList<cuotas> cuotasBD = null;

        cuotasBD = crudsqlite.ConsultaTodas_CUOTAS();
        ArrayList<cuotas> cuotasenviar = new ArrayList<cuotas>();
        if (response.code() == 200) {
          for (int i = 0; i < cuotasBD.size(); i++) {
            cuotas cbd = cuotasBD.get(i);
            int cencontrada = 0;
            if (cbd.getMora() == 0) {
              for (int j = 0; j < response.body().size(); j++) {
                cuotas cser = response.body().get(j);
                if (cbd.getPrestamo_prestamoid() == cser.getPrestamo_prestamoid() && cbd.getFecha().equalsIgnoreCase(cser.getFecha()) == true) {
                  cencontrada = 1;
                  j = response.body().size() + 5;
                }
              }
              if (cencontrada == 0) {
                cuotasenviar.add(cbd);
              }
            }
          }
          etcuotasnuevas.setText(String.valueOf(cuotasenviar.size()) + " Cuotas Nuevas");
          datospublicos.listacuotas = cuotasenviar;
        }
      }

      @Override
      public void onFailure(Call<ArrayList<cuotas>> call, Throwable t) {
        Toast.makeText(context, t.getMessage() + "   ", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void BuscarMorasNuevas(final TextView etmorasNuevas) {
    final crudsqlite crudsqlite = new crudsqlite(context);
    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ArrayList<moras>> callcuotas = interfasproducto.ObtenerTodasMoras_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<moras>>() {
      @Override
      public void onResponse(Call<ArrayList<moras>> call, Response<ArrayList<moras>> response) {

        ArrayList<moras> cuotasBD = null;
        cuotasBD = crudsqlite.ConsultaTodas_Moras();
        ArrayList<moras> moranueva = new ArrayList<moras>();

        if (response.code() == 200) {
          for (int i = 0; i < cuotasBD.size(); i++) {
            moras cbd = cuotasBD.get(i);
            int cencontrada = 0;

            for (int j = 0; j < response.body().size(); j++) {
              moras cser = response.body().get(j);
              String fechaBD = cbd.getFecha().split(" ")[0];
              String fechaRem = cser.getFecha().split(" ")[0];
              if (cbd.getIdprestamo() == cser.getIdprestamo() && fechaBD.equalsIgnoreCase(fechaRem) == true) {
                cencontrada = 1;
                j = response.body().size() + 5;

              }


            }
            //Comprobar si la cencontrada esta vacia, si lo esta, la cuota no existe en el servidor por lo tanto
            //hay que enviarla
            if (cencontrada == 0) {
              moranueva.add(cbd);
            }
          }
          etmorasNuevas.setText(String.valueOf(moranueva.size()) + " Moras Nuevas");
          datospublicos.lista_mora_nueva = moranueva;
        }

      }

      @Override
      public void onFailure(Call<ArrayList<moras>> call, Throwable t) {

      }


    });


  }


  public void insertartodas_Cuotas(View view)
  //public void insertartodas_Cuotas()
  {

    final crudsqlite crud = new crudsqlite(context);


    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ArrayList<cuotas>> callcuotas = interfasproducto.ObtenerTodasCuotas_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<cuotas>>() {
      @Override
      public void onResponse(Call<ArrayList<cuotas>> call, Response<ArrayList<cuotas>> response) {
        if (response.code() == 200) {
          for (int i = 0; i < response.body().size(); i++) {
            cuotas c = response.body().get(i);

            c.save();

          }


          Snackbar snackbar = Snackbar.make(view, String.valueOf(response.body().size()) + " Cuotas cargadas Correctamente", Snackbar.LENGTH_LONG);
          snackbar.show();
          // Toast.makeText(context,"Cuotas Enviadas Correctamente",Toast.LENGTH_SHORT).show();


        }
      }

      @Override
      public void onFailure(Call<ArrayList<cuotas>> call, Throwable t) {

      }
    });

  }

  public void insertartodas_Moras(View view) {

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);

    Call<ArrayList<moras>> callcuotas = interfasproducto.ObtenerTodasMoras_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<moras>>() {
      @Override
      public void onResponse(Call<ArrayList<moras>> call, Response<ArrayList<moras>> response) {
        if (response.code() == 200) {
          for (int i = 0; i < response.body().size(); i++) {
            moras c = response.body().get(i);
            c.save();

          }
          Snackbar snackbar = Snackbar.make(view, String.valueOf(response.body().size()) + " Moras cargadas Correctamente", Snackbar.LENGTH_LONG);
          snackbar.show();


        }
      }

      @Override
      public void onFailure(Call<ArrayList<moras>> call, Throwable t) {
        Log.e("Error654", "Error al cargar Moras");

      }
    });

  }

  public void ConsultarTodasCuotas() {
    ArrayList<creditocliente> listacredito = new ArrayList<creditocliente>();
    final crudsqlite crudsqlite = new crudsqlite(context);

    listacredito = crudsqlite.consultaTodo();

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ArrayList<cuotas>> callcuotas = interfasproducto.ObtenerTodasCuotas_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<cuotas>>() {
      @Override
      public void onResponse(Call<ArrayList<cuotas>> call, Response<ArrayList<cuotas>> response) {

        int contador = 0;
        if (response.code() == 200) {
          for (int i = 0; i < response.body().size(); i++) {
            int encontrado = 0;
            cuotas c = response.body().get(i);
            encontrado = crudsqlite.ConsultaByID(c.getPrestamo_prestamoid());
            if (encontrado == 1) {
              contador++;
              c.save();
            }
          }
          Toast.makeText(context, "Cuotas Insertadas=" + String.valueOf(contador), Toast.LENGTH_SHORT).show();
        }

      }

      @Override
      public void onFailure(Call<ArrayList<cuotas>> call, Throwable t) {
        Toast.makeText(context, t.getMessage().toString() + "   " + t.getCause().toString(), Toast.LENGTH_SHORT).show();
      }
    });
  }


  public void ActualizarDatosClienteCredito() {
    final ArrayList<creditocliente> lista = new ArrayList<creditocliente>();
    final crudsqlite crudsqlite = new crudsqlite(context);

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);

    Call<ArrayList<creditocliente>> listacredito = interfasproducto.obtenercredito_cobrador(idcobrador);

    listacredito.enqueue(new Callback<ArrayList<creditocliente>>() {
      @Override
      public void onResponse(Call<ArrayList<creditocliente>> call, Response<ArrayList<creditocliente>> response) {
        if (response.code() == 200) {
          int nuevo = 0;
          for (int i = 0; i < response.body().size(); i++) {
            long encontrado = 0;
            creditocliente cc = null, ccq;
            cc = response.body().get(i);

            encontrado = SQLite.select().from(creditocliente.class).where(creditocliente_Table.prestamoid.is(cc.getPrestamoid())).count();

            if (encontrado == 0) {
              nuevo++;
              cc.save();
            } else {
            }
          }
        }
      }

      @Override
      public void onFailure(Call<ArrayList<creditocliente>> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }

  public void InsertarCuota(String fecha, float monto, int prestamoid) {

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ResponseBody> callcuotas = interfasproducto.InsertarCuota(fecha, monto, prestamoid);

    callcuotas.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          Toast.makeText(context, "Cuota Insertada Correctamente", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call call, Throwable t) {

      }
    });
  }

  public void setMalo(int prestamoid, int malo) {

    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ResponseBody> callcuotas = interfasproducto.setmalo(prestamoid, malo);

    callcuotas.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          Toast.makeText(context, "set malo", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call call, Throwable t) {
      }
    });

  }

  public void InsertarCuota_mora(String fecha, float monto, int prestamo_prestamoid, int mora, float saldo) {
    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ResponseBody> callcuotas = interfasproducto.InsertarCuota_mora(fecha, monto, prestamo_prestamoid, mora, saldo);

    callcuotas.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          cspublic.can_cuotas_enviadas++;
          cspublic.setTv_cuotas_enviadas();
        }
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        Log.e("Error al guardar=", String.valueOf(t.getMessage()));
      }
    });
  }

  public void InsertarMora(String fecha, float monto, int prestamo_prestamoid, float saldo) {
    interfacecredito interfasproducto = retrofit.create(interfacecredito.class);
    Call<ResponseBody> callcuotas = interfasproducto.Insertar_Mora(fecha, monto, prestamo_prestamoid, saldo);

    callcuotas.enqueue(new Callback() {
      @Override
      public void onResponse(Call call, Response response) {
        if (response.code() == 200) {
          cspublic.can_moras_enviadas++;
          cspublic.setTv_moras_enviadas();
        }
      }

      @Override
      public void onFailure(Call call, Throwable t) {
        Log.e("Error al guardar=", String.valueOf(t.getMessage()));
      }
    });
  }
}
