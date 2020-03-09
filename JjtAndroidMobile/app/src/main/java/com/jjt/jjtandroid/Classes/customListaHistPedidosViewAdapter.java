package com.jjt.jjtandroid.Classes;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jjt.jjtandroid.R;

import java.util.List;

public class customListaHistPedidosViewAdapter extends BaseAdapter {

    Context context;
    List<customHolderFieldsHistoricoPedidos> rowItem;

    public customListaHistPedidosViewAdapter(Context context, List<customHolderFieldsHistoricoPedidos> items) {
        this.context = context;
        this.rowItem = items;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolderHistPedidos holder = new viewHolderHistPedidos();

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.layout_view_historico_pedidos, null);
            //holder = new viewHolderHistPedidos();

            Button btnAcaoReplicarPedido = convertView.findViewById(R.id.btnReplicarPedido);
            TextView tituloPedido = convertView.findViewById(R.id.txtTituloVlrPedido);
            TextView dtPedido = convertView.findViewById(R.id.txtTituloVlrDtPedido);
            TextView itemPedido = convertView.findViewById(R.id.txtTituloVlrPedido);
            TextView qtdeProdsPedido = convertView.findViewById(R.id.txtTituloVlrQtdeProdsPedido);
            TextView qtdeTotalItensPedido = convertView.findViewById(R.id.txtTituloVlrTotalItensPedido);
            TextView vlrTotalPedido = convertView.findViewById(R.id.txtTituloTotalVlrPedido);
            ListView listaDetalhesProdsPedido = convertView.findViewById(R.id.listaDetalhadaProdutos);

            holder = new viewHolderHistPedidos(btnAcaoReplicarPedido,tituloPedido,dtPedido,itemPedido,qtdeProdsPedido,qtdeTotalItensPedido,vlrTotalPedido,listaDetalhesProdsPedido);

            //holder.holderTxtViewCodigoPedido = (TextView) convertView.findViewById(R.id.txtTituloVlrPedido);
            //holder.holderTxtViewDataPedido = (TextView) convertView.findViewById(R.id.txtTituloVlrDtPedido);
            //holder.holderTxtViewQtdeProdsPedido = (TextView) convertView.findViewById(R.id.txtTituloVlrQtdeProdsPedido);
            //holder.holderTxtViewQtdeItensProdsPedido = (TextView) convertView.findViewById(R.id.txtTituloVlrTotalItensPedido);
            //holder.holderListViewDetalhesPedido = (ListView) convertView.findViewById(R.id.listaDetalhadaProdutos);

            convertView.setTag(holder);
        }
        else {
            holder = (viewHolderHistPedidos) convertView.getTag();
        }



        customHolderFieldsHistoricoPedidos rowItem = (customHolderFieldsHistoricoPedidos) getItem(position);


        try{

            //Log.e("InfoHist","Info " + String.valueOf(rowItem.holderViewProdutosString.length));


            holder.holderTxtViewCodigoPedido.setText(String.valueOf(rowItem.holderViewCodigoPedido));
            holder.holderTxtViewDataPedido.setText(String.valueOf(rowItem.holderViewDataPedido));
            holder.holderTxtViewQtdeProdsPedido.setText(String.valueOf(rowItem.holderViewQtdeProdsPedido));
            holder.holderTxtViewQtdeItensProdsPedido.setText(String.valueOf(rowItem.holderViewQtdeItensProdsPedido));
            holder.holderTxtViewVlrTotalPedido.setText(String.valueOf(rowItem.holderViewQtdeTotalVlrPedido));

            String[] listaProdutos = new String[rowItem.holderViewProdutosString.size()];

            Log.e("InfoHist","TAMANHO ARRAY: " + String.valueOf(listaProdutos.length));

            for(int j=0; j<rowItem.holderViewProdutosString.size();j++){
                listaProdutos[j] = rowItem.holderViewProdutosString.get(j);
            }

            holder.setClickEventoOnReplicarPedido(context);
            holder.setDadosParaListView(context, listaProdutos);

            Log.e("InfoHist","TAMANHO LISTVIEW : " + String.valueOf(holder.holderListViewDetalhesPedido.getCount()));


        }catch (Exception xp){
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

}
