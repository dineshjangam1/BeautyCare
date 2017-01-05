<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign up</title>
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

String msg = (String)request.getAttribute("msg");

if (!loggedIn)
		{
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
	out.print(
  		  "<form class='boxSignup' name='SignupForm' action='SignupServlet' method='post'><table><tr><td>Username:</td><td><input type='text' name='username' size='20' maxlength='20' /></td></tr><tr><td><input type='submit' name='checkUsername' value='Check Username' /></td></tr><tr><td>First Name:</td><td><input type='text' name='firstName' size='20' maxlength='20' /></td></tr><tr><td>Last Name:</td><td><input type='text' name='lastName' size='20' maxlength='20' /></td></tr><tr><td>Phone Number:</td><td><input type='text' name='phoneNumber' size='20' maxlength='20' /></td></tr><tr><td>Email:</td><td><input type='text' name='email' size='20' maxlength='20' /></td></tr><tr><td>Street:</td><td><input type='text' name='street' size='20' maxlength='20'/></td></tr><tr><td>City:</td><td><input type='text' name='city' size='20' maxlength='20' /></td></tr><tr><td>State:</td><td><input type='text' name='state' size='20' maxlength='20'/></td></tr><tr><td>Zip:</td><td><input type='text' name='zip' size='20' maxlength='20'/></td></tr><tr><td>Password:</td><td><input type='password' name='password' size='20' maxlength='20'/></td></tr><tr><td>Confirm Password:</td><td> <input type='password' name='confirmPassword' size='20' maxlength='20'/></td></tr><tr><td><input type='submit' value='Signup' /></td></tr></table></form>");
		}
else{
	out.print("You are already a member.");
}
%>
</body>
</html>