package com.jjt.jjtandroid.Classes;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjt.jjtandroid.FinalizarPedido;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Interfaces.retornaObjLista;
import com.jjt.jjtandroid.ProdutosListar;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;
import com.jjt.jjtandroid.Sqlite.SqliteHandler;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class viewHolderHistPedidos {

    public Button holderBtnViewReplicarPedido;
    public TextView holderTxtViewCodigoPedido;
    public TextView holderTxtViewDataPedido;
    public TextView holderTxtViewCodigoItemPedido;
    public TextView holderTxtViewQtdeProdsPedido;
    public TextView holderTxtViewQtdeItensProdsPedido;
    public TextView holderTxtViewVlrTotalPedido;
    public ListView holderListViewDetalhesPedido;

    public viewHolderHistPedidos(){}

    public viewHolderHistPedidos(Button btnReplicarPedido, TextView codPedido, TextView dataPedido, TextView codItemPedido, TextView qtdeProdsPedido, TextView qtdeItensProdsPedido,TextView vlrTotalProdsPedido, ListView listaDetalhesProdsPedido) {

        this.holderBtnViewReplicarPedido = btnReplicarPedido;
        this.holderTxtViewCodigoPedido= codPedido;
        this.holderTxtViewDataPedido = dataPedido;
        this.holderTxtViewCodigoItemPedido= codItemPedido;
        this.holderTxtViewQtdeProdsPedido= qtdeProdsPedido;
        this.holderTxtViewQtdeItensProdsPedido= qtdeItensProdsPedido;
        this.holderTxtViewVlrTotalPedido = vlrTotalProdsPedido;
        this.holderListViewDetalhesPedido =listaDetalhesProdsPedido;
    }

    public void setClickEventoOnReplicarPedido(Context context){

        final Context cxt = context;

        try{

            holderBtnViewReplicarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(cxt,"Teste - Código Pedido: " + String.valueOf(holderTxtViewCodigoPedido.getText()),Toast.LENGTH_LONG).show();

                    geraListaItensHistPedido(cxt, String.valueOf(holderTxtViewCodigoPedido.getText()), new retornaObjLista() {
                        @Override
                        public void retornaObjLista(List listaObj) {

                            Intent finalizaPedido = new Intent(cxt,FinalizarPedido.class);
                            cxt.startActivity(finalizaPedido);

                        }
                    });
                }
            });

        }catch (Exception xp){

        }

    }

    public void setDadosParaListView(Context context, String[] values){

        for(int k=0;k<values.length;k++){
            Log.e("InfoHist","VALOR INDICE ARRAY : " + String.valueOf(values[k]));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, android.R.id.text1, values);
        this.holderListViewDetalhesPedido.setAdapter(adapter);

    }

    public void geraListaItensHistPedido(Context cxt, String codPedidoCliente, final retornaObjLista callbacks){

        final Context contexto = cxt;

        String usrCnpjCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CPFCNPJ);
        String usrCodigoCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CODIGOUSER);
        String usrLoginString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_USER;
        String usrTokenString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_HASHTOKEN;

        RetrofitClient retroCliente = new RetrofitClient();
        HttpClient clienteHttp = new HttpClient();
        OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
        Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
        WsdlJjtApi RetrofitCallPedido  = localRetrofit.create(WsdlJjtApi.class);
        final Call<List<ItemPedido>> listaItensCall = RetrofitCallPedido.listarHistItensPedidoCliente(codPedidoCliente);

        Log.e("LOG_URL","URL ITENS PEDIDO: " + String.valueOf(listaItensCall.request().url()));

        listaItensCall.enqueue(new Callback<List<ItemPedido>>() {
            @Override
            public void onResponse(Call<List<ItemPedido>> call, Response<List<ItemPedido>> response) {

                Log.e("LOG",response.message().toString());
                Log.e("LOG","JSON !");

                List<ItemPedido> listaItemProdViewHolder = new ArrayList<ItemPedido>();

                if(response.code() == 200){

                    listaItemProdViewHolder = response.body();

                    if(listaItemProdViewHolder != null && listaItemProdViewHolder.size() > 0) {

                        Log.e("LOG", "JSON NÃO ESTA VAZIO !");

                        new SqliteHandler(contexto).executaSqlScript("DELETE FROM tbl003JjtItemPedido;");


                        for (int i = 0; i < listaItemProdViewHolder.size(); i++) {

                            Log.e("LOG_JSON", "RESP: " + listaItemProdViewHolder.get(i).getDescricao());

                            Integer qtdeTotalProd = listaItemProdViewHolder.get(i).getQtdeProdutoItemPedido();

                            Produto prod = listaItemProdViewHolder.get(i).getDadosProduto();

                            String vlrUnitario = String.valueOf(prod.getValorUnitario());

                            String vlrTotal =  String.valueOf(Double.valueOf(vlrUnitario) * Integer.valueOf(qtdeTotalProd));

                            String referenciaProd = prod.getReferencia();

                            new SqliteHandler(null).executaSqlScript("INSERT INTO tbl003JjtItemPedido (tbl003LoginUsuario,tbl003VlrUnitario ,tbl003VlrTotal,tbl003CodigoProduto,tbl003QtdeProduto) VALUES('daniel.baringer@gmail.com','" + String.valueOf(vlrUnitario) + "','" + String.valueOf(vlrTotal) + "','" + String.valueOf(referenciaProd) + "','" + String.valueOf(qtdeTotalProd) + "');");


                        }


                        callbacks.retornaObjLista(listaItemProdViewHolder);

                    } else {
                        callbacks.retornaObjLista(new ArrayList<ItemPedido>());
                    }

                } else {
                    callbacks.retornaObjLista(new ArrayList<ItemPedido>());
                }

            }

            @Override
            public void onFailure(Call<List<ItemPedido>> call, Throwable t) {
                callbacks.retornaObjLista(new ArrayList<ItemPedido>());
            }
        });

    }

}
