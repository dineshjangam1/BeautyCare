package model;

import java.util.ListIterator;
import java.util.Vector;
import java.text.*;
import enums.Department;

public class Catalog {

    public Vector<Product>  products;
    public double taxRate = 0.08;


  public void catalog() {
	  //this.products = new Vector<Product>();
  }
  
  public ListIterator<Product> getProducts()
  {
	  return products.listIterator();
  }
  
  public double getTaxRate()
  {
	  return taxRate;
  }
  
  
  //Returns product if found else null.
  public Product getProductById(int productId)
  {
	  Product product = null;
	  Product tempProduct = null;
	  ListIterator<Product> productIterator = getProducts();
	  while(productIterator.hasNext())
	  {
		  
		  //go to next product
		  tempProduct = productIterator.next();
		  //if product found break out of while loop
		  if(tempProduct.getProductId() == productId)
		  {
			  product = tempProduct;
			  tempProduct = null;
			  break;
		  }
		  
	  }
	  tempProduct = null;
	  return product;
  }
  
  //Return vector of departments in order of integer value.
  public Vector<String> getDepartments()
  {
		Vector<String> departments = new Vector<String>();
		Department[] departmentValues = Department.values();
		int size = departmentValues.length;
		for(int i =0;i<size;i++)
		{
			departments.add(departmentValues[i].name());
		}

		return departments;
  }
  
  //Returns department name based on value.
  public String getDepartmentByValue(int departmentId)
  {
	  String departmentValue = null;
		
	  Department[] departmentValues = Department.values();
	  departmentValue = departmentValues[departmentId].name();
		
	  return departmentValue;
  }
  
  //Returns department name based on value.
  public String getDecimalString(double price)
  {
	  NumberFormat formatter = new DecimalFormat("####.00");
	  String answer = formatter.format(price);
	  
	  return answer;
  }

}