package kij_chat_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Main {
    // Tid-User list
    public static volatile ArrayList<Pair<Socket,String>> _loginlist = new ArrayList<>();
    
    public static final User user = new User();
    public static final ArrayList<Pair<String,String>> _userlist = user.getUserList();
    
    public static final Group group = new Group();
    public static final ArrayList<Pair<String,String>> _grouplist = group.getGroupList();
    
    // untuk menyimpan list key
    public static Key key = new Key();
    public static ArrayList<Pair<String, PublicKey>> __keylist = key.getKeyList();

    public static void main(String[] args) throws IOException {
            try 
            {
                    final int PORT = 6677;//SET NEW CONSTANT VARIABLE: PORT
                    ServerSocket server = new ServerSocket(PORT); //SET PORT NUMBER
                    System.out.println("Waiting for clients...");//AT THE START PRINT THIS

                    while (true)//WHILE THE PROGRAM IS RUNNING
                    {												
                            Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT

                            System.out.println("Client connected from " + s.getLocalAddress().getHostName());	//	TELL THEM THAT THE CLIENT CONNECTED
                            
                            Client chat = new Client(s, _loginlist, _userlist, _grouplist, __keylist);//CREATE A NEW CLIENT OBJECT
                            Thread t = new Thread(chat);//MAKE A NEW THREAD
                            t.start();//START THE THREAD
                    }
            } 
            catch (Exception e) 
            {
                    System.out.println("An error occured.");//IF AN ERROR OCCURED THEN PRINT IT
                    e.printStackTrace();
            }
    }

}

