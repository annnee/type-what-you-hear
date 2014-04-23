package dissertation;

import java.io.*;
import java.sql.SQLException;
import java.util.Enumeration;

import com.google.gson.*;

import database.DBInterface;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Gson gson;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();
			//gson = new Gson();
			
		} catch (Exception e){
			e.printStackTrace();
		}
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); 
	}
	
	/**
	 * Register for an account
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8"); 
	    String remoteAddr = request.getRemoteAddr();
	    
		//if data is received from the register form
		if (request.getParameter("json")!=null){

			//flags for input validation
			boolean usernameError = true;
			boolean captchaError = true;
			
			//data received from the form
			User user = gson.fromJson(request.getParameter("json"), User.class);
			RecaptchaResponse recaptcha = gson.fromJson(request.getParameter("json"), RecaptchaResponse.class);
			
			//message to send back to the client;
			String message = "";
			
			try {

				//check if username doesn't already exist in the database
				usernameError = DBInterface.doesUserExist(user.getUsername());

				//check if captcha is correct
				ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
				reCaptcha.setPrivateKey("6Le_L_ISAAAAAOWmsi0Ll_diOHeDaS8TtaJA4TUw");

				String challenge = recaptcha.getRecaptcha_challenge_field();
				String uresponse = recaptcha.getRecaptcha_response_field();
				ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

				if (reCaptchaResponse.isValid()) {
					captchaError = false;
				}

				//if no errors in input, store info in DB
				if (!usernameError && !captchaError) {
					
					DBInterface.registerForAccount(user);
					DBInterface.createNewPlayerProfile(user);
						
					message = "SUCCESS";
					response.getWriter().write(message);				
				}
				
				//error with only the username
				else if (usernameError) {
					message = "That username already exists! Please try again.";
					response.getWriter().write(message);
				}
				
				//error with only the captcha
				else {
					message = "Incorrect captcha response! Please try again.";
					response.getWriter().write(message);
				}
				
			}
			catch (SQLException e) {
				message = "A database error has occurred while creating your account. Please try again.";
				response.getWriter().write(message);
				e.printStackTrace();
			}
			catch (Exception e) {
				message = "An error has occurred while creating your account. Please try again.";
				response.getWriter().write(message);
				e.printStackTrace();
			}			
		}
		
		//if data is received from the login form
		else if (request.getParameter("formType").equals("login")) {
			try {
				HttpSession session = request.getSession(true);
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				String user = DBInterface.getLogin(username, password);
				if (user!=null) {
					session.setAttribute("firstName", user);
					session.setAttribute("username", username);
					response.sendRedirect("welcome.jsp");
				}
				else {
					String message = "Your username/password was incorrect. Please try again.";
					request.setAttribute("message", message);
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				String message = "Failed to connect to the database. Please try again.";
				request.setAttribute("message", message);
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}	
	}
}
