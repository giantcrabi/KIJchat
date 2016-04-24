/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsa_try1;

/**
 *
 * @author rahmat
 */
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;


public class RSA_try1 {
  public static void main(String[] args) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    byte[] input = new byte[] { (byte) 0xbe, (byte) 0xef };
    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");

    KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
    RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
        "12345678", 16), new BigInteger("11", 16));
    RSAPrivateKeySpec privKeySpec = new RSAPrivateKeySpec(new BigInteger(
        "12345678", 16), new BigInteger("12345678",
        16));

    RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
    RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(privKeySpec);

    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

    byte[] cipherText = cipher.doFinal(input);
    System.out.println("cipher: " + new String(cipherText));

    cipher.init(Cipher.DECRYPT_MODE, privKey);
    byte[] plainText = cipher.doFinal(cipherText);
    System.out.println("plain : " + new String(plainText));
  }
}