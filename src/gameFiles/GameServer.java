package gameFiles;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import audioMixer.NoisyTokensGenerator;

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
		//socket.bind(new InetSocketAddress("localhost", portNum));
		try {
			System.out.println("The game server on port " + portNum + " is running.");		
			
            while (true) {
            	//if 20 seconds has elapsed and no response has been received from the client
    			//assume that the client has failed to connect or disconnected from the game
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
	
	private final int NUM_OF_ROUNDS = 10;
	private final int NUM_OF_PLAYERS = 2;
	private final int NUM_EASY_TOKENS = 3;
	private final int NUM_MEDIUM_TOKENS = 3;
	private final int NUM_HARD_TOKENS = 4;
	
	private String[][] responses = new String[NUM_OF_ROUNDS][NUM_OF_PLAYERS];
	
	//have both players responded to the noisy token?
	boolean bothAnswered = false;
	
	//have both players finished preloading the noisy tokens?
	boolean bothPreloaded = false;
	
	//number to determine if both players have finished downloading the noisy tokens
    int preloadedCount = 0;
    
    boolean generated = false;
    
    //list of players
    ArrayList<String> players = new ArrayList<String>();
    
    //list of tokens
    ArrayList<String> noisyTokens = new ArrayList<String>();
       
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
    
    public synchronized String getAnswer(int roundNum, int index) {
    	return responses[roundNum-1][index];
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
    	
    	//reset to false for the next round
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
    }
    
    public synchronized void notifyThatBothHavePreloaded() {
    	bothPreloaded = true;
    	notifyAll();
    }
    
    public synchronized void notifyGenerated() {
    	generated = true;
    	notifyAll();
    }
    
    public synchronized void waitForGeneratedTokens() {
    	while (!generated) {
    		try {
    			wait();
    		}
    		catch (InterruptedException e) {}
    	}
    }
   
    public double getScore(String userResponse, String correctAnswer) {
    	if (userResponse.equalsIgnoreCase(correctAnswer))
    		return 200.0;
    	
    	else 
    		return ScoringModule.computePartialScore(userResponse, correctAnswer); 	
    }
    
    /**
     * Selects 3 easy, 3 medium, and 4 hard noisy tokens
     * @param player1 username of player1
     * @param player2 username of player2
     */
    public synchronized void setNoisyTokens(String player1, String player2) {
    	ArrayList<String> noisyTokens = new ArrayList<String>();
		double easySNR = NoisyTokensGenerator.getEasySNR();
		double mediumSNR = NoisyTokensGenerator.getMediumSNR();
		double hardSNR = NoisyTokensGenerator.getHardSNR();
		int tempTotal = NUM_EASY_TOKENS;
		
    	try {
			noisyTokens.addAll(DBInterface.getNoisyTokens(NUM_EASY_TOKENS, 
					easySNR, player1, player2));
			System.out.println("num easy tokens: " + noisyTokens.size());
			if (noisyTokens.size()!= tempTotal) {
				ArrayList<String> tokens = NoisyTokensGenerator.generateNoisyTokens(easySNR, 
						tempTotal-noisyTokens.size());
				noisyTokens.addAll(tokens);
			}
			
			tempTotal += NUM_MEDIUM_TOKENS;
			System.out.println("num easy tokens: " + noisyTokens.size());
			noisyTokens.addAll(DBInterface.getNoisyTokens(NUM_MEDIUM_TOKENS,
					mediumSNR, player1, player2));
			
			if (noisyTokens.size() != tempTotal) {
				ArrayList<String> tokens = NoisyTokensGenerator.generateNoisyTokens(mediumSNR, 
						tempTotal-noisyTokens.size());
				noisyTokens.addAll(tokens);
			}
			
			tempTotal += NUM_HARD_TOKENS;
			
			noisyTokens.addAll(DBInterface.getNoisyTokens(NUM_HARD_TOKENS,
					hardSNR, player1, player2));			
			if (noisyTokens.size() != tempTotal){
				ArrayList<String> tokens = NoisyTokensGenerator.generateNoisyTokens(hardSNR, 
						tempTotal-noisyTokens.size());
				noisyTokens.addAll(tokens);
			}
			
			Collections.shuffle(noisyTokens);
			
			System.out.println("Files for this game: ");
			for (String token: noisyTokens) {
				System.out.println(token);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}

        this.noisyTokens = noisyTokens;
    }
    
    public ArrayList<String> getNoisyTokens() {
    	return noisyTokens;
    }
    
    public synchronized String getOpponent(String playerName) {   	
    	return players.get((players.indexOf(playerName)+1)%2);
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
        Gson gson = new Gson();
    	double totalScore = 0;
        double score = 0; 
        boolean successfulGame = false;
		
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
                if (msgFromClient.startsWith("USERNAME")) {
                	this.playerName = msgFromClient.substring(9).replaceAll("\r\n ", "");
                	synchronized (this) {
                		players.add(this.playerName);
                	}
                }
                else {
                	this.playerName = playerName;
                }
            }
            catch (IOException e) {
               e.printStackTrace();
            }            
        }

        /**
         * The run method of this thread.
         */
        public void run() {         
            try {
            	
            	// The thread is only started after everyone connects.
                output.println("BOTH CON");
                
                //only one player should retrieve noisy tokens
                if (playerName.equals(players.get(0))) {              	
                	setNoisyTokens(players.get(0), players.get(1));
                	Thread.sleep(1500);
                	notifyGenerated();
                }
                
                else {
                	waitForGeneratedTokens();           	
                }     
                
                
                
                //send players sound files                         
                String soundFiles = gson.toJson(getNoisyTokens());
                
                //send clients list of noisy tokens file names
                output.println(soundFiles);
                
                //wait until both clients have finished preloading the sound files, then only start the game
                String preloadResponse = input.readLine();
                
                //if the client has finished preloading the sound files
                if (preloadResponse!=null && preloadResponse.equals("Finished preloading")) {
                	
                	//check if the other client has finished
                	notifyTokensPreloaded();
                	
                	//if this client is the last one to finish preloading
                	if (preloadedCount==2) 
                		notifyThatBothHavePreloaded();
                	               	
                	else 
                		waitForBothToPreload();              	
                }
                
                output.println(getOpponent(playerName));

                //continuously get the player's responses and process them.
                while (true) {              	
                	//need to get response from player
                	String response = input.readLine();
                	
                	String opponentResponse = "";
                	
                	String playerAnswer = response.split("\t")[0];
                	double timeTaken = Double.parseDouble(response.split("\t")[1]);
                	
                	//store response to database
                	int clipID = Integer.parseInt(noisyTokens.get(roundNum-1).replace(".wav", "")); 	
                	DBInterface.storeUserResponse(playerName, clipID, playerAnswer);
                	String correctWord = DBInterface.getCorrectWord(clipID);                	
                	
                	//it would be easier to check if both answers are in the same category if the
                	//time penalty is applied after performing the check
                	score = getScore(playerAnswer, correctWord);
                	
                	//store player's response to a 2D array
                	makeMove(roundNum, playerAnswer + "\t" + score);
                	               	
                	//if both players have answered, notify all threads
                	if (bothPlayersAnswered(roundNum)) {
                		DBInterface.updateListenCount(clipID);
                    	notifyThatBothHaveResponded();
                    	opponentResponse = getAnswer(roundNum, 0).split("\t")[0];
                	}
                	
                	//wait until the other player has made a move before continuing
                	else {  
                		DBInterface.updateListenCount(clipID);
                		waitForBothResponses();		
                		opponentResponse = getAnswer(roundNum, 1).split("\t")[0];
                	}	    
                	
                	//check if both players' answers are in the same category
                	double score1 = Double.parseDouble(getAnswer(roundNum, 0).split("\t")[1]);
                	double score2 = Double.parseDouble(getAnswer(roundNum, 1).split("\t")[1]);
                	
                	score = score - (timeTaken*5);
                	
                	if (score1==score2) 
                		score = 2 * score;                                
                	
                	score = (score<0) ? 0 : score;	 //make sure the score isn't negative
                	totalScore += score;
                	
                	ServerResponse msgToClient = new ServerResponse(correctWord, score , totalScore, opponentResponse);
                	String serverResponse = gson.toJson(msgToClient);
                	
                	//if this is the last round
                	if (roundNum==responses.length) {           		
                		//prepend the word "last" to the correct answer to inform the client this is the last round                		
                		msgToClient.setCorrectAnswer("GAME OVER " + msgToClient.getCorrectAnswer());
                		
                		//player one adds his/her score to the DB first
                		if (playerName.equals(players.get(0))) {
                			DBInterface.insertIntoLeaderboards(playerName, (int)Math.round(totalScore));
                			Thread.sleep(1500);
                		}
                		
                		//player two adds his/her score to the DB last
                		else {
                			Thread.sleep(1500);
                			DBInterface.insertIntoLeaderboards(playerName, (int)Math.round(totalScore));
                		}
                		
                		serverResponse = gson.toJson(msgToClient);
                		output.println(serverResponse);
                		successfulGame = true;    		
                		break;
                	}

                	else {		
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
            		//add to leaderboards table 
            		if (successfulGame == false) {
            			//player one adds his/her score to the DB first
                		if (playerName.equals(players.get(0))) {
                			DBInterface.insertIntoLeaderboards(playerName, (int)Math.round(totalScore));
                			Thread.sleep(1500);
                		}
                		
                		//player two adds his/her score to the DB last
                		else {
                			Thread.sleep(1500);
                			DBInterface.insertIntoLeaderboards(playerName, (int)Math.round(totalScore));
                		}
            		}
            		
            		//close the input, output, and server sockets
            		output.close();
            		input.close();            		
            		socket.close();    		
            	} 
            	catch (IOException e) {
            		e.printStackTrace();
            	}
            	catch (SQLException e) {
            		System.out.println("A database error has occurred.");
            	} 
            	catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
    }
}