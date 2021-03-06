package kij_chat_server;

import java.awt.Point;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Collections;



import javax.crypto.Cipher;

//import java.util.Base64;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import org.apache.commons.codec.binary.Base64; 

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */


public class Client implements Runnable{

	private Socket socket;//SOCKET INSTANCE VARIABLE
        private String username;
        private boolean login = false;  // jika sudah login, true
        
        private ArrayList<Pair<Socket,String>> _loginlist;
        private ArrayList<Pair<String,String>> _userlist;
        private ArrayList<Pair<String,String>> _grouplist;
        
        // key list user
        private ArrayList<Pair<String,PublicKey>> _keylist;
        
        private PublicKey _clientPublicKey;
        
	
	public Client(Socket s, ArrayList<Pair<Socket,String>> _loginlist, ArrayList<Pair<String,String>> _userlist, ArrayList<Pair<String,String>> _grouplist, ArrayList<Pair<String,PublicKey>> _keylist)
	{
		socket = s;//INSTANTIATE THE SOCKET)
                this._loginlist = _loginlist;
                this._userlist = _userlist;
                this._grouplist = _grouplist;
                this._keylist = _keylist;       // terima key dari Main. key ini berisi privat dan public (ingat ini masih di server)
	}
	
	@Override
	public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
	{
		try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
		{
			Scanner in = new Scanner(socket.getInputStream());//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)
                        
//                        old
//                        // melakukan pembuatan key
//                        KeyPair _clientkey = rsa.generateKey();
//                        this._keylist.add(new Pair(this.username, _clientkey));
//                        
//                        // mengirimkan private key ke client + encode Private Key ke string
//                        String publicK = Base64.encodeBase64String(_clientkey.getPrivate().getEncoded());
//                        out.println(publicK);
                        
//                        new
//                        // menerima public key client + decode string ke public key
//                        String publicKeyString = in.nextLine();
//                        byte[] publicBytes = Base64.decodeBase64(publicKeyString);
//                        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
//                        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                        _clientPublicKey = keyFactory.generatePublic(keySpec);
//                        System.out.println("New public key recieved :\n" + _clientPublicKey.toString());
                        
//                        // membuat dan memasukkan pair ke keylist
//                        this._keylist.add(new Pair(this.username, this._clientPublicKey));
                        
//                        // tes; print array list keylist
//                        System.out.println("Keylist :\n" + _keylist.toString());

			while (true)//WHILE THE PROGRAM IS RUNNING
			{		
				if (in.hasNext()) // selama in punya token
				{
					String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
//					System.out.println("Client Said: " + input);//PRINT IT OUT TO THE SCREEN
//					out.println("You Said: " + input);//RESEND IT TO THE CLIENT
//					out.flush();//FLUSH THE STREAM
                                        
                                        // param LOGIN <userName> <pass>
                                        if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                            String[] vals = input.split(" ");
                                            
//                                            vals[0] = LOGIN
//                                            vals[1] = username
//                                            vals[2] = password

                                            if (this._userlist.contains(new Pair(vals[1], vals[2])) == true) {  // jika userlist mengandung username & password (jika match)
                                                if (this.login == false) {
                                                    this._loginlist.add(new Pair(this.socket, vals[1]));
                                                    // menambah daftar public key user
                                                    this._keylist.add(new Pair(this.username, _clientPublicKey));
                                                    this.username = vals[1];
                                                    this.login = true;
                                                    System.out.println("Users count: " + this._loginlist.size());
                                                    out.println("SUCCESS login");
                                                    out.flush();
                                                    /* 
                                                        jika sesuai diskusi, maka KEY pertama akan dikirimkan disini dengan kerangka kasar sbb:
                                                        out.println("*01*" + KEY);
                                                        dugaan saya, nanti di client terdapat if. jika terdapat token (katakan saja message daiwali dengan *01* maka
                                                        message tersebut dianggap mengandung key dan akan langsung disimpan ke variable di client tanpa di print ke cmd.
                                                        else, jika message dari server tidak mengandung token penting, maka langsung ditampilkan
                                                    */
                                                    
                                                    // menerima public key client + decode string ke public key
                                                    String publicKeyString = in.nextLine();
                                                    byte[] publicBytes = Base64.decodeBase64(publicKeyString);
                                                    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
                                                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                                                    _clientPublicKey = keyFactory.generatePublic(keySpec);
                                                    System.out.println("New public key recieved :\n" + _clientPublicKey.toString());
                                                    
                                                    // membuat dan memasukkan pair ke keylist
                                                    this._keylist.add(new Pair(this.username, this._clientPublicKey));
                                                    
//                                                    _keylist.removeAll(Collections.singleton(null));
                                                    // tes; print array list keylist
                                                    System.out.println("Keylist :\n" + _keylist.toString());
                                                    
                                                } else {
                                                    out.println("FAIL login");
                                                    out.flush();
                                                }
                                            } else {
                                                out.println("FAIL login");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param LOGOUT
                                        if (input.split(" ")[0].toLowerCase().equals("logout") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            if (this._loginlist.contains(new Pair(this.socket, this.username)) == true) {
                                                this._loginlist.remove(new Pair(this.socket, this.username));
                                                System.out.println(this._loginlist.size());
                                                out.println("SUCCESS logout");
                                                out.flush();
                                                this.socket.close();
                                                break;
                                            } else {
                                                out.println("FAIL logout");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param PM <userName dst> <message>
                                        if (input.split(" ")[0].toLowerCase().equals("pm") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            boolean exist = false;
                                            
                                            for(Pair<Socket, String> cur : _loginlist) {
                                                if (cur.getSecond().equals(vals[1])) {
                                                    PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                    String messageOut = "";
                                                    for (int j = 2; j<vals.length; j++) {
                                                        messageOut += vals[j] + " ";
                                                    }
                                                    System.out.println(this.username + " to " + vals[1] + " : " + messageOut);
                                                    
                                                    /*
                                                        dugaan saya, messageOut di enkripsi dulu dengan KEY, baru dikirim dengan println seperti dibawah
                                                    */
                                                    outDest.println(this.username + ": " + messageOut);
                                                    out.println(this.username + ": " + messageOut);     // kirim ke pengirim juga, untuk konfirmasi
                                                    outDest.flush();
                                                    out.flush();
                                                    exist = true;
                                                }
                                            }
                                            
                                            // cmiiw jika message nya kosong, maka dikatakan failed
                                            if (exist == false) {
                                                System.out.println("pm to " + vals[1] + " by " + this.username + " failed.");
                                                out.println("FAIL pm");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param CG <groupName> ; create group
                                        if (input.split(" ")[0].toLowerCase().equals("cg") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            boolean exist = false;
                                            
                                            for(Pair<String, String> selGroup : _grouplist) {   // iterasi seluruh nama grup, jika grup sudah ada, tandai ada (exist)
                                                if (selGroup.getFirst().equals(vals[1])) {
                                                    exist = true;
                                                }
                                            }
                                            
                                            // jika nggak ada, buatkan grup baru
                                            if(exist == false) {
                                                Group group = new Group();
                                                int total = group.updateGroup(vals[1], this.username, _grouplist);
                                                System.out.println("total group: " + total);
                                                System.out.println("cg " + vals[1] + " by " + this.username + " successed.");
                                                out.println("SUCCESS cg");
                                                out.flush();
                                            } else {
                                                System.out.println("cg " + vals[1] + " by " + this.username + " failed.");
                                                out.println("FAIL cg");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param GM <groupName> <message> ; group message
                                        if (input.split(" ")[0].toLowerCase().equals("gm") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            boolean exist = false;
                                            
                                            for(Pair<String, String> selGroup : _grouplist) {
                                                if (selGroup.getSecond().equals(this.username)) {
                                                    exist = true;
                                                }
                                            }
                                            
                                            if (exist == true) {
                                                for(Pair<String, String> selGroup : _grouplist) {
                                                    if (selGroup.getFirst().equals(vals[1])) {
                                                        for(Pair<Socket, String> cur : _loginlist) {
                                                            if (cur.getSecond().equals(selGroup.getSecond()) && !cur.getFirst().equals(socket)) {
                                                                PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                                String messageOut = "";
                                                                for (int j = 2; j<vals.length; j++) {
                                                                    messageOut += vals[j] + " ";
                                                                }
                                                                System.out.println(this.username + " to " + vals[1] + " group: " + messageOut);
                                                                /*
                                                                    dugaan saya, messageOut di enkripsi dulu dengan KEY, baru dikirim dengan println seperti dibawah
                                                                */
                                                                outDest.println (this.username + " @ " + vals[1] + " group: " + messageOut);
                                                                out.println     (this.username + " @ " + vals[1] + " group: " + messageOut); // kirim ke pengirim juga, untuk konfirmasi
                                                                outDest.flush();
                                                                out.flush();
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                System.out.println("gm to " + vals[1] + " by " + this.username + " failed.");
                                                out.println("FAIL gm");
                                                out.flush();
                                            }
                                        }
                                        
                                        // param BM <message> ; broadcast message - karena broadcast jadi mungkin tidak perlu enkripsi (?) tp gakpapa juga buat kerahasiaan antar anggota
                                        if (input.split(" ")[0].toLowerCase().equals("bm") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            for(Pair<Socket, String> cur : _loginlist) {
                                                if (!cur.getFirst().equals(socket)) {
                                                    PrintWriter outDest = new PrintWriter(cur.getFirst().getOutputStream());
                                                    String messageOut = "";
                                                    for (int j = 1; j<vals.length; j++) {
                                                        messageOut += vals[j] + " ";
                                                    }
                                                    System.out.println(this.username + " to alls: " + messageOut);
                                                    /*
                                                        dugaan saya, messageOut di enkripsi dulu dengan KEY, baru dikirim dengan println seperti dibawah
                                                    */
                                                    outDest.println (this.username + " <BROADCAST>: " + messageOut);
                                                    out.println     (this.username + " <BROADCAST>: " + messageOut); // kirim ke pengirim juga, untuk konfirmasi
                                                    outDest.flush();
                                                    out.flush();
                                                }
                                            }
                                        }
                                        
                                        // DEBUG : user A meminta public key milik B ke server; param PK <userName dst>
                                        if (input.split(" ")[0].toLowerCase().equals("pk") == true) {
                                            String[] vals = input.split(" ");
                                            
                                            boolean exist = false;
                                            
                                            _keylist.remove(0);
                                            _keylist.remove(0);
                                            
                                            // mendapatkan socket yang sesuai
                                            for(Pair<String, PublicKey> cur : _keylist) {
                                                if (cur.getFirst().equals(vals[1])) {
                                                    // mengirimkan public key terminta ke peminta + encode public Key terminta ke string
                                                    String publicK = "[EN]";    // informasikan jika string merupakan hasil encoding dari public key
                                                    publicK += Base64.encodeBase64String(cur.getSecond().getEncoded());  
                                                    System.out.println(this.username + " want " + vals[1] + " public key :\n" + publicK);
//                                                    out.println(vals[1] + " public key :\n" + publicK);     // kirim balik
                                                    out.println(publicK);     // kirim balik
                                                    out.flush();
                                                    exist = true;
                                                }
                                            }
                                            
                                            // cmiiw jika public key nya kosong, maka dikatakan failed
                                            if (exist == false) {
                                                System.out.println("pk to " + vals[1] + " by " + this.username + " failed.");
                                                out.println("FAIL pk");
                                                out.flush();
                                            }
                                        }
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
		}	
	}

}


