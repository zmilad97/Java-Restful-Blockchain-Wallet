package com.github.zmilad97.restfulblockchainwallet.Service;

import com.github.zmilad97.restfulblockchainwallet.Module.Transaction.TRX;
import com.github.zmilad97.restfulblockchainwallet.Module.Transaction.Transaction;
import com.github.zmilad97.restfulblockchainwallet.Module.Wallet;
import com.github.zmilad97.restfulblockchainwallet.Security.ECDSA;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

@Service
public class WalletService {
    private static final Logger LOG = LoggerFactory.getLogger(WalletService.class);
    @Value("${app.core-address}")
    private String coreAddress;
    private ECDSA ecdsa = new ECDSA();
    private Wallet wallet = new Wallet() ;
    private HashMap<String,Object> walletStatus;  //  ==  privateKey , publicKey , balance , Transactions snapshots

    public void generateWallet() {
        ecdsa.generateKeys();                                                       //TODO : Fix this Method and Class
        System.out.println("Private key : " + ecdsa.getPrivateKey());
        System.out.println("Public Key : " + ecdsa.getPublicKey());
        System.out.println("PublicKey in string " + Base64.getEncoder().encodeToString(ecdsa.getPublicKey().getEncoded()));
        String pubKeyHash =Base64.getEncoder().encodeToString(ecdsa.getPublicKey().getEncoded());
        System.out.println("Signature :" + ecdsa.signData(ecdsa.getPrivateKey(),pubKeyHash));
//        wallet.setPrivateKey(Base64.getEncoder().encodeToString(ecdsa.getPrivateKey().getEncoded()));
//        wallet.setPublicKey(Base64.getEncoder().encodeToString(ecdsa.getPublicKey().getEncoded()));
//        wallet.setSignature(ecdsa.signData(ecdsa.getPrivateKey(),pubKeyHash));

        wallet.setPrivateKey(ecdsa.getPrivateKey());
        wallet.setPublicKey(ecdsa.getPublicKey());
        wallet.setSignature(ecdsa.signData(ecdsa.getPrivateKey(),pubKeyHash));
        wallet.setLastTransactionHash(null);
        wallet.setBalance(0);
        wallet.setUTXOs(null);
//        saveWalletDetailsOnSystem(wallet);
        saveWallet(wallet);

    }


    public void saveWallet(Wallet wallet) {
    try {
        FileOutputStream fileOutputStream = new FileOutputStream("Wallet.dat");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(wallet);
        LOG.info("Wallet Saved On This System ! ");
    } catch (IOException e) {
        LOG.error(e.getMessage());
    }
    }



    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public void trx(TRX trx){
        Transaction transaction = new Transaction();
        transaction.setTransactionId(new Random(1024).toString()); // TODO : Fix Transaction Id to be Auto Generated
//        transaction.setTransactionInput(null,0,trx.getSource()); //TODO :FIX here
//        transaction.setTransactionOutput(trx.getAmount(),trx.getDestination());
    }

    public String newTransaction(Transaction transaction) {
        Gson gson = new Gson();
        StringEntity params;
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(this.coreAddress + "/transaction/new");
            params = new StringEntity(gson.toJson(transaction));
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(params);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return "transaction added";
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return "unknown error";
    }





    public String getCoreAddress() {
        return coreAddress;
    }

    public String connectionTest() {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(this.coreAddress + "/connectionTest");
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            return String.valueOf(httpResponse.getStatusLine().getStatusCode());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}




