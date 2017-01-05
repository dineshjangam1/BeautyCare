<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Purchase Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.List" %>
<%@ page import="model.Catalog" %>
<%@ page import="model.User" %>
<% 
User user = (User)session.getAttribute("user");
List shoppingList = (List)session.getAttribute("shoppingCart");
Catalog catalog = (Catalog)session.getAttribute("catalog");
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
	double subTotal = shoppingList.getListItemsTotalCost(catalog);
	double tax = catalog.getTaxRate() * subTotal;
	double total = subTotal + tax;
	
	session.setAttribute("subTotal", subTotal);
	session.setAttribute("tax", tax);
	session.setAttribute("total", total);
	
	out.print("Subtotal: " + catalog.getDecimalString(subTotal));
	out.print("<br />Tax: " + catalog.getDecimalString(tax));
	out.print("<br />Total: " + catalog.getDecimalString(total));
	
	out.print("<br /><form  name='PurchaseForm' action='PurchaseServlet' method='post'><table><tr><td><table><tr><td>Receiver Name:</td><td align='right'> <input type='text' name='receiverName' size='20' maxlength='20' /></td></tr><tr><td>Street:</td><td align='right'> <input type='text' name='street' size='20' maxlength='20' /></td></tr><tr><td>City:</td><td align='right'> <input type='text' name='city' size='20' maxlength='20' /></td></tr><tr><td>State:</td><td align='right'> <input type='text' name='state' size='20' maxlength='20' /></td></tr><tr><td>Zip:</td><td align='right'> <input type='text' name='zip' size='20' maxlength='20' /></td></tr><tr><td>PayPal Account:</td><td align='right'> <input type='text' name='payPalAccount' size='20' maxlength='20' /></td></tr></table></td></tr><tr><td align='center'><input type='submit' name='purchase' value='Purchase' /></td></tr></table></form>");
}
%>
</body>
</html>