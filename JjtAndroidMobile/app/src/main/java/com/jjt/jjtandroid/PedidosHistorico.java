package com.jjt.jjtandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjt.jjtandroid.Classes.Pedido;
import com.jjt.jjtandroid.Classes.Produto;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PedidosHistorico extends AppCompatActivity {


    ProgressBar progressbar;
    RecyclerView listaPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_historico);

        progressbar = findViewById(R.id.progressbar);
        listaPedidos = findViewById(R.id.listaHistPedidos);

        carregaHistoricoPedidos();


    }


    public void carregaHistoricoPedidos(){


        progressbar.setVisibility(View.VISIBLE);

        Log.e("DadosItemPedido",String.valueOf("Iniciou a função carregaHistoricoPedidos();"));

        try{

            Log.e("DadosItemPedido",String.valueOf("Entrou no TRY CATCH carregaHistoricoPedidos();"));

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient("","");
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<List<Pedido>> listaHistoricoPedidos = RetrofitCallProds.listarHistoricoPedidosCliente("2","");


            listaHistoricoPedidos.enqueue(new Callback<List<Pedido>>() {
                @Override
                public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {

                    progressbar.setVisibility(View.GONE);
                    int pos = 0;

                    List<Pedido> listaPedidoHist = new ArrayList<Pedido>();

                    if(response.code() == 200 && response.isSuccessful()){

                        Toast.makeText(PedidosHistorico.this,"Histórico de pedidos localizado!",Toast.LENGTH_LONG).show();

                        listaPedidoHist = response.body();

                        /*final String[] arrayHistPedidos = new String[listaPedidoHist.size()];
                        Gson gson= new Gson();

                        for(Pedido ped : listaPedidoHist){

                            arrayHistPedidos[pos] = String.valueOf(gson.toJson(ped));  //ped.getCodPedido() + " | " + ped.getDataPedido() + " | " + ped.getQtdeProdPedido();

                            //pos += 1;
                        }


                        Log.e("DadosItemPedido",String.valueOf(arrayHistPedidos[pos]));*/

                        /*ArrayAdapter adapterHistPedidos = new ArrayAdapter(PedidosHistorico.this,android.R.layout.simple_spinner_item, arrayHistPedidos);
                        adapterHistPedidos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listaPedidos.setAdapter(adapterHistPedidos);*/

                        HistoricoAdapter adapter = null;

                        listaPedidos.setLayoutManager(new LinearLayoutManager(PedidosHistorico.this));
                        adapter = new HistoricoAdapter(PedidosHistorico.this, listaPedidoHist);
                        //adapter.setClickListener(this);
                        listaPedidos.setAdapter(adapter);

                    } else {

                        Toast.makeText(PedidosHistorico.this,"Erro! Histórico de pedidos não localizado!",Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<List<Pedido>> call, Throwable t) {

                    Toast.makeText(PedidosHistorico.this,"Falha! Histórico de pedidos não localizado!",Toast.LENGTH_LONG).show();

                }
            });


        }catch (Exception xp){

            progressbar.setVisibility(View.GONE);
        }
    }


    public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.ViewHolder> {

        private List<Pedido> mData = new ArrayList<Pedido>();
        private LayoutInflater mInflater;
        //private ItemClickListener mClickListener;

        // data is passed into the constructor
        HistoricoAdapter(Context context, List<Pedido> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_historico_pedidos, parent, false);
            return new ViewHolder(itemView );
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Pedido ped = mData.get(position);

            Log.e("AGENDAMENTO_RICYCLER","AGENDAMENTO: " + String.valueOf(ped.getCodPedido()));

            holder.myTextViewCodHistorico.setText("0");
            if(ped.getCodPedido() != null && Integer.parseInt(String.valueOf(ped.getCodPedido())) > 0){
                holder.myTextViewCodHistorico.setText(ped.getCodPedido().toString());
            }

            holder.myTextViewCodHistorico.setText(String.valueOf(ped.getCodPedido()));
            holder.myTextViewDataHistorico.setText("Data pedido: " + String.valueOf(ped.getDataPedido()));
            holder.myTextViewQtdeItensHistorico.setText("Qtde de itens: " + String.valueOf(ped.getQtdeProdPedido()));
        }


        // total number of rows
        @Override
        public int getItemCount() {

            Log.e("" +
                    "ANHO_LISTA_AGENDA","TAM: " + String.valueOf(mData.size()));
            return mData.size();

        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView myTextViewCodHistorico;
            protected TextView myTextViewDataHistorico;
            protected TextView myTextViewQtdeItensHistorico;

            public ViewHolder(final View itemView) {
                super(itemView);


                myTextViewCodHistorico = itemView.findViewById(R.id.CodIdRegHist);
                myTextViewDataHistorico = itemView.findViewById(R.id.txtViewLabelDataPedido);
                myTextViewQtdeItensHistorico = itemView.findViewById(R.id.txtViewLabelQtdeItens);

                //Setup the click listener
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Pedido dadosPedido = (Pedido) mData.get(getLayoutPosition());

                        //Log.e("INFO_CLICK_AGENDA","AGENDA CLICK: " + String.valueOf(dadosAgenda.getId()));

                        Toast.makeText(PedidosHistorico.this,String.valueOf(dadosPedido.getCodPedido() + " - " + dadosPedido.getDataPedido()),Toast.LENGTH_LONG).show();

                        //Log.e("LOG_DADOS_TELA","DADOS:" + String.valueOf(dadosAgenda.getId()) + " - " + String.valueOf(dadosAgenda.getIDAgendamento()) + " - " + String.valueOf(dadosAgenda.getPlaca()));


                        //handlerIniciaAgendamento(dadosAgenda);


                    }
                });

            }
        }

    }


}
