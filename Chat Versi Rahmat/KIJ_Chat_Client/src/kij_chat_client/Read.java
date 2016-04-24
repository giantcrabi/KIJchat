/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.security.KeyFactory;
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
        String input;
        boolean keepGoing = true;
        ArrayList<String> log;
	
	public Read(Scanner in, ArrayList<String> log)
	{
		this.in = in;
                this.log = log;
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
                                        if(input.split(" ")[0].toLowerCase().equals("[en]")){
                                            // menerima public key terminta + decode stsring ke public key
                                            String publicKeyString = this.in.nextLine().split(" ")[1];
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
