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
import dao.CatalogDAO;

/**
 * Servlet implementation class ShoppingCartServlet
 */
public class ShoppingCartServlet extends HttpServlet {
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
		//If remove from shopping cart clicked
		if(request.getParameter("removeFromShoppingcart") != null)
		{
			String msg = "";
			
			//get session with user object
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			//get listItemId
			int listItemId = Integer.parseInt(request.getParameter("listItemId"));
			
			//Create connection
			CatalogDAO catalogData = new CatalogDAO();
			
			//Update Data
			try {
				catalogData.removeProductShoppingCart(listItemId);
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
				dispatcher.forward(request,  response);
			}
			
			List shoppingCart = new List();
			
			//Update user Shopping Cart Object
			try {
				shoppingCart = catalogData.getShoppingCart(user.getUserId());
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
				dispatcher.forward(request,  response);
			}
			
			session.setAttribute("shoppingCart", shoppingCart);
			msg = "Product Removed Successfully!";
			request.setAttribute("msg", msg);

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/shoppingCart.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If remove from wish list clicked
		if(request.getParameter("removeFromWishList") != null)
		{
			String msg = "";
			
			//get session with user object
			HttpSession session = request.getSession();
			User user = (User)session.getAttribute("user");
			
			//get listItemId
			int listItemId = Integer.parseInt(request.getParameter("listItemId"));
			
			//Create connection
			CatalogDAO catalogData = new CatalogDAO();
			
			//Update Data
			try {
				catalogData.removeProductWishList(listItemId);
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/wishList.jsp");
				dispatcher.forward(request,  response);
			}
			
			List wishList = new List();
			
			//Update user Shopping Cart Object
			try {
				wishList = catalogData.getWishList(user.getUserId());
			} catch (SQLException e) {
				msg = msg + " Sql Exception " + e.toString();
				e.printStackTrace();
			}
			
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/wishList.jsp");
				dispatcher.forward(request,  response);
			}
			
			session.setAttribute("wishList", wishList);
			msg = "Product Removed Successfully!";
			request.setAttribute("msg", msg);

			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/wishList.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Add to shopping Cart was clicked.
		if(request.getParameter("addToShoppingCart") != null){
			String msg = "";
			HttpSession session = request.getSession();
			List shoppingCart = (List)session.getAttribute("shoppingCart");
			List wishList = (List)session.getAttribute("wishList");
			User user = (User)session.getAttribute("user");
			CatalogDAO catalogData = new CatalogDAO();
			//get listItemId
			int listItemId = Integer.parseInt(request.getParameter("listItemId"));
		
				try {
					catalogData.moveListItem(listItemId, shoppingCart.getListId());
				} catch (NumberFormatException | SQLException e1) {
					msg = msg + " Sql Exception " + e1.toString();
					e1.printStackTrace();
				}
				
				//Update in memory shopping cart
				try {
					wishList = catalogData.getWishList(user.getUserId());
					shoppingCart = catalogData.getShoppingCart(user.getUserId());
				} catch (SQLException e1) {
					msg = msg + " Sql Exception " + e1.toString();
					e1.printStackTrace();
				}
				request.setAttribute("msg", msg);
				
				session.setAttribute("wishList", wishList);
				session.setAttribute("shoppingCart", shoppingCart);

			try {
				catalogData.connection.DB_Close();
			} catch (Throwable e) {
				
				e.printStackTrace();
			}
			
			msg = "Product added to shopping cart successfully!";
			request.setAttribute("msg", msg);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/wishList.jsp");
			dispatcher.forward(request,  response);
		}
		
		//If submit shopping cart clicked
		if(request.getParameter("submitShoppingCart") != null)
		{
			//forward to purchase page
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
			dispatcher.forward(request,  response);
		}
	}

}
