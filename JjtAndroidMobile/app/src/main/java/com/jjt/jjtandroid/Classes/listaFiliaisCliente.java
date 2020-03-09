package com.jjt.jjtandroid.Classes;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Interfaces.retornaObjLista;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class listaFiliaisCliente {

    public void retornaListaFiliaisCliente(Context cxt,  final retornaObjLista callbacks){

        String usrCnpjCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CPFCNPJ);
        String usrCodigoCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CODIGOUSER);
        String usrLoginString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_USER;
        String usrTokenString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_HASHTOKEN;

        RetrofitClient retroCliente = new RetrofitClient();
        HttpClient clienteHttp = new HttpClient();
        OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
        Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
        WsdlJjtApi RetrofitCallFiliais  = localRetrofit.create(WsdlJjtApi.class);
        final Call<List<Cliente>> listaProdsCall = RetrofitCallFiliais.listaFiliaisCliente(usrCnpjCadastro);

        Log.e("LOG_URL","URL CLIENTE: " + String.valueOf(listaProdsCall.request().url()));

        listaProdsCall.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {

                Log.e("LOG",response.message().toString());
                Log.e("LOG","JSON !");

                List<Cliente> meuJsonFiliaisArray = new ArrayList<Cliente>();

                if(response.code() == 200){

                    meuJsonFiliaisArray = response.body();

                    if(meuJsonFiliaisArray != null && meuJsonFiliaisArray.size() > 0) {

                        Log.e("LOG", "JSON NÃO ESTA VAZIO !");

                        callbacks.retornaObjLista(meuJsonFiliaisArray);

                    }

                } else {
                    callbacks.retornaObjLista(new ArrayList<Cliente>());
                }

            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                callbacks.retornaObjLista(new ArrayList<Cliente>());
            }
        });

    }

}
