package com.jjt.jjtandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjt.jjtandroid.Classes.Cliente;
import com.jjt.jjtandroid.Classes.Pedido;
import com.jjt.jjtandroid.Classes.SessaoUsuario;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Retrofit.HttpClient;
import com.jjt.jjtandroid.Retrofit.RetrofitClient;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AtualizaSessaoUsrActivity extends AppCompatActivity {

    String usrLoginString;
    String usrTokenString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualiza_sessao_usr);

        usrLoginString = ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_USER.toString();
        usrTokenString = ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_HASHTOKEN.toString();

        atualizaDadosSessaoUsr();

        startActivity(new Intent(this,ProdutosListar.class));
        finish();

    }

    public void atualizaDadosSessaoUsr(){

        try{

            RetrofitClient retroCliente = new RetrofitClient();
            HttpClient clienteHttp = new HttpClient();
            OkHttpClient localHttpCliente =  clienteHttp.constroiOkHttpClient(usrTokenString,usrLoginString);
            Retrofit localRetrofit = retroCliente.constroidRetrofitClient(localHttpCliente);
            WsdlJjtApi RetrofitCallProds  = localRetrofit.create(WsdlJjtApi.class);
            final Call<Cliente> callDadosCliente = RetrofitCallProds.obtemDadosCliente(String.valueOf(usrLoginString));

            callDadosCliente.enqueue(new Callback<Cliente>() {
                @Override
                public void onResponse(Call<Cliente> call, Response<Cliente> response) {

                    if(response.code() == 200 && response.isSuccessful()){


                        Cliente cli = response.body();


                        ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CODIGOUSER = String.valueOf(cli.getCodCliente());
                        ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ = String.valueOf(cli.getCpfCnpjCliente());
                        ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CPFCNPJ_PEDIDO = String.valueOf(cli.getCpfCnpjCliente());
                        ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_NOMEUSER = String.valueOf(cli.getNomeCliente());


                    } else {

                        startActivity(new Intent(AtualizaSessaoUsrActivity.this,MainActivity.class));
                        finish();

                    }

                }

                @Override
                public void onFailure(Call<Cliente> call, Throwable t) {
                    startActivity(new Intent(AtualizaSessaoUsrActivity.this,MainActivity.class));
                    finish();
                }
            });


        }catch (Exception xp){

            startActivity(new Intent(AtualizaSessaoUsrActivity.this,MainActivity.class));
            finish();

        }

    }

}
