package gameFiles;

import gameFiles.*;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import audioMixer.NoisyTokensGenerator;

import com.google.gson.Gson;

import java.util.*;
import java.util.concurrent.Executors;

/**
 * Servlet implementation class GameHandler
 */
public class GameHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//username -> user's game client
	private Map<String, GameClient> activePlayers = new HashMap<String, GameClient>();
	
	//list of unused port numbers
	private ArrayList<Integer> inactivePorts = new ArrayList<Integer>();
	
	//players waiting for a game
	private Map<String, GameClient> playersWaitingForGame = new HashMap<String, GameClient>();
	
	//a map of active game
	private Map<Integer, String> activeGames = new HashMap<Integer, String>();
	
	private int portNum = 0;
	
	//
	private final int NUM_OF_GAMES = 15;
	private Gson gson;
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();			
			for (int i = 1; i<=NUM_OF_GAMES; i++) {
				inactivePorts.add(9000+i);			
			}
			gson = new Gson();
			ScoringModule.initFiles(); 
			NoisyTokensGenerator.initFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(System.getProperty("user.dir"));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Obtain the session object, create a new session if doesn't exist
        HttpSession session = request.getSession(true);
        
		//identify player
		UserResponse message = gson.fromJson(request.getParameter("json"), UserResponse.class);
		String messageForServer = message.getMessage();
		String username = (String) session.getAttribute("username");
		
		//user wants to find a match
		if (message.getFormType().equals("connect")) {
			
			GameClient newPlayer = findAMatch(username);
			
			//if the servers are full
			if (newPlayer == null) {
				response.getWriter().write("Sorry, the servers are full at the moment. Please come back later!");
			}
			
			else {
				//retrieve welcome message when both players are connected	
				String welcomeMsg = newPlayer.receiveServerResponse();
				response.getWriter().write(welcomeMsg);
			}	
		}
		
		//user either wants to receive sound files 
		//or let the server know that it is done loading the noisy tokens
		else if (message.getFormType().equals("sendMessage")) {
			GameClient player = activePlayers.get(username);
			if (messageForServer.equals("Finished preloading")) 
				player.makeMove(messageForServer);
		
			String acknowledgementMsg = player.receiveServerResponse();
			response.getWriter().write(acknowledgementMsg);
		}
		
		//user wants to send response (to noisy token) to server		
		else if (message.getFormType().equals("sendResponse")) {
			messageForServer += "\t" + message.getTimeTaken();
			String serverResponse = sendResponse(username, messageForServer);
			response.getWriter().write(serverResponse);
		}	
	}
	
	public synchronized GameClient findAMatch(String username) {
		GameClient newPlayer = null;
		if (inactivePorts.size()==0) {
			return newPlayer;
		}
		
		try {				
			//add player to list of players waiting for a game		
			if (!playersWaitingForGame.containsKey(username)) {
				newPlayer = new GameClient(username);
				playersWaitingForGame.put(username, newPlayer);	
			}
			
			//don't create a new game client object if the player is already waiting for a game
			else {
				newPlayer = playersWaitingForGame.get(username);
			}
			
			int numOfWaitingPlayers = playersWaitingForGame.size();
			//if there are enough players, connect players together
			if (numOfWaitingPlayers>=2) {
				
				//get a port number from list of inactive ports and remove it
				portNum = inactivePorts.get(0);
				inactivePorts.remove(0);
				
				//start game server
				new Thread(new GameServer(portNum)).start();
				
				Thread.sleep(2000);
				
				//let one thread know that there are enough players
				notify();
				
				//connect to game server		
				newPlayer.connect(portNum);
				activeGames.put(portNum, username);			
			}			
			
			//otherwise, wait until there are enough players
			else {				
				try {
					while(true) {			
						wait();
						break;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				//connect to game server
				newPlayer.connect(portNum);
				
				activeGames.put(portNum, activeGames.get(portNum)+"/"+username);
			}
			
			//remove player from list of players looking for a game
			playersWaitingForGame.remove(username);
			
			activePlayers.put(username, newPlayer);
				
		} 
		catch (Exception e) {
			activePlayers.remove(username);
			e.printStackTrace();
		}		
		return newPlayer;
	}
	
	public String sendResponse(String username, String userResponse) {
		String serverResponse = "";
		try {
			//get player's game client
			GameClient curPlayer = activePlayers.get(username);
			
			//send response to server
			curPlayer.makeMove(userResponse);

			//receive data from server				
			serverResponse = curPlayer.receiveServerResponse();

			//if game over or there was an timeout error	
			if (serverResponse.contains("GAME OVER") ||
					serverResponse.startsWith("TIMED_OUT")) {
				
				//add port number back to list of inactive ports
				int inactivePort = activePlayers.get(username).getPortNum();
				
				synchronized(inactivePorts) {
					if (!inactivePorts.contains(inactivePort)) {
						inactivePorts.add(inactivePort);
						System.out.println("added port # " + inactivePort + " back to inactive ports");
					}
				}		
				
				//remove player from active players map				
				synchronized(activePlayers) {
					activePlayers.remove(username);
				}
					
				System.out.println("Removed " + username + " from a collection of active players.");
				
				//removes all players connected to the current game port
				if (serverResponse.contains("TIMED_OUT")) {
					int port = curPlayer.getPortNum();
					
					String[] players = activeGames.get(port).split("[/]");
					for (int i=0;i<players.length; i++) {
						activePlayers.remove(players[i]);
					}		
				}
			}			
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
			
		//send data back to client
		return serverResponse;
	}
}
