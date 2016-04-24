/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.*;
import java.security.*;

/**
 *
 * @author DELL
 */
public class DigitalSignature {
    
    private String input;
    
    public DigitalSignature(String input){
        this.input = input;
    }
    
    public byte[] generateSignatures() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); //Create a Key Pair Generator using RSA algorithm
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyGen.initialize(1024, random); //Initialize the Key Pair Generator(keysize & source of randomness)
        KeyPair pair = keyGen.generateKeyPair(); //Generate the Pair of Keys(public & private key)
        PrivateKey privKey = pair.getPrivate();
        PublicKey pubKey = pair.getPublic();
            
        Signature rsa = Signature.getInstance("SHA512withRSA"); //gets a Signature object for generating or verifying signatures using the RSA algorithm
        rsa.initSign(privKey); //Initialize the Signature Object
        rsa.update((input).getBytes()); //Supply the Signature Object the Data to Be Signed
        byte[] realSig = rsa.sign(); //Generate the Signature
        
        return realSig;
    }

}
