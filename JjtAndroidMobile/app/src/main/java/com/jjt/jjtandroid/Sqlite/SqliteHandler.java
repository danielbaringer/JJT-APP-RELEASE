package com.jjt.jjtandroid.Sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SqliteHandler extends SQLiteOpenHelper {



    private SQLiteDatabase JjtDistribuidoraCloud;
    private Context context = null;
    private static final String DATABASE_NAME = "JjtDistribuidoraCloud.db";
    private final static String DATABASE_PATH = "/data/data/com.jjt.jjtandroid/databases/";
    private static final int DATABASE_VERSION = 2;

    public SqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        try {
            // Log.e("PATHDATABASE",DATABASE_PATH.toString());
            createDatabase();

            openDatabase();

            SQLiteDatabase db = JjtDistribuidoraCloud; //this.getReadableDatabase();
            db.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        JjtDistribuidoraCloud = sqLiteDatabase;

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    private boolean checkDatabaseExists() {

        File db = new File(DATABASE_PATH + DATABASE_NAME);
        if(db.exists()) return true;
        File dir = new File(db.getParent());
        if (!dir.exists()) {
            dir.mkdirs();
            this.getReadableDatabase();
            this.close();
        }


        return false;

    }

    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabaseExists();

        if (dbExist) {
            Log.v("DB Exists", "db exists!");
        } else {
            Log.v("DB Not Exists", "db not exists!");
        }

        boolean dbExist1 = checkDatabaseExists();
        if (!dbExist1) {
            this.getWritableDatabase();
            try {
                this.getReadableDatabase();
                this.close();
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database " + e.getMessage().toString());
            }
        } else {
            Log.v("Again DB Exists", "Again db exists!");
        }

    }

    public void dropDataBase(){

        String PATH = DATABASE_PATH + DATABASE_NAME;

        File db = new File(DATABASE_PATH + DATABASE_NAME);
        File dir = new File(db.getParent());

        Log.e("DB_EXISTES_DROP","BANCO DE DADOS EXIST? " + String.valueOf(checkDatabaseExists()));

        if (checkDatabaseExists()) {

            context.deleteDatabase(PATH);
            context.deleteDatabase(DATABASE_NAME);
            SQLiteDatabase.deleteDatabase(dir);

            if (!checkDatabaseExists()) {

                Log.e("DROPDATABASE","BANCO DE DADOS DELETADO ! :)");
            }

        }


    }

    private void copyDataBase() throws IOException {

        // Log.e("CONTEXT",context.toString());

        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    public void openDatabase() throws SQLException {

        String PATH = DATABASE_PATH + DATABASE_NAME;
        JjtDistribuidoraCloud = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);

        JjtDistribuidoraCloud.execSQL("CREATE TABLE IF NOT EXISTS tbl001JjtUsuarioAtivo (tbl001CodId INTEGER PRIMARY KEY AUTOINCREMENT, tbl001LoginUsuario NVARCHAR(100), tbl001Token NVARCHAR(600));");

        JjtDistribuidoraCloud.execSQL("CREATE TABLE IF NOT EXISTS tbl003JjtItemPedido (tbl003CodId INTEGER PRIMARY KEY AUTOINCREMENT, tbl003LoginUsuario NVARCHAR(100), tbl003CodigoProduto NVARCHAR(100),tbl003VlrUnitario DECIMAL(13, 2) ,tbl003VlrTotal DECIMAL(13, 2) ,tbl003QtdeProduto INTEGER);");


    }

    public void closeDatabase() throws SQLException {

        String PATH = DATABASE_PATH + DATABASE_NAME;
        JjtDistribuidoraCloud = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);

        JjtDistribuidoraCloud.releaseReference();
        JjtDistribuidoraCloud.close();
    }


    public void executaSqlScript(String sqlScript) throws SQLException
    {

        openDatabase();

        String sqlCommand = sqlScript.toString();
        JjtDistribuidoraCloud.execSQL(sqlCommand);

        JjtDistribuidoraCloud.close();

    }


    public SQLiteDatabase connBancoDados(){

        try{

            openDatabase();


        } catch (Exception exp) {

        }

        return this.JjtDistribuidoraCloud;

    }


}
