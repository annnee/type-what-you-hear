package dissertation;

import java.io.*;
import java.net.*;


public class GameServer implements Runnable {
	 /**
     * Runs the application. Pairs up clients that connect.
     */
	private int portNum;
	public GameServer(int portNum) throws Exception{
		this.portNum = portNum;		
	}
	
	public void createGame() throws IOException {	
		ServerSocket socket = new ServerSocket(portNum);
		try {	
			System.out.println("The game server on port " + portNum + " is running.");
            while (true) {
            	//if 20 seconds has elapsed and no response has been received from the client
    			//assume that the client has disconnected
    			socket.setSoTimeout(20000);
            	Game game = new Game();
                Game.Player player1 = game.new Player(socket.accept(), "Player 1");
                Game.Player player2 = game.new Player(socket.accept(), "Player 2");
                
                Thread player1Thread = new Thread(player1);
                player1Thread.start();
                
                Thread player2Thread = new Thread(player2);
                player2Thread.start();  
                
                //wait for both threads to finish.
                player1Thread.join();
                player2Thread.join();
                
                //once finish, shut down the server
                break;
            } 
        }

		catch (SocketTimeoutException e) {
			System.out.println("No response from client for 30 seconds. Server will shut down)");
			socket.close();
			System.out.println("Server has stopped running");
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			socket.close();
			System.out.println("Server has stopped running");
		}
	}
	
	public void run() {
		try {
			createGame();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}


class Game {
	
	//rows = number of rounds (5)
	//columns = number of players (2)
	private String[][] responses = new String[5][2];
	
	//have both players responded to the noisy token?
	boolean bothAnswered = false;
    
    public void printBoard() {
    	for (int row = 0; row<responses.length; row++) {
    		for (int col = 0; col<responses[row].length; col++) {
    			System.out.print("[" +responses[row][col]+"]\t");
    		}
    		System.out.println();
    	}
    }
    
    public synchronized boolean makeMove(int roundNum, String response) {    	
    	if(responses[roundNum-1][0]==null)
    		responses[roundNum-1][0] = response;
    	
    	else 
    		responses[roundNum-1][1] = response;
    	
    	return true;
    }
    
    public boolean bothPlayersAnswered(int roundNum) {
    	if (!(responses[roundNum-1][0]==null || responses[roundNum-1][1]==null))
    		return true;
    	else
    		return false;
    }
    
    public synchronized void waitForBothResponses() {
    	while (!bothAnswered) {
    		try {
                wait();
            } catch (InterruptedException e) {}
    	}
    	bothAnswered = false;
    }
    
    public synchronized void notifyThatBothHaveResponded() {
    	bothAnswered = true;
    	notifyAll();
    }
    
    

    /**
     * The class for the helper threads in this multithreaded server
     * application.  A Player is identified by a character mark
     * which is either 'X' or 'O'.  For communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.
     */
    class Player extends Thread {
        String playerName;
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        int roundNum = 1;
        
		
        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, String playerName) {
            this.socket = socket;
            this.playerName = playerName;
            try {
            	
            	input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        /**
         * The run method of this thread.
         */
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("BOTH CON");
                
                //after everyone connects, send players sound files
                

                // Repeatedly get commands from the client and process them.
                while (true) {    	
                	//need to get response
                	String response = input.readLine();

                	//store to makeshift board
                	makeMove(roundNum, response);
                	printBoard();

                	//wait until both players have made a move
                	System.out.println(playerName + " thread: Waiting for both players to say something...");
                	
                	//if both players have answered, notify all threads
                	if (bothPlayersAnswered(roundNum)) 
                		notifyThatBothHaveResponded();
                	
                	
                	//wait until the other player has made a move before continuing 
                	else 
                		waitForBothResponses();
                	
                	
                	System.out.println(playerName + " thread: Both players moved");
                	
                	//if this is the last round
                	if (roundNum==responses.length)
                	{	
                		output.println("Round #" + roundNum + "<br />" +
                				playerName + " said " + response 
                				+ "<br />Score: 999999<br />Thank you for playing!");
                		//Thread.sleep(1000);
                		break;
                	}

                	else
                	{	
                		output.println("Round #" + roundNum + "<br />" +
                				playerName + " said " + response 
                				+ "<br />Score: 999999<br />");
                		roundNum++;
                	}
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}