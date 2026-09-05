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

public interface interfacesLogin
{


    @FormUrlEncoded
    @POST("api/autenticar/login")
    Call<responseLogin> login(@Field("email") String email, @Field("password") String password);



}
