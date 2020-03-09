package com.jjt.jjtandroid.Classes;

public class ItemPedido extends  Produto {

    private Integer CodItemPedido;
    private Integer CodPedido;
    private Integer QtdeProdutoItemPedido;
    private Produto DadosProduto;

    public ItemPedido(){}

    public ItemPedido(Integer codItemPedido, Integer codPedido, Integer qtdeProdutoItemPedido, Produto dadosProduto) {
        CodItemPedido = codItemPedido;
        CodPedido = codPedido;
        QtdeProdutoItemPedido = qtdeProdutoItemPedido;
        DadosProduto = dadosProduto;
    }



    public Integer getCodItemPedido() {
        return CodItemPedido;
    }

    public void setCodItemPedido(Integer codItemPedido) {
        CodItemPedido = codItemPedido;
    }

    public Integer getCodPedido() {
        return CodPedido;
    }

    public void setCodPedido(Integer codPedido) {
        CodPedido = codPedido;
    }

    public Integer getQtdeProdutoItemPedido() {
        return QtdeProdutoItemPedido;
    }

    public void setQtdeProdutoItemPedido(Integer qtdeProdutoItemPedido) {
        QtdeProdutoItemPedido = qtdeProdutoItemPedido;
    }

    public Produto getDadosProduto() {
        return DadosProduto;
    }

    public void setDadosProduto(Produto dadosProduto) {
        DadosProduto = dadosProduto;
    }


}
