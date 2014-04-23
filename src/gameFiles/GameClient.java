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
	public String connect(int portNum) throws IOException{
		String connectStatus = "success";
		try {
			System.out.println(username + " is connecting to port " + portNum);
	    	// Setup networking
	        socket = new Socket("localhost", portNum);
	        
	        //if 30 seconds has elapsed and no response has been received from the client
			//assume that the server has shut down
	        socket.setSoTimeout(30000);
	        
	        in = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
	        out.println("USERNAME " + username);
		}
		catch (SocketTimeoutException e) {
			connectStatus = "timeout";
			System.out.println("No response from server for 30 seconds.");
			System.out.println("Disconnected from game server.");
			socket.close();
		}
		catch(IOException e) {
			connectStatus = "timeout";
			e.printStackTrace();
			socket.close();
		}
		return connectStatus;
	}
	
	public int getPortNum() {
		return socket.getLocalPort();
	}
	
	public String receiveServerResponse () {
		String serverResponse = "";
		try {
			serverResponse = in.readLine();			
		}	
		catch (SocketTimeoutException e) {
			System.out.println("No response from server for 30 seconds.");
			return "timeout";
		}
		
		catch (IOException e) {
			e.printStackTrace();
			return "timeout";
		}
		
		return serverResponse;
	}
	
	public void makeMove(String userResponse) {
		out.println(userResponse);
	}

}