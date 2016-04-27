package kij_chat_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */

public class Main {
    // Tid-User list
    public static volatile ArrayList<Pair<Socket,Pair<String,SecretKey>>> _loginlist = new ArrayList<>();
    public static final User user = new User();
    public static final ArrayList<Pair<String,String>> _userlist = user.getUserList();
    public static final Group group = new Group();
    public static final ArrayList<Pair<String,String>> _grouplist = group.getGroupList();
    private static int counter = 0;

    public static void main(String[] args) throws IOException {
            try 
            {
                    final int PORT = 6677;//SET NEW CONSTANT VARIABLE: PORT
                    ServerSocket server = new ServerSocket(PORT); //SET PORT NUMBER
                    DigitalSignature signature = new DigitalSignature();
                    System.out.println("Waiting for clients...");//AT THE START PRINT THIS

                    while (true)//WHILE THE PROGRAM IS RUNNING
                    {												
                            Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT

                            System.out.println("Client connected from " + s.getLocalAddress().getHostName());	//	TELL THEM THAT THE CLIENT CONNECTED
                            counter += 1; //jumlah socket yang terhubung
                            
                            Client chat = new Client(s, _loginlist, _userlist, _grouplist, signature, counter);//CREATE A NEW CLIENT OBJECT
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
    
    static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

}

