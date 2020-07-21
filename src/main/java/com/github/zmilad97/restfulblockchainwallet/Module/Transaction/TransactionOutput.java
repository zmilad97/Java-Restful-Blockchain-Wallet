package com.github.zmilad97.restfulblockchainwallet.Module.Transaction;

public class TransactionOutput {
    private double amount;
    private String publicKeyScript;

    public TransactionOutput(double amount, String publicKeyScript) {
        this.amount = amount;
        this.publicKeyScript = publicKeyScript;
    }

    public TransactionOutput() {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPublicKeyScript() {
        return publicKeyScript;
    }

    public void setPublicKeyScript(String publicKeyScript) {
        this.publicKeyScript = publicKeyScript;
    }


 /*   private double amount;
    private String publicKeyScript;     //hash(Signature(privateKey,"pubKey"))   or hash(pubKey)    signature


    public TransactionOutput(double amount, String publicKeyScript) {
        this.amount = amount;
        this.publicKeyScript = publicKeyScript;
    }

    public TransactionOutput() {

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPublicKeyScript() {
        return publicKeyScript;
    }

    public void setPublicKeyScript(String publicKeyScript) {
        this.publicKeyScript = publicKeyScript;
    }*/
}
