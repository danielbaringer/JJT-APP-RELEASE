package com.jjt.jjtandroid.Classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MensagemDialogo {

    public MensagemDialogo() { }

    public AlertDialog constroiDialogoClientNeutro(Context contexto, String Titulo, String mensagem){

        AlertDialog alertDialog = new AlertDialog.Builder(contexto).create();
        alertDialog.setTitle(Titulo);
        alertDialog.setMessage(mensagem);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return alertDialog;

    }


    public AlertDialog constroiDialogoClient(Context contexto, String Titulo, String mensagem){

        AlertDialog alertDialog = new AlertDialog.Builder(contexto).create();
        alertDialog.setTitle(Titulo);
        alertDialog.setMessage(mensagem);

        return alertDialog;

    }


}
