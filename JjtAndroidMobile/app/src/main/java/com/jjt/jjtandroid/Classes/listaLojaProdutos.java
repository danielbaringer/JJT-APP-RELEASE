package com.jjt.jjtandroid.Classes;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.google.gson.JsonObject;
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

public class listaLojaProdutos {

    public void retornaListaLojaProdutos(Context cxt, String selecionarEmpresa, String selecionarProduto, final retornaObjLista callbacks){

        String usrCnpjCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CPFCNPJ);
        String usrCodigoCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CODIGOUSER);
        String usrLoginString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_USER;
        String usrTokenString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_HASHTOKEN;

        RetrofitClient retroCliente = new RetrofitClient();
        HttpClient clienteHttp = new HttpClient();
        OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
        Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
        WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
        final Call<List<Produto>> listaProdsCall = RetrofitCallProds.listaProds(selecionarEmpresa,selecionarProduto);

        Log.e("LOG_URL","URL PRODUTOS: " + String.valueOf(listaProdsCall.request().url()));

        listaProdsCall.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {

                Log.e("LOG",response.message().toString());
                Log.e("LOG","JSON !");

                List<customHolderFieldsProduto> listaProdutoItemViewHolder = new ArrayList<customHolderFieldsProduto>();

                if(response.code() == 200){

                    List<Produto> meuJsonArray = response.body();

                    if(meuJsonArray != null && meuJsonArray.size() > 0) {

                        Log.e("LOG", "JSON NÃO ESTA VAZIO !");

                        for (int i = 0; i < meuJsonArray.size(); i++) {

                            Log.e("LOG_JSON", "RESP: " + meuJsonArray.get(i).getDescricao());

                            customHolderFieldsProduto prd = new customHolderFieldsProduto();

                            prd.holderViewCodigoProd = (String.valueOf(meuJsonArray.get(i).getCodigoproduto()));
                            prd.holderViewQtdeProd= (String.valueOf("0"));
                            prd.holderViewDescProd = (String.valueOf(meuJsonArray.get(i).getDescricao()));
                            prd.holderViewReferenciaProd =(String.valueOf(meuJsonArray.get(i).getReferencia()));
                            prd.holderViewVlrUnitario =Double.parseDouble(String.valueOf(meuJsonArray.get(i).getValorUnitario()));
                            prd.holderViewVlrTotal =Double.parseDouble(String.valueOf("0.00"));
                            prd.holderViewImgByte = (meuJsonArray.get(i).getImgProduto().length() > 0 ? Base64.decode(meuJsonArray.get(i).getImgProduto(), Base64.DEFAULT) : new byte[0]);

                            Log.e("LOG_IMG","DADOS IMG PROD: " + String.valueOf(prd.holderViewImgByte.length));

                            Log.e("LOG_JSON_BASE64", "Tam Img: " + String.valueOf(meuJsonArray.get(i).getImgProduto().length() / 1024));


                            listaProdutoItemViewHolder.add(prd);

                        }


                        callbacks.retornaObjLista(listaProdutoItemViewHolder);

                    }

                } else {
                    callbacks.retornaObjLista(new ArrayList<customHolderFieldsProduto>());
                }

            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                callbacks.retornaObjLista(new ArrayList<customHolderFieldsProduto>());
            }
        });

    }

}
