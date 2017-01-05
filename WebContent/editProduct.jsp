<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit Product</title>
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
String productId = (String)session.getAttribute("productId");

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

else if(productId == null)
{
	out.print("You have reached this page in error.");
}
//User is logged in.
else{
	//Check to ensure user is not buyer
	if(user.getUserType() == userType.buyer.value)
	{
		out.print("<p>Hello " + user.getFirstName() + " " + user.getLastName() + ".  Only Administrators and sellers are allowed here!</p>");
	}
	//user is seller or an administrator
	else{
		Catalog catalog = (Catalog)session.getAttribute("catalog");
		Product product = catalog.getProductById(Integer.parseInt(productId));
		
		//If product not found.
		if(null == product)
		{
			out.print("Woops!  Product not found.");
		}
		else{
		String department = null;
			

			
				//Display Product
			out.print(
			  		  "<form class='editProduct' name='editProductForm' action='EditProductServlet' method='post'><table width='200'><tr><td>Product Name:</td><td><input type='text' name='productName' size='20' maxlength='20' value='" + product.getName() + "' /></td></tr><tr><td>Product Description:</td><td><input type='text' name='productDescription' size='45' maxlength='45' lenght='45' width = '200px' value='"+product.getDescription()+"'/></td></tr><tr><td>Product Department:</td><td><select id='departmentDropDown' name='departmentDropDown' >");
			  		  
				//Department DropDown
				Vector<String> departments = catalog.getDepartments();
				ListIterator<String> departmentsIterator = departments.listIterator();
				String departmentName = null;
				int i = 0;
				while(departmentsIterator.hasNext())
				{
					departmentName = departmentsIterator.next();
					//Option is Selected
					if(catalog.getDepartmentByValue(product.getDepartment()) == departmentName)
					{
						out.print("<option value='" + i + "' selected>"+departmentName+"</option>");
					}
					else{
						out.print("<option value='" + i + "'>"+departmentName+"</option>");
					}
					i++;
				}
				//Finish Drop Down filter
				out.print("</td></tr><tr><td><input type='submit' name='saveProduct' value='Save Product' /></td><td><input type='submit' name='deleteProduct' value='Delete Product' /></td></tr></table></form>");
				
				//If Seller.  Display option to update Seller Fields.
				if(user.getUserType() == userType.seller.value)
				{
					ProductSeller seller = product.getProductSellerBySellerId(user.getUserId());
					//If seller already sells product pre-populate fields.
					if(seller != null)
					{
						out.print("<form class='editSellerInfo' name='editSellerInfoForm' action='EditProductServlet' method='post'><table><tr><td>Price:</td><td><input type='text' name='productPrice' size='20' maxlength='20' value='" + seller.getPrice() + "' /></td></tr><tr><td>Shipping Cost:</td><td><input type='text' name='productShippingCost' size='20' maxlength='20' value='"+ seller.getShippingCost() +"'/></td></tr><tr><td><input type='submit' name='saveSellingInformation' value='Save Selling Information' /></td></tr></table></form>");	
					}
					else{out.print("<form class='editSellerInfo' name='editSellerInfoForm' action='EditProductServlet' method='post'><table><tr><td>Price:</td><td><input type='text' name='productPrice' size='20' maxlength='20' value='' /></td></tr><tr><td>Shipping Cost:</td><td><input type='text' name='productShippingCost' size='20' maxlength='20' value=''/></td></tr><tr><td><input type='submit' name='saveSellingInformation' value='Save Selling Information' /></td></tr></table></form>");	
					}
				}
				
			}
		}	
}
%>
</body>
</html>