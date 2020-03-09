package com.jjt.jjtandroid.Classes;

public class Cliente {

    private int CodCliente;
    private String CpfCnpjCliente;
    private String NomeCliente;
    private String EmailCliente;
    private String TelCliente;
    private String MobileTelCliente;

    public Cliente(){}

    public Cliente(int codCliente, String cpfCnpjCliente, String nomeCliente, String emailCliente, String telCliente, String mobileTelCliente) {
        CodCliente = codCliente;
        CpfCnpjCliente = cpfCnpjCliente;
        NomeCliente = nomeCliente;
        EmailCliente = emailCliente;
        TelCliente = telCliente;
        MobileTelCliente = mobileTelCliente;
    }


    public int getCodCliente() {
        return CodCliente;
    }

    public void setCodCliente(int codCliente) {
        CodCliente = codCliente;
    }

    public String getCpfCnpjCliente() {
        return CpfCnpjCliente;
    }

    public void setCpfCnpjCliente(String cpfCnpjCliente) {
        CpfCnpjCliente = cpfCnpjCliente;
    }

    public String getNomeCliente() {
        return NomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        NomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return EmailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        EmailCliente = emailCliente;
    }

    public String getTelCliente() {
        return TelCliente;
    }

    public void setTelCliente(String telCliente) {
        TelCliente = telCliente;
    }

    public String getMobileTelCliente() {
        return MobileTelCliente;
    }

    public void setMobileTelCliente(String mobileTelCliente) {
        MobileTelCliente = mobileTelCliente;
    }


}
