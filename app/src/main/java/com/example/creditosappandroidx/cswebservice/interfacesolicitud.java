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
import retrofit2.http.Query;

public interface interfacesolicitud
{



    @POST("api/credito_servicio/guardar_solicitud")
    Call<ResponseBody> guardar_solicitud(@Body List<solicitud_credito> datos);

}
