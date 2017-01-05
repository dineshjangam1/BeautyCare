<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Account</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="enums.userType" %>
<%@ page import="model.Address" %>
<%@ page import="model.Users" %>
<%@ page import= "java.util.Vector" %>
<%@ page import = "java.util.ListIterator" %>
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
	out.print("<p>Hello " + user.getFirstName() + " " + user.getLastName() + " </p>");
	
	//If not Admin
	if(user.getUserType() != userType.admin.value)
	{
		out.print(
		  		  "<form name='AccountForm' action='AccountServlet' method='post'><table><tr><td>UserName:</td><td><input type='text' name='username' disabled maxlength='20' size='20'  value= '"+ user.getUserName() +"' /></td></tr><tr><td>First Name:</td><td><input type='text' name='firstName' maxlength='20' size='20' value= '"+ user.getFirstName() +"' /></td></tr><tr><td>Last Name:</td><td><input type='text' name='lastName' maxlength='20' size='20' value= '"+ user.getLastName() +"' /></td></tr><tr><td>Phone Number:</td><td><input type='text' name='phoneNumber' maxlength='20' size='20' value= '"+ user.getPhoneNumber() +"' /></td></tr><tr><td>Email:</td><td><input type='text' name='email' maxlength='20' size='20' value= '"+ user.getEmail() +"' /></td></tr><tr><td>Street:</td><td><input type='text' name='street' size='20' maxlength='20' value= '"+ user.getMailingAddress().getStreet() +"' /></td></tr><tr><td>City:</td><td><input type='text' name='city' size='20' maxlength='20' value= '"+ user.getMailingAddress().city +"' /></td></tr><tr><td>State:</td><td><input type='text' name='state' maxlength='20' size='20' value= '"+ user.getMailingAddress().getState() +"' /></td></tr><tr><td>Zip:</td><td><input type='text' name='zip' size='20'  maxlength='20' value= '"+ user.getMailingAddress().getZip() +"' /></td></tr><tr><td>Password:</td><td><input type='password' name='password' size='20' maxlength='20' value='"+user.getPassword()+"'/></td></tr>");
		
		if(user.getUserType() == userType.seller.value)
		{
			out.print("<tr><td>Routing Number:</td><td><input type='text' name='routingNumber'  maxlength='9' size='20'  value= '"+ user.getRoutingNumber() +"' /></td></tr>");
			out.print("<tr><td>Account Number:</td><td><input type='text' name='accountNumber'  maxlength='9' size='20'  value= '"+ user.getAccountNumber() +"' /></td></tr>");
			out.print("<tr><td>URL:</td><td><input type='text' name='url'  maxlength='20' size='20'  value= '"+ user.getUrl() +"' /></td></tr>");
			out.print("<tr><td>Company Name:</td><td><input type='text' name='companyName'  maxlength='20' size='20'  value= '"+ user.getCompanyName() +"' /></td></tr>");
		}

		out.print("<tr><td><input type='submit' name='updateAccount' value='Update' /><input type='submit' name='removeUser' value='Delete Account' /></td></tr>");
		
		if(user.getUserType() == userType.buyer.value)
		{
			out.print("<tr><td><input type='submit' name='requestSellerAccount' value='Request Seller Account' /></td></tr>");
		}
		
		out.print("</table></form>");
		
	}
	else{
		Users users = (Users)session.getAttribute("users");
		if(users == null)
		{
			out.print("You have reached this page in error");
		}
		else{
			//User is Admin

			
			
			out.print("<table border='line'>");
			out.print("<tr><td>ID</td><td>First Name</td><td>Last Name</td><td>Type</td></tr>");
			
			ListIterator<User> userIterator = users.getUsers();
			User userInfo = null;
			
			while(userIterator.hasNext())
			{
				userInfo = userIterator.next();
				if(userInfo.getUserType() != userType.admin.value)
				{
					out.print("<form name='AccountsForm' action='AccountServlet' method='post'>");
					out.print("<tr>");
					out.print("<td> " + userInfo.getUserId() + "</td>" );
					out.print("<td> " + userInfo.getFirstName() + "</td>" );
					out.print("<td> " + userInfo.getLastName() + "</td>" );
					out.print("<td> " + userInfo.getUserTypeString() + "</td>" );
					out.print("<input type='hidden' name='userIdHidden' value='"+userInfo.getUserId()+"' />");
					out.print("<input type='hidden' name='userNameHidden' value='"+userInfo.getFirstName() + " "+ userInfo.getLastName() +"' />");
					out.print("<td><input type='submit' name='removeUserAdmin' value='Delete' />");
					out.print("<input type='submit' name='messageAdmin' value='Message' />");
					if(userInfo.userType == userType.buyer.value)
					{
						out.print("<input type='submit' name='approveSeller' value='Approve Seller' />");
						out.print("<input type='submit' name='denySeller' value='Deny Seller' />");
					}
					out.print("</td></tr></form>");
				}
			}
			
			out.print("</table>");
		}
	}
}
%>
</body>
</html>