package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CatalogDAO;
import model.List;
import model.User;

/**
 * Servlet implementation class CatalogServlet
 */
public class CatalogServlet extends HttpServlet {
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
		//Check to see if Filter Catalog was clicked.
		if(request.getParameter("filterCatalog") != null){
			String departmentFilter = request.getParameter("departmentDropDown");
			String catalogFilter = request.getParameter("filterText");
			HttpSession session = request.getSession();
			session.setAttribute("departmentFilter", departmentFilter);
			session.setAttribute("catalogTextFilter", catalogFilter);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Edit Product was clicked.
		if(request.getParameter("editProduct") != null){
			String productId = request.getParameter("productId");
			HttpSession session = request.getSession();
			session.setAttribute("productId", productId);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/editProduct.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Message Seller was clicked.
		if(request.getParameter("messageSeller") != null){
			HttpSession session = request.getSession();
			String receiverIdString = request.getParameter("sellerId");
			if(receiverIdString == null)
			{
				String msg = "Seller ID missing";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
				dispatcher.forward(request, response);
			}
			session.setAttribute("receiverId", receiverIdString);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Rate Product was clicked.
		if(request.getParameter("rateProduct") != null){
			String productId = request.getParameter("productId");
			HttpSession session = request.getSession();
			session.setAttribute("productId", productId);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/review.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Add to shopping Cart was clicked.
		if(request.getParameter("addToShoppingCart") != null){
			String msg = "";
			String productSellerId = request.getParameter("productSellerId");
			HttpSession session = request.getSession();
			List shoppingCart = (List)session.getAttribute("shoppingCart");
			User user = (User)session.getAttribute("user");
			CatalogDAO catalogData = new CatalogDAO();
		
				try {
					catalogData.addProductShoppingCart(shoppingCart.getListId(), Integer.parseInt(productSellerId));
				} catch (NumberFormatException | SQLException e1) {
					msg = msg + " Sql Exception " + e1.toString();
					e1.printStackTrace();
				}
				
				//Update in memory shopping cart
				try {
					shoppingCart = catalogData.getShoppingCart(user.getUserId());
				} catch (SQLException e1) {
					msg = msg + " Sql Exception " + e1.toString();
					e1.printStackTrace();
				}
				session.setAttribute("shoppingCart", shoppingCart);

			try {
				catalogData.connection.DB_Close();
			} catch (Throwable e) {
				
				e.printStackTrace();
			}
			
			msg = "Product successfully added to shopping cart!";
			request.setAttribute("msg", msg);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
			dispatcher.forward(request,  response);
		}
		
		//Check to see if Add to wish list was clicked.
		if(request.getParameter("addToWishList") != null){
			String msg = "";
			String productSellerId = request.getParameter("productSellerId");
			HttpSession session = request.getSession();
			List wishList = (List)session.getAttribute("wishList");
			User user = (User)session.getAttribute("user");
			CatalogDAO catalogData = new CatalogDAO();
		
				try {
					catalogData.addProductWishList(wishList.getListId(), Integer.parseInt(productSellerId));
				} catch (NumberFormatException | SQLException e1) {
					msg = msg + " Sql Exception " + e1.toString();
					e1.printStackTrace();
				}
				
				//Update in memory shopping cart
				try {
					wishList = catalogData.getWishList(user.getUserId());
				} catch (SQLException e1) {
					msg = msg + " Sql Exception " + e1.toString();
					e1.printStackTrace();
				}
				session.setAttribute("wishList", wishList);

			try {
				catalogData.connection.DB_Close();
			} catch (Throwable e) {
				
				e.printStackTrace();
			}
			
			msg = "Product successfully added to wish list!";
			request.setAttribute("msg", msg);
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/catalog.jsp");
			dispatcher.forward(request,  response);
		}
	}

}
