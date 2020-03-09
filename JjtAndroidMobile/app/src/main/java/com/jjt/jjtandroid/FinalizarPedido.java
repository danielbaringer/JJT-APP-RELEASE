package com.jjt.jjtandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jjt.jjtandroid.Classes.Cliente;
import com.jjt.jjtandroid.Classes.ItemPedido;
import com.jjt.jjtandroid.Classes.MensagemDialogo;
import com.jjt.jjtandroid.Classes.Pedido;
import com.jjt.jjtandroid.Classes.Produto;
import com.jjt.jjtandroid.Classes.SessaoUsuario;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;
import com.jjt.jjtandroid.Sqlite.SqliteHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FinalizarPedido extends AppCompatActivity {

    String usrLoginString;
    String usrTokenString;

    EditText textCnpjEntrega;
    EditText textDescEntrega;

    String usrCnpjCadastro;
    String usrCodigoCadastro;
    Button btnAcaoConfirmarPedido;

    SQLiteDatabase connSql;
    List<Produto> listaProdutosPedido = new ArrayList<Produto>();
    ListView ListaProdsFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_pedido);

        usrLoginString = ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_USER.toString();
        usrTokenString = ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_HASHTOKEN.toString();
        usrCnpjCadastro = String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ);
        usrCodigoCadastro = String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CODIGOUSER);

        textCnpjEntrega = findViewById(R.id.editTextCnpjEntrega);
        textDescEntrega = findViewById(R.id.editTextDescEntrega);


        btnAcaoConfirmarPedido = findViewById(R.id.btnFinalizarPedido);

        ListaProdsFinalizar = findViewById(R.id.ListaProdutosFinalizar);


        FinalizarPedido.adaptadorFinalizarProdutos adapter = new FinalizarPedido.adaptadorFinalizarProdutos(FinalizarPedido.this,R.layout.layout_view_finalizar_pedido,0,retornaListaPedidos());
        ListaProdsFinalizar.setAdapter(adapter);

        btnAcaoConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("LOG_EVENTO_CLICK","CLICOU EVENTO FINALIZAR PEDIDO");
                enviaDadosPedidoJjt();

            }
        });

    }

    public List<Produto> retornaListaPedidos(){

        try {

            connSql = new SqliteHandler(FinalizarPedido.this).connBancoDados();

            Cursor cursorSql = connSql.rawQuery("SELECT tbl003CodigoProduto, SUM(tbl003QtdeProduto) tbl003QtdeProduto, tbl003VlrUnitario , cast(SUM(tbl003VlrTotal) as decimal(18,2)) tbl003VlrTotal FROM tbl003JjtItemPedido GROUP BY tbl003CodigoProduto, tbl003VlrUnitario;", null);
            //Cursor cursorSql = connSql.rawQuery("SELECT tbl003CodigoProduto, tbl003QtdeProduto FROM tbl003JjtItemPedido;", null);


            if (cursorSql != null) {

                Log.e("TAM_CURSOR", "TAMANHO DO CURSOR: " + String.valueOf(cursorSql.getCount()));

                Integer posicao = 0;

                try {
                    while (cursorSql.moveToNext()) {

                        Produto novoProd = new Produto();

                        novoProd.setReferencia(cursorSql.getString(cursorSql.getColumnIndex("tbl003CodigoProduto")));
                        novoProd.setQtdeProduto(cursorSql.getString(cursorSql.getColumnIndex("tbl003QtdeProduto")));
                        novoProd.setValorUnitario(Double.parseDouble(cursorSql.getString(cursorSql.getColumnIndex("tbl003VlrUnitario"))));
                        novoProd.setValorTotal(Double.parseDouble(cursorSql.getString(cursorSql.getColumnIndex("tbl003VlrTotal"))));
                        listaProdutosPedido.add(novoProd);

                        Log.e("SQL_LOG","PREÇO: " + String.valueOf(Double.parseDouble(cursorSql.getString(cursorSql.getColumnIndex("tbl003VlrUnitario")))));

                        posicao += 1;

                    }
                } finally {
                    cursorSql.close();
                }

            }

        } catch (Exception exp) {

            Log.e("LOG_EXP", "ERRO: " + exp.getMessage().toString());
        }

        return listaProdutosPedido;

    }

    public class adaptadorFinalizarProdutos extends ArrayAdapter {

        Context cxt;
        int viewLayout;
        List<Produto> listaArrayProdutos = new ArrayList<Produto>();
        LayoutInflater LInflator = null;

        public adaptadorFinalizarProdutos(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
            super(context, resource, textViewResourceId, objects);

            cxt = context;
            viewLayout = resource;
            listaArrayProdutos = objects;
            LInflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View minhaView = convertView;
            ProdutoItemViewHolder holderView;

            if (minhaView == null) {

                //minhaView = LInflator.inflate(viewLayout,null);
                minhaView = LayoutInflater.from(getContext()).inflate(viewLayout, parent, false);

                TextView txtViewQuantidadeProd = (TextView) minhaView.findViewById(R.id.txtQtdeProdFinalizar);
                TextView txtViewReferenciaProd = (TextView) minhaView.findViewById(R.id.txtTituloProdFinalizar);
                TextView txtViewValorUnitario = (TextView) minhaView.findViewById(R.id.txtProdValorUnitario);
                TextView txtViewValorTotal = (TextView) minhaView.findViewById(R.id.txtProdValorTotal);

                txtViewReferenciaProd.setText("Produto: " + listaArrayProdutos.get(position).getReferencia());
                txtViewQuantidadeProd.setText("Qtde do pedido: " + listaArrayProdutos.get(position).getQtdeProduto());
                txtViewValorUnitario.setText(String.valueOf(String.format("%.2f",listaArrayProdutos.get(position).getValorUnitario())));
                txtViewValorTotal.setText(String.valueOf(String.format("%.2f",listaArrayProdutos.get(position).getValorTotal())));

                Log.e("SQL_LOG","LISTAGEM PREÇO: " + (String.format("%.2f",listaArrayProdutos.get(position).getValorUnitario())));

                holderView = new FinalizarPedido.ProdutoItemViewHolder(txtViewReferenciaProd, txtViewQuantidadeProd,txtViewValorUnitario,txtViewValorTotal);

                minhaView.setTag(holderView);


            } else {

                holderView = (ProdutoItemViewHolder) minhaView.getTag();
            }


            return minhaView;
        }
    }

    public static class ProdutoItemViewHolder {

        public TextView txtViewQuantidadeProd;
        public TextView txtViewReferenciaProd;
        public TextView txtViewValorUnitarioProd;
        public TextView txtViewValorTotalProd;

        public ProdutoItemViewHolder() {
        }

        public ProdutoItemViewHolder(TextView referenciaProd, TextView quantidadeProd,TextView vlrUnitarioProd,TextView vlrTotalProd) {

            this.txtViewQuantidadeProd = quantidadeProd;
            this.txtViewReferenciaProd = referenciaProd;
            this.txtViewValorUnitarioProd = vlrUnitarioProd;
            this.txtViewValorTotalProd = vlrTotalProd;

        }

    }


    public Pedido configuraDadosPedidoJjt(){

        Cliente dadosCliente = new Cliente();
        Pedido dadosNovoPedido = new Pedido();
        ItemPedido dadosItemNovoPedido = new ItemPedido();
        List<Produto> listaDadosProdutos = new ArrayList<Produto>();
        List<ItemPedido> listaDadosItemNovoPedido = new ArrayList<ItemPedido>();

        try{

            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            String pedidoNome = "PEDIDO_" + timeStamp;
            dadosCliente.setCpfCnpjCliente(String.valueOf(usrCnpjCadastro));

            Log.e("CNPJ_PEDIDO","CNPJ: " + String.valueOf(usrCnpjCadastro));

            String dadosCnpjSelecionadoPedido = String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ_PEDIDO);


            dadosNovoPedido.setCodPedidoMobile(pedidoNome);
            dadosNovoPedido.setClientePedido(dadosCliente);
            //dadosNovoPedido.setCnpjEntrega(String.valueOf(textCnpjEntrega.getText()));
            dadosNovoPedido.setCnpjEntrega(dadosCnpjSelecionadoPedido);
            dadosNovoPedido.setDescEntrega(String.valueOf(textDescEntrega.getText()));

            listaDadosItemNovoPedido.clear();
            listaDadosProdutos = retornaListaPedidos();

            for(Produto prd : listaDadosProdutos){

                dadosItemNovoPedido = new ItemPedido();
                dadosItemNovoPedido.setCodPedido(prd.getCodigoproduto());
                dadosItemNovoPedido.setReferencia(String.valueOf(prd.getReferencia()));
                dadosItemNovoPedido.setQtdeProdutoItemPedido(Integer.parseInt(prd.getQtdeProduto()));

                if(!listaDadosItemNovoPedido.contains(dadosItemNovoPedido)){

                    listaDadosItemNovoPedido.add(dadosItemNovoPedido);
                }


            }

            /*dadosItemNovoPedido.setCodigoproduto(1);
            dadosItemNovoPedido.setQtdeProdutoItemPedido(15);

            listaDadosItemNovoPedido.add(dadosItemNovoPedido);

            dadosItemNovoPedido = new ItemPedido();
            dadosItemNovoPedido.setCodigoproduto(2);
            dadosItemNovoPedido.setQtdeProdutoItemPedido(105);


            listaDadosItemNovoPedido.add();*/


            dadosNovoPedido.setListaPedidoProdutos(listaDadosItemNovoPedido);


        }catch (Exception xp){

        }

        return dadosNovoPedido;

    }

    public void enviaDadosPedidoJjt() {


        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(FinalizarPedido.this,"Aguarde!","Enviando os dados do pedido para o servidor...");
        MsgDialogo.show();

        try{


            Log.e("FUNC_ENVIA_PEDIDO_CLICK","CLICOU ENVIA PEDIDO FINALIZAR !");

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<Pedido> callNovoPedidoCall = RetrofitCallProds.registraCadastroPedido(configuraDadosPedidoJjt());

            callNovoPedidoCall.enqueue(new Callback<Pedido>() {
                @Override
                public void onResponse(Call<Pedido> call, Response<Pedido> response) {

                    MsgDialogo.dismiss();

                    Log.e("LOG_RESP_PEDIDO", String.valueOf(response.body()));

                    Log.e("LOG_RESP_PEDIDO", String.valueOf(response.message()));

                    if(response.code() == 200){

                        Pedido ped = response.body();

                        Cliente cli = ped.getClientePedido();

                        Log.e("FUNC_PEDIDO_OK","CLIENTE: " + String.valueOf(cli.getCodCliente()));
                        Log.e("FUNC_PEDIDO_OK","CLIENTE: " + String.valueOf(cli.getCpfCnpjCliente()));
                        Log.e("FUNC_PEDIDO_OK","CLIENTE: " + String.valueOf(cli.getNomeCliente()));
                        Log.e("FUNC_PEDIDO_OK","CLIENTE: " + String.valueOf(cli.getEmailCliente()));
                        Log.e("FUNC_PEDIDO_OK","CLIENTE: " + String.valueOf(cli.getTelCliente()));
                        Log.e("FUNC_PEDIDO_OK","CLIENTE: " + String.valueOf(cli.getMobileTelCliente()));

                        Log.e("FUNC_PEDIDO_OK","RESP PEDIDO OK !");

                        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(FinalizarPedido.this,"Ok!","Pedido enviado com sucesso...");
                        MsgDialogo.show();


                        new SqliteHandler(FinalizarPedido.this).executaSqlScript("DELETE FROM tbl003JjtItemPedido;");

                        startActivity(new Intent(FinalizarPedido.this,ProdutosListar.class));
                        finish();

                    } else {
                        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(FinalizarPedido.this,"Erro!","Não foi possível enviar os dados do pedido!");
                        MsgDialogo.show();
                    }

                }

                @Override
                public void onFailure(Call<Pedido> call, Throwable t) {
                    MsgDialogo.dismiss();
                    Log.e("LOG_RESP_PEDIDO", String.valueOf(t.getMessage()));
                    final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(FinalizarPedido.this,"Falha!","Falha: " + String.valueOf(t.getMessage()));
                    MsgDialogo.show();
                }
            });

        }catch (Exception xp){
            Log.e("LOG_RESP_PEDIDO", String.valueOf(xp.getMessage()));
            MsgDialogo.dismiss();
            final AlertDialog MsgAlert = new MensagemDialogo().constroiDialogoClientNeutro(FinalizarPedido.this,"Erro!","Exception: " + String.valueOf(xp.getMessage()));
            MsgAlert.show();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menutopocarrinho, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {


            case R.id.idSobre:

                MensagemDialogo dialogo = new MensagemDialogo();
                AlertDialog alerta = dialogo.constroiDialogoClientNeutro(FinalizarPedido.this,"JJT IMPORTADORA","Desenvolvido por Daniel Baringer\r\ndaniel.baringer@gmail.com");
                alerta.show();

                return(true);

            case R.id.idVoltar:



                startActivity(new Intent(FinalizarPedido.this,ProdutosListar.class));
                finish();
                return(true);


            case R.id.idSair:

                startActivity(new Intent(this, MainActivity.class));
                return(true);



        }

        return(super.onOptionsItemSelected(item));
    }


}