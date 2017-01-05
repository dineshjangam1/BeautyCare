<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Review Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>

<% 

User user = (User)session.getAttribute("user");
String productIdString = (String)session.getAttribute("productId");


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
	
	if(productIdString == null)
	{
		out.print("<center>You have reached this page in error.  No product id specified.</center>");
	}
	else{
		int productId = Integer.parseInt(productIdString);
		out.print("<center><form  name='ReviewForm' action='ReviewServlet' method='post'>Review: <input type='text' id='reviewText' name='reviewText' size='20' maxlength='45' /></td></tr><tr><td>Rating: <select id='ratingDropDown' name='ratingDropDown'></center>");
		
		int maxRating = 10;
		for(int i=0;i<maxRating;i++)
		{

				out.print("<option value='" + (i+1) + "'>"+(i+1)+"</option>");
			
		}
		//Finish Drop Down filter
		out.print("</td><input type='submit' name='submitReview' value='Submit Review' /></form>");
	}
}
%>
</body>
</html>