package com.jjt.jjtandroid.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jjt.jjtandroid.Sqlite.SqliteHandler;

import java.util.ArrayList;
import java.util.List;

public class uteisClasse {

    public boolean isNumero(String string) {
        try {
            int amount = Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Produto> listaProdsPedidosBancoDados(Context cxt){

        SQLiteDatabase connSql;
        List<Produto> listaProdutosPedido = new ArrayList<Produto>();

        try {

            connSql = new SqliteHandler(cxt).connBancoDados();

            Cursor cursorSql = connSql.rawQuery("SELECT tbl003CodigoProduto, SUM(tbl003QtdeProduto) tbl003QtdeProduto, tbl003VlrUnitario , cast(SUM(tbl003VlrTotal) as decimal(18,2)) tbl003VlrTotal FROM tbl003JjtItemPedido GROUP BY tbl003CodigoProduto, tbl003VlrUnitario;", null);
            //Cursor cursorSql = connSql.rawQuery("SELECT tbl003CodigoProduto, tbl003QtdeProduto FROM tbl003JjtItemPedido;", null);


            if (cursorSql != null) {

                Log.e("TAM_CURSOR", "TAMANHO DO CURSOR: " + String.valueOf(cursorSql.getCount()));

                Integer posicao = 0;

                try {
                    while (cursorSql.moveToNext()) {

                        Produto novoProd = new Produto();

                        novoProd.setReferencia(cursorSql.getString(cursorSql.getColumnIndex("tbl003CodigoProduto")));
                        novoProd.setQtdeProduto(cursorSql.getString(cursorSql.getColumnIndex("tbl003QtdeProduto")));
                        novoProd.setValorUnitario(Double.parseDouble(cursorSql.getString(cursorSql.getColumnIndex("tbl003VlrUnitario"))));
                        novoProd.setValorTotal(Double.parseDouble(cursorSql.getString(cursorSql.getColumnIndex("tbl003VlrTotal"))));
                        listaProdutosPedido.add(novoProd);

                        posicao += 1;

                    }
                } finally {
                    cursorSql.close();
                }

            }

        } catch (Exception exp) {

            Log.e("LOG_EXP", "ERRO: " + exp.getMessage().toString());
        }

        return listaProdutosPedido;

    }

}
