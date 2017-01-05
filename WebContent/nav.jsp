<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link href="style.css" rel="stylesheet" type="text/css" />
<div id='nav'>
<%@ page import="model.User" %>
<%@ page import="enums.userType" %>
<%
ServletContext sc = getServletContext();
String path = sc.getContextPath();

User userNav = (User)session.getAttribute("user");

//Begin table navigation
out.write("<form name='navForm' action='NavServlet' method='post'><table><tr>");

boolean loggedInNav = false;
if(userNav != null)
{
	loggedInNav = userNav.isLoggedIn();
}

//Ouput not logged in navigation
if(!loggedInNav)
{
	out.write("<td><input type='submit' name='login' value='Login' /></td>");
	out.write("<td><input type='submit' name='signUp' value='Sign Up' /></td>");
}
else 
{
	
	if(userNav.getUserType() == userType.buyer.value)
	{
		out.write("<td><input type='submit' name='shoppingCart' value='Shopping Cart' /></td>");
		out.write("<td><input type='submit' name='wishList' value='Wish List' /></td>");
	}
	
	if(userNav.getUserType() != userType.admin.value)
	{
		out.write("<td><input type='submit' name='myAccount' value='My Account' /></td>");
		out.write("<td><input type='submit' name='messageAdmin' value='Message Admin' /></td>");
	}
	else
	{
		out.write("<td><input type='submit' name='accounts' value='Accounts' /></td>");	
	}
	
	//Navigation for all logged in users.
	out.write("<td><input type='submit' name='catalog' value='Catalog' /></td>");
	out.write("<td><input type='submit' name='messages' value='Messages' /></td>");
	out.write("<td><input type='submit' name='orders' value='Orders' /></td>");
	out.write("<td><input type='submit' name='logout' value='Logout' /></td>");
	
}


//End Table Navigation
out.write("</tr></table></form>");


%>

</div>