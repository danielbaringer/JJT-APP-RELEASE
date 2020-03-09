package com.jjt.jjtandroid.Interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jjt.jjtandroid.Classes.Cliente;
import com.jjt.jjtandroid.Classes.ItemPedido;
import com.jjt.jjtandroid.Classes.Pedido;
import com.jjt.jjtandroid.Classes.Produto;
import com.jjt.jjtandroid.DefinicoesStaticos.DefinicoesEstatico;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WsdlJjtApi {


    @FormUrlEncoded
    @POST("ProvideHashToken")
    Call<JsonObject> login(@Field("UserName") String UserName,
                           @Field("Password") String Password,
                           @Field("grant_type") String grant_type);

    @GET("loja/produtos/detalheProd/")
    Call<Produto> exibeDetalheProd(@Query("codProduto") String codProduto);


    @GET("loja/produtos/listarProdutos/")
    Call<List<Produto>> listaProds(@Query("empresa") String empresa,@Query("categoriaProduto") String categoriaProduto);

    @GET("loja/categorias/listarCategorias/")
    Call<JsonArray> listarCategorias();

    @GET("loja/segmentos/listarSegmentos/")
    Call<JsonArray> listarSegmentos();

    @GET("loja/clientes/infoCliente/{dadosCliente}/")
    Call<Cliente> obtemDadosCliente(@Path("dadosCliente") String dadosCliente);

    @GET("loja/clientes/listaFiliaisCliente/")
    Call<List<Cliente>> listaFiliaisCliente(@Query("cpfCnpjClente") String cpfCnpjClente);


    @GET("loja/clientes/novaFilialCliente/")
    Call<JsonObject> novaFilial(@Query("dadosCliente") String dadosCliente,@Query("novaFilial") String novaFilial);


    @GET("loja/produtos/proxImgProdudo/")
    Call<JsonObject> proxImgProdudo(@Query("Produto") String Produto,@Query("FotoAtual") String FotoAtual);

    @GET("loja/pedidos/listarPedidosCliente/")
    Call<List<Pedido>> listarPedidosCliente(@Query("codCliente") String codCliente,@Query("cpfCnpjClente") String cpfCnpjClente);

    @GET("loja/pedidos/listarHistoricoPedidosCliente/")
    Call<List<Pedido>> listarHistoricoPedidosCliente(@Query("codCliente") String codCliente,@Query("cpfCnpjClente") String cpfCnpjClente);

    @GET("loja/pedidos/itensPedidoCliente")
    Call<List<ItemPedido>> listarHistItensPedidoCliente(@Query("codPedido") String codPedido);


    @Headers("Content-Type: application/json")
    @POST("loja/clientes/registraCadCliente")
    Call<JsonObject> registraCadastroCliente(@Body Cliente cadastroCliente);


    /*@Headers("Content-Type: application/json")
    @POST("api/Clientes/salvaDadosNovoCliente")
    Call<JsonObject> registraCadastroCliente(@Body String cadastroCliente);*/


    @Headers("Content-Type: application/json")
    @POST("loja/pedidos/registraPedido")
    Call<Pedido> registraCadastroPedido(@Body Pedido cadastroPedido);

    //#############################################################################################
    //Builder RETROFIT para autenticacao do usuario
    public static final Retrofit retrofitAutenticacao = new Retrofit.Builder()
            .baseUrl(DefinicoesEstatico.getHostServerWsdl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();



}
