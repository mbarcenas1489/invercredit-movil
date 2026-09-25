package cssqlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cswebservice.creditocliente;
import cswebservice.interfacecredito;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.support.v4.content.ContextCompat.getSystemService;

public final class csnetwork
{
    public static Context context=null;
    static Retrofit retrofit;
    static SharedPreferences pref ;
   /* public static String Conneccion(Context con)
    {
        context=con;
        String mensaje="";
        if(!isNetDisponible())
        {
            mensaje="La wifi esta apagada";
        }
        else {
            if (!isOnlineNet())
                mensaje += "No han connecion a internet";
            else
                mensaje="Hay Conexion";
        }
        return mensaje;


    }*/
    public static Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isNetDisponible(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public  static void isServerConect(final Context context, final TextView etservidor)
    {

        pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        String ip=pref.getString("ip","192.168.1.1");

        String cobrador= pref.getString("idcobrador","1");
        //Toast.makeText(context,"Cobrador:"+cobrador,Toast.LENGTH_SHORT).show();
        int idcobrador=Integer.parseInt(cobrador);


        retrofit =new Retrofit.Builder().
                baseUrl("http://"+ip+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        interfacecredito interfasproducto =
                retrofit.create(interfacecredito.class);
        Call<ArrayList<creditocliente>> verconexion= interfasproducto.conexion(idcobrador);

        verconexion.enqueue(new Callback<ArrayList<creditocliente>>() {
            @Override
            public void onResponse(Call<ArrayList<creditocliente>> call, Response<ArrayList<creditocliente>> response) {
                if(response.code()==200)
                {
                    etservidor.setText("Hay Connecion con el servidor");
                    etservidor.setTextColor(Color.rgb(34, 153, 84));//Verde

                    //Toast.makeText(context,"Hay connecion al servidor",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    etservidor.setText("No hay connexion con el servidor");
                    etservidor.setTextColor(Color.rgb(203, 67, 53));//Rojo
                    //Toast.makeText(context,"No Hay connecion al servidor",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ArrayList<creditocliente>> call, Throwable t)
            {
                etservidor.setText("Error al conectar al servidor");
                Toast.makeText(context,"Error en la connecion",Toast.LENGTH_SHORT).show();


            }
        });







    }

}
