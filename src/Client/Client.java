package Client;

import Game.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	public static Client cli;
    public String id;
    public Cell[][] map;
    public int longueur;
    public int largeur;
    public Team[] teams;
    public Fruits[] fruits;
	public Connection co;
	public int iterations;
	public ArrayList<Cell> targetedFruits;
	public int delayQuaterback = 0; // Full agressive
	
    public static void main(String[] args) throws Exception {
    	Client clii = new Client();
    	cli = clii;
    	cli.co.send = "La modifiée !\n";
     	System.out.println("Envoi du nom");
     	cli.co.send();
     	cli.co.listen();
    }
    
    public Client() throws Exception {
    	while(true) {
	    	try {
	    		co = new Connection(InetAddress.getByName(Connection.HOST), Connection.PORT, this);
	    		break;
	    	} catch (java.net.ConnectException ex) {
	    		System.out.println("Connexion refusée..");
	    	}
    	}
        System.out.println("Connecté");
    }
    
}
