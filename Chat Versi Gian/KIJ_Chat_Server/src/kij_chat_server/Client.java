package kij_chat_server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;
import javax.crypto.SecretKey;

/** original ->http://www.dreamincode.net/forums/topic/262304-simple-client-and-server-chat-program/
 * 
 * @author santen-suru
 */


public class Client implements Runnable{

	private Socket socket;//SOCKET INSTANCE VARIABLE
        private String username;
        private boolean login = false;  // jika sudah login, true
        private DigitalSignature signature;
        private int counter;
        private RC4 rc4;
        
        private ArrayList<Pair<Socket,String>> _loginlist;
        private ArrayList<Pair<String,String>> _userlist;
        private ArrayList<Pair<String,String>> _grouplist;
        private ArrayList<Pair<Socket,SecretKey>> _keylist;
	
	public Client(Socket s, ArrayList<Pair<Socket,String>> _loginlist, ArrayList<Pair<String,String>> _userlist, ArrayList<Pair<String,String>> _grouplist, 
                DigitalSignature signature, int counter, ArrayList<Pair<Socket,SecretKey>> _keylist)
	{
		socket = s;//INSTANTIATE THE SOCKET)
                this._loginlist = _loginlist;
                this._userlist = _userlist;
                this._grouplist = _grouplist;
                this._keylist = _keylist;
                this.signature = signature;
                this.counter = counter;
                rc4 = new RC4();
	}
	
	@Override
	public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
	{
		try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
		{
			Scanner in = new Scanner(socket.getInputStream());//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)
                        String sig;
                        String concate;
                        
                        SecretKey secKey = rc4.GenerateSymKey();
                        System.out.println(Main.toHexString(secKey.getEncoded()));
                        this._keylist.add(new Pair(this.socket, secKey));
                        
                        String encryptedKey = Main.toHexString(signature.EncryptKey(secKey));
                        
                        out.println(counter);
                        out.flush();
                        out.println(encryptedKey);
                        out.flush();
			
			while (true)//WHILE THE PROGRAM IS RUNNING
			{		
				if (in.hasNext()) // selama in punya token
				{
					String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
//					System.out.println("Client Said: " + input);//PRINT IT OUT TO THE SCREEN
//					out.println("You Said: " + input);//RESEND IT TO THE CLIENT
//					out.flush();//FLUSH THE STREAM

                                        boolean verified = false;
                                        
                                        if(input.length() > 0){
                                            String decryptedText = rc4.Decrypt(input);
                                            System.out.println(decryptedText);

                                            String[] inputs = decryptedText.split(" ");
                                            String realInput;
                                            
                                            if(inputs.length <= 2){
                                                realInput = inputs[0];
                                            }else{
                                                StringJoiner joiner = new StringJoiner(" ");
                                                for(int i = 0; i<inputs.length - 1; i++){
                                                    joiner.add(inputs[i]);
                                                }
                                                realInput = joiner.toString();
                                            }

                                            verified = signature.VerifySignature(Main.toByteArray(inputs[inputs.length - 1]), realInput, counter);
                                            System.out.println("Verified: " + verified);
                                        }
                                                                                
                                        if(verified == true){
                                            // param LOGIN <userName> <pass>
                                            if (input.split(" ")[0].toLowerCase().equals("login") == true) {
                                                String[] vals = input.split(" ");

    //                                            vals[0] = LOGIN
    //                                            vals[1] = username
    //                                            vals[2] = password

                                                if (this._userlist.contains(new Pair(vals[1], vals[2])) == true) {  // jika userlist mengandung username & password (jika match)
                                                    if (this.login == false) {
                                                        this._loginlist.add(new Pair(this.socket, vals[1]));
                                                        this.username = vals[1];
                                                        this.login = true;
                                                        System.out.println("Users count: " + this._loginlist.size());
                                                        sig = Main.toHexString(signature.GenerateSignature("SUCCESS login"));
                                                        concate = "SUCCESS login" + " " + sig;
                                                    } else {
                                                        sig = Main.toHexString(signature.GenerateSignature("FAIL login"));
                                                        concate = "FAIL login" + " " + sig;
                                                    }
                                                } else {
                                                    sig = Main.toHexString(signature.GenerateSignature("FAIL login"));
                                                    concate = "FAIL login" + " " + sig;
                                                    
                                                }
                                                out.println(concate);
                                                out.flush();
                                            }

                                            // param LOGOUT
                                            if (input.split(" ")[0].toLowerCase().equals("logout") == true) {
                                                String[] vals = input.split(" ");

                                                if (this._loginlist.contains(new Pair(this.socket, this.username)) == true) {
                                                    this._loginlist.remove(new Pair(this.socket, this.username));
                                                    System.out.println(this._loginlist.size());
                                                    sig = Main.toHexString(signature.GenerateSignature("SUCCESS logout"));
                                                    concate = "SUCCESS logout" + " " + sig;
                                                    out.println(concate);
                                                    out.flush();
                                                    this.socket.close();
                                                    break;
                                                } else {
                                                    sig = Main.toHexString(signature.GenerateSignature("FAIL logout"));
                                                    concate = "FAIL logout" + " " + sig;
                                                    out.println(concate);
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
                                                        for (int j = 2; j<vals.length - 1; j++) {
                                                            messageOut += vals[j] + " ";
                                                        }
                                                        System.out.println(this.username + " to " + vals[1] + " : " + messageOut);

                                                        /*
                                                            dugaan saya, messageOut di enkripsi dulu dengan KEY, baru dikirim dengan println seperti dibawah
                                                        */
                                                        sig = Main.toHexString(signature.GenerateSignature(this.username + ": " + messageOut));
                                                        concate = this.username + ": " + messageOut + " " + sig;
                                                        outDest.println(concate);
                                                        out.println(concate);     // kirim ke pengirim juga, untuk konfirmasi
                                                        outDest.flush();
                                                        out.flush();
                                                        exist = true;
                                                    }
                                                }

                                                // cmiiw jika message nya kosong, maka dikatakan failed
                                                if (exist == false) {
                                                    System.out.println("pm to " + vals[1] + " by " + this.username + " failed.");
                                                    sig = Main.toHexString(signature.GenerateSignature("FAIL pm"));
                                                    concate = "FAIL pm" + " " + sig;
                                                    out.println(concate);
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
                                                    sig = Main.toHexString(signature.GenerateSignature("SUCCESS cg"));
                                                    concate = "SUCCESS cg" + " " + sig;
                                                } else {
                                                    System.out.println("cg " + vals[1] + " by " + this.username + " failed.");
                                                    sig = Main.toHexString(signature.GenerateSignature("FAIL cg"));
                                                    concate = "FAIL cg" + " " + sig;
                                                }
                                                out.println(concate);
                                                out.flush();
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
                                                                    for (int j = 2; j<vals.length - 1; j++) {
                                                                        messageOut += vals[j] + " ";
                                                                    }
                                                                    System.out.println(this.username + " to " + vals[1] + " group: " + messageOut);
                                                                    sig = Main.toHexString(signature.GenerateSignature(this.username + " @ " + vals[1] + " group: " + messageOut));
                                                                    concate = this.username + " @ " + vals[1] + " group: " + messageOut + " " + sig;
                                                                    outDest.println (concate);
                                                                    out.println (concate);
                                                                    outDest.flush();
                                                                    out.flush();
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("gm to " + vals[1] + " by " + this.username + " failed.");
                                                    sig = Main.toHexString(signature.GenerateSignature("FAIL gm"));
                                                    concate = "FAIL gm" + " " + sig;
                                                    out.println(concate);
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
                                                        for (int j = 1; j<vals.length - 1; j++) {
                                                            messageOut += vals[j] + " ";
                                                        }
                                                        System.out.println(this.username + " to alls: " + messageOut);
                                                        sig = Main.toHexString(signature.GenerateSignature(this.username + " <BROADCAST>: " + messageOut));
                                                        concate = this.username + " <BROADCAST>: " + messageOut + " " + sig;
                                                        outDest.println (concate);
                                                        out.println (concate);
                                                        outDest.flush();
                                                        out.flush();
                                                    }
                                                }
                                            }
                                        }
                                        else{
                                            out.println("Your message isn't valid");
                                            out.flush();
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


