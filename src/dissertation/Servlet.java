package dissertation;

import java.io.*;

import audioMixer.NoisePlayer;

import com.mysql.jdbc.Driver;

import database.DBInterface;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.Date;

/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		} catch (Exception e){
			e.printStackTrace();
		}
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Servlet Directory = " + System.getProperty("user.dir"));
		response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); 
	    /*NoisePlayer noise = new NoisePlayer();
	    noise.processFile();*/
	}
	
	/**
	 * Register for an account
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");  // Set content type of the response so that jQuery knows what it can expect.
	    response.setCharacterEncoding("UTF-8"); 
		//if data is received from the register form
		if (request.getParameter("formType").equals("register")){
			//a flag for input validation
			boolean error = true;
			
			String fName = request.getParameter("fName");
			String lName = request.getParameter("lName");
			String birthday = request.getParameter("birthday");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			String hearingImpaired = request.getParameter("hearing");
			
			hearingImpaired = (hearingImpaired==null) ? "no" : "yes";
			
			// Set content type of the response so that jQuery knows what it can expect.
			response.setContentType("text/plain");
		    response.setCharacterEncoding("UTF-8");
			
			//perform input validation
		    Date dob = java.sql.Date.valueOf("1993-11-02");
		    
		    
			//if no errors in input, store info in DB
			if (!error) {
				
				
			}
			
			//send error message
			else {
				try {
					DBInterface.registerForAccount(new Users(fName, lName, dob, gender, email, password, hearingImpaired));
					
					response.sendRedirect("success.jsp");
					
				} catch (Exception e) {
					String message = "An error has occurred in creating your account. Please try again.";
					request.setAttribute("message", message);
					request.getRequestDispatcher("/register.jsp").forward(request, response);
					e.printStackTrace();
				}
			}
		}
		
		//if data is received from the login form
		else if (request.getParameter("formType").equals("login")) {
			try {
				HttpSession session = request.getSession(true);
				String email = request.getParameter("email");
				String password = request.getParameter("password");
				Users user = DBInterface.getLogin(email, password);
				if (user.getfName()!="") {
					session.setAttribute("userFName", user.getfName());
					session.setAttribute("email", email);
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
			}
		}
		
		else if (request.getParameter("formType").equals("login")) {
			
		}
	}
}
