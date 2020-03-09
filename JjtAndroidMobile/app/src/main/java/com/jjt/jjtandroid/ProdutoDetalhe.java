package com.jjt.jjtandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jjt.jjtandroid.Classes.Produto;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProdutoDetalhe extends AppCompatActivity {

    String codProdSelecionado;

    Button btnAcaoVoltar;

    ImageView imgEsquerda;
    ImageView imgProduto;
    ImageView imgDireita;

    TextView inputCodImgProdSelecionado;
    TextView inputCodProdSelecionado;
    TextView inputMostraReferencia;
    TextView inputMostraDescricao;
    TextView inputMostraNroSerie;
    TextView inputMostraCaracteristicas;

    RelativeLayout layoutConteudo;

    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhe);

        Intent detalheProd = getIntent();
        codProdSelecionado = detalheProd.getStringExtra("codProdutoDetalhe");

        carregaComponentesTela();

        carregaDetalhesProduto();

    }

    public void carregaComponentesTela(){

        try{

            btnAcaoVoltar = findViewById(R.id.btnListagemPedido);

            imgEsquerda = findViewById(R.id.imgLeft);
            imgProduto = findViewById(R.id.imgCenter);
            imgDireita = findViewById(R.id.imgRight);

            inputCodImgProdSelecionado = findViewById(R.id.codImgProdSelecionado);
            inputCodProdSelecionado = findViewById(R.id.codProdutoSelecionado);
            inputMostraReferencia = findViewById(R.id.editTextReferencia);
            inputMostraDescricao = findViewById(R.id.editTextDescricao);
            inputMostraNroSerie = findViewById(R.id.editTextNroSerie);
            inputMostraCaracteristicas = findViewById(R.id.editTextCaracteristicas);

            layoutConteudo = findViewById(R.id.layoutPrincipal);

            progressbar = findViewById(R.id.progressbar);

            btnAcaoVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent abreActivity = new Intent(ProdutoDetalhe.this,ProdutosListar.class);
                    startActivity(abreActivity);
                    finish();

                }
            });

            imgEsquerda.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{

                        //Toast.makeText(ProdutoDetalhe.this, "Clicou ! " + String.valueOf(inputCodImgProdSelecionado.getText()), Toast.LENGTH_SHORT).show();

                        carregaProxImagemProd(String.valueOf(inputCodImgProdSelecionado.getText()));


                    }catch (Exception xp){

                    }

                }
            });

            imgDireita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try{

                        //Toast.makeText(ProdutoDetalhe.this, "Clicou ! " + String.valueOf(inputCodImgProdSelecionado.getText()), Toast.LENGTH_SHORT).show();

                        carregaProxImagemProd(String.valueOf(inputCodImgProdSelecionado.getText()));


                    }catch (Exception xp){

                    }

                }
            });

        }catch (Exception xp){

        }
    }


    public void carregaProxImagemProd(String codImgProdSelecionado){


        progressbar.setVisibility(View.VISIBLE);

        try{

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient("","");
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<JsonObject> detalheProdCall = RetrofitCallProds.proxImgProdudo(codProdSelecionado,codImgProdSelecionado);

            detalheProdCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                    progressbar.setVisibility(View.GONE);

                    Produto prodDetalhe = new Produto();

                    if(response.code() == 200 && response.isSuccessful()) {

                        JsonObject prodFotoDetalhe = response.body();

                        JsonObject jsonImgProdDetalhe = prodFotoDetalhe.get("respFoto").getAsJsonObject();

                        Gson g = new Gson();
                        prodDetalhe = g.fromJson(jsonImgProdDetalhe, Produto.class);

                        inputCodImgProdSelecionado.setText(prodDetalhe.getCodImgExibida());

                        //Toast.makeText(ProdutoDetalhe.this, "Clicou ! " + String.valueOf(inputCodImgProdSelecionado.getText()), Toast.LENGTH_SHORT).show();


                        byte[] imgByte = (prodDetalhe.getImgProduto().length() > 0 ? Base64.decode(prodDetalhe.getImgProduto(), Base64.DEFAULT) : new byte[0]);
                        prodDetalhe.setByteImgProd(imgByte);

                        if(prodDetalhe.getByteImgProd() != null && prodDetalhe.getByteImgProd().length > 0){

                            Bitmap bitmapImg = BitmapFactory.decodeByteArray(prodDetalhe.getByteImgProd(), 0, prodDetalhe.getByteImgProd().length);

                            if(bitmapImg != null && bitmapImg.getByteCount() > 0){


                                Log.e("IMG","Existe imagem!!!");

                                imgProduto.setImageBitmap(bitmapImg);
                            } else {


                                Log.e("IMG","Sem imagem!!!");


                                int imgDrawable = R.drawable.noimageicon;
                                imgProduto.setImageResource(imgDrawable);
                            }



                        } else {

                            Log.e("IMG","Sem imagem!!!");

                            int imgDrawable = R.drawable.noimageicon;
                            imgProduto.setImageResource(imgDrawable);

                        }


                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                    progressbar.setVisibility(View.GONE);
                }
            });



        }catch (Exception xp){

            progressbar.setVisibility(View.GONE);
        }

    }

    public void carregaDetalhesProduto(){


        progressbar.setVisibility(View.VISIBLE);

        try{

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient("","");
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<Produto> detalheProdCall = RetrofitCallProds.exibeDetalheProd(codProdSelecionado);

            detalheProdCall.enqueue(new Callback<Produto>() {
                @Override
                public void onResponse(Call<Produto> call, Response<Produto> response) {


                    progressbar.setVisibility(View.GONE);

                    if(response.code() == 200 && response.isSuccessful()){

                        Produto prodDetalhe = response.body();

                        layoutConteudo.setVisibility(View.VISIBLE);

                        //Toast.makeText(ProdutoDetalhe.this, "Clicou ! " + String.valueOf(prodDetalhe.getCodigoproduto()), Toast.LENGTH_SHORT).show();

                        inputCodProdSelecionado.setText(String.valueOf(prodDetalhe.getCodigoproduto()));

                        inputCodImgProdSelecionado.setText(String.valueOf(prodDetalhe.getCodImgExibida()));

                        //Toast.makeText(ProdutoDetalhe.this, "Clicou ! " + String.valueOf(inputCodImgProdSelecionado.getText()), Toast.LENGTH_SHORT).show();


                        inputMostraDescricao.setText(prodDetalhe.getDescricao());

                        inputMostraReferencia.setText(prodDetalhe.getReferencia());

                        inputMostraNroSerie.setText(prodDetalhe.getCodigobarras());

                        inputMostraCaracteristicas.setText(prodDetalhe.getCaracteristicas());

                        byte[] imgByte = (prodDetalhe.getImgProduto().length() > 0 ? Base64.decode(prodDetalhe.getImgProduto(), Base64.DEFAULT) : new byte[0]);
                        prodDetalhe.setByteImgProd(imgByte);

                        if(prodDetalhe.getByteImgProd() != null && prodDetalhe.getByteImgProd().length > 0){

                            Bitmap bitmapImg = BitmapFactory.decodeByteArray(prodDetalhe.getByteImgProd(), 0, prodDetalhe.getByteImgProd().length);

                            if(bitmapImg != null && bitmapImg.getByteCount() > 0){


                                Log.e("IMG","Existe imagem!!!");

                                imgProduto.setImageBitmap(bitmapImg);
                            } else {


                                Log.e("IMG","Sem imagem!!!");


                                int imgDrawable = R.drawable.noimageicon;
                                imgProduto.setImageResource(imgDrawable);
                            }



                        } else {

                            Log.e("IMG","Sem imagem!!!");

                            int imgDrawable = R.drawable.noimageicon;
                            imgProduto.setImageResource(imgDrawable);

                        }



                    }

                }

                @Override
                public void onFailure(Call<Produto> call, Throwable t) {

                    progressbar.setVisibility(View.GONE);
                }
            });



        }catch (Exception xp){

            progressbar.setVisibility(View.GONE);
        }
    }
}
