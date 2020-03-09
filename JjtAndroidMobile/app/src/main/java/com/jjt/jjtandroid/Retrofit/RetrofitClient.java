package com.jjt.jjtandroid.Retrofit;

import com.jjt.jjtandroid.DefinicoesStaticos.DefinicoesEstatico;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public RetrofitClient() {
    }

    public Retrofit constroidRetrofitClient(OkHttpClient cliente) {

        OkHttpClient httpClient;

        httpClient = cliente;

        Retrofit retrofitAutenticado = new Retrofit.Builder()
                .baseUrl(DefinicoesEstatico.getHostServerWsdl())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofitAutenticado;

    }

}
