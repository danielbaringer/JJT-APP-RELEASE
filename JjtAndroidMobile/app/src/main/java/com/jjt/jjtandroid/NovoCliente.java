package com.jjt.jjtandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jjt.jjtandroid.Classes.Cliente;
import com.jjt.jjtandroid.Classes.MensagemDialogo;
import com.jjt.jjtandroid.Classes.SessaoUsuario;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NovoCliente extends AppCompatActivity {

    EditText textCpfCnpjCliente;
    EditText textRazaoSocialCliente;
    EditText textEmailCliente;
    EditText textTelefoneCliente;
    EditText textCelularCliente;

    Button btnCadastroCliente;
    Button btnCadCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_cliente);

        carregaObjetos();

        btnCadCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentCancelar = new Intent(NovoCliente.this,MainActivity.class);
                startActivity(intentCancelar);
            }
        });


        btnCadastroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(NovoCliente.this,"Aguarde...","Enviando seus dados!");
                MsgDialogo.show();


                Cliente dadosCliente = new Cliente();

                WsdlJjtApi RetrofitCall = WsdlJjtApi.retrofitAutenticacao.create(WsdlJjtApi.class);

                try{

                    dadosCliente.setCpfCnpjCliente(textCpfCnpjCliente.getText().toString());
                    dadosCliente.setNomeCliente(textRazaoSocialCliente.getText().toString());
                    dadosCliente.setEmailCliente(textEmailCliente.getText().toString());
                    dadosCliente.setTelCliente(textTelefoneCliente.getText().toString());
                    dadosCliente.setMobileTelCliente(textCelularCliente.getText().toString());


                    Log.e("DADOS_NOVO_CLIENTE","CLIENTE: " + String.valueOf(textCpfCnpjCliente.getText()));
                    Log.e("DADOS_NOVO_CLIENTE","CLIENTE: " + String.valueOf(textRazaoSocialCliente.getText()));
                    Log.e("DADOS_NOVO_CLIENTE","CLIENTE: " + String.valueOf(textEmailCliente.getText()));
                    Log.e("DADOS_NOVO_CLIENTE","CLIENTE: " + String.valueOf(textTelefoneCliente.getText()));
                    Log.e("DADOS_NOVO_CLIENTE","CLIENTE: " + String.valueOf(textCelularCliente.getText()));

                    Gson gson = new Gson();
                    String json = gson.toJson(dadosCliente);

                    Call<JsonObject> minhaReqCliente = RetrofitCall.registraCadastroCliente(dadosCliente);

                    Log.e("URL","URL: " + String.valueOf(minhaReqCliente.request().url()));

                    Log.e("DADOS_URL","DADOS:" + String.valueOf(minhaReqCliente.request().method()));

                    Log.e("DADOS_URL","DADOS:" + String.valueOf(minhaReqCliente.request().body().contentLength()));



                    minhaReqCliente.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            MsgDialogo.dismiss();
                            Log.e("RESPOSTA_SERVIDOR","RESP: " + String.valueOf(response.message()));

                            if(response.code() == 200 && response.isSuccessful()){

                                JsonObject jsonObjCliente = response.body().getAsJsonObject();

                                String jsonObjString = jsonObjCliente.toString();


                                if(jsonObjCliente.get("isok").getAsBoolean() == true) {

                                    Log.e("CLIENTE_RESULT", "Cliente cadastrado com sucesso!");

                                    carregaObjetos();

                                    AlertDialog msgUsr = new MensagemDialogo().constroiDialogoClientNeutro(NovoCliente.this,"Cliente","Cliente cadastrado com sucesso !");
                                    msgUsr.show();

                                } else{
                                    Log.e("CLIENTE_RESULT", "Não foi possível cadastrar o cliente!");


                                    AlertDialog msgUsr = new MensagemDialogo().constroiDialogoClientNeutro(NovoCliente.this,"Cliente","Não foi possível cadastrar os dados solicitados !");
                                    msgUsr.show();
                                }


                            } else {

                                Log.e("LOG_CAD_CLI", response.message().toString());


                                AlertDialog msgUsr = new MensagemDialogo().constroiDialogoClientNeutro(NovoCliente.this,"Cliente","Erro ! Resposta inválida do servidor !");
                                msgUsr.show();

                            }


                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                            MsgDialogo.dismiss();
                            Log.e("LOG_CAD_CLI", t.getMessage().toString());


                            AlertDialog msgUsr = new MensagemDialogo().constroiDialogoClientNeutro(NovoCliente.this,"Cliente","Erro ! Falha na comunicação ! ");
                            msgUsr.show();

                        }
                    });


                }catch (Exception exp){
                    MsgDialogo.dismiss();
                }

            }
        });


    }

    private void carregaObjetos(){

        textCpfCnpjCliente = findViewById(R.id.cpfCnpjCliente);
        textRazaoSocialCliente = findViewById(R.id.nomeCliente);
        textEmailCliente = findViewById(R.id.emailCliente);
        textTelefoneCliente = findViewById(R.id.telefoneCliente);
        textCelularCliente = findViewById(R.id.telMovelCliente);

        btnCadastroCliente= findViewById(R.id.btnCadastrarCliente);
        btnCadCancelar = findViewById(R.id.btnVoltar);

        textCpfCnpjCliente.setText("");
        textRazaoSocialCliente.setText("");
        textEmailCliente.setText("");
        textTelefoneCliente.setText("");
        textCelularCliente.setText("");
    }
}
