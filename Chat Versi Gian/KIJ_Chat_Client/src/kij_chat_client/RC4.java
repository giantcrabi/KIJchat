/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author DELL
 */
public class RC4 {
    private Cipher cipher;
    private SecretKey secKey;
    
    public RC4(SecretKey secKey){
        try {
            this.secKey = secKey;
            cipher = Cipher.getInstance("RC4");
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    
    public byte[] Encrypt(String input){
        byte[] encrypted = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secKey);
            encrypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return encrypted;
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
