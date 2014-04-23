package gameFiles;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import database.DBInterface;


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
            	//if 30 seconds has elapsed and no response has been received from the client
    			//assume that the client has failed to connect or disconnected from the game
    			socket.setSoTimeout(30000);
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
			System.out.println("No response from client for 30 seconds. Server will shut down.");
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
	
	//rows = number of rounds (10)
	//columns = number of players (2)
	private String[][] responses = new String[5][2];
	
	//have both players responded to the noisy token?
	boolean bothAnswered = false;
	
	//have both players finished preloading the noisy tokens?
	boolean bothPreloaded = false;
	
	//number to determine if both players have finished downloading the noisy tokens
    int preloadedCount = 0;
    
    //the correct word for the current round
    String correctWord="";
    
    public Game() {
    	ScoringModule.initFiles();
    }
       
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
    
    public synchronized void notifyTokensPreloaded() {
    	preloadedCount++;
    }
    
    public synchronized void waitForBothToPreload() {
    	while (!bothPreloaded) {
    		try {
                wait();
            } catch (InterruptedException e) {}
    	}
    	bothPreloaded = false;
    }
    
    public synchronized void notifyThatBothHavePreloaded() {
    	bothPreloaded = true;
    	notifyAll();
    }
    
    public synchronized void setCorrectWord(String correctWord) {
    	this.correctWord = correctWord;
    }
    
    public int getScore(String userResponse, String correctAnswer) {
    	int score = 0;
    	
    	if (userResponse.equalsIgnoreCase(correctAnswer))
    		score = 200;
    	
    	else 
    		score = ScoringModule.computePartialScore(userResponse, correctAnswer);
    	
    	
    	return score;
    }
    
    public void getNoisyTokens() {
    	
    }
    
   /* public String getCorrectWord() {
    	
    }*/
    

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
        int totalScore = 0;
        Gson gson = new Gson();
        //ServerResponse serverResponse;
		
        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, String playerName) throws Exception{
            this.socket = socket;
            
            try {
            	this.socket.setSoTimeout(30000);
            	input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                
                //get message from client the moment he/she connects
                //this message contains his/her username
                String msgFromClient = input.readLine();
                
                //set player name
                if (msgFromClient.startsWith("USERNAME")) 
                	this.playerName = msgFromClient.substring(9);
                
                else
                	this.playerName = playerName;
            }
            catch (IOException e) {
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
                getNoisyTokens();
                //dummy code, to be changed
                ArrayList<String> noisyTokens = new ArrayList<String>();
                noisyTokens.add("6.wav");
        		noisyTokens.add("2.wav");
        		noisyTokens.add("3.wav");
        		noisyTokens.add("4.wav");
        		noisyTokens.add("5.wav");       		
                String soundFiles = gson.toJson(noisyTokens);
                
                //send clients list of noisy tokens file names
                output.println(soundFiles);
                
                //wait until both clients have finished preloading the sound files, then only start the game
                String preloadResponse = input.readLine();
                
                //if the client has finished preloading the sound files
                if (preloadResponse!=null && preloadResponse.equals("Finished preloading")) {
                	
                	//check if the other client has finished
                	notifyTokensPreloaded();
                	
                	//if this client is the last one to finish preloading
                	if (preloadedCount==2) {
                		notifyThatBothHavePreloaded();
                	}
                	
                	else {
                		waitForBothToPreload();
                	}
                }
                
                output.println("The first round will start in 3 seconds. <br />");

                //continuously get the player's responses and process them.
                while (true) {    	
                	//need to get response from player
                	String response = input.readLine();
                	
                	//store to makeshift board
                	makeMove(roundNum, response);
                	
                	//store response to database
                	int clipID = Integer.parseInt(noisyTokens.get(roundNum-1).replace(".wav", ""));
                	DBInterface.storeUserResponse(playerName, clipID, response);

                	//wait until both players have made a move                	
                	//if both players have answered, notify all threads
                	//this player is the last to answer
                	if (bothPlayersAnswered(roundNum)) {
                		//set correct answer for this round
                		//this is to prevent an extra call per round to the database           		
                    	setCorrectWord(DBInterface.getCorrectWord(clipID));
                		notifyThatBothHaveResponded();		
                	}
                	
                	//wait until the other player has made a move before continuing
                	//this player is the first to answer
                	else 
                	{	
                		waitForBothResponses();
                	}
     	
                	int score = getScore(response, correctWord);
                	totalScore += score;
                	
                	//correct answer, score, total score
                	ServerResponse msgToClient = new ServerResponse(correctWord, score , totalScore);
                	String serverResponse = gson.toJson(msgToClient);
                	
                	//if this is the last round
                	if (roundNum==responses.length)
                	{	
                		//append the word "last" to the correct answer to inform the client this is the last round                		
                		msgToClient.setCorrectAnswer("LAST " + msgToClient.getCorrectAnswer());
                		serverResponse = gson.toJson(msgToClient);
                		output.println(serverResponse);
                		
                		//add to leaderboards table             		
                		DBInterface.insertIntoLeaderboards(playerName, totalScore);
                			
                		break;
                	}

                	else
                	{	
                		output.println(serverResponse);
                		roundNum++;
                	}
                	
                	
                }
            }
            catch (SocketTimeoutException e) {
    			System.out.println("No response from client for 30 seconds. Server will shut down.");
    			System.out.println("Server has stopped running");
    		}
            catch (SQLException e) {
            	System.out.println("A database error has occurred.");
            	e.printStackTrace();
            }
            catch (Exception e) {
            	e.printStackTrace();
            } 
            finally {
            	try {
            		//close the input, output, and server sockets
            		output.close();
            		input.close();            		
            		socket.close();    		
            	} 
            	catch (IOException e) {
            		e.printStackTrace();
            	}
            }
        }
    }
}