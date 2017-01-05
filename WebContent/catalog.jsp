<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Catalog Page</title>
</head>
<body>
<%@ include file="nav.jsp" %>
<%@ page import="model.User" %>
<%@ page import="model.Catalog" %>
<%@ page import="model.Product" %>
<%@ page import="model.ReviewsRanking" %>
<%@ page import="model.ProductSeller" %>
<%@ page import="model.Image" %>
<%@ page import= "java.util.Vector" %>
<%@ page import = "java.util.ListIterator" %>
<%@ page import = "enums.Department" %>
<%@ page import = "enums.userType" %>
<% 

User user = (User)session.getAttribute("user");
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
	
	String filterDepartment = (String)session.getAttribute("departmentFilter");
	String catalogFilter = (String)session.getAttribute("catalogTextFilter");
	
	out.print("<center><form class='boxSignup' name='FilterForm' action='CatalogServlet' method='post'>Filter Text: <input type='text' id='filterText' name='filterText' size='20' value='"+ catalogFilter + "' /><select id='departmentDropDown' name='departmentDropDown'><option value=''>Select Department</option></center>");
	
	Vector<String> departments = catalog.getDepartments();
	ListIterator<String> departmentsIterator = departments.listIterator();
	String departmentName = null;
	while(departmentsIterator.hasNext())
	{
		departmentName = departmentsIterator.next();
		//Option is Selected
		if(filterDepartment.contains(departmentName) )
		{
			out.print("<option value='" + departmentName + "' selected>"+departmentName+"</option>");
		}
		else{
			out.print("<option value='" + departmentName + "'>"+departmentName+"</option>");
		}
	}
	//Finish Drop Down filter
	out.print("<input type='submit' name='filterCatalog' value='Filter Catalog' /></form>");
	
	out.print("<p><center>Hello " + user.getFirstName() + " " + user.getLastName() + " welcome to Cosmetics Catalog</center></p>");
	
	
	ListIterator<Product> products = catalog.getProducts();
	
	String department = null;
	
	//Number of products per row.
	int productPerRow = 4;
	int count = 0;
	//Start Table
	out.print("<table align='center' border='line'><row>");
	while(products.hasNext())
	{
		Product product = products.next();	
		
		//If product is not deleted.
		if(product.getIsDeleted() == 0)
		{
			department = catalog.getDepartmentByValue(product.getDepartment());
			
			//Filter. department is chosen and product name or description contains text. display product. 
			if((department.indexOf(filterDepartment) != -1) && ((product.name.indexOf(catalogFilter) != -1)    || (product.description.indexOf(catalogFilter) != -1)))
			{
	
				//Create new Row, reset count.
				if(count > 4)
				{
					out.print("</row><row>");
					count = 0;
				}
				count++;
				
				//Start new cell
				out.print("<td valign='top'>");
				
				//Display Products
				
		
				out.print("<p>Product Name: " + product.getName() + "</br> Product Description: " + product.getDescription() + "</br>Product Department: " + department + "</p>");
				
				//Display images
				ListIterator<Image> images = product.getImages();
				while(images.hasNext())
				{
					Image image = images.next();
					out.print("<img src='Images/"+ image.location + "' height='50' width='50'>");
				}
				
				//Edit product Link for Sellers and Admins
				if(user.getUserType() != userType.buyer.value){
						out.print(
					    		  "<form name='editProduct' action='CatalogServlet' method='post'><input type='hidden' id='productId' name='productId' value='"+ product.getProductId() + "' /><input type='submit' value='Edit Product' name='editProduct' /></form>");
				}
				//user is buyer and print rating button.
				else
				{
					out.print(
				    		  "<form name='rateProduct' action='CatalogServlet' method='post'><input type='hidden' id='productId' name='productId' value='"+ product.getProductId() + "' /><input type='submit' value='Rate Product' name='rateProduct' /></form>");
			
				}
				//Display sellers
				ListIterator<ProductSeller> sellers = product.getSellers();
				while(sellers.hasNext())
				{
					ProductSeller seller = sellers.next();
					out.print("<p>Seller Id: " + seller.getSellerCompanyName() +"</p>");
					out.print("<p>Price: " + catalog.getDecimalString( seller.getPrice()) +"</p>");
					out.print("<p>Shipping: " + catalog.getDecimalString( seller.getShippingCost()) +"</p>");
					if(user.getUserType() == userType.buyer.value)
					{
						out.print(
					    		  "<form name='addToShoppingCart'  action='CatalogServlet' method='post'><input type ='hidden' name='productSellerId' value='" + seller.getProductSellerId() + "'><input type='submit' name='addToShoppingCart' value='Add to Shopping Cart' /></form>");
						out.print(
					    		  "<form name='addToWishList' action='CatalogServlet' method='post'><input type ='hidden' name='productSellerId' value='" + seller.getProductSellerId() + "'><input type='submit' name='addToWishList' value='Add to Wish List' /></form>");
						out.print(
					    		  "<form name='ContactSellerForm'  action='CatalogServlet' method='post'><input type='hidden' name='sellerId' value='"+seller.getSellerId()+"'><input type='submit' name='messageSeller' value='Message Seller' /></form>");
					        	
					}
				}
				
				//Display Reviews and Rankings
				ListIterator<ReviewsRanking> reviewsRankings = product.getReviewsRanking();
				while(reviewsRankings.hasNext())
				{
					ReviewsRanking reviewsRanking = reviewsRankings.next();
					out.print("<p>Ranking : " + reviewsRanking.getRanking() +"</p>");
					out.print("<p>Review : " + reviewsRanking.getReview() +"</p>");	
				}
				
				//End cell
				out.print("</td>");
			}
		}
		
	}
	//End Catalog Table
	out.print("<table><row>");
	
	
	
}
%>
</body>
</html>