/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
/*
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
*/

/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
	private Scanner chat;
        private PrintWriter out;
        boolean keepGoing = true;
        ArrayList<String> log;
        /*
        KeyPair keyPair;
        PrivateKey privateKey;
        MessageDigest sha512;
        Cipher cipher;
        */
        
        /*
	public Write(Scanner chat, PrintWriter out, ArrayList<String> log) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
		this.chat = chat;
                this.out = out;
                this.log = log;
                keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
                sha512 = MessageDigest.getInstance("SHA-512");
                privateKey = keyPair.getPrivate();
                cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, privateKey);
	}
        */
        
        public Write(Scanner chat, PrintWriter out, ArrayList<String> log){
            this.chat = chat;
            this.out = out;
            this.log = log;
        }
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                                DigitalSignature signature = new DigitalSignature(input);
                                System.out.println("Input data: " + input);
                                System.out.println("Signature: " + Main.bytes2String(signature.generateSignatures()));
                                /*
                                System.out.println("Digest: " + bytes2String(digest));
                                System.out.println("Cipher text: " + bytes2String(cipherText));
                                */
                                
				out.println(input);//SEND IT TO THE SERVER
				out.flush();//FLUSH THE STREAM
                                
                                if (input.contains("logout")) {
                                    if (log.contains("true"))
                                        keepGoing = false;
                                    
                                }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}

}
