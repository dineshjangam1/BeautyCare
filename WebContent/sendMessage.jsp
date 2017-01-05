<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Messages Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="model.Message" %>
<%@ page import="model.Messages" %>
<%@ page import= "java.util.Vector" %>
<%@ page import = "java.util.ListIterator" %>
<% 

User user = (User)session.getAttribute("user");
String receiverIdString = (String)session.getAttribute("receiverId");
int receiverId = Integer.parseInt(receiverIdString);
String receiverName = (String)session.getAttribute("receiverName");
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
			
	      out.print("<center>You are not logged in.  Please go to login.jsp to log in.</center>");
		}
else{
	out.print("<p>Hello " + user.getFirstName() + " " + user.getLastName() + " welcome!</p>");
	
	if(receiverId < 1)
	{
		out.print("<center>You have reached this page in error.  No receiver id specified.</center>");
	}
	else{
		if(receiverName != null)
		{
			out.print("<center>Sending message to " + receiverName + ".<center>");
		}
		
		out.print(
	    		  "<form name='MessageForm' action='MessagesServlet' method='post'><table><tr><td><table><tr><td>Message:</td><td align='right'> <input type='text' name='messageText' size='100' maxlength='60' /></td></tr></table></td></tr><tr><td align='center'><input type='submit' name='sendMessage' value='Send' /></td></tr></table></form>");
	      	
	}
}
%>
</body>
</html>