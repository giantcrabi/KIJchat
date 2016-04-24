/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

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
    
     public DigitalSignature(){
         try {
            keyGen = KeyPairGenerator.getInstance("RSA"); //Create a Key Pair Generator using RSA algorithm
            random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(1024, random); //Initialize the Key Pair Generator(keysize & source of randomness)
            pair = keyGen.generateKeyPair(); //Generate the Pair of Keys(public & private key)
            privKey = pair.getPrivate();
            pubKey = pair.getPublic();
            keyFactory = KeyFactory.getInstance("RSA");

            byte[] key = pubKey.getEncoded();
            FileOutputStream keyfos = new FileOutputStream("../Public_Key_Directory/serverkey");
            keyfos.write(key);
            keyfos.close();
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
     }
     
     public boolean VerifySignature(String filepath, byte[] sigToVerify, String input){
         boolean verifies = false;
         try{
             FileInputStream keyfis = new FileInputStream(filepath);
             byte[] encKey = new byte[keyfis.available()];  
             keyfis.read(encKey);
             keyfis.close();
             
             X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
             PublicKey pubKeyClient = keyFactory.generatePublic(pubKeySpec);
             
             Signature sig = Signature.getInstance("SHA512withRSA");
             sig.initVerify(pubKeyClient);
             sig.update((input).getBytes());
             verifies = sig.verify(sigToVerify);
         } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
         }
         return verifies;
     }
}
