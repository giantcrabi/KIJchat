/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.*;
import java.security.*;
import java.security.spec.*;

/**
 *
 * @author DELL
 */
public class DigitalSignature {
   
    private KeyPairGenerator keyGen;
    private SecureRandom random;
    private KeyPair pair;
    private PrivateKey privKey;
    private PublicKey pubKey;
    private KeyFactory keyFactory;
    
    public DigitalSignature(int counter){
        try {
            keyGen = KeyPairGenerator.getInstance("RSA"); //Create a Key Pair Generator using RSA algorithm
            random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(1024, random); //Initialize the Key Pair Generator(keysize & source of randomness)
            pair = keyGen.generateKeyPair(); //Generate the Pair of Keys(public & private key)
            privKey = pair.getPrivate();
            pubKey = pair.getPublic();
            keyFactory = KeyFactory.getInstance("RSA");

            String filepath = "../Public_Key_Directory/clientkey" + Integer.toString(counter);
            byte[] key = pubKey.getEncoded();
            FileOutputStream keyfos = new FileOutputStream(filepath);
            keyfos.write(key);
            keyfos.close();
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    
    public byte[] GenerateSignature(String input) {
        byte[] realSig = null;
        try {
            Signature rsa = Signature.getInstance("SHA512withRSA"); //gets a Signature object for generating or verifying signatures using the RSA algorithm
            rsa.initSign(privKey); //Initialize the Signature Object
            rsa.update((input).getBytes()); //Supply the Signature Object the Data to Be Signed
            realSig = rsa.sign(); //Generate the Signature
            
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return realSig;
    }
    
    public boolean VerifySignature(String filepath, byte[] sigToVerify, String input){
         boolean verifies = false;
         try{
             FileInputStream keyfis = new FileInputStream(filepath);
             byte[] encKey = new byte[keyfis.available()];  
             keyfis.read(encKey);
             keyfis.close();
             
             X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
             PublicKey pubKeyServer = keyFactory.generatePublic(pubKeySpec);
             
             Signature sig = Signature.getInstance("SHA512withRSA");
             sig.initVerify(pubKeyServer);
             sig.update((input).getBytes());
             verifies = sig.verify(sigToVerify);
         } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
         }
         return verifies;
     }
}
