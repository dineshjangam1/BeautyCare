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
Messages messages = (Messages)session.getAttribute("messages");
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
	out.print("<p>Hello " + user.getFirstName() + " " + user.getLastName() + " welcome to your messages!</p>");
	
	ListIterator<Message> messagesIterator = messages.getMessages();
	
	//Start Table
	out.print("<table width='1000' align='center' border='line'><tr><td align='center' width='200'>From</td><td align='center' width='800'>Message</td></tr>");

	while(messagesIterator.hasNext())
	{
		Message message = messagesIterator.next();	
			
			//Display Messages
			
			out.print("<tr><td>" + message.getSenderName() + "</td><td>" + message.getMessage() + "</td></tr>");	
	}
	
	//End Table
	out.print("</table>");
}
%>
</body>
</html>