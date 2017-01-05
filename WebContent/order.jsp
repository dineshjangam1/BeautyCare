<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Order Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="model.List" %>
<%@ page import="model.ListItem" %>
<%@ page import="model.Address" %>
<%@ page import="model.Order" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Catalog" %>
<%@ page import="enums.userType" %>
<%@ page import="enums.Shipped" %>
<%@ page import= "java.util.Vector" %>
<%@ page import = "java.util.ListIterator" %>



<% 

User user = (User)session.getAttribute("user");
boolean loggedIn = false;
if(user != null)
{
	loggedIn = user.isLoggedIn();
}

String msg = (String)request.getAttribute("msg");

if (msg == null)
{
	msg = "";
}
if(msg != "")
{
	out.print("<div id='message'>");
	out.print(msg);
	out.print("</div>");
}


if (!loggedIn)
		{
			
	      out.print("You are not logged in.  Please go to login.jsp to log in.");
		}
else{
	out.print("<p>Hello " + user.getFirstName() + " " + user.getLastName() + "</p>");

		
		Orders orders = (Orders)session.getAttribute("orders");
		if(orders.orders != null)
		{
			Catalog catalog = (Catalog)session.getAttribute("catalog");
			Order order = new Order();
			Address shippingAddress = new Address();
			
			ListIterator<Order> orderIterator = orders.getOrders();
			out.print("<table border='line'><tr><td>Order ID</td><td>Receiver</td><td>Tax</td><td>Total</td><td>Time</td><td>Shipped</td><td>Street</td><td>City</td><td>State</td><td>Zip</td></tr>");
			
			while(orderIterator.hasNext())
			{
				order = orderIterator.next();
				//Print table headings
				shippingAddress = order.getShippingAddress();
				out.print("<tr><td>"+order.getOrderId()+"</td><td>" + order.getReceiverName() + "</td><td>" + catalog.getDecimalString(order.getTax()) + "</td><td>" + catalog.getDecimalString(order.getTotalPrice()) + "</td><td>" + order.getTime()+ "</td><td>" + order.getShippedString(order.getShippingStatus()) + "</td><td>" + shippingAddress.getStreet() + "</td><td>" + shippingAddress.getCity() + "</td><td>" + shippingAddress.getState() + "</td><td>" + shippingAddress.getZip() + "</td>");
				
				if(user.getUserType() == userType.buyer.value)
				{
					if(order.getShippingStatus() == Shipped.Packing.value)
					{
						//If order has not shipped display update order button.
						out.print("<td>");
						out.print("<form name='UpdateOrderForm' action='PurchaseServlet' method='post'>");
						out.print("<input type='hidden' name='orderId' value= '"+ order.getOrderId() +"' />");
						out.print("<input type='submit' name='updateOrder' value='Update Order' />");
						out.print("<input type='submit' name='deleteOrder' value='Delete Order' /></form>");
						out.print("</td>");
					}
				}
				
				if(user.getUserType() == userType.seller.value)
				{
					out.print("<td>");
					if(order.getShippingStatus() == Shipped.Packing.value)
					{
						//If order has not shipped display update order button.
						out.print("<form name='UpdateOrderForm' action='PurchaseServlet' method='post'>");
						out.print("<input type='hidden' name='orderId' value= "+ order.getOrderId() +" />");
						out.print("<input type='submit' name='shipOrder' value='Ship Order' />");
					}
					out.print("<input type='hidden' name='buyerId' value='"+order.getBuyerId()+"'>");
					out.print("<input type='submit' name='contactBuyer' value='Contact Buyer' /></form>");
					out.print("</td>");
				}
				
				if(user.getUserType() == userType.admin.value)
				{
					out.print("<td>");
					out.print("<form name='UpdateOrderForm' action='PurchaseServlet' method='post'>");
					out.print("<input type='hidden' name='orderId' value= "+ order.getOrderId() +" />");
					out.print("<input type='submit' name='updateOrder' value='Update Order' />");
					out.print("<input type='submit' name='deleteOrder' value='Delete Order' /></form>");
					out.print("</td>");
				}
				
				out.print("</tr>");
				
				
			}
			
			out.print("</table>");
		}
		//Else user does not have any orders
		else{
			out.print("No orders to display");
		}
		
	
}
%>
</body>
</html>