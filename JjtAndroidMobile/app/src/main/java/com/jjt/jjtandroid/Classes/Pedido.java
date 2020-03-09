package com.jjt.jjtandroid.Classes;

import java.util.List;

public class Pedido {

    private Integer CodPedido;
    private String DataPedido;
    private String CodPedidoMobile;
    private Cliente ClientePedido;
    private Integer QtdeProdPedido;
    private Integer QtdeTotalItensPedido;
    private String CnpjEntrega;
    private String DescEntrega;
    private List<ItemPedido> ListaPedidoProdutos;

    public Pedido(){}

    public Pedido(Integer codPedido, String dataPedido, String codPedidoMobile, Cliente clientePedido, Integer qtdeProdPedido,Integer qtdeTotalItensPedido, String cnpjEntrega, String descEntrega, List<ItemPedido> listaPedidoProdutos) {
        CodPedido = codPedido;
        DataPedido = dataPedido;
        CodPedidoMobile = codPedidoMobile;
        ClientePedido = clientePedido;
        QtdeProdPedido = qtdeProdPedido;
        QtdeTotalItensPedido = qtdeTotalItensPedido;
        CnpjEntrega = cnpjEntrega;
        DescEntrega = descEntrega;
        ListaPedidoProdutos = listaPedidoProdutos;
    }

    public Integer getCodPedido() {
        return CodPedido;
    }

    public void setCodPedido(Integer codPedido) {
        CodPedido = codPedido;
    }

    public String getDataPedido() {
        return DataPedido;
    }

    public void setDataPedido(String dataPedido) {
        DataPedido = dataPedido;
    }

    public String getCodPedidoMobile() {
        return CodPedidoMobile;
    }

    public void setCodPedidoMobile(String codPedidoMobile) {
        CodPedidoMobile = codPedidoMobile;
    }

    public Cliente getClientePedido() {
        return ClientePedido;
    }

    public void setClientePedido(Cliente clientePedido) {
        ClientePedido = clientePedido;
    }

    public Integer getQtdeProdPedido() {
        return QtdeProdPedido;
    }

    public void setQtdeProdPedido(Integer qtdeProdPedido) {
        QtdeProdPedido = qtdeProdPedido;
    }

    public Integer getQtdeTotalItensPedido() {
        return QtdeTotalItensPedido;
    }

    public void setQtdeTotalItensPedido(Integer qtdeTotalItensPedido) {
        QtdeTotalItensPedido = qtdeTotalItensPedido;
    }

    public String getCnpjEntrega() {
        return CnpjEntrega;
    }

    public void setCnpjEntrega(String cnpjEntrega) {
        CnpjEntrega = cnpjEntrega;
    }

    public String getDescEntrega() {
        return DescEntrega;
    }

    public void setDescEntrega(String descEntrega) {
        DescEntrega = descEntrega;
    }

    public List<ItemPedido> getListaPedidoProdutos() {
        return ListaPedidoProdutos;
    }

    public void setListaPedidoProdutos(List<ItemPedido> listaPedidoProdutos) {
        ListaPedidoProdutos = listaPedidoProdutos;
    }




}
