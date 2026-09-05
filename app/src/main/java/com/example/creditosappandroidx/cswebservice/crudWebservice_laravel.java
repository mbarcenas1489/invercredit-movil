
package com.example.creditosappandroidx.cswebservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.creditosappandroidx.Api.CobradorService;
import com.example.creditosappandroidx.R;
import com.example.creditosappandroidx.actividades.login.loginActivity;
import com.example.creditosappandroidx.cssqlite.crudsqlite;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.ArrayList;
import java.util.List;
import cswebservice.datospublicoskt;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.creditosappandroidx.Api.response.cuotasResponse;
import com.example.creditosappandroidx.models.response.CuotasResponse;


public class crudWebservice_laravel {
  Context context;
  Retrofit retrofit;
  SharedPreferences pref;
  int idcobrador = 0;

  public crudWebservice_laravel(Context c) {
    context = c;

    pref = PreferenceManager
      .getDefaultSharedPreferences(context);
    String ip = pref.getString("ip", "192.168.1.1");
    String cobrador = pref.getString("idcobrador", "1");
    idcobrador = Integer.parseInt(cobrador);

    if (datospublicoskt.INSTANCE.getConexion_server().equalsIgnoreCase("Remota")) {
      retrofit = new Retrofit.Builder()
        .baseUrl("https://invercredit.homeflowapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    } else {
      retrofit = new Retrofit.Builder()
        .baseUrl(datospublicoskt.INSTANCE.GetUrlServer())
//        .baseUrl("http://" + ip + "/invercreditWeb/public/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    }
  }

  public void consultarCreditoCliente(Context context, TextView textView) {
    final ArrayList<creditocliente> lista = new ArrayList<creditocliente>();
    final crudsqlite crud = new crudsqlite(context);

    interfacesCreditoLaravel interfasproducto = retrofit.create(interfacesCreditoLaravel.class);
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
            textView.setText("Cargando Clientes..."+(i*100)/response.body().size()+"%");
          }
//          textView.setBackground(ContextCompat.getDrawable(context, R.drawable.textview_round));
          textView.setBackgroundColor(R.color.verde);
          textView.setText("Cargando Clientes...100%");

          crudsqlite crud = new crudsqlite(context);
          ArrayList<cuotas> cuotas1 = new ArrayList<cuotas>();
          cuotas1 = (ArrayList<cuotas>) SQLite.select().from(cuotas.class).queryList();
          crud.Eliminar_Cuotas_sin_Credito();

        }
      }

      @Override
      public void onFailure(Call<ArrayList<creditocliente>> call, Throwable t) {

      }
    });



  }

  public void consultarCuotasAServidor(Context context, TextView textView) {

    interfacesCreditoLaravel interfasproducto =
      retrofit.create(interfacesCreditoLaravel.class);
    Call<ArrayList<cuotas>> callcuotas = interfasproducto.ObtenerTodasCuotas_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<cuotas>>() {
      @Override
      public void onResponse(Call<ArrayList<cuotas>> call, Response<ArrayList<cuotas>> response) {
        if (response.code() == 200) {
          for (int i = 0; i < response.body().size(); i++) {
            cuotas c = response.body().get(i);
            c.save();
            textView.setText("Cargando Cuotas..."+(i*100)/response.body().size()+"%");
          }
          textView.setBackgroundColor(R.color.verde);
          textView.setText("Cuotas Cargadas...100%");
        }
      }

      @Override
      public void onFailure(Call<ArrayList<cuotas>> call, Throwable t) {

      }
    });
  }

  public void isConnectServer(View view, String titulo, String mensaje) {
    interfacesCreditoLaravel interfazCuotas =
      retrofit.create(interfacesCreditoLaravel.class);
    Call<ResponseBody> response = interfazCuotas.isConnectServer();
    response.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 200) {
          Snackbar.make(view, "Hay connexion al servidor", Snackbar.LENGTH_LONG).show();
        } else {
          Snackbar.make(view, "Error al conectar al servidor", Snackbar.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
      }
    });
  }

  public void obtenerMoras(View view) {

    interfacesCreditoLaravel interfasproducto =
      retrofit.create(interfacesCreditoLaravel.class);

    Call<ArrayList<moras>> callcuotas = interfasproducto.ObtenerTodasMoras_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<moras>>() {
      @Override
      public void onResponse(Call<ArrayList<moras>> call, Response<ArrayList<moras>> response) {
        if (response.code() == 200) {
          for (int i = 0; i < response.body().size(); i++) {
            moras c = response.body().get(i);
            c.save();
          }
          Snackbar snackbar = Snackbar
            .make(view, String.valueOf(response.body().size()) + " Moras cargadas Correctamente", Snackbar.LENGTH_LONG);
          snackbar.show();
        }
      }

      @Override
      public void onFailure(Call<ArrayList<moras>> call, Throwable t) {
      }
    });
  }

  public void enviarCuotasServidor_mejorada(List<cuotas> listacuotas, View view) {
    String json = new Gson().toJson(listacuotas);
    json = json;

    interfacesCreditoLaravel interfazCuotas =
      retrofit.create(interfacesCreditoLaravel.class);
    Call<CuotasResponse> response = interfazCuotas.envioCuotas(json, idcobrador);
    response.enqueue(new Callback<CuotasResponse>() {
      @Override
      public void onResponse(Call<CuotasResponse> call, Response<CuotasResponse> response) {
        final crudsqlite crud = new crudsqlite(context);
        if (response.code() == 200 && response.body().getDatos().size() > 0) {
          Toast.makeText(context, "Cuotas Enviadas correctamente", Toast.LENGTH_SHORT).show();
          datospublicoskt.INSTANCE.getProgressbar().setVisibility(View.GONE);
        }
      }
      @Override
      public void onFailure(Call<CuotasResponse> call, Throwable t) {
        Log.e("Error", t.getMessage());
        Toast.makeText(context, "Error al guardar Cuotas" + t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });

  }

  public void guardarCuotaServer(cuotas c) {
    String json = new Gson().toJson(c);

    interfacesCreditoLaravel interfazCuotas =
      retrofit.create(interfacesCreditoLaravel.class);
    Call<responseCuota> response = interfazCuotas.guardarCuota(json);

    response.enqueue(new Callback<responseCuota>() {
      @Override
      public void onResponse(Call<responseCuota> call, Response<responseCuota> response) {
        if (response.code() == 200) {
          c.setCuentaid(response.body().getIdcuota());
          c.save();

          AlertDialog.Builder dialog = new AlertDialog.Builder(context);
          dialog.setTitle("Cuota enviada al servidor Correctamente");
          dialog.setIcon(R.drawable.ic_check);
          dialog.setMessage("La cuota se guardo en el telefono");
          dialog.setCancelable(false);
          dialog.setPositiveButton("ok", null);
          dialog.show();
        }
      }

      @Override
      public void onFailure(Call<responseCuota> call, Throwable t) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Error al guardar cuota en servidor");
        dialog.setIcon(R.drawable.ic_error);
        dialog.setMessage("La cuota se guardo en el telefono");
        dialog.setCancelable(false);
        dialog.setPositiveButton("ok", null);
        dialog.show();
      }
    });


  }

  public void enviarCuotasServidor(List<cuotas> listacuotas, View view) {
    String json = new Gson().toJson(listacuotas);
    json = json;
    interfacesCreditoLaravel interfazCuotas =
      retrofit.create(interfacesCreditoLaravel.class);
    Call<ResponseBody> response = interfazCuotas.guardarCuotas1(listacuotas);
    response.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Toast.makeText(context, "Codigo de Respuesta Cuotas" + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
        if (response.code() == 200) {
          view.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(context, "Error al guardar Cuotas" + t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void enviarMorasServidor(List<moras> listamoras, View view) {

    interfacesCreditoLaravel interfazCuotas =
      retrofit.create(interfacesCreditoLaravel.class);
    Call<ResponseBody> response = interfazCuotas.guardarMoras(listamoras);
    response.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Toast.makeText(context, "Codigo de Respuesta Moras" + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
        if (response.code() == 200) {
          view.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(context, "Error al guardar Moras" + t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void enviarCuotasServidor(View view) {

    interfacecredito interfasproducto =
      retrofit.create(interfacecredito.class);

    Call<ArrayList<moras>> callcuotas = interfasproducto.ObtenerTodasMoras_cobrador(idcobrador);
    callcuotas.enqueue(new Callback<ArrayList<moras>>() {
      @Override
      public void onResponse(Call<ArrayList<moras>> call, Response<ArrayList<moras>> response) {
        if (response.code() == 200) {
          for (int i = 0; i < response.body().size(); i++) {
            moras c = response.body().get(i);
            c.save();
          }
          Snackbar snackbar = Snackbar
            .make(view, String.valueOf(response.body().size()) + " Moras cargadas Correctamente", Snackbar.LENGTH_LONG);
          snackbar.show();
        }
      }

      @Override
      public void onFailure(Call<ArrayList<moras>> call, Throwable t) {
      }
    });

  }

  public void guardar_solicitud(List<solicitud_credito> listasolicitud, View view) {
    interfacesolicitud interfacesolicitud =
      retrofit.create(interfacesolicitud.class);
    Call<ResponseBody> response = interfacesolicitud.guardar_solicitud(listasolicitud);
    response.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 200) {
          Toast.makeText(context, "Desembolsos Enviados Correctamente", Toast.LENGTH_SHORT).show();
          Snackbar.make(view, "Desembolsos Enviados Correctamente", Snackbar.LENGTH_LONG).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(context, "Ha ocurrido un Error", Toast.LENGTH_SHORT).show();
      }
    });


  }

  public void login(String email, String password, loginActivity loginActivity) {

    interfacesLogin login =
      retrofit.create(interfacesLogin.class);
    Call<responseLogin> response = login.login(email, password);

    response.enqueue(new Callback<responseLogin>() {
      @Override
      public void onResponse(Call<responseLogin> call, Response<responseLogin> response) {
        if (response.code() == 200) {
          if (response.body().getStatus().equalsIgnoreCase("true") && response.body().getError() == false) {
            Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
            loginActivity.finish();
          } else {
            Toast.makeText(context, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
          }
        }
      }

      @Override
      public void onFailure(Call<responseLogin> call, Throwable t) {
        Toast.makeText(context, "Usuario o Contraseña incorrecto", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
