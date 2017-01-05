package controller;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.List;
import model.User;
import model.Catalog;
import dao.AuthDAO;
import dao.CatalogDAO;
import enums.userType;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
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
		
		
		String msg = "";
		User user = new User();
		AuthDAO data = new AuthDAO();
		String username = request.getParameter("username");
		if(username == null) username = "";
		String password = request.getParameter("password");
		if(password == null) password = "";
		if (username.length() == 0)
		{
			msg = " Please enter username.<br>";
		}
		if (password.length() == 0)
		{
			msg = msg + " Please enter password.<br>";
		}
		
		//If no error message.  Check username and password.
		if(msg.equals(""))
		{
			int userId = -1;
			try {
				
				userId = data.checkUserPass(username, password);
				//If valid user ID returned.  Get user object.
				if (userId != -1)
				{
					 user = data.getUserById(userId) ;
					 //If valid user returned.  Set logged in to true.
					 if(user.getUserId() != -1)
					 {
						 user.setLoggedIn(true);
					 }
				}
			} catch (SQLException e) {
				
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			} catch (Throwable e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If login not valid.
			if (userId == -1 )
			{
				msg = " Username/Password Not Valid.";
			}
			//if login valid.  Start session.
			else{
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				request.setAttribute("msg", "Login Successful");
				
				//Load products in Catalog
				Catalog catalog = new Catalog();
				CatalogDAO catalogData = new CatalogDAO();
				List wishList = new List();
				List shoppingCart = new List();				
				try {
					catalog = catalogData.getCatalog();
					shoppingCart = catalogData.getShoppingCart(userId);
					wishList = catalogData.getWishList(userId);					
					catalogData.connection.DB_Close();
					data.connection.DB_Close();
				} catch (SQLException e) {
					
					msg = msg + " Sql Exception " + e.toString();
					e.printStackTrace();
				} catch (Throwable e) {
					
					msg = msg + " Sql Exception " + e.toString();
					e.printStackTrace();
				}
				session.setAttribute("catalog", catalog);
				session.setAttribute("departmentFilter", "");
				session.setAttribute("catalogTextFilter", "");
				
				//Load WishList and Shopping List if buyer
				if(user.getUserType() == userType.buyer.value)
				{
					session.setAttribute("shoppingCart", shoppingCart);
					session.setAttribute("wishList", wishList);
				}
				
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
					dispatcher.forward(request,  response);
				}
				
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
				dispatcher.forward(request,  response);
			}
		}

		if(!msg.equals("")){
			request.setAttribute("msg", msg);
			request.setAttribute("User", user);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp");
			dispatcher.forward(request,  response);
		}
		

		try {
			data.connection.DB_Close();
			
		} catch (Throwable e) {
			msg = "Error occurred closing Database. " + e.toString();
			e.printStackTrace();
		}
	}

}
