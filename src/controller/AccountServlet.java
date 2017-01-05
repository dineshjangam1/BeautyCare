package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.Users;
import dao.AuthDAO;
import dao.MessageDAO;
import enums.userType;
import enums.Admin;

/**
 * Servlet implementation class AccountServlet
 */
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//If remove user Clicked
		if(request.getParameter("removeUser") != null)
		{
			String msg = "";
			
			//get session with user object
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			//Create connection
			AuthDAO dataAuth = new AuthDAO();
			
			//Update Data
			try {
				dataAuth.closeAccount(user.getUserId());
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
			}
			
			msg = "Account Deleted Successfully!";
			request.setAttribute("msg", msg);

			//Logout
			session.invalidate();
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If remove user admin Clicked
		if(request.getParameter("removeUserAdmin") != null)
		{
			String msg = "";
			
			int userId = Integer.parseInt(request.getParameter("userIdHidden"));
			
			//get session with user object
			HttpSession session = request.getSession();
			
			//Create connection
			AuthDAO dataAuth = new AuthDAO();
			
			//Update Data
			try {
				dataAuth.closeAccount(userId);
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
			}
			
			msg = "Account Deleted Successfully!";
			request.setAttribute("msg", msg);

			//Reload user Data
			AuthDAO data = new AuthDAO();
			try {
				Users users = data.getUsers();
				session.setAttribute("users", users);
				data.connection.DB_Close();
			} catch (ClassNotFoundException | SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			request.setAttribute("msg", msg);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If approve seller Clicked
		if(request.getParameter("approveSeller") != null)
		{
			String msg = "";
			
			int userId = Integer.parseInt(request.getParameter("userIdHidden"));
			
			//get session with user object
			HttpSession session = request.getSession();
			
			//Create connection
			AuthDAO dataAuth = new AuthDAO();
			
			//Update Data
			try {
				dataAuth.approveSeller(userId);
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
			}
			
			msg = "Account updated to seller Successfully!";
			request.setAttribute("msg", msg);

			//Reload user Data
			AuthDAO data = new AuthDAO();
			try {
				Users users = data.getUsers();
				session.setAttribute("users", users);
				data.connection.DB_Close();
			} catch (ClassNotFoundException | SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//Send message to new seller.
			User user = (User)session.getAttribute("user");
			MessageDAO dataMessage = new MessageDAO();
			try {
				dataMessage.sendMessage(userId, user.getUserId(), "Seller account approved.");
				dataMessage.connection.DB_Close();
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			request.setAttribute("msg", msg);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Navigate to request Seller Account Page
		if(request.getParameter("requestSellerAccount") != null)
		{
			//Redirect to seller request page
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/requestSeller.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If Message Admin button clicked
		if(request.getParameter("messageAdmin") != null)
		{
			String userId = request.getParameter("userIdHidden");
			String userName = request.getParameter("userNameHidden");
			HttpSession session = request.getSession();
			session.setAttribute("receiverId", userId);
			session.setAttribute("receiverName", userName);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If deny seller button clicked
		if(request.getParameter("denySeller") != null)
		{
			String msg = "";
			
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			int userId = Integer.parseInt(request.getParameter("userIdHidden"));
			MessageDAO dataMessage = new MessageDAO();
			try {
				dataMessage.sendMessage(userId, user.getUserId(), "Seller account denied.");
				dataMessage.connection.DB_Close();
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			
				msg = "Seller Account denied successfully.";
			
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
		}

		
		//If request Seller Account Clicked
		
		if(request.getParameter("requestSellerAccountClick") != null)
		{
			String msg = "";
			
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			int accountNumber = -1;
			int routingNumber = -1;
			String url = "";
			String companyName = "";
			
			//Verify routingNumber
			String routingNumberString = request.getParameter("routingNumber");
			if(routingNumberString == null) routingNumberString = "";
			if (routingNumberString.length() == 0)
			{
				msg = msg + " Please enter Routing Number.<br>";
			}
			else if(routingNumberString.length() > 9)
			{
				msg = msg + " Routing Number can only be 9 digits long.<br>";
			}
			else{
				
				try {
					routingNumber = Integer.parseInt(routingNumberString);
				}
				catch (NumberFormatException nfe)
				{
					msg = msg + " Please enter routing number as all numbers.";
				}
			}
			
			//Verify accountNumber
			String accountNumberString = request.getParameter("accountNumber");
			if(accountNumberString == null) accountNumberString = "";
			if (accountNumberString.length() == 0)
			{
				msg = msg + " Please enter Account Number.<br>";
			}
			else if(accountNumberString.length() > 9)
			{
				msg = msg + " Account Number can only be 9 digits long.<br>";
			}
			else{
				
				try {
					accountNumber = Integer.parseInt(accountNumberString);
				}
				catch (NumberFormatException nfe)
				{
					msg = msg + " Please enter account number as all numbers.";
				}
			}
			
			//Verify url
			url = request.getParameter("url");
			if(url == null) url = "";
			if (url.length() == 0)
			{
				msg = msg + " Please enter URL.<br>";
			}
			else if(url.length() > 20)
			{
				msg = msg + " URL can only be 20 characters long.<br>";
			}
			
			//Verify companyName
			companyName = request.getParameter("companyName");
			if(companyName == null) companyName = "";
			if (companyName.length() == 0)
			{
				msg = msg + " Please enter Company Name.<br>";
			}
			else if(url.length() > 20)
			{
				msg = msg + " Company Name can only be 20 characters long.<br>";
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/requestSeller.jsp");
				dispatcher.forward(request,  response);
			}
			
			//If no issues with form, process Sign up.
			else{
				AuthDAO data = new AuthDAO();
				try {
						int userId = user.getUserId();
						int result=data.UpdateSeller(userId, routingNumber, accountNumber, url, companyName);
						
						//If user account entered
						if(result == 1)
						{
								msg = "Seller Account Requested";
								request.setAttribute("msg", msg);
								
								//Send message to admin requesting seller account
								MessageDAO dataMessage = new MessageDAO();
								try {
									
									session.setAttribute("receiverId", "1");
									session.setAttribute("receiverName", "Admin");
									
									
									String message = "Please upgrade user " + user.getUserName() + " to a seller account";
									dataMessage.sendMessage(Admin.Admin.value, user.getUserId(), message);
									dataMessage.connection.DB_Close();
								} catch (SQLException e) {
									msg = msg + " Sql Exception " + e.toString();
									e.printStackTrace();
								} catch (Throwable e) {
									msg = msg + " Sql Exception " + e.toString();
									e.printStackTrace();
								}
								
								//Reload new user information.
								user =  data.getUserById(userId) ;
								user.setLoggedIn(true);
								session.setAttribute("user", user);
								
								RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/requestSeller.jsp");
								dispatcher.forward(request,  response);
						}
						else{
							msg = "Seller Account request failed.";
						}
					}
				 catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/requestSeller.jsp");
					dispatcher.forward(request,  response);
				}
				try {
					data.connection.DB_Close();
				} catch (Throwable e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			}
			
			msg = "Request Submitted Successfully!";
			request.setAttribute("msg", msg);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/requestSeller.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If update account Clicked
		if(request.getParameter("updateAccount") != null)
		{
			String msg = "";
			
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			//Verify first name
			String firstName = request.getParameter("firstName");
			if(firstName == null) firstName = "";
			if (firstName.length() == 0)
			{
				msg = msg + " Please enter First Name.<br>";
			}
			else if(firstName.length() > 20)
			{
				msg = msg + " First name can only be 20 characters long.<br>";
			}
			
			//Verify last name
			String lastName = request.getParameter("lastName");
			if(lastName == null) lastName = "";
			if (lastName.length() == 0)
			{
				msg =msg + " Please enter Last Name.<br>";
			}
			else if(lastName.length() > 20)
			{
				msg = msg + " Last name can only be 20 characters long.<br>";
			}
			
			//Verify phoneNumber
			String phoneNumber = request.getParameter("phoneNumber");
			if(phoneNumber == null) phoneNumber = "";
			if (phoneNumber.length() == 0)
			{
				msg =msg + " Please enter Phone Number.<br>";
			}
			else if(phoneNumber.length() > 20)
			{
				msg = msg + " Phone number can only be 20 characters long.<br>";
			}
			
			//Verify email
			String email = request.getParameter("email");
			if(email == null) email = "";
			if (email.length() == 0)
			{
				msg =msg + " Please enter Email.<br>";
			}
			else if(email.length() > 20)
			{
				msg = msg + " Email can only be 20 characters long.<br>";
			}
			
			//Verify Street
			String street = request.getParameter("street");
			if(street == null) street = "";
			if (street.length() == 0)
			{
				msg =msg + " Please enter Street.<br>";
			}
			else if(street.length() > 20)
			{
				msg = msg + " Street can only be 20 characters long.<br>";
			}
			
			//Verify City
			String city = request.getParameter("city");
			if(city == null) city = "";
			if (city.length() == 0)
			{
				msg =msg + " Please enter City.<br>";
			}
			else if(city.length() > 20)
			{
				msg = msg + " City can only be 20 characters long.<br>";
			}
			
			//Verify State
			String state = request.getParameter("state");
			if(state == null) city = "";
			if (state.length() == 0)
			{
				msg =msg + " Please enter State.<br>";
			}
			else if(state.length() > 20)
			{
				msg = msg + " State can only be 20 characters long.<br>";
			}
			
			//Verify Zip
			String zip = request.getParameter("zip");
			if(zip == null) zip = "";
			if (zip.length() == 0)
			{
				msg =msg + " Please enter Zip.<br>";
			}
			else if(zip.length() > 20)
			{
				msg = msg + " Zip can only be 20 characters long.<br>";
			}
			
			//Verify Password
			String password = request.getParameter("password");
			if(password == null) password = "";
			if (password.length() == 0)
			{
				msg = msg + " Please enter Password.<br>";
			}
			else if(password.length() > 20)
			{
				msg = msg + " Password can only be 20 characters long.<br>";
			}
			
			int accountNumber = -1;
			int routingNumber = -1;
			String url = "";
			String companyName = "";
			
			if(user.getUserType() == userType.seller.value)
			{
				//Verify routingNumber
				String routingNumberString = request.getParameter("routingNumber");
				if(routingNumberString == null) routingNumberString = "";
				if (routingNumberString.length() == 0)
				{
					msg = msg + " Please enter Routing Number.<br>";
				}
				else if(routingNumberString.length() > 9)
				{
					msg = msg + " Routing Number can only be 9 digits long.<br>";
				}
				else{
					
					try {
						routingNumber = Integer.parseInt(routingNumberString);
					}
					catch (NumberFormatException nfe)
					{
						msg = msg + " Please enter routing number as all numbers.";
					}
				}
				
				//Verify accountNumber
				String accountNumberString = request.getParameter("accountNumber");
				if(accountNumberString == null) accountNumberString = "";
				if (accountNumberString.length() == 0)
				{
					msg = msg + " Please enter Account Number.<br>";
				}
				else if(accountNumberString.length() > 9)
				{
					msg = msg + " Account Number can only be 9 digits long.<br>";
				}
				else{
					
					try {
						accountNumber = Integer.parseInt(accountNumberString);
					}
					catch (NumberFormatException nfe)
					{
						msg = msg + " Please enter account number as all numbers.";
					}
				}
				
				//Verify url
				url = request.getParameter("url");
				if(url == null) url = "";
				if (url.length() == 0)
				{
					msg = msg + " Please enter URL.<br>";
				}
				else if(url.length() > 20)
				{
					msg = msg + " URL can only be 20 characters long.<br>";
				}
				
				//Verify companyName
				companyName = request.getParameter("companyName");
				if(companyName == null) companyName = "";
				if (companyName.length() == 0)
				{
					msg = msg + " Please enter Company Name.<br>";
				}
				else if(url.length() > 20)
				{
					msg = msg + " Company Name can only be 20 characters long.<br>";
				}
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
				dispatcher.forward(request,  response);
			}
			
			//If no issues with form, process Sign up.
			else{
				AuthDAO data = new AuthDAO();
				try {
						int userId = user.getUserId();
						int result=data.AccountUpdate(firstName,lastName,phoneNumber,email,street,city,state,zip,password,userId, routingNumber, accountNumber, url, companyName);
						
						//If user account entered
						if(result == 1)
						{
								msg = "Account Updated Successfully";
								request.setAttribute("msg", msg);
								
								//Reload new user information.
								user =  data.getUserById(userId) ;
								user.setLoggedIn(true);
								session.setAttribute("user", user);
								
								RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
								dispatcher.forward(request,  response);
						}
						else{
							msg = "Account update Failed";
						}
					}
				 catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/account.jsp");
					dispatcher.forward(request,  response);
				}
				try {
					data.connection.DB_Close();
				} catch (Throwable e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			}
		}

	}

}
