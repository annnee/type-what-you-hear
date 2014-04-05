package database;

import java.sql.*;

import com.mysql.jdbc.Driver;

import dissertation.Users;

public class DBInterface {
	
	// Declare a connection and statement to use:
	private static Connection conn = null;
	private static Statement stat = null;

	// Declare the connection settings strings:	
	//database login credentials
	private static final String server = "stusql.dcs.shef.ac.uk";
	private static final String database = "acb11anl";
	private static final String username = "acb11anl";
	private static final String password = "28b4b874 ";
	
	//
	/*private static final String server = "127.0.0.1:3306";
	private static final String database = "javabase";
	private static final String username = "acb11anl";
	private static final String password = "mkbbtf8";*/
	
	public static void main (String[]args) throws Exception {
		openConnection();
		closeConnection();
	}
	
	/**
	 * Method tries to open a connection to the database
	 */
	private static void openConnection() throws Exception {
		try {
			// Try to open the connection:
			System.out.println("Connecting database...");
			conn = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database, username, password);
			System.out.println("Database connected!");
		}
		catch (Exception ex) {
			// Just pass on any exception we get:
			throw ex;
		}
	}
	
	/**
	 * Method closes any open database connections
	 */
	private static void closeConnection() {
		// Check whether our connection exists:
		if (conn != null) {
			try {
				// If so, try to close it and set the connection back to null:
				System.out.println("Closing the connection.");
				conn.close();
				conn = null;
			}
			catch (Exception ex){
				/* Ignore this - if we can't close the connection, 
				 * there is nothing we can do about it.
				 */
			}
		}
	}
	
	/**
	 * Method closes any open statements
	 */
	private static void closeStatement() {
		// Check whether a statement exists:
		if (stat != null) { 
			try {
				// If so, try to close it and set it back to null:
				stat.close();
				stat = null;
			}
			catch (Exception ex){
			}
		}
	}
	
	public static void registerForAccount(Users user) throws Exception {
		try{
			openConnection();
			stat = conn.createStatement();
			
			// Generate the query string based on the input parameter:
			String query = "INSERT INTO Accounts(firstName, lastName, birthday, gender, email, pword)";
			query += " VALUES ('" + user.getfName() + "', '" + user.getlName() 
					+ "', '" + user.getDob() + "', '" + user.getGender() + "', '" 
					+ user.getEmail() + "', PASSWORD('" + user.getPassword() + "'));";
			
			// Try to execute the query:
			stat.executeUpdate(query);
		}
		catch (Exception ex){
			throw ex;
			
		}
		finally {
			closeStatement();
			closeConnection();
		}
	}
	
	public static Users getLogin(String email, String password) throws Exception {
		try{
			openConnection();
			stat = conn.createStatement();
			Users user;
			// Generate the query string based on the input parameter:
			String query = "select * from Accounts where email='" + email + "'"
					+ " and pword= PASSWORD('" + password + "');";
			// Try to execute the query:
			ResultSet rs = stat.executeQuery(query);
			
			if (rs.next()) {
				user = new Users(rs.getString("firstName"),rs.getString("lastName"),null,"",rs.getString("email"),rs.getString("pword"),"");
			}
			else {
				user = new Users("","",null,"","","","");
			}
			
			return user;
		}
		catch (Exception ex){
			throw ex;	
		}
		finally {
			// Close the statement & connection
			closeStatement();
			closeConnection();
		}
	}
	
	public static String getNoisyTokens() {
		return "";
	}
	
	public static void storeUserResponse(String username, String tokenID, String response) {
		
	}
}
