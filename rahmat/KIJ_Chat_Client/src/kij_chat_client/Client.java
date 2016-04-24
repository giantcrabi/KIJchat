package kij_chat_client;

import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.Scanner;


import org.apache.commons.codec.binary.Base64; 

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Client implements Runnable {

	private Socket socket;  //MAKE SOCKET INSTANCE VARIABLE
        private KeyPair _clientkey;
        
        // use arraylist -> arraylist dapat diparsing as reference
        volatile ArrayList<String> log = new ArrayList<>();
        
	public Client(Socket s)
	{
		socket = s; //INSTANTIATE THE INSTANCE VARIABLE
                log.add(String.valueOf("false"));
	}
	
	@Override
	public void run()   //INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
	{
		try
		{
                    Scanner chat = new Scanner(System.in);  //GET THE INPUT FROM THE CMD
                    Scanner in = new Scanner(socket.getInputStream());  //GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
                    PrintWriter out = new PrintWriter(socket.getOutputStream());    //GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVER)
                    
                    // pembuatan key RSA, lalu public key dikirimkan ke server
                    _clientkey = rsa.generateKey();

<<<<<<< HEAD:KIJ_Chat_Client/src/kij_chat_client/Client.java
//                    while (true)//WHILE THE PROGRAM IS RUNNING
//                    {						
//                            String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
//                            out.println(input);//SEND IT TO THE SERVER
//                            out.flush();//FLUSH THE STREAM
//
//                            if(in.hasNext())//IF THE SERVER SENT US SOMETHING
//                                    System.out.println(in.nextLine());//PRINT IT OUT
//                    }

                    DigitalSignature signature = new DigitalSignature();
=======
                    // mengirimkan public key ke server + encode public Key ke string
                    String publicK = Base64.encodeBase64String(_clientkey.getPublic().getEncoded());
                    out.println(publicK);
                    
                    
                    
                    
//                    // menerima private key dari server
//                    String privateK = in.nextLine();
//                    
//                    // decode string ke Private Key
//                    byte[] publicBytes = Base64.decodeBase64(privateK);
//                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(publicBytes);
//                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                    clientPrivateKey = keyFactory.generatePrivate(keySpec);
//                    
//                    System.out.println("a");
//                    System.out.println("Public Key server :\n" + clientPrivateKey.toString());
//                    System.out.println("a");
>>>>>>> 2d9aabb0fd974cf5790ce7aa5212f5476a188385:rahmat/KIJ_Chat_Client/src/kij_chat_client/Client.java
                    
                    Read reader = new Read(in, log);

                    Thread tr = new Thread(reader);
                    tr.start();

                    Write writer = new Write(signature, chat, out, log);

                    Thread tw = new Thread(writer);
                    tw.start();

//                        System.out.println(tr.isAlive());
                    while (tr.isAlive() == true) {
                        if (tr.isAlive() == false && tw.isAlive() == false) {
                            socket.close();
                        }
                    }
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}

}

