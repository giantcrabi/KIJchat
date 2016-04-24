/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;

/**
 *
 * @author rahmat
 */

public class rsa {
    
    /**
    * String to hold name of the encryption algorithm.
    */
    public static final String ALGORITHM = "RSA";

    /**
    * Generate key which contains a pair of private and public key using 1024
    * bytes. Store the set of keys in Private.key and Public.key files.
    * 
    * @throws NoSuchAlgorithmException
    * @throws IOException
    * @throws FileNotFoundException
    */
    public static KeyPair generateKey() throws Exception {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            // debug : print key
            //      System.out.println("public key :\n" + key.getPublic());
            //      System.out.println("private key :\n" + key.getPrivate());
            
            return key;
    }


    /**
    * Encrypt the plain text using public key.
    * 
    * @param text
    *          : original plain text
    * @param key
    *          :The public key
    * @return Encrypted text
    * @throws java.lang.Exception
    */
    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
    * Decrypt text using private key.
    * 
    * @param text
    *          :encrypted text
    * @param key
    *          :The private key
    * @return plain text
    * @throws java.lang.Exception
    */
    public static String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }
}
