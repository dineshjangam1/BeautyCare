package model;


public class Order {

  public Integer buyerId;

  public String receiverName;

  public double tax;

  public double totalPrice;

  public String time;

  public Integer orderId;

  public Integer shippingStatus;

  public String payPalAccount;

  public Address shippingAddress;
  
  public List productList;

public String getShippedString(int value)
	{
		String shipped = "";
		
		if(value == 0)
		{
			shipped = "Not Shipped";
		}
		else if(value == 1)
		{
			shipped = "Shipped";
		}
		
		return shipped;
		
	}
  
  public Order() {
	  this.buyerId = -1;
	  this.receiverName = "";
	  this.tax = 0.0;
	  this.totalPrice = 0.0;
	  this.time = "";
	  this.orderId = -1;
	  this.shippingStatus = 0;
	  this.payPalAccount = "";
	  this.shippingAddress = new Address();
	  this.productList = new List();
  }
  
  public Order( String receiverName, double tax, double totalPrice, String time, int orderId, int shippingStatus, Address shipping, List productList, int buyerId) {
	  this.buyerId =buyerId;
	  this.receiverName = receiverName;
	  this.tax = tax;
	  this.totalPrice = totalPrice;
	  this.time = time;
	  this.orderId = orderId;
	  this.shippingStatus = shippingStatus;
	  this.payPalAccount = "";
	  this.shippingAddress = shipping;
	  this.productList = productList;
  }

  public int getBuyerId() {
	  return buyerId;
  }

  public String getReceiverName() {
	  return receiverName;
  }

  public double getTax() {
	  return tax;
  }

  public double getTotalPrice() {
	  return totalPrice;
  }

  public String getTime() {
	  return time;
  }

  public int getOrderId() {
	  return orderId;
  }

  public int getShippingStatus() {
	  return shippingStatus;
  }

  public String getPayPalAccount() {
	  return payPalAccount;
  }
  
  public Address getShippingAddress() {
	  return shippingAddress;
  }

}