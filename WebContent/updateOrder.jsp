<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Update Order</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="model.Orders" %>
<%@ page import="model.Order" %>
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
	String orderIdString = (String)session.getAttribute("orderId");
	Orders orders = (Orders)session.getAttribute("orders");
	if(orderIdString == null || orders == null)
	{
		out.print("You have reached this page in error");
	}
	else{
		int orderId = Integer.parseInt(orderIdString);
		Order order = orders.getOrderById(orderId);
		session.setAttribute("order", order);
		
		out.print("<br /><form  name='UpdateOrderForm' action='PurchaseServlet' method='post'><table><tr><td><table><tr><td>Receiver Name:</td><td align='right'> <input type='text' name='receiverName' size='20' maxlength='20' value='"+order.getReceiverName()+"' /></td></tr><tr><td>Street:</td><td align='right'> <input type='text' name='street' size='20' maxlength='20' value='"+order.getShippingAddress().getStreet()+"' /></td></tr><tr><td>City:</td><td align='right'> <input type='text' name='city' size='20' maxlength='20' value='"+order.getShippingAddress().getCity()+"'/></td></tr><tr><td>State:</td><td align='right'> <input type='text' name='state' size='20' maxlength='20' value='"+order.getShippingAddress().getState()+"' /></td></tr><tr><td>Zip:</td><td align='right'> <input type='text' name='zip' size='20' maxlength='20' value='"+order.getShippingAddress().getZip()+"'/></td></tr></table></td></tr><tr><td align='center'><input type='submit' name='updateOrderInfo' value='Update' /></td></tr></table></form>");	
	}
}
%>
</body>
</html>