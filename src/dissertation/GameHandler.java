package dissertation;

import gameFiles.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	
	private int portNum = 0;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			System.out.println("add 10 port numbers to array list of port nums");
			int numberOfGames = 15;
			for (int i = 1; i<=numberOfGames; i++) {
				inactivePorts.add(9000+i);
			}
			//new Thread(new GameServer(9000)).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public GameHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//identify player
		String username = request.getParameter("player");
		
		if (request.getParameter("formType").equals("connect")) {
			
			GameClient newPlayer = findAMatch(username);
			
			//retrieve welcome message when both players are connected	
			String welcomeMsg = newPlayer.getWelcomeMessage();
			response.getWriter().write(welcomeMsg);
		}
		
		//user wants to send response to server
		else if (request.getParameter("formType").equals("sendResponse")) {
			String userResponse = request.getParameter("response");
			String serverResponse = sendResponse(username, userResponse);
			response.getWriter().write(serverResponse);
		}	
	}
	
	public synchronized GameClient findAMatch(String username) {
		//at this point, it is assumed the player isn't an active player
		GameClient newPlayer = new GameClient(username);
		try {				
			//add player to list of players waiting for a game		
			playersWaitingForGame.put(username, newPlayer);		
			int numOfWaitingPlayers = playersWaitingForGame.size();
			
			//if there are enough players, connect players together
			if (numOfWaitingPlayers==2) {
				//get a port number from list of inactive ports and remove it
				portNum = inactivePorts.get(0);
				inactivePorts.remove(0);
				
				//start game server
				new Thread(new GameServer(portNum)).start();
				
				Thread.sleep(1000);
			
				//connect to game server		
				newPlayer.connect(portNum);
				
				//let one thread know that there are enough players
				notify();
			}			
			
			//otherwise, wait until there are enough players
			else {				
				try {
					System.out.println(username + " is waiting for another player");
					while(true) {			
						wait();
						break;
					}
					System.out.println(username + " is done waiting for another player");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				//connect to game server
				newPlayer.connect(portNum);
			}
			
			//remove player from list of players looking for a game
			playersWaitingForGame.remove(username);
			activePlayers.put(username, newPlayer);
			System.out.println("Added " + username + " to a collection of active players.");	
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			System.out.println("Sending '" + userResponse + "' to server...");

			curPlayer.makeMove(userResponse);

			//receive data from server				
			serverResponse = curPlayer.receiveServerResponse();

			//if game over or there was an timeout error	
			if (serverResponse.contains("Thank you for playing") ||
					serverResponse.contains("timeout")) {
				
				//add port number back to list of inactive ports
				/*int inactivePort = activePlayers.get(username).getPortNum();
				
				if (!inactivePorts.contains(inactivePort)) {
					inactivePorts.add(inactivePort);
				}*/
				
				//remove player from active players map
				
				synchronized(activePlayers) {
					activePlayers.remove(username);	
				}
					
				System.out.println("Removed " + username + " to a collection of active players.");			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//send data back to client
		return serverResponse;
	}
}
