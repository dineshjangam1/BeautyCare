<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Shopping Cart</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="model.List" %>
<%@ page import="model.Product" %>
<%@ page import="model.ProductSeller" %>
<%@ page import="model.ListItem" %>
<%@ page import="model.Catalog" %>
<%@ page import= "java.util.Vector" %>
<%@ page import = "java.util.ListIterator" %>
<% 

User user = (User)session.getAttribute("user");
List shoppingCart = (List)session.getAttribute("shoppingCart");
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
	
	//If not buyer, do not attempt to display shopping cart.
	if(user.userType != userType.buyer.value)
	{
		out.print("<p>You have come to this page in error. </p>");
	}
	//User Type is buyer.  Display shopping Cart.
	else
	{
		//start table.
		out.print("<table border='line'><tr><td>Quantity</td><td>Name</td><td>Description</td><td>Price</td><td>Shipping</td><td>Remove</td></tr>");
		
		//Print Items in Shopping Cart.  Along with remove button.
		ListIterator<ListItem> items = shoppingCart.getListItems();
		ListItem item = new ListItem();
		ProductSeller seller = new ProductSeller();
		Product product = new Product();
		
		while(items.hasNext())
		{
			item = items.next();
			product = catalog.getProductById(item.getProductId());
			seller = product.getProductSellerById(item.getSellerId());
			
			//Print item information.
			out.print("<tr><td>" + item.getQuantity() + "</td><td>" + product.getName() + "</td><td>" + product.getDescription() + "</td><td>" + catalog.getDecimalString( seller.getPrice()) + "</td><td>" +  catalog.getDecimalString( seller.getShippingCost()) + "</td><td><form name='ShoppingCartForm' action='ShoppingCartServlet' method='post'><input type ='hidden' name='listItemId' value='" + item.getListItemId() + "'><input type='submit' name='removeFromShoppingcart' value='Remove' /></form></td></tr>");
			
		}
		
		//End Table
		out.print("</table>");
		
		out.print("<form name='SubmitShoppingCartForm' action='ShoppingCartServlet' method='post'><input type ='hidden' name='listItemId' value='" + item.getListItemId() + "'><input type='submit' name='submitShoppingCart' value='Purchase' /></form>");
	}
}
%>
</body>
</html>