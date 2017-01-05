package model;


public class ListItem {

  public Integer quantity;

  public double price;
  
  public double shippingPrice;

  public Integer sellerId;

  public Integer productId;

  public Integer listId;

  public Integer listItemId;


  public int getQuantity() {
	  return quantity;
  }

  public double getPrice() {
	  return price;
  }
  
  public double getShippingPrice() {
	  return shippingPrice;
  }

  public int getSellerId() {
	  return sellerId;
  }

  public int getProductId() {
	  return productId;
  }

  public  ListItem() {
	  this.quantity = -1;
	  this.price = -1;
	  this.sellerId = -1;
	  this.productId = -1;
	  this.listId = -1;
	  this.listItemId = -1;
  }
  
  public  ListItem(int listItemId, int listId, int productId, double price, int quantity, int sellerId) {
	  this.quantity = quantity;
	  this.price = price;
	  this.sellerId = sellerId;
	  this.productId = productId;
	  this.listId = listId;
	  this.listItemId = listItemId;
  }

  public int getListId() {
	  return listId;
  }

  public int getListItemId() {
	  return listItemId;
  }

}