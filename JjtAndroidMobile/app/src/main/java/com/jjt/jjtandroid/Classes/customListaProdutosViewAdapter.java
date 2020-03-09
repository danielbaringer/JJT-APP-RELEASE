package com.jjt.jjtandroid.Classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjt.jjtandroid.ProdutoDetalhe;
import com.jjt.jjtandroid.ProdutosListar;
import com.jjt.jjtandroid.R;
import com.jjt.jjtandroid.Sqlite.SqliteHandler;

import java.util.List;

public class customListaProdutosViewAdapter extends BaseAdapter {

    Context context;
    List<customHolderFieldsProduto> rowItem;

    public customListaProdutosViewAdapter(Context context, List<customHolderFieldsProduto> items) {
        this.context = context;
        this.rowItem = items;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder = new viewHolder();

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.layout_view_lista_produtos, null);
            holder = new viewHolder();

            holder.holderTxtViewCodigoProd = (TextView) convertView.findViewById(R.id.codInternoProd);
            holder.holderTxtViewReferenciaProd = (TextView) convertView.findViewById(R.id.txtTituloProd);
            holder.holderTxtViewDescProd = (TextView) convertView.findViewById(R.id.txtConteudoProd);
            holder.holderTxtViewQuantidadeProd = (TextView) convertView.findViewById(R.id.textQtdeProd);
            holder.holderTxtViewVlrUnitario = (TextView) convertView.findViewById(R.id.txtProdValorUnitario);
            holder.holderTxtViewVlrTotal = (TextView) convertView.findViewById(R.id.txtProdValorTotal);
            holder.holderImgViewProduto = (ImageView) convertView.findViewById(R.id.imgCenter);
            holder.holderBtnClassAdicionar = (Button) convertView.findViewById(R.id.btnQtdeProdSomar);
            holder.holderBtnClassAdicionarCinco = (Button) convertView.findViewById(R.id.btnQtdeProdSomarCinco);
            holder.holderBtnClassSubtrair = (Button) convertView.findViewById(R.id.btnQtdeProdSubtrair);
            holder.holderBtnClassSubtrairCinco = (Button) convertView.findViewById(R.id.btnQtdeProdSubtrairCinco);

            convertView.setTag(holder);
        }
        else {
            holder = (viewHolder) convertView.getTag();
        }



        customHolderFieldsProduto rowItem = (customHolderFieldsProduto) getItem(position);


        try{

            holder.holderTxtViewCodigoProd.setText(String.valueOf(rowItem.holderViewCodigoProd));

            holder.holderTxtViewReferenciaProd.setText(String.valueOf(rowItem.holderViewReferenciaProd));
            holder.holderTxtViewDescProd.setText(String.valueOf(rowItem.holderViewDescProd));
            holder.holderTxtViewQuantidadeProd.setText(String.valueOf(rowItem.holderViewQtdeProd));

            //holder.holderTxtViewVlrUnitario.setText(String.valueOf(rowItem.holderViewVlrUnitario));
            //holder.holderTxtViewVlrTotal.setText(String.valueOf(rowItem.holderViewVlrTotal));
            holder.holderTxtViewVlrUnitario.setText(String.format("%.2f",rowItem.holderViewVlrUnitario));
            holder.holderTxtViewVlrTotal.setText(String.format("%.2f",rowItem.holderViewVlrTotal));

            holder.holderBtnClassAdicionar.setOnClickListener(holder.adicionarProduto);
            holder.holderBtnClassSubtrair.setOnClickListener(holder.subtrairProduto);
            holder.holderBtnClassAdicionarCinco.setOnClickListener(holder.adicionarProdutoCinco);
            holder.holderBtnClassSubtrairCinco.setOnClickListener(holder.subtrairProdutoCinco);


            if(rowItem.holderViewImgByte != null && rowItem.holderViewImgByte.length > 0){
                Bitmap bitmapImg = BitmapFactory.decodeByteArray(rowItem.holderViewImgByte, 0, rowItem.holderViewImgByte.length);

                if(bitmapImg != null && bitmapImg.getByteCount() > 0){
                    holder.holderImgViewProduto.setImageBitmap(bitmapImg);
                } else {
                    int imgDrawable = R.drawable.noimageicon;
                    holder.holderImgViewProduto.setImageResource(imgDrawable);
                }

            } else {

                int imgDrawable = R.drawable.noimageicon;
                holder.holderImgViewProduto.setImageResource(imgDrawable);

            }

            List<Produto> listaDbProdutos = new uteisClasse().listaProdsPedidosBancoDados(context);
            for(Produto prd : listaDbProdutos){

                if(prd.getReferencia().equals(String.valueOf(rowItem.holderViewReferenciaProd))){

                    Log.e("LOG_ENCONTROU_PROD","ENCONTROU PRODUTO ADICIONADO!");

                    Integer calcVlrTotal = Integer.parseInt(prd.getQtdeProduto());
                    Double vlrUnitario = Double.parseDouble(String.valueOf(rowItem.holderViewVlrUnitario));
                    String vlrTotal = String.format("%.2f",(vlrUnitario * calcVlrTotal));

                    holder.holderTxtViewQuantidadeProd.setText(String.valueOf(prd.getQtdeProduto()));
                    holder.holderTxtViewVlrTotal.setText(String.valueOf(vlrTotal));

                    //Log.e("VLR_TOTAL_RECUPERADO","TOTAL: " + vlrTotal);
                }

            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView txview = view.findViewById(R.id.codInternoProd);

                    //Toast.makeText(context, "Clicou !" + String.valueOf(txview.getText()), Toast.LENGTH_SHORT).show();

                    Intent detalheProd = new Intent(context, ProdutoDetalhe.class);
                    detalheProd.putExtra("codProdutoDetalhe",String.valueOf(txview.getText()));
                    context.startActivity(detalheProd);


                }
            });


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
