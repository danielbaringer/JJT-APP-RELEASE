package com.jjt.jjtandroid.Classes;

public class Produto {

    private int Codigoproduto;
    private String Codigointerno;
    private String QtdeProduto;
    private String Referencia;
    private String Codigobarras;
    private String Ncm;
    private String Descricao;
    private String Caracteristicas;
    private String Caixamaster;
    private String Embalagem;
    private String Pesoliquido;
    private String Pesobruto;
    private String Cubagemcaixa;
    private String Promocao;
    private String Lancamento;
    private String Liquidacao;
    private String Segmento;
    private String Categoria;
    private Double ValorUnitario;
    private Double ValorTotal;
    private String Base64ImgProduto;
    private String CodImgExibida;
    private String ImgProduto;
    private byte[] ByteImgProd;
    private String Ativo;

    public Produto(){}


    public Produto(int codigoproduto, String codigointerno, String qtdeProduto, String referencia, String codigobarras, String ncm, String descricao, String caracteristicas, String caixamaster, String embalagem, String pesoliquido, String pesobruto, String cubagemcaixa, String promocao, String lancamento, String liquidacao, String segmento, String categoria, Double valorUnitario,Double valorTotal,String codImgExibida,String imgProduto, String base64ImgProduto, byte[] byteImgProd, String ativo) {
        Codigoproduto = codigoproduto;
        Codigointerno = codigointerno;
        QtdeProduto = qtdeProduto;
        Referencia = referencia;
        Codigobarras = codigobarras;
        Ncm = ncm;
        Descricao = descricao;
        Caracteristicas = caracteristicas;
        Caixamaster = caixamaster;
        Embalagem = embalagem;
        Pesoliquido = pesoliquido;
        Pesobruto = pesobruto;
        Cubagemcaixa = cubagemcaixa;
        Promocao = promocao;
        Lancamento = lancamento;
        Liquidacao = liquidacao;
        Segmento = segmento;
        Categoria = categoria;
        ValorUnitario = valorUnitario;
        ValorTotal = valorTotal;
        CodImgExibida = codImgExibida;
        Base64ImgProduto = base64ImgProduto;
        ImgProduto = imgProduto;
        ByteImgProd = byteImgProd;
        Ativo = ativo;
    }

    public int getCodigoproduto() {
        return Codigoproduto;
    }

    public void setCodigoproduto(int codigoproduto) {
        Codigoproduto = codigoproduto;
    }

    public String getCodigointerno() {
        return Codigointerno;
    }

    public void setCodigointerno(String codigointerno) {
        Codigointerno = codigointerno;
    }

    public String getQtdeProduto() {
        return QtdeProduto;
    }

    public void setQtdeProduto(String qtdeProduto) {
        QtdeProduto = qtdeProduto;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String referencia) {
        Referencia = referencia;
    }

    public String getCodigobarras() {
        return Codigobarras;
    }

    public void setCodigobarras(String codigobarras) {
        Codigobarras = codigobarras;
    }

    public String getNcm() {
        return Ncm;
    }

    public void setNcm(String ncm) {
        Ncm = ncm;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getCaracteristicas() {
        return Caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        Caracteristicas = caracteristicas;
    }

    public String getCaixamaster() {
        return Caixamaster;
    }

    public void setCaixamaster(String caixamaster) {
        Caixamaster = caixamaster;
    }

    public String getEmbalagem() {
        return Embalagem;
    }

    public void setEmbalagem(String embalagem) {
        Embalagem = embalagem;
    }

    public String getPesoliquido() {
        return Pesoliquido;
    }

    public void setPesoliquido(String pesoliquido) {
        Pesoliquido = pesoliquido;
    }

    public String getPesobruto() {
        return Pesobruto;
    }

    public void setPesobruto(String pesobruto) {
        Pesobruto = pesobruto;
    }

    public String getCubagemcaixa() {
        return Cubagemcaixa;
    }

    public void setCubagemcaixa(String cubagemcaixa) {
        Cubagemcaixa = cubagemcaixa;
    }

    public String getPromocao() {
        return Promocao;
    }

    public void setPromocao(String promocao) {
        Promocao = promocao;
    }

    public String getLancamento() {
        return Lancamento;
    }

    public void setLancamento(String lancamento) {
        Lancamento = lancamento;
    }

    public String getLiquidacao() {
        return Liquidacao;
    }

    public void setLiquidacao(String liquidacao) {
        Liquidacao = liquidacao;
    }

    public String getSegmento() {
        return Segmento;
    }

    public void setSegmento(String segmento) {
        Segmento = segmento;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public Double getValorUnitario() {
        return ValorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        ValorUnitario = valorUnitario;
    }

    public Double getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        ValorTotal = valorTotal;
    }

    public String getCodImgExibida() {
        return CodImgExibida;
    }

    public void setCodImgExibida(String codImgExibida) {
        CodImgExibida = codImgExibida;
    }

    public String getBase64ImgProduto() {
        return Base64ImgProduto;
    }

    public void setBase64ImgProduto(String base64ImgProduto) {
        Base64ImgProduto = base64ImgProduto;
    }


    public String getImgProduto() {
        return ImgProduto;
    }

    public void setImgProduto(String imgProduto) {
        ImgProduto = imgProduto;
    }


    public byte[] getByteImgProd() {
        return ByteImgProd;
    }

    public void setByteImgProd(byte[] byteImgProd) {
        ByteImgProd = byteImgProd;
    }



    public String getAtivo() {
        return Ativo;
    }

    public void setAtivo(String ativo) {
        Ativo = ativo;
    }

}
