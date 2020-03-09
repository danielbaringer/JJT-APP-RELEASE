package com.jjt.jjtandroid.Interfaces;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewWsdlJjtWebApi {

    @GET("api/AcessoLogin/execLogin/")
    Call<JsonObject> execLoginUsr(@Query("usr") String usr, @Query("pwd") String pwd);



}
