package kij_chat_client;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.SecretKey;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Client implements Runnable {

	private Socket socket;  //MAKE SOCKET INSTANCE VARIABLE
        
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
                    
                    int counter = Integer.parseInt(in.nextLine());
                    byte[] encryptedKey = Main.toByteArray(in.nextLine());
                    
                    String tempfilepath = "../Public_Key_Directory/clientkey" + Integer.toString(counter);
                    String filepath = tempfilepath.replace('/','\\');

                    DigitalSignature signature = new DigitalSignature(counter);
                    SecretKey secKey = signature.DecryptKey(encryptedKey);
                    
                    //System.out.println(Main.toHexString(secKey.getEncoded()));
                    
                    Read reader = new Read(secKey, signature, in, log);

                    Thread tr = new Thread(reader);
                    tr.start();

                    Write writer = new Write(secKey, signature, chat, out, log);

                    Thread tw = new Thread(writer);
                    tw.start();

//                  System.out.println(tr.isAlive());
                    while (true) {
                        if (tr.isAlive() == false && tw.isAlive() == false) {
                            File clientfile = new File(filepath);
                            clientfile.delete();
                            socket.close();
                            break;
                        }
                    }
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
	}

}

