package com.jjt.jjtandroid.Retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {


    public HttpClient() {
    }

    public OkHttpClient constroiOkHttpClient(String API_HASH_TOKEN,String API_HASH_USR){

        final String localHASHTOKEN = API_HASH_TOKEN;
        final String localHASHUSR = API_HASH_USR;

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + localHASHTOKEN.toString())
                        .addHeader("From", localHASHUSR.toString())
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        return client;

    }



}

