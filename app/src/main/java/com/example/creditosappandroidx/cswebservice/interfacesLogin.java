package com.example.creditosappandroidx.cswebservice;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface interfacesLogin {


  @FormUrlEncoded
  @POST("api/autenticar/login")
  Call<responseLogin> login(@Field("email") String email, @Field("password") String password);


}
