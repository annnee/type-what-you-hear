package dissertation;

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

	public void connect(int portNum) throws IOException{
		try {
			System.out.println(username + " is connecting to port " + portNum);
	    	// Setup networking
	        socket = new Socket("localhost", portNum);
	        
	        //if 20 seconds has elapsed and no response has been received from the client
			//assume that the server has shut down
	        socket.setSoTimeout(20000);
	        
	        in = new BufferedReader(new InputStreamReader(
	            socket.getInputStream()));
	        out = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (SocketTimeoutException e) {
			System.out.println("No response from server for 20 seconds.");
			socket.close();
			System.out.println("Disconnected from game server.");
		}
		catch(IOException e) {
			socket.close();
			e.printStackTrace();
		}
	}
	
	public String getWelcomeMessage() {
		String serverResponse = "";
		try {
			serverResponse = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverResponse;
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
		}
		
		return serverResponse;
	}
	
	public void makeMove(String userResponse) {
		out.println(userResponse);
	}

}