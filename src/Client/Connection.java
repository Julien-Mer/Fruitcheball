package Client;

import java.net.*;
import Game.Analyzer;
import java.io.*;

public class Connection {
	
	final static int PORT = 1337;
	final static String HOST = "127.0.0.1";
    public String send;
    public String received = "";
    public BufferedReader in;
    public BufferedWriter out;
    public Client client;
	
    private Socket socket;
    
    Connection(InetAddress serverAddress, int serverPort, Client client) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.client = client;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    
    public void listen() throws IOException {
        while (true) {
	            this.received = in.readLine();
	            System.out.println("<< " + this.received);
	            if(this.received.equalsIgnoreCase("FIN"))
	            	break;
	          if(client.id != null) {
	        	  client.iterations++;
	        	  Analyzer.analyse(this);
	          } else {
	        	  client.id = this.received;
	        	  System.out.println("Joueur: " + client.id);
	          }
        }
    }
    
    public void send() throws IOException {
    	out.write(send);
        out.flush();
        System.out.println(">> " + send);
    }
    
}
