package com.example.creditosappandroidx.cswebservice;



import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface interfacesolicitud
{



    @POST("api/credito_servicio/guardar_solicitud")
    Call<ResponseBody> guardar_solicitud(@Body List<solicitud_credito> datos);

}
