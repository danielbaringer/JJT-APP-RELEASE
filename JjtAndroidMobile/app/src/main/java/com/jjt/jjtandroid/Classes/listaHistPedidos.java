package com.jjt.jjtandroid.Classes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Interfaces.retornaObjLista;
import com.jjt.jjtandroid.PedidosHistorico;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class listaHistPedidos {

    public void retornaListaHistPedidos(Context cxt, String codCliente, String cpfCpnjCliente, final retornaObjLista callbacks){

        String usrCnpjCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CPFCNPJ);
        String usrCodigoCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CODIGOUSER);
        String usrLoginString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_USER;
        String usrTokenString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_HASHTOKEN;

        try{

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient("","");
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<List<Pedido>> listaHistoricoPedidos = RetrofitCallProds.listarHistoricoPedidosCliente(codCliente,cpfCpnjCliente);


            listaHistoricoPedidos.enqueue(new Callback<List<Pedido>>() {
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {

                    int pos = 0;

                    List<Pedido> listaPedidoHist = new ArrayList<Pedido>();
                    List<customHolderFieldsHistoricoPedidos> listaHistorico = new ArrayList<customHolderFieldsHistoricoPedidos>();

                    if(response.code() == 200 && response.isSuccessful()){

                        listaPedidoHist = response.body();

                        if(listaPedidoHist != null && listaPedidoHist.size() > 0){


                            for(Pedido ped : listaPedidoHist){

                                customHolderFieldsHistoricoPedidos pediHist = new customHolderFieldsHistoricoPedidos();

                                pediHist.holderViewCodigoPedido = String.valueOf(ped.getCodPedido());
                                pediHist.holderViewDataPedido = String.valueOf(ped.getDataPedido());
                                pediHist.holderViewCodigoItemPedido = String.valueOf(ped.getCodPedido());
                                pediHist.holderViewQtdeProdsPedido = String.valueOf(ped.getQtdeProdPedido());
                                pediHist.holderViewQtdeItensProdsPedido = String.valueOf(ped.getQtdeTotalItensPedido());

                                List<ItemPedido> listHistItensPedido= new ArrayList<ItemPedido>();

                                listHistItensPedido = ped.getListaPedidoProdutos();
                                final String[] arrayHistProdsPedidos = new String[listHistItensPedido.size()];


                                Integer indice = 0;
                                Double vlrProd = Double.parseDouble(String.valueOf("0.00"));
                                for(ItemPedido item : listHistItensPedido) {

                                    Produto dadosProd = new Produto();
                                    dadosProd = item.getDadosProduto();

                                    vlrProd = vlrProd + (Double.parseDouble(String.valueOf(dadosProd.getValorUnitario())) * Integer.valueOf(item.getQtdeProdutoItemPedido()));

                                    pediHist.holderViewCodigoItemPedido = String.valueOf(item.getCodItemPedido());

                                    String dadosItemProduto = "Ref: " + String.valueOf(dadosProd.getReferencia()) + " - Qde: " + String.valueOf(item.getQtdeProdutoItemPedido()) + "- Vlr: " + String.valueOf(String.format("%.2f",dadosProd.getValorUnitario()));
                                    arrayHistProdsPedidos[indice] = dadosItemProduto;

                                    pediHist.holderViewProdutosString.add(dadosItemProduto);

                                    indice++;


                                }

                                pediHist.holderViewQtdeTotalVlrPedido = String.valueOf(String.format("%.2f",vlrProd));

                                listaHistorico.add(pediHist);
                            }

                        }

                        callbacks.retornaObjLista(listaHistorico);

                    } else {

                        callbacks.retornaObjLista(new ArrayList<customHolderFieldsHistoricoPedidos>());
                    }


                }

                @Override
                public void onFailure(Call<List<Pedido>> call, Throwable t) {

                    callbacks.retornaObjLista(new ArrayList<customHolderFieldsHistoricoPedidos>());
                }
            });

        }catch (Exception xp){


            callbacks.retornaObjLista(new ArrayList<customHolderFieldsHistoricoPedidos>());


        }

    }

}
