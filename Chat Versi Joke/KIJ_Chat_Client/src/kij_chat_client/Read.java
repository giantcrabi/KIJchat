/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;
import javax.crypto.SecretKey;

/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
        private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
        String input;
        boolean keepGoing = true;
        private DigitalSignature signature;
        private SecretKey secKey;
        private RC4 rc4;
        ArrayList<String> log;
	
	public Read(SecretKey secKey, DigitalSignature signature, Scanner in, ArrayList<String> log)
	{
		this.in = in;
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
                        String filepath = "../Public_Key_Directory/serverkey";
			while (keepGoing)//WHILE THE PROGRAM IS RUNNING
			{						
				if(this.in.hasNext()) {
                                                                   //IF THE SERVER SENT US SOMETHING
                                        input = this.in.nextLine();
                                        
                                        boolean verified = false;
                                        String decryptedText = null;
                                        String realInput = null;
                                        
                                        if(input.length() > 0){
                                            decryptedText = rc4.Decrypt(input);

                                            String[] inputs = decryptedText.split(" ");
                                            
                                            if(inputs.length <= 2){
                                                realInput = inputs[0];
                                            }else{
                                                StringJoiner joiner = new StringJoiner(" ");
                                                for(int i = 0; i<inputs.length - 1; i++){
                                                    joiner.add(inputs[i]);
                                                }
                                                realInput = joiner.toString();
                                            }

                                            verified = signature.VerifySignature(Main.toByteArray(inputs[inputs.length - 1]), realInput);
                                            //System.out.println("Verified: " + verified);
                                        }
                                        
                                        if(verified == true){
                                            System.out.println(realInput);
                                            if (decryptedText.split(" ")[0].toLowerCase().equals("success")) {
                                                if (decryptedText.split(" ")[1].toLowerCase().equals("logout")) {
                                                    keepGoing = false;
                                                } else if (decryptedText.split(" ")[1].toLowerCase().equals("login")) {
                                                    log.clear();
                                                    log.add("true");
                                                }
                                            }
                                        }
                                        else{
                                            System.out.println("Message from server is not valid");
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
