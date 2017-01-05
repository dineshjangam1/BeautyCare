<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Page</title>
<link href="style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<% 


User user = (User)session.getAttribute("user");
boolean loggedIn = false;
if(user != null)
{
	loggedIn = user.isLoggedIn();
}

//If not logged in display any messages with Login form.
if (!loggedIn)
		{
			out.print("<table><tr><td>");

	      	out.print(
	    		  "<form class='box' name='LoginForm' action='login' method='post'><table><tr><td><table><tr><td>Username:</td><td align='right'> <input type='text' name='username' size='20' /></td></tr><tr><td>Password:</td><td align='right'> <input type='password' name='password' size='20' /></td></tr></table></td></tr><tr><td align='center'><input type='submit' value='Login' /></td></tr></table></form>");
	      	
	      	out.print("</td><td>");
	      
			String msg = (String)request.getAttribute("msg");

			if (msg == null)
			{
				msg = "";
			}
			if(msg != "")
			{
				out.print("<div id='message'> " + msg + "</div>");
			}
			out.print("</td></tr></table>");
		}
//If Logged in do not display Login Form.  Print message user is logged in.
else{

	out.print("User: " + user.getFirstName() + " " + user.getLastName() + " is already logged in");
}
%>
	
</body>
</html>