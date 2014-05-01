package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

import accounts.User;
import audioMixer.NoisyTokensGenerator;

public class DBInterface {

	// Declare the connection settings strings:	
	//database login credentials
	private static final String server = DBCredentials.getServer();
	private static final String database = DBCredentials.getDatabase();
	private static final String username = DBCredentials.getUsername();
	private static final String password = DBCredentials.getPassword();
	
	public static void main (String[]args) {
		/*try {
			insertIntoLeaderboards("AAAAAAAAAA", 1001);
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * Opens a connection to the database
	 * @param conn the database connection
	 */
	private static Connection openConnection(Connection conn) throws SQLException {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" 
					+ server + "/" + database, username, password);
		}
		catch (SQLException e) {
			throw e;
		}
		return conn;
	}

	/**
	 * Closes an open database connection
	 * @param conn the database connection
	 */
	private static void closeConnection(Connection conn) {
		// Check whether the connection exists
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
			}
		}
	}

	/**
	 * Closes an open statement
	 * @param stat the statement
	 */
	private static void closeStatement(PreparedStatement stat) {
		// Check whether the statement exists
		if (stat != null) { 
			try {
				stat.close();
			}
			catch (SQLException e){

			}
		}
	}
	
	/**
	 * Closes an open statement
	 * @param stat the statement
	 */
	private static void closeStatement(Statement stat) {
		// Check whether the statement exists
		if (stat != null) { 
			try {
				stat.close();
			}
			catch (SQLException e){

			}
		}
	}

	/**
	 * Adds an entry to the accounts table. It assumes the user doesn't already exist
	 * @param user the user to be added to the accounts table
	 * @throws SQLException
	 */
	public static void registerForAccount(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);

			// The SQL query for registering for an account
			String query = "INSERT INTO Accounts(username, pword, birthday, gender, hearingImpaired)";
			query += " VALUES (?, PASSWORD(?), ?, ?, ?);";
			stat = conn.prepareStatement(query);
			stat.setString(1, user.getUsername());
			stat.setString(2, user.getPassword());
			stat.setDate(3, user.getBirthday());
			stat.setString(4, user.getGender());
			stat.setString(5, user.getHearingImpaired());

			// Execute the query
			stat.executeUpdate();
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}

	/**
	 * Creates a new player profile when the user registers for an account
	 * @param user
	 */
	/*public static void createNewPlayerProfile(User user) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);

			// The query for creating a new player profile
			String query = "INSERT INTO PlayerProfile(username) VALUES (?);";
			stat = conn.prepareStatement(query);
			stat.setString(1, user.getUsername());			

			// Execute the query
			stat.executeUpdate(); 
		}
		catch (SQLException ex) {
			throw ex;
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}*/

	/**
	 * Checks whether the username already exists in the database
	 * @param username
	 * @return true if the username exists, false if it doesn't exist
	 * @throws SQLException
	 */
	public static boolean doesUserExist(String username) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);

			// The query for checking whether the username exists
			String query = "select username from Accounts where username = ?;";
			stat = conn.prepareStatement(query);
			stat.setString(1, username);

			// Execute the query
			ResultSet rs = stat.executeQuery();

			if (rs.next()) 
				return true;

			else 
				return false;
		}
		catch (SQLException ex){
			throw ex;	
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}

	/**
	 * Check whether the user input matches the login credentials in the database
	 * @param username
	 * @param password
	 * @return the player's first name in the database if they match, null if they don't match
	 * @throws SQLException
	 */
	public static String getLogin(String username, String password) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);
			String user;

			// The query for logging in
			String query = "SELECT username FROM Accounts WHERE username = ? and pword = PASSWORD(?);";					
			stat = conn.prepareStatement(query);
			stat.setString(1, username);
			stat.setString(2, password);

			// Execute the query
			ResultSet rs = stat.executeQuery();
			if (rs.next()) 	
				user = rs.getString("username");

			else 
				user = null;

			return user;
		}
		catch (SQLException ex){
			throw ex;	
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}

	/**
	 * Get the word that is said in the noisy token
	 * @param clipID the clipID of the noisy token
	 * @return the correct word
	 * @throws SQLException
	 */
	public static String getCorrectWord(int clipID) throws SQLException{
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);
			String correctWord = "";
			
			// The query for retrieving the correct answer
			String query = "select word from NoisyTokens where clipID = ?;";
			stat = conn.prepareStatement(query);
			stat.setInt(1, clipID);

			// Execute the query
			ResultSet rs = stat.executeQuery();

			if (rs.first()) 
				correctWord = rs.getString("word");			

			return correctWord;
		}
		catch (SQLException ex){
			throw ex;	
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}	
	}
	
	/**
	 * Retrieve noisy tokens based on what both players haven't listened to
	 * @param player1's username
	 * @param player2's username
	 * @return an array list of noisy tokens
	 * @throws SQLException
	 */
	public static ArrayList<String> getNoisyTokens(int numTokens, double SNR, String player1, String player2) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);
			ArrayList<String> noisyTokens = new ArrayList<String>();
			
			// The query for selecting the noisy tokens that the players have already listened to
			String query = "SELECT DISTINCT NoisyTokens.clipID from NoisyTokens LEFT OUTER JOIN "
					+ "UsersResponses ON NoisyTokens.clipID = UsersResponses.clipID WHERE "
					+ "(UsersResponses.username = ? OR UsersResponses.username = ?) AND SNR = ? ;";
			stat = conn.prepareStatement(query);
			stat.setString(1, player1);
			stat.setString(2, player2);
			stat.setDouble(3, SNR);

			// Execute the query
			ResultSet rs = stat.executeQuery();
			
			// Select tokens that neither players have heard before
			query = "SELECT clipID FROM NoisyTokens WHERE (listenCount < 15) AND "
					+ "(SNR = ?)";		
			
			if (rs.next()) {
				query += " AND (clipID <> " + rs.getInt("NoisyTokens.clipID");
				while (rs.next()) {
					query += " AND clipID <> " + rs.getInt("NoisyTokens.clipID");
				}
				query += ")";
			}
			
			query += " LIMIT ?;";
			stat = conn.prepareStatement(query);
			stat.setDouble(1, SNR);
			stat.setInt(2, numTokens);
			
			ResultSet resSet = stat.executeQuery();
			
			while (resSet.next()) {
				noisyTokens.add(resSet.getInt("clipID")+".wav");
			}
			
			return noisyTokens;
		}
		catch (SQLException ex){
			throw ex;	
		}
		finally {

			closeStatement(stat);
			closeConnection(conn);
		}	
	}

	/**
	 * Store what the player thought he/she heard
	 * @param userResponse
	 * @throws SQLException
	 */
	public static void storeUserResponse(String username, int clipID, String response) throws SQLException{
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);
			
			// Query for inserting the user's response into the database
			String query = "INSERT INTO UsersResponses(username, clipID, response, date)"
					+ " VALUES (?, ? , ?, NOW());";
			stat = conn.prepareStatement(query);
			stat.setString(1, username);
			stat.setInt(2, clipID);
			stat.setString(3, response);
			stat.executeUpdate();

		}
		catch (SQLException ex){
			throw ex;

		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}
	
	public static void updateListenCount(int clipID) throws SQLException{
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);
			
			// Query for updating listen count in the NoisyTokens table
			String query = "SELECT listenCount FROM NoisyTokens WHERE clipID = ?;";
			stat = conn.prepareStatement(query);
			stat.setInt(1, clipID);

			ResultSet rs = stat.executeQuery();

			if (rs.next()) {
				int updatedCount = rs.getInt("listenCount")+1;
				query = "UPDATE NoisyTokens SET listenCount = ? WHERE clipID = ?;";
				stat = conn.prepareStatement(query);
				stat.setInt(1, updatedCount);
				stat.setInt(2, clipID);
				stat.executeUpdate();
			}			
		}
		catch (SQLException ex){
			throw ex;

		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}

	/**
	 * Inserts score into leaderboards
	 * @param username
	 * @param score the player's high score
	 * @throws SQLException
	 */
	public static void insertIntoLeaderboards(String username, int score) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			System.out.println("sending score to DB");
			conn = openConnection(conn);
			
			// Display only 15 high scores in the leaderboards table
			final int MAX_HIGH_SCORES = 15;
			int numHighScores = 0;
			int lowestHighScore = 9999999;
			String query = "SELECT * FROM Leaderboards;";
			stat = conn.prepareStatement(query);
			ResultSet rs = stat.executeQuery();

			// Get lowest high score & username of player with the lowest score
			while (rs.next()) {
				numHighScores++; 
				int curScore = rs.getInt("score");
				lowestHighScore = (curScore < lowestHighScore) ? curScore: lowestHighScore;
				if (curScore < lowestHighScore) 
					lowestHighScore = curScore;							
			}

			// If number of high scores < max num of high scores
			if (numHighScores<MAX_HIGH_SCORES) {
				System.out.println("adding high score!");
				// Query for adding score to leaderboards
				query = "INSERT INTO Leaderboards(username, score) VALUES(?, ?);";
				stat = conn.prepareStatement(query);
				stat.setString(1, username);
				stat.setInt(2, score);
				
				// Execute the query
				stat.executeUpdate();
			}

			// If the score to be added is high enough
			// Replace this high score with the lowest high score
			else if (numHighScores == MAX_HIGH_SCORES && score > lowestHighScore ) {
				System.out.println("new high score!!");
				// Query for removing lowest high score 
				query = "DELETE FROM Leaderboards WHERE score = ? LIMIT 1;";
				stat = conn.prepareStatement(query);
				stat.setInt(1, lowestHighScore);
				
				// Execute the query
				stat.executeUpdate();
				
				// Query for inserting new high score
				query = "INSERT INTO Leaderboards(username, score) VALUES(?, ?);";
				stat = conn.prepareStatement(query);
				stat.setString(1, username);
				stat.setInt(2, score);
				
				// Execute the query
				stat.executeUpdate();
			}
			
			// Do nothing if score is not high enough to be added			
		}
		catch (SQLException ex){
			throw ex;
		}
		finally {
			stat.close();
			closeConnection(conn);
		}
	}

	/**
	 * Stores information about the noisy token in the database
	 * @param word
	 * @param noiseType
	 * @param SNR
	 * @param offset
	 * @throws SQLException
	 */
	public static void storeTokenInfo(String word, String noiseType, double SNR, int offset) throws SQLException{
		Connection conn = null;
		PreparedStatement stat = null;

		try {
			conn = openConnection(conn);				

			// The query storing token information
			String query = "INSERT INTO NoisyTokens(word, noiseType, SNR, offset) VALUES(?, ?, ?, ?);";
			stat = conn.prepareStatement(query);
			stat.setString(1, word);
			stat.setString(2, noiseType);
			stat.setFloat(3, (float)SNR);
			stat.setInt(4, offset);
			
			// Execute the query
			stat.executeUpdate();
		}
		catch (SQLException ex) {
			throw ex;
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}
	
	/**
	 * Get the auto increment value from the noisy tokens table
	 * @return
	 */
	public static String getTokenAutoIncVal() throws SQLException{
		Connection conn = null;
		Statement stat = null;
		
		try {
			conn = openConnection(conn);
			stat = conn.createStatement();
			
			String query = "SHOW TABLE STATUS LIKE 'NoisyTokens'";
			
			ResultSet rs = stat.executeQuery(query);
			if (rs.next()) {
				return Integer.toString(rs.getInt("Auto_increment"));
			}
			return "-1";
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			closeStatement(stat);
			closeConnection(conn);
		}
	}	
}
