package com.jjt.jjtandroid.DefinicoesStaticos;

import com.jjt.jjtandroid.BuildConfig;

public class DefinicoesEstatico {


    private static String APP_PACKAGENAME = BuildConfig.APPLICATION_ID.toString();
    //private static String HOST_SERVER_WSDL = "http://www.danielbaringer.com/jjtexternapi/";
    private static String HOST_SERVER_WSDL = "http://www.jjtimportadora.com.br/apijjt/";
    private static String GOOGLE_API_KEY = "AIzaSyAxT5bxaoN28_SVR4lZvNeuBQGHXBnQEyg";

    private static String DATABASE_NAME = "JjtDataBase.db";
    private static String DATABASE_PATH = "/data/data/" + APP_PACKAGENAME.toString() + "/databases/";
    private static String FULL_PATH_DB = DATABASE_PATH + DATABASE_NAME;


    public DefinicoesEstatico(){

    }

    public static String getHostServerWsdl() {
        return HOST_SERVER_WSDL;
    }
    public static String getAppPackageName() {
        return APP_PACKAGENAME;
    }
    public static String getGoogleApiKey() {
        return GOOGLE_API_KEY;
    }
    public static String getDatabaseName() { return DATABASE_NAME; }
    public static String getDatabasePath() { return DATABASE_PATH; }
    public static String getFullPathDb() { return FULL_PATH_DB; }

}

