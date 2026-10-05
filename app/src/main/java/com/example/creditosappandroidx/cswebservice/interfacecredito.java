package com.example.creditosappandroidx.cswebservice;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface interfacecredito {
  @GET("webservice/prestamocliente.php")
  Call<ArrayList<creditocliente>> obtenercredito();


  @GET("webservice/prestamocliente.php")
  Call<ArrayList<creditocliente>> obtenercredito_cobrador(@Query("idcobrador") Integer cobrador);

  //@GET("webservice/consultatodascuotas.php")
  //Call<ArrayList<cuotas>> ObtenerTodasCuotas();


  //Obtener todas las coutas por cobrador
  @GET("webservice/consultatodascuotas.php")
  Call<ArrayList<cuotas>> ObtenerTodasCuotas_cobrador(@Query("idcobrador") Integer cobrador);


  @GET("webservice/consultatodasmoras.php")
  Call<ArrayList<moras>> ObtenerTodasMoras_cobrador(@Query("idcobrador") Integer cobrador);


  @GET("webservice/consultacuotas.php")
  Call<ArrayList<cuotas>> ObtenercuotasByidprestamo(@Query("idprestamo") Integer idprestamo);

  @GET("webservice/consultamora.php")
  Call<ArrayList<moras>> ObtenerMora_by_idprestamo(@Query("idprestamo") Integer idprestamo);

  @GET("webservice/prestamocliente.php")
  Call<ArrayList<creditocliente>> conexion(@Query("idcobrador") Integer cobrador);


  @FormUrlEncoded
  @POST("webservice/insertcuota.php")
  Call<ResponseBody> InsertarCuota(@Field("fecha") String fecha,
                                   @Field("monto") float monto,
                                   @Field("prestamo_prestamoid") Integer prestamo_prestamoid);

  @FormUrlEncoded
  @POST("webservice/setmalo.php")
  Call<ResponseBody> setmalo(@Field("idprestamo") Integer idpresmo, @Field("malo") int malo);


  @FormUrlEncoded
  @POST("webservice/insertcuota_mora.php")
  Call<ResponseBody> InsertarCuota_mora(@Field("fecha") String fecha,
                                        @Field("monto") float monto,
                                        @Field("prestamo_prestamoid") Integer prestamo_prestamoid,
                                        @Field("mora") Integer mora, @Field("saldo") float saldo);

  @FormUrlEncoded
  @POST("webservice/insertMora.php")
  Call<ResponseBody> Insertar_Mora(@Field("fecha") String fecha,
                                   @Field("monto") float monto,
                                   @Field("idprestamo") Integer idprestamo,
                                   @Field("saldo") float saldo);

}
