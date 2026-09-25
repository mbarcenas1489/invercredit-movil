package com.example.creditosappandroidx.cswebservice;

import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import com.example.creditosappandroidx.models.response.CuotasResponse;

public interface interfacesCreditoLaravel
{
    @POST("api/credito_servicio/guardar_cuotas")
    Call<responseWebService> guardarCuotas(@Body List<cuotas> datos);

    @POST("api/credito_servicio/guardar_cuotas")
    Call<ResponseBody> guardarCuotas1(@Body List<cuotas> datos);

    @FormUrlEncoded
    @POST("api/credito_servicio/envioCuotas")
    Call<CuotasResponse> envioCuotas(@Field("datos") String datos, @Field("idcobrador") Integer cobrador);

    @FormUrlEncoded
    @POST("api/credito_servicio/guardarCuota")
    Call<responseCuota> guardarCuota(@Field("datos") String datos);

    @POST("api/credito_servicio/guardar_moras")
    Call<ResponseBody> guardarMoras(@Body List<moras> datos);

    @POST("api/credito_servicio/isConnectServer")
    Call<ResponseBody> isConnectServer();

    @GET("api/credito_servicio/{idcobrador}/getPrestamoCliente")
    Call<ArrayList<creditocliente>> obtenercredito_cobrador(@Path("idcobrador") Integer cobrador);

    @GET("api/credito_servicio/{idcobrador}/getCuotasPorCobrador")
    Call<ArrayList<cuotas>> ObtenerTodasCuotas_cobrador(@Path("idcobrador") Integer cobrador);

    @GET("api/credito_servicio/{idcobrador}/getMorasPorCobrador")
    Call<ArrayList<moras>> ObtenerTodasMoras_cobrador(@Path("idcobrador") Integer cobrador);
}
