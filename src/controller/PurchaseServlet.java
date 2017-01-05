package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Catalog;
import model.List;
import model.ListItem;
import model.Order;
import model.Orders;
import model.Product;
import model.ProductSeller;
import model.User;
import dao.CatalogDAO;
import dao.AuthDAO;
import dao.MessageDAO;
import enums.Admin;
import enums.ListType;
import enums.userType;

/**
 * Servlet implementation class PurchaseServlet
 */
public class PurchaseServlet extends HttpServlet {
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
		String orderIdString = "";
		
		//Check to see if Message Buyer was clicked.
		if(request.getParameter("contactBuyer") != null){
			HttpSession session = request.getSession();
			String receiverIdString = request.getParameter("buyerId");
			if(receiverIdString == null)
			{
				msg = "Buyer ID missing";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
				dispatcher.forward(request, response);
			}
			session.setAttribute("receiverId", receiverIdString);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/sendMessage.jsp");
			dispatcher.forward(request,  response);
		}
		
		
		//If Delete Order was clicked
		if(request.getParameter("deleteOrder") != null)
		{
			msg = "";
			orderIdString = request.getParameter("orderId");
			if(orderIdString == null)
			{
				msg = "Order ID not supplied";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
				dispatcher.forward(request,  response);
			}
			else{
				HttpSession session = request.getSession();
				User user = (User)session.getAttribute("user");
				int orderId = Integer.parseInt(orderIdString);
				CatalogDAO data = new CatalogDAO();
				try {
					data.deleteOrder(orderId);
					msg = "Order deleted";
				} catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
				
				
				Orders orders = null;
				
				//Reload buyer orders
				try {
					 orders = data.getSellerOrder(user.getUserId());
				} catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
				
				//Reload Admin Orders
				try {
					 orders = data.getAdminOrder();
				} catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
				
				session.setAttribute("orders", orders);
				request.setAttribute("msg", msg);
				
				msg = "Order Deleted Successfully!";
				request.setAttribute("msg", msg);
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
				dispatcher.forward(request,  response);
			}
		}
		
		
		//If Ship Order was clicked
		if(request.getParameter("shipOrder") != null)
		{
			msg = "";
			orderIdString = request.getParameter("orderId");
			if(orderIdString == null)
			{
				msg = "Order ID not supplied";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
				dispatcher.forward(request,  response);
			}
			else{
				HttpSession session = request.getSession();
				User user = (User)session.getAttribute("user");
				int orderId = Integer.parseInt(orderIdString);
				CatalogDAO data = new CatalogDAO();
				try {
					data.shipOrder(orderId);
					msg = "Order shipped";
				} catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
				//Reload orders
				Orders orders = null;
				try {
					 orders = data.getSellerOrder(user.getUserId());
				} catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
				
				session.setAttribute("orders", orders);
				request.setAttribute("msg", msg);
				
				msg = "Order Shipped Successfully!";
				request.setAttribute("msg", msg);
				
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
				dispatcher.forward(request,  response);
			}
		}
		
		//If update order clicked
		if(request.getParameter("updateOrder") != null)
		{
			
			msg = "";
			orderIdString = request.getParameter("orderId");
			if(orderIdString == null)
			{
				msg = "Order ID not supplied";
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
				dispatcher.forward(request,  response);
			}
			else{
				HttpSession session = request.getSession();
				session.setAttribute("orderId", orderIdString);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/updateOrder.jsp");
				dispatcher.forward(request,  response);
			}
		}
		
		//If update order info clicked
		if(request.getParameter("updateOrderInfo") != null)
		{
			//Verify fields have been filled in
			msg = "";
			
			//Verify first name
			String receiverName = request.getParameter("receiverName");
			if(receiverName == null) receiverName = "";
			if (receiverName.length() == 0)
			{
				msg = msg + " Please enter Receiver Name.<br>";
			}
			else if(receiverName.length() > 20)
			{
				msg = msg + " Receiver name can only be 20 characters long.<br>";
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
			
				
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
				dispatcher.forward(request,  response);
			}
			//Update Order
			else{
				CatalogDAO data = new CatalogDAO();
				MessageDAO messageData = new MessageDAO();
			
				try {

						HttpSession session = request.getSession();
						User user = (User)session.getAttribute("user");
						Order order = (Order)session.getAttribute("order");
						int userId = user.getUserId();
						int orderId = Integer.parseInt((String)session.getAttribute("orderId"));
						data.updateOrder(orderId,  receiverName,  street, city, state, zip);
						int currentUserType = user.getUserType();
						Orders orders = null;
						
						//Update order info in memory
						if(currentUserType == userType.buyer.value)
						{
							orders = data.getBuyerOrder(userId);
						}
						else if(currentUserType == userType.seller.value)
						{
							orders = data.getSellerOrder(userId);
						}
						else if(currentUserType == userType.admin.value)
						{
							orders = data.getAdminOrder();
						}
						session.setAttribute("orders", orders);
						
						//Email Sellers that order has been updated
						Catalog catalog = (Catalog)session.getAttribute("catalog");
						List list = data.getListByOrderId(orderId);
						Set<Integer> sellerSet = new HashSet<Integer>();
						ListIterator<ListItem> items =list.getListItems();
						ListItem item = null;
						ProductSeller seller = null;
						Product product = null;
						String message = "Order " + order.getOrderId() + " has been updated.";
						while(items.hasNext())
						{
							item = items.next();
							product = catalog.getProductById(item.productId);
							seller = product.getProductSellerById(item.sellerId);
							sellerSet.add(seller.getSellerId());
						}
						Integer sellerId = -1;
						Iterator<Integer> sellerIds = sellerSet.iterator();
						while(sellerIds.hasNext())
						{
							sellerId = sellerIds.next();
							messageData.sendMessage(sellerId, Admin.Admin.value, message);
						}
						
						msg = "Order updated successfully!";
						
						request.setAttribute("msg", msg);
						RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/updateOrder.jsp");
						dispatcher.forward(request,  response);
						
					}
				 catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				} catch (Throwable e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
			
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/updateOrder.jsp");
					dispatcher.forward(request,  response);
				}
				try {
					data.connection.DB_Close();
					messageData.connection.DB_Close();
				} catch (Throwable e) {
					msg = "Class Not Found Exception " + e.toString();
					e.printStackTrace();
				}
			
				
				
			}
			
		}
		
		
		
		
		//If purchase Clicked
		if(request.getParameter("purchase") != null)
		{
			//Verify fields have been filled in
			msg = "";
			
			//Verify first name
			String receiverName = request.getParameter("receiverName");
			if(receiverName == null) receiverName = "";
			if (receiverName.length() == 0)
			{
				msg = msg + " Please enter Receiver Name.<br>";
			}
			else if(receiverName.length() > 20)
			{
				msg = msg + " Receiver name can only be 20 characters long.<br>";
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
			
			//Verify Pay Pal Account
			String payPalAccount = request.getParameter("payPalAccount");
			if(payPalAccount == null) payPalAccount = "";
			if (payPalAccount.length() == 0)
			{
				msg = msg + " Please enter Pay Pal Account Number.<br>";
			}
			else if(payPalAccount.length() > 20)
			{
				msg = msg + " Pay Pal Account number can only be 20 characters long.<br>";
			}
				
			//If issues with form display message
			if(!msg.equals("")){
				request.setAttribute("msg", msg);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/purchase.jsp");
				dispatcher.forward(request,  response);
			}
			//Submit Order
			else{
				CatalogDAO data = new CatalogDAO();
				try {

						HttpSession session = request.getSession();
						double tax = (double)session.getAttribute("tax");
						double subTotal = (double)session.getAttribute("subTotal");
						User user = (User)session.getAttribute("user");
						Catalog catalog = (Catalog)session.getAttribute("catalog");
						int userId = user.getUserId();
						int orderId=data.createOrder(userId, tax, subTotal, receiverName, payPalAccount, street, city, state, zip);
						
						//Order Entered
						if(orderId > 0)
						{
								List shoppingList = (List)session.getAttribute("shoppingCart");
								
								//Update ListItem with ShippingCost and Price.
								ListIterator<ListItem> items = shoppingList.getListItems();
								ListItem item = new ListItem();
								Product product = new Product();
								ProductSeller seller = new ProductSeller();
								Set<Integer> sellerSet = new HashSet<Integer>();
								
								while(items.hasNext())
								{
									item = items.next();
									product = catalog.getProductById(item.getProductId());
									seller = product.getProductSellerById(item.sellerId);
									item.price = seller.getPrice();
									item.shippingPrice = seller.getShippingCost();
									data.updateListItemPrices(item);
									sellerSet.add(seller.getSellerId());
								}
								
								//Switch Shopping list to Order list
								data.switchShopCartToOrderList(shoppingList.getListId(), orderId);

								msg = "Order Created Successfully";
								request.setAttribute("msg", msg);
								
								//Message Sellers of order
								String message = "Order " + orderId + " has been submitted.";
								Integer sellerId = -1;
								Iterator<Integer> sellerIds = sellerSet.iterator();
								MessageDAO messageData = new MessageDAO();
								while(sellerIds.hasNext())
								{
									sellerId = sellerIds.next();
									messageData.sendMessage(sellerId, Admin.Admin.value, message);
								}
								messageData.connection.DB_Close();
								
								//Load Orders
								Orders orders = new Orders();
								orders = data.getBuyerOrder(user.getUserId() );
								
								//Create and load new empty shopping Cart.
								AuthDAO dataAuth = new AuthDAO();
								dataAuth.createList(userId, ListType.shoppingCart.value, null);
								List shoppingCart = data.getShoppingCart(userId);
								session.setAttribute("shoppingCart", shoppingCart);
								session.setAttribute("orders", orders);
								dataAuth.connection.DB_Close();
								data.connection.DB_Close();
								
								RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
								dispatcher.forward(request,  response);
						}
						else{
							msg = "Order Creation Failed.";
						}
					}
				 catch (SQLException e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				} catch (Throwable e) {
					msg = "SQL Exception " + e.toString();
					e.printStackTrace();
				}
			
				//If issues with form display message
				if(!msg.equals("")){
					request.setAttribute("msg", msg);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/order.jsp");
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
