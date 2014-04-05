package gameFiles;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class ClientProgram extends Listener {

	//Our client object.
	private Client client;
	//IP to connect to.
	private String ip = "localhost";
	//Ports to connect on.
	private int tcpPort = 27960, udpPort = 27960;
	
	//A boolean value.
	private String messageReceived = "";
	
	/*public static void main(String[] args) throws Exception {
		ClientProgram a = new ClientProgram();
		a.connectToGame();
	}*/
	
	//I'm only going to implement this method from Listener.class because I only need to use this one.
	public void received(Connection c, Object p){
		//Is the received packet the same class as PacketMessage.class?
		if(p instanceof PacketMessage){
			//Cast it, so we can access the message within.
			PacketMessage packet = (PacketMessage) p;
			
			//We have now received the message!
			System.out.println(packet.message);
		}
	
	}
	
	public void setPacketMessage(String msg) {
		messageReceived = msg;
	}
	
	public String getPacketMessage() {
		return messageReceived;
	}
	
	public void sendMessageToServer(PacketMessage message) {
		client.sendTCP(message);
	}
	
	public void connectToGame() throws Exception{
		System.out.println("Connecting to the server...");
		//Create the client.
		client = new Client();
		
		//Register the packet object.
		client.getKryo().register(PacketMessage.class);

		//Start the client
		client.start();
		//The client MUST be started before connecting can take place.
		
		//Connect to the server - wait 5000ms before failing.
		client.connect(5000, ip, tcpPort, udpPort);
		
		//Add a listener
		client.addListener(new ClientProgram());
		
		System.out.println("Connected! The client program is now waiting for a packet");
		
	}
}
