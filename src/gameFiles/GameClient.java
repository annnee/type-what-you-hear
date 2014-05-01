package gameFiles;

import java.io.*;
import java.net.*;

public class GameClient {
	private String username;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
	
    public GameClient(String username) {
    	this.username = username;
    }
    
    /**
     * 
     * @param portNum
     * @return
     * @throws IOException
     */
	public void connect(int portNum) throws Exception{
		try {
			
	    	// Setup networking
	        socket = new Socket("localhost", portNum);
	        //socket = new Socket("tomcat.dcs.shef.ac.uk:41532", portNum);
	        
	        //if 20 seconds has elapsed and no response has been received from the client
			//assume that the server has shut down
	        socket.setSoTimeout(20000);
	        
	        in = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
	        out.println("USERNAME " + username);
	        System.out.println(username + " has connected to port " + getPortNum());
	        
		}
		catch (SocketTimeoutException e) {
			System.out.println("No response from server for 30 seconds.");
			System.out.println("Disconnected from game server.");
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
			socket.close();
		}
	}
	
	public int getPortNum() {
		return socket.getPort();
	}
	
	public String receiveServerResponse () {
		String serverResponse = "";
		try {
			serverResponse = in.readLine();			
		}	
		catch (SocketTimeoutException e) {
			System.out.println("No response from server for 30 seconds.");
			return "TIMED_OUT\t"+getPortNum();
		}
		
		catch (IOException e) {
			e.printStackTrace();
			return "TIMED_OUT\t"+getPortNum();
		}
		
		return serverResponse;
	}
	
	public void makeMove(String userResponse) {
		out.println(userResponse);
	}

}