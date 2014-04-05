package gameFiles;
import java.util.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;


public class ServerProgram extends Listener implements Runnable {

	//Server object
	private Server server;
	//Ports to listen on
	private int tcpPort;
	private Map<Integer,Connection> players = new HashMap<Integer,Connection>();
	private int numClients = 0;
	public ServerProgram(int tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	public void initServer(int tcpPort) throws Exception {
		System.out.println("Creating the server...");
		//Create the server
		server = new Server();
		
		//Register a packet class.
		server.getKryo().register(PacketMessage.class);
		//We can only send objects as packets if they are registered.
		
		//Bind to a port
		server.bind(tcpPort, tcpPort);
		
		//Start the server
		server.start();
		
		//Add the listener
		server.addListener(new ServerProgram(tcpPort));
		System.out.println("Server is running...");
		//Shut down the server
		//server.stop();
	}
	
	//This is run when a connection is received!
	public void connected(Connection c){
		numClients += 1;
		
		System.out.println("Received a connection from "+c.getRemoteAddressTCP().getHostName() +"\n ID: " + c.getID());
		System.out.println("Number of people connected: " + numClients);
		PacketMessage packetMessage = new PacketMessage();
		players.put(numClients, c);
		//Start the game
		if (numClients == 2)
		{	
			packetMessage.message = "All players connected";
			
			for (Connection conn : players.values()) {
				conn.sendTCP(packetMessage);
			}			
		}
		
		else if (numClients < 2) {
			packetMessage.message = "WAITING";
			c.sendTCP(packetMessage);
		}
		else {
			//do nothing if there are more than 2 players on this server
		}
		
	}
	public void run() {
		try {
			initServer(tcpPort);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//This is run when we receive a packet.
	public void received(Connection c, Object p){
		//We will do nothing here.
		//We do not expect to receive any packets.
		//(But if we did, nothing would happen)
	}
	
	//This is run when a client has disconnected.
	public void disconnected(Connection c){
		System.out.println("A client disconnected!");
		numClients -= 1;
	}
	
	 
	
	
}
