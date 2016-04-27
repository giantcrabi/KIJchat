/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import org.apache.commons.codec.binary.Base64;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.SecretKey;

/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
	private Scanner chat;
        private PrintWriter out;
        boolean keepGoing = true;
        private DigitalSignature signature;
        private SecretKey secKey;
        private RC4 rc4;
        ArrayList<String> log;
        
        public Write(SecretKey secKey, DigitalSignature signature, Scanner chat, PrintWriter out, ArrayList<String> log){
            this.chat = chat;
            this.out = out;
            this.log = log;
            this.signature = signature;
            this.secKey = secKey;
            rc4 = new RC4(secKey);
        }
	
	@Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				String input = chat.nextLine().replaceAll("[\n\r]", "");	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                                String sig = Main.toHexString(signature.GenerateSignature(input));
                                
                                String concate = input + " " + sig;
                                String encrypted = Base64.encodeBase64String(rc4.Encrypt(concate));
                                
				out.println(encrypted);//SEND IT TO THE SERVER
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
