package com.jjt.jjtandroid;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.jjt.jjtandroid.Classes.Cliente;
import com.jjt.jjtandroid.Classes.MensagemDialogo;
import com.jjt.jjtandroid.Classes.SessaoUsuario;
import com.jjt.jjtandroid.Interfaces.NewWsdlJjtWebApi;
import com.jjt.jjtandroid.Interfaces.WsdlJjtApi;
import com.jjt.jjtandroid.Notificacao.MensagemNotificacao;
import com.jjt.jjtandroid.Sqlite.SqliteHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    Button btnLoginAcesso;
    Button btnCadastroCliente;
    Toast msgToast;

    EditText txtLogin;
    EditText txtSenha;

    private static final int NOTIFY_ME_ID=1337;
    Context thisCxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thisCxt = this;
        carregaObjetos();

        //criarNotificacaoMsgView(null);
        startService(new Intent(this, MensagemNotificacao.class));

        btnCadastroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intentCadastroCliente = new Intent(MainActivity.this,NovoCliente.class);
                startActivity(intentCadastroCliente);

            }
        });

        btnLoginAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(MainActivity.this,"Login...","Aguarde !");
                MsgDialogo.show();

                final String retrofitLogin= txtLogin.getText().toString().replace(" ","");
                final String retrofitSenha = txtSenha.getText().toString().replace(" ","");

                WsdlJjtApi RetrofitCall = WsdlJjtApi.retrofitAutenticacao.create(WsdlJjtApi.class);

                Call<JsonObject> callLogin = RetrofitCall.login(retrofitLogin,retrofitSenha,"password");

                Log.e("LOGIN_URL","URL: " + String.valueOf(callLogin.request().url()));

                callLogin.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        MsgDialogo.dismiss();

                        boolean usrLogado = false;
                        String strTokenAcesso;

                        if(response.code() == 200 && response.isSuccessful()){

                            JsonObject jsonObjResp = response.body();

                            if(jsonObjResp != null){

                                Log.e("jsonObjLogin","Login: " + String.valueOf(jsonObjResp));

                                strTokenAcesso = String.valueOf(jsonObjResp.get("access_token")).replace("\"","").trim();

                                Log.e("jsonObjLogin","Token: " + String.valueOf(strTokenAcesso));


                                if(strTokenAcesso != null && strTokenAcesso.length() > 0){

                                    usrLogado = true;
                                    String tokenAcesso = strTokenAcesso; //jsonObjResp.get("Access_token").getAsString().replaceAll("\"", "");

                                    //((SessaoUsuario) getApplicationContext()).ENVIRONMENT_CODIGOUSER = jsonObjResp.get("Id").getAsString().replaceAll("\"", "");
                                    ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_HASHTOKEN = tokenAcesso.toString();
                                    ((SessaoUsuario) getApplicationContext()).ENVIRONMENT_USER = retrofitLogin.toString();

                                    //Toast.makeText(MainActivity.this,"TOKEN: " + String.valueOf(tokenAcesso), Toast.LENGTH_LONG).show();

                                    Intent intentLoginOk = new Intent(MainActivity.this,ProdutosListar.class);
                                    //Intent intentLoginOk = new Intent(MainActivity.this,menuInicialTabActivity.class);
                                    startActivity(intentLoginOk);

                                }

                            }


                            if(usrLogado == false){
                                AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(MainActivity.this,"Login...","Login falhou. Usuário e/ou senha estão incorretos !");
                                MsgDialogo.show();
                            }

                        } else {

                            AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(MainActivity.this,"Login...","Login falhou. Usuário e/ou senha estão incorretos !");
                            MsgDialogo.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        MsgDialogo.dismiss();

                        AlertDialog MsgDialogo = new MensagemDialogo().constroiDialogoClientNeutro(MainActivity.this,"Login...","Erro: " + t.getMessage().toString());
                        MsgDialogo.show();

                    }
                });

            }
        });

    }

    private void carregaObjetos(){

        new SqliteHandler(thisCxt).dropDataBase();
        SqliteHandler meuSqlLite = new SqliteHandler(thisCxt);

        btnLoginAcesso = findViewById(R.id.btnLogin);
        btnCadastroCliente= findViewById(R.id.btnCadNovoCliente);

        txtLogin = findViewById(R.id.email);
        txtSenha = findViewById(R.id.password);

    }


    public void criarNotificacaoMsgView(View view) {

        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, Cliente.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Nova mensagem da JJT Distribuidora")
                .setContentText("Seu cadastro foi aprovado. Apartir de agora você conseguirá fazer seus pedidos pelo app").setSmallIcon(R.drawable.ic_mr_button_connected_00_dark)
                .setContentIntent(pIntent)
                .addAction(R.drawable.ic_mr_button_connected_00_dark, "Acesse", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);


    }

}
