package model;

import java.util.ListIterator;
import java.util.Vector;

public class Orders {

    /**
   * 
   * @element-type Order
   */
  public Vector<Order>  orders;

  public void orders() {
  }
  
  public ListIterator<Order> getOrders()
  {
	  return orders.listIterator();
  }
  
  //returns order or null if not found.
  public Order getOrderById(int orderId)
  {
	  Order order = null;
	  Order temp = null;
	  
	  ListIterator<Order> orders = this.getOrders();
	  while(orders.hasNext())
	  {
		  temp = orders.next();
		  if(temp.getOrderId() == orderId)
		  {
			  order = temp;
			  break;
		  }
	  }
	  
	  
	  return order;
  }

}