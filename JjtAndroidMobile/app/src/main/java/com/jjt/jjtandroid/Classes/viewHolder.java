package com.jjt.jjtandroid.Classes;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjt.jjtandroid.Sqlite.SqliteHandler;

public class viewHolder {

    public TextView holderTxtViewCodigoProd;

    public TextView holderTxtViewQuantidadeProd;
    public TextView holderTxtViewReferenciaProd;
    public TextView holderTxtViewDescProd;

    public TextView holderTxtViewVlrUnitario;
    public TextView holderTxtViewVlrTotal;

    public ImageView holderImgViewProduto;

    public Button holderBtnClassAdicionarCinco;
    public Button holderBtnClassAdicionar;
    public Button holderBtnClassSubtrair;
    public Button holderBtnClassSubtrairCinco;


    public viewHolder(){}

    public viewHolder(TextView codProd, TextView quantidadeProd, Button btnAdicionar, Button btnSubtrair, Button btnAdicionarCinco, Button btnSubtrairCinco, TextView referenciaProd, TextView descProd,TextView vlrUnitario,TextView vlrTotal, ImageView vlrImgViewProd) {

        this.holderTxtViewCodigoProd = codProd;
        this.holderBtnClassAdicionar = btnAdicionar;
        this.holderBtnClassSubtrair = btnSubtrair;
        this.holderBtnClassAdicionarCinco = btnAdicionarCinco;
        this.holderBtnClassSubtrairCinco = btnSubtrairCinco;

        this.holderTxtViewReferenciaProd = referenciaProd;
        this.holderTxtViewDescProd = descProd;
        this.holderTxtViewQuantidadeProd = quantidadeProd;
        this.holderTxtViewVlrUnitario = vlrUnitario;
        this.holderTxtViewVlrTotal = vlrTotal;

        this.holderImgViewProduto = vlrImgViewProd;

        this.holderBtnClassAdicionar.setOnClickListener(adicionarProduto);
        this.holderBtnClassSubtrair.setOnClickListener(subtrairProduto);
        this.holderBtnClassAdicionarCinco.setOnClickListener(adicionarProdutoCinco);
        this.holderBtnClassSubtrairCinco.setOnClickListener(subtrairProdutoCinco);

    }

    public View.OnClickListener adicionarProduto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Object qtdeAtual = holderTxtViewQuantidadeProd.getText();
            Double vlrUnitario = Double.parseDouble(holderTxtViewVlrUnitario.getText().toString().replace(",","."));
            Double vlrTotal = Double.parseDouble(holderTxtViewVlrTotal.getText().toString().replace(",","."));

            Double calcVlrTotal = (Integer.parseInt(qtdeAtual.toString()) + 1) * (vlrUnitario <= 0 ? 1 : vlrUnitario);

            holderTxtViewQuantidadeProd.setText(String.valueOf(Integer.parseInt(qtdeAtual.toString()) + 1));

            holderTxtViewVlrTotal.setText(String.valueOf(String.format("%.2f",calcVlrTotal)));

            qtdeAtual = holderTxtViewQuantidadeProd.getText();

            new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");

            new SqliteHandler(null).executaSqlScript("INSERT INTO tbl003JjtItemPedido (tbl003LoginUsuario,tbl003VlrUnitario ,tbl003VlrTotal,tbl003CodigoProduto,tbl003QtdeProduto) VALUES('daniel.baringer@gmail.com','" + vlrUnitario + "','" + calcVlrTotal + "','" + holderTxtViewReferenciaProd.getText().toString() + "','" + qtdeAtual.toString() + "');");

        }
    };

    public View.OnClickListener subtrairProduto = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            Object qtdeAtual = holderTxtViewQuantidadeProd.getText();
            Double vlrUnitario = Double.parseDouble(holderTxtViewVlrUnitario.getText().toString().replace(",","."));
            Double vlrTotal = Double.parseDouble(holderTxtViewVlrTotal.getText().toString().replace(",","."));

            holderTxtViewVlrTotal.setText(String.valueOf("0.00"));

            new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");

            if(Integer.parseInt(qtdeAtual.toString()) > 0){

                //txtViewVlrTotal.setText(String.valueOf(Integer.parseInt(qtdeAtual.toString()) - 1));
                holderTxtViewQuantidadeProd.setText(String.valueOf(Integer.parseInt(qtdeAtual.toString()) - 1));

                qtdeAtual = holderTxtViewQuantidadeProd.getText();

                Double calcVlrTotal = (Integer.parseInt(qtdeAtual.toString()) * (vlrUnitario <= 0 ? 1 : vlrUnitario));

                holderTxtViewVlrTotal.setText(String.valueOf(String.format("%.2f",calcVlrTotal)));

                if(Integer.parseInt(qtdeAtual.toString()) > 0) {

                    new SqliteHandler(null).executaSqlScript("INSERT INTO tbl003JjtItemPedido (tbl003LoginUsuario,tbl003VlrUnitario ,tbl003VlrTotal,tbl003CodigoProduto,tbl003QtdeProduto) VALUES('daniel.baringer@gmail.com','" + vlrUnitario + "','" + calcVlrTotal + "','" + holderTxtViewReferenciaProd.getText().toString() + "','" + qtdeAtual.toString() + "');");


                }else{

                    holderTxtViewQuantidadeProd.setText(String.valueOf("0"));

                    holderTxtViewVlrTotal.setText(String.valueOf("0.00"));

                    new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");


                }

            } else {

                new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");

            }

        }

    };



    public View.OnClickListener adicionarProdutoCinco = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Object qtdeAtual = holderTxtViewQuantidadeProd.getText();
            Double vlrUnitario = Double.parseDouble(holderTxtViewVlrUnitario.getText().toString().replace(",","."));
            Double vlrTotal = Double.parseDouble(holderTxtViewVlrTotal.getText().toString().replace(",","."));

            Double calcVlrTotal = (Integer.parseInt(qtdeAtual.toString()) + 5) * (vlrUnitario <= 0 ? 1 : vlrUnitario);

            holderTxtViewQuantidadeProd.setText(String.valueOf(Integer.parseInt(qtdeAtual.toString()) + 5));

            holderTxtViewVlrTotal.setText(String.valueOf(String.format("%.2f",calcVlrTotal)));

            qtdeAtual = holderTxtViewQuantidadeProd.getText();

            new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");

            new SqliteHandler(null).executaSqlScript("INSERT INTO tbl003JjtItemPedido (tbl003LoginUsuario,tbl003VlrUnitario ,tbl003VlrTotal,tbl003CodigoProduto,tbl003QtdeProduto) VALUES('daniel.baringer@gmail.com','" + vlrUnitario + "','" + calcVlrTotal + "','" + holderTxtViewReferenciaProd.getText().toString() + "','" + qtdeAtual.toString() + "');");

        }
    };

    public View.OnClickListener subtrairProdutoCinco = new View.OnClickListener(){
        @Override
        public void onClick(View view) {

            holderTxtViewVlrTotal.setText(String.valueOf("0.00"));

            Object qtdeAtual = holderTxtViewQuantidadeProd.getText();
            Double vlrUnitario = Double.parseDouble(holderTxtViewVlrUnitario.getText().toString().replace(",","."));
            Double vlrTotal = Double.parseDouble(holderTxtViewVlrTotal.getText().toString().replace(",","."));

            new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");


            if(Integer.parseInt(qtdeAtual.toString()) > 0){

                //txtViewVlrTotal.setText(String.valueOf(Integer.parseInt(qtdeAtual.toString()) - 5));
                holderTxtViewQuantidadeProd.setText(String.valueOf(Integer.parseInt(qtdeAtual.toString()) - 5));

                qtdeAtual = holderTxtViewQuantidadeProd.getText();

                Double calcVlrTotal = Double.parseDouble((qtdeAtual.toString())) * (vlrUnitario <= 0 ? 1 : vlrUnitario);

                holderTxtViewVlrTotal.setText(String.valueOf(String.format("%.2f",calcVlrTotal)));

                if(Integer.parseInt(qtdeAtual.toString()) > 0) {

                    new SqliteHandler(null).executaSqlScript("INSERT INTO tbl003JjtItemPedido (tbl003LoginUsuario,tbl003VlrUnitario ,tbl003VlrTotal,tbl003CodigoProduto,tbl003QtdeProduto) VALUES('daniel.baringer@gmail.com','" + vlrUnitario + "','" + calcVlrTotal + "','" + holderTxtViewReferenciaProd.getText().toString() + "','" + qtdeAtual.toString() + "');");


                }else{

                    holderTxtViewQuantidadeProd.setText(String.valueOf("0"));

                    holderTxtViewVlrTotal.setText(String.valueOf("0.00"));

                    new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");


                }


            } else {

                new SqliteHandler(null).executaSqlScript("DELETE FROM tbl003JjtItemPedido WHERE tbl003CodigoProduto = '" + holderTxtViewReferenciaProd.getText().toString() + "';");

            }
        }

    };

}
