/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.*;
import javax.crypto.*;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author DELL
 */
public class RC4 {
    
    private KeyGenerator keyGen;
    private SecureRandom random;
    private Cipher cipher;
    private SecretKey secKey;
    
    public RC4(){
        try {
            keyGen = KeyGenerator.getInstance("ARCFOUR");
            random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.init(random);
            cipher = Cipher.getInstance("RC4");
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    
    public SecretKey GenerateSymKey(){
        secKey = keyGen.generateKey();
        return secKey;
    }
    
    public String Decrypt(String input){
        byte[] decrypted = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, secKey);
            decrypted = cipher.doFinal(Base64.decodeBase64(input));
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return new String(decrypted);
    }
    
}
