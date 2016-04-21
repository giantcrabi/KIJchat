package kij_chat_client;

import java.io.IOException;
import java.net.Socket;
import static javafx.application.Platform.exit;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Main {

	private final static int PORT = 6677;//SET A CONSTANT VARIABLE PORT
	private final static String HOST = "localhost";//SET A CONSTANT VARIABLE HOST
//        private final static String HOST = "10.151.xx.xx";    // jika jaringan
        
	
	public static void main(String[] args) throws IOException
	{
		try 
		{
			
			Socket s = new Socket(HOST, PORT);//CONNECT TO THE SERVER
			
			System.out.println("You connected to " + HOST);//IF CONNECTED THEN PRINT IT OUT
			
			Client client = new Client(s);//START NEW CLIENT OBJECT
			
			Thread t = new Thread(client);//INITIATE NEW THREAD
			t.start();//START THREAD
			
		} 
		catch (Exception noServer)//IF DIDNT CONNECT PRINT THAT THEY DIDNT
		{
			System.out.println("The server might not be up at this time.");
			System.out.println("Please try again later.");
		}
	}
}


