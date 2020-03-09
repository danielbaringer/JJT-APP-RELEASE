package com.jjt.jjtandroid.Classes;

import java.util.ArrayList;
import java.util.List;

public class customHolderFieldsHistoricoPedidos {

    public String holderViewCodigoPedido;
    public String holderViewDataPedido;
    public String holderViewCodigoItemPedido;
    public String holderViewQtdeProdsPedido;
    public String holderViewQtdeItensProdsPedido;
    public String holderViewQtdeTotalVlrPedido;
    public List<String> holderViewProdutosString;

    public customHolderFieldsHistoricoPedidos(){

        holderViewCodigoPedido = "";
        holderViewDataPedido = "";
        holderViewCodigoItemPedido = "";
        holderViewQtdeProdsPedido = "";
        holderViewQtdeItensProdsPedido = "";
        holderViewQtdeTotalVlrPedido = "0,00";
        holderViewProdutosString = new ArrayList<String>();

    }

}
