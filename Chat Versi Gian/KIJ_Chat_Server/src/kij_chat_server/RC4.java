/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.*;
import javax.crypto.*;

/**
 *
 * @author DELL
 */
public class RC4 {
    
    private KeyGenerator keyGen;
    private SecureRandom random;
    private Cipher cipher;
    
    public RC4(){
        try {
            keyGen = KeyGenerator.getInstance("ARCFOUR");
            random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.init(random);
            cipher = Cipher.getInstance("RSA");
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    
    public SecretKey GenerateSymKey(){
        SecretKey secKey = keyGen.generateKey();
        return secKey;
    }
    
}
