<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Index Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="enums.userType" %>
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
	if(user.getUserType() != userType.buyer.value)
	{
		out.print("You have reached this page in error.");
	}
	else{
		out.print("<form name='RequestSeller' action='AccountServlet' method='post'><table>");
		
		out.print("<tr><td>Routing Number:</td><td><input type='text' name='routingNumber'  maxlength='9' size='20'  /></td></tr>");
		out.print("<tr><td>Account Number:</td><td><input type='text' name='accountNumber'  maxlength='9' size='20'  /></td></tr>");
		out.print("<tr><td>URL:</td><td><input type='text' name='url'  maxlength='20' size='20'  /></td></tr>");
		out.print("<tr><td>Company Name:</td><td><input type='text' name='companyName'  maxlength='20' size='20' /></td></tr>");
		out.print("<tr><td><input type='submit' name='requestSellerAccountClick' value='Request Seller Account' /></td></tr>");
		out.print("</table></form>");
	}
}
%>
</body>
</html>