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
        ArrayList<String> log;
	
	public Read(SecretKey secKey, DigitalSignature signature, Scanner in, ArrayList<String> log)
	{
		this.in = in;
                this.log = log;
                this.signature = signature;
                this.secKey = secKey;
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
                                        
                                        String[] inputs = input.split(" ");
                                        String realInput = null;
                                        boolean verified = false;
                                        
                                        if(input.length() > 0){
                                            if(inputs.length <= 2){
                                                realInput = inputs[0];
                                            }else{
                                                StringJoiner joiner = new StringJoiner(" ");
                                                for(int i = 0; i<inputs.length - 1; i++){
                                                    joiner.add(inputs[i]);
                                                }
                                                realInput = joiner.toString();
                                            }
                                            
                                            verified = signature.VerifySignature(filepath, Main.toByteArray(inputs[inputs.length - 1]), realInput);
                                            System.out.println("Verified: " + verified);
                                        }
                                        
                                        if(verified == true){
                                            System.out.println(realInput);
                                            if (input.split(" ")[0].toLowerCase().equals("success")) {
                                                if (input.split(" ")[1].toLowerCase().equals("logout")) {
                                                    keepGoing = false;
                                                } else if (input.split(" ")[1].toLowerCase().equals("login")) {
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
