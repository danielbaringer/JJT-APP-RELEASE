package com.jjt.jjtandroid;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jjt.jjtandroid.Classes.Cliente;
import com.jjt.jjtandroid.Classes.MensagemDialogo;
import com.jjt.jjtandroid.Classes.Pedido;
import com.jjt.jjtandroid.Classes.Produto;
import com.jjt.jjtandroid.Classes.SessaoUsuario;
import com.jjt.jjtandroid.Classes.customHolderFieldsHistoricoPedidos;
import com.jjt.jjtandroid.Classes.customHolderFieldsProduto;
import com.jjt.jjtandroid.Classes.customListaHistPedidosViewAdapter;
import com.jjt.jjtandroid.Classes.customListaProdutosViewAdapter;
import com.jjt.jjtandroid.Classes.listaFiliaisCliente;
import com.jjt.jjtandroid.Classes.listaHistPedidos;
import com.jjt.jjtandroid.Classes.listaLojaProdutos;
import com.jjt.jjtandroid.Classes.uteisClasse;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Interfaces.retornaObjLista;
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

public class ProdutosListar extends AppCompatActivity {

    SQLiteDatabase connSql;

    List<Produto> listaDbProdutos;
    List<Produto> listaProdutosPedido = new ArrayList<Produto>();

    Activity activity;
    Context cxt;

    TabHost tabHost;

    TextView textCnpjSelecionado;

    String usrCnpjCadastro;
    String usrCodigoCadastro;
    String usrLoginString;
    String usrTokenString;
    Button btnAcaoCancelarLimparPedido;
    Button btnAcaoAtualizaListaProdutos;

    Spinner dropDownSegmentoEmpresa;
    Spinner dropDownSegmentoProdutos;

    ProgressBar progressbar;
    ListView listagemDadosPessoa;
    ListView listagemProds;
    ListView listagemHistPedidosCliente;
    Button btnEventoListaDetalhes;
    Button btnEventoMostrarCabecalho;
    Button btnEventoIncluirNovaFilial;

    EditText editTextCnpjFilial;

    RelativeLayout layoutCabecalhoPedidos;
    RelativeLayout layoutMostrarDetalhesPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos_listar);

        activity = this;
        cxt = this;

        configTabMenu();

        carregaConfiguraComponentesTela();


        carregaHistoricoPedidos();
    }

    public void configTabMenu(){

        try{

            tabHost = findViewById(R.id.tabHost);
            tabHost.setup();

            TabHost.TabSpec spec1=tabHost.newTabSpec("TAB 1");
            spec1.setContent(R.id.tab1);
            spec1.setIndicator("Meus dados");


            TabHost.TabSpec spec2=tabHost.newTabSpec("TAB 2");
            spec2.setContent(R.id.tab2);
            spec2.setIndicator("Produtos");

            TabHost.TabSpec spec3=tabHost.newTabSpec("TAB 3");
            spec3.setContent(R.id.tab3);
            spec3.setIndicator("Histórico");


            tabHost.addTab(spec1);
            tabHost.addTab(spec2);
            tabHost.addTab(spec3);

        }catch (Exception xp){

        }

    }


    public void carregaConfiguraComponentesTela(){

        try{

            usrCnpjCadastro = String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ);
            usrCodigoCadastro = String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CODIGOUSER);
            usrLoginString = ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_USER.toString();
            usrTokenString = ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_HASHTOKEN.toString();

            if(usrCodigoCadastro == null || usrCodigoCadastro.isEmpty() || usrCodigoCadastro.equals("0") || Integer.parseInt(String.valueOf(usrCodigoCadastro)) <= 0){

                startActivity(new Intent(ProdutosListar.this,AtualizaSessaoUsrActivity.class));
                finish();
            }

            Log.e("CODIGO_CLIENTE","CODIGO CLINETE: " + String.valueOf(usrCodigoCadastro));

            Log.e("CNPJ_CLIENTE","CNPJ CLINETE: " + String.valueOf(usrCnpjCadastro));

            progressbar = findViewById(R.id.progressbar);

            textCnpjSelecionado = findViewById(R.id.labelTextCnpjSelecionado);
            editTextCnpjFilial = findViewById(R.id.editTextValueCnpjFilial);

            btnAcaoCancelarLimparPedido = findViewById(R.id.btnCancelarPedido);
            listagemProds = findViewById(R.id.ListaProdutos);
            btnAcaoAtualizaListaProdutos = findViewById(R.id.btnAtualizarLista);

            dropDownSegmentoEmpresa = findViewById(R.id.idSpinnerEmpresa);
            dropDownSegmentoProdutos = findViewById(R.id.idSpinnerCategoria);

            layoutCabecalhoPedidos = findViewById(R.id.viewCabecalhoHistPedidos);
            layoutMostrarDetalhesPedido = findViewById(R.id.viewListaHistPedidos);
            btnEventoMostrarCabecalho = findViewById(R.id.btnListaCabecalhoPedido);
            btnEventoListaDetalhes = findViewById(R.id.btnListaDetalhesPedido);
            btnEventoIncluirNovaFilial = findViewById(R.id.btnIncluirFilial);

            listagemDadosPessoa = findViewById(R.id.listaDadosPessoa);
            listagemHistPedidosCliente  = findViewById(R.id.ListaHistPedidos);

            btnEventoListaDetalhes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutCabecalhoPedidos.setVisibility(View.GONE);
                    layoutMostrarDetalhesPedido.setVisibility(View.VISIBLE);
                }
            });

            btnEventoMostrarCabecalho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutCabecalhoPedidos.setVisibility(View.VISIBLE);
                    layoutMostrarDetalhesPedido.setVisibility(View.GONE);
                }
            });

            btnAcaoCancelarLimparPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    new SqliteHandler(ProdutosListar.this).executaSqlScript("DELETE FROM tbl003JjtItemPedido;");
                    carregaListaProdutos();

                }
            });

            btnEventoIncluirNovaFilial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Aguarde...","Enviando os dados!");
                    MsgDialogo.show();

                    final String cnpjPrincipal = String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ);
                    final String cnpjFilial = String.valueOf(editTextCnpjFilial.getText());

                    try{

                        RetrofitClient retroCliente = new RetrofitClient();
                        HttpClient clienteHttp = new HttpClient();
                        OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
                        Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
                        WsdlJjtApi RetrofitCallFilial  = localRetrofit.create(WsdlJjtApi.class);
                        final Call<JsonObject> callNovoCnpj = RetrofitCallFilial.novaFilial(cnpjPrincipal,cnpjFilial);

                        final String urllCalled = String.valueOf(callNovoCnpj.request().url());

                        callNovoCnpj.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                MsgDialogo.dismiss();

                                if(response.isSuccessful()){

                                    final AlertDialog MsgDialogo2 = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Filial cadastrado com sucesso!","CNPJ FILIAL: " + cnpjFilial);
                                    MsgDialogo2.show();

                                }
                                carregaListaFiliais();

                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                MsgDialogo.dismiss();
                                carregaListaFiliais();
                            }
                        });


                    }catch (Exception exp){
                        MsgDialogo.dismiss();
                        carregaListaFiliais();
                    }

                }
            });

            dropDownSegmentoEmpresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listaDbProdutos =  new uteisClasse().listaProdsPedidosBancoDados(ProdutosListar.this);
                    carregaListaProdutos();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            dropDownSegmentoProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listaDbProdutos = new uteisClasse().listaProdsPedidosBancoDados(ProdutosListar.this);
                    carregaListaProdutos();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            btnAcaoAtualizaListaProdutos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listaDbProdutos = new uteisClasse().listaProdsPedidosBancoDados(ProdutosListar.this);
                    carregaListaProdutos();
                }
            });

            listagemDadosPessoa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                    String valorSelecionado =  parent.getAdapter().getItem(i).toString();
                    ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ_PEDIDO = valorSelecionado;
                    carregaListaFiliais();
                    Toast.makeText(ProdutosListar.this, "CNPJ SELECIONADO: " + valorSelecionado, Toast.LENGTH_LONG).show();
                }
            });

            carregaListaFiliais();

            carregaSegmentoEmpresa();
            carregaSegmentoProdutos();


            listaDbProdutos = new uteisClasse().listaProdsPedidosBancoDados(ProdutosListar.this);
            carregaListaProdutos();


        }catch (Exception exp){

        }

    }

    public void carregaSegmentoProdutos(){


        try{
            dropDownSegmentoProdutos.setAdapter(null);

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<JsonArray> listaProdsCall = RetrofitCallProds.listarCategorias();

            listaProdsCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    if(response.code() == 200 && response.isSuccessful()){

                        JsonArray arrayListaProdutos = new JsonArray();

                        arrayListaProdutos = response.body();

                        if(arrayListaProdutos != null && arrayListaProdutos.size() > 0){

                            String[] arrayStringList = new String[arrayListaProdutos.size()];
                            for(int i=0; i< arrayListaProdutos.size(); i++){

                                String newJsonStringObj;
                                JsonObject newJsonObj = new JsonObject();

                                newJsonObj = arrayListaProdutos.get(i).getAsJsonObject();

                                newJsonStringObj = String.valueOf(newJsonObj.get("NomeCategoriaProd")).replace("\"","").trim();
                                arrayStringList[i] = newJsonStringObj;
                            }

                            ArrayAdapter adapterProdSegmeneto = new ArrayAdapter(ProdutosListar.this,android.R.layout.simple_list_item_1, arrayStringList);
                            dropDownSegmentoProdutos.setAdapter(adapterProdSegmeneto);

                        }

                    }

                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });

        }catch (Exception exp){

        }

    }

    public void carregaSegmentoEmpresa(){


        try{
            dropDownSegmentoEmpresa.setAdapter(null);

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<JsonArray> listaEmpresasCall = RetrofitCallProds.listarSegmentos();

            listaEmpresasCall.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    if(response.code() == 200 && response.isSuccessful()){

                        JsonArray arrayListaEmpresas = new JsonArray();

                        arrayListaEmpresas = response.body();

                        if(arrayListaEmpresas != null && arrayListaEmpresas.size() > 0){

                            String[] arrayStringList = new String[arrayListaEmpresas.size()];
                            for(int i=0; i< arrayListaEmpresas.size(); i++){

                                String newJsonStringObj = "";
                                JsonObject newJsonObj = new JsonObject();

                                newJsonObj = arrayListaEmpresas.get(i).getAsJsonObject();

                                newJsonStringObj = String.valueOf(newJsonObj.get("NomeSegmentoEmpresa")).replace("\"","").trim();
                                arrayStringList[i] = newJsonStringObj;
                            }

                            ArrayAdapter adapterEmpresasSegmeneto = new ArrayAdapter(ProdutosListar.this,android.R.layout.simple_list_item_1, arrayStringList);
                            dropDownSegmentoEmpresa.setAdapter(adapterEmpresasSegmeneto);

                        }

                    }

                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                }
            });

        }catch (Exception exp){

        }

    }

    public void carregaListaFiliais(){

        try{


            textCnpjSelecionado.setText("CNPJ SELECIONADO: " + String.valueOf(((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ_PEDIDO));

            new listaFiliaisCliente().retornaListaFiliaisCliente(ProdutosListar.this, new retornaObjLista() {

                @Override
                public void retornaObjLista(List listaObj) {

                    progressbar.setVisibility(View.GONE);
                    List<Cliente> listaFiliaisItemViewHolder = new ArrayList<Cliente>();

                    if(listaObj != null && listaObj.size() > 0){

                        Integer tam = listaObj.size();
                        listaFiliaisItemViewHolder = listaObj;

                        String[] dadosFiliais = new String[tam];

                        for(int i=0;i<tam;i++){

                            Cliente cli = (Cliente) listaObj.get(i);

                            dadosFiliais[i] = String.valueOf(cli.getCpfCnpjCliente());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProdutosListar.this,android.R.layout.simple_list_item_1, android.R.id.text1, dadosFiliais);
                        listagemDadosPessoa.setAdapter(adapter);



                    } else {

                        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Erro!","Não retornou nenhuma lista de produtos !");
                        MsgDialogo.show();

                    }

                }
            });

        }catch (Exception xp){

        }

    }


    public void carregaListaProdutos(){


        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Aguarde...","Carregando lista de produtos!");
        //MsgDialogo.show();

        progressbar.setVisibility(View.VISIBLE);


        try{

            String vlrSelecionadoEmpresa = "";
            String vlrSelecionadoProduto = "";

            listagemProds.setAdapter(null);

            String[] textTipoEmpresa     = String.valueOf( dropDownSegmentoEmpresa.getSelectedItem()).split("-");
            String[] textTipoProduto     = String.valueOf( dropDownSegmentoProdutos.getSelectedItem()).split("-");

            if(!(textTipoEmpresa.equals("null")) && textTipoEmpresa != null && textTipoEmpresa.length > 0){
                vlrSelecionadoEmpresa = String.valueOf((textTipoEmpresa[0].trim()));
            }
            if(!(textTipoEmpresa.equals("null")) && textTipoProduto != null && textTipoProduto.length > 0){
                vlrSelecionadoProduto = String.valueOf((textTipoProduto[0].trim()));
            }

            if(new uteisClasse().isNumero(vlrSelecionadoEmpresa) == false){
                vlrSelecionadoEmpresa = "";
            }

            if(new uteisClasse().isNumero(vlrSelecionadoProduto) == false){
                vlrSelecionadoProduto = "";
            }


            new listaLojaProdutos().retornaListaLojaProdutos(ProdutosListar.this, vlrSelecionadoEmpresa, vlrSelecionadoProduto, new retornaObjLista() {

                @Override
                public void retornaObjLista(List listaObj) {

                    progressbar.setVisibility(View.GONE);
                    List<customHolderFieldsProduto> listaProdutoItemViewHolder = new ArrayList<customHolderFieldsProduto>();

                    if(listaObj != null && listaObj.size() > 0){

                        listaProdutoItemViewHolder = listaObj;


                        customListaProdutosViewAdapter adapterProdutosLista = new customListaProdutosViewAdapter(ProdutosListar.this, listaProdutoItemViewHolder);
                        listagemProds.setAdapter(adapterProdutosLista);


                    } else {

                        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Erro!","Não retornou nenhuma lista de produtos !");
                        MsgDialogo.show();

                    }

                }
            });


        }catch (Exception exp){

            MsgDialogo.dismiss();
            final AlertDialog MsgExp = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Erro!","Exception: " + String.valueOf(exp.getMessage()));
            MsgExp.show();

        }
    }

    public void carregaHistoricoPedidos(){

        String usrCnpjCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CPFCNPJ);
        String usrCodigoCadastro = String.valueOf(((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_CODIGOUSER);
        String usrLoginString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_USER;
        String usrTokenString = ((SessaoUsuario) cxt.getApplicationContext()).ENVIRONMENT_HASHTOKEN;


        Log.e("DadosItemPedido",String.valueOf("Iniciou a função carregaHistoricoPedidos();"));
        try{

            Log.e("DadosItemPedido",String.valueOf("Entrou no TRY CATCH carregaHistoricoPedidos();"));


            new listaHistPedidos().retornaListaHistPedidos(ProdutosListar.this, String.valueOf(usrCodigoCadastro), "", new retornaObjLista() {

                @Override
                public void retornaObjLista(List listaObj) {

                    progressbar.setVisibility(View.GONE);
                    List<customHolderFieldsHistoricoPedidos> listaHistHolder = new ArrayList<customHolderFieldsHistoricoPedidos>();

                    if(listaObj != null && listaObj.size() > 0){

                        listaHistHolder = listaObj;

                        customListaHistPedidosViewAdapter adapterHistLista = new customListaHistPedidosViewAdapter(ProdutosListar.this, listaHistHolder);
                        listagemHistPedidosCliente.setAdapter(adapterHistLista);


                    } else {

                        final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(ProdutosListar.this,"Erro!","Não retornou nenhuma lista de produtos !");
                        MsgDialogo.show();

                    }

                }
            });



        }catch (Exception xp) {

        }
    }

    public class endlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 3;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public endlessScrollListener() {
        }
        public endlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // I load the next page of gigs using a background task,
                // but you can call any function here.
                new showProgress().execute();
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    class showProgress extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressbar.setVisibility(View.GONE);
           // insertNewData();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menutopoloja, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId()) {


            case R.id.idSobre:

                MensagemDialogo dialogo = new MensagemDialogo();
                AlertDialog alerta = dialogo.constroiDialogoClientNeutro(ProdutosListar.this,"JJT IMPORTADORA","Desenvolvido por Daniel Baringer\r\ndaniel.baringer@gmail.com");
                alerta.show();

                return(true);

            case R.id.idCarrinho:


                Intent finalizaPedido = new Intent(ProdutosListar.this,FinalizarPedido.class);
                startActivity(finalizaPedido);
                return(true);

            case R.id.idHistorico:

                startActivity(new Intent(this, PedidosHistorico.class));
                return(true);

            case R.id.idSair:

                startActivity(new Intent(this, MainActivity.class));
                return(true);

        }

        return(super.onOptionsItemSelected(item));
    }


}
