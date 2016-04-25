/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        private PrintWriter out;
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
        
        private KeyPair _clientkey;
	
	public Read(Scanner in, PrintWriter out, ArrayList<String> log, KeyPair _clientkey)
	{
		this.in = in;
                this.log = log;
                this._clientkey = _clientkey;
                this.out = out;
	}
    
        @Override
	public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				if(this.in.hasNext()) {
                                                                   //IF THE SERVER SENT US SOMETHING
                                        input = this.in.nextLine();
                                        
                                        // input disaring. message biasa atau ada encoding
                                        if(input.contains("[EN]")){
                                            // menerima public key terminta + decode stsring ke public key
                                            String publicKeyString = this.input.substring(4);
                                            byte[] publicBytes = Base64.decodeBase64(publicKeyString);
                                            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
                                            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                                            
//                                            _clientPublicKey = keyFactory.generatePublic(keySpec);
                                            System.out.println("Jawaban permintaan public key :\n" + keyFactory.generatePublic(keySpec).toString());
                                        }
                                        else{
                                            System.out.println(input);//PRINT IT OUT
                                        }
					
                                        // jika success login atau success logout
                                        if (input.split(" ")[0].toLowerCase().equals("success")) {
                                            if (input.split(" ")[1].toLowerCase().equals("logout")) {
                                                keepGoing = false;
                                            } else if (input.split(" ")[1].toLowerCase().equals("login")) {
                                                
                                                // pembuatan key RSA, lalu public key dikirimkan ke server
                                                _clientkey = rsa.generateKey();

                                                // mengirimkan public key ke server + encode public Key ke string
                                                String publicK = Base64.encodeBase64String(_clientkey.getPublic().getEncoded());
                                                out.println(publicK);
                                                
                                                log.clear();
                                                log.add("true");
                                            }
                                        }
                                        
                                }
                                
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}
}
