package dao;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Address;
import model.List;
import model.ListItem;
import model.Order;
import model.Orders;
import model.Product;
import model.Catalog;
import model.ReviewsRanking;
import model.ProductSeller;
import model.Image;
import enums.Shipped;

import java.util.Vector;
import java.util.ListIterator;

import enums.ListType;

public class CatalogDAO {

    public ConnectionInfo connection;

  public CatalogDAO()  {
	  		  
  }
  
  public Catalog getCatalog() throws SQLException
  {
	  	Catalog catalog = new Catalog();
		//create connection
		connection = new ConnectionInfo();
		
	  	catalog.products = new Vector<Product>();
		//create query
		String sql = "SELECT productID, name, description, department, isDeleted FROM product";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		connection.executeQuery();

		try {
			while(connection.result.next()) 
			{
				Product product = new Product(connection.result.getInt(1),connection.result.getString(2),connection.result.getString(3),connection.result.getInt(4), connection.result.getInt(5));
				catalog.products.add(product);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
		
		//Load images/sellers/Reviews Rankings for items
		ListIterator<Product> productsIterator = catalog.products.listIterator();
		while(productsIterator.hasNext())
		{
			
			Product product = productsIterator.next();
			
			addImagesToProduct(product);
			
			addSellersToProduct(product);
			
			addRankingsReviewsToProduct(product);
			
		}
		
		
		return catalog;
  }
	
  
private void addImagesToProduct(Product product) throws SQLException
{
	//load images
	product.images = new Vector<Image>();
	
	//create query
	String sql = "SELECT imageId, productId, location FROM image WHERE productID = ?";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, product.productId);
	
	connection.executeQuery();

	try {
		while(connection.result.next()) 
		{
			Image image = new Image(connection.result.getInt(1),connection.result.getInt(2),connection.result.getString(3));
			product.images.add(image);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}

private void addSellersToProduct(Product product) throws SQLException
{
	//load Sellers
	product.sellers = new Vector<ProductSeller>();
	
	//create query
	String sql = "SELECT productSellerId, sellerId, price, shippingCost, productId, companyName FROM productSeller p JOIN user u on p.sellerId = u.userId WHERE productID = ? ";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, product.productId);
	
	connection.executeQuery();

	try {
		while(connection.result.next()) 
		{
			ProductSeller seller = new ProductSeller(connection.result.getInt(1),connection.result.getInt(2),connection.result.getDouble(3), connection.result.getDouble(4), connection.result.getInt(5), connection.result.getString(6) );
			product.sellers.add(seller);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}

private void addRankingsReviewsToProduct(Product product) throws SQLException
{
	//load Rankings and Reviews
	product.reviewsRankings = new Vector<ReviewsRanking>();
	
	//create query
	String sql = "SELECT reviewsRankingId, ranking, review FROM reviewsRanking WHERE productID = ? ";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, product.productId);
	
	connection.executeQuery();

	try {
		while(connection.result.next()) 
		{
			ReviewsRanking reviewRankings = new ReviewsRanking(connection.result.getInt(1),connection.result.getInt(2),connection.result.getString(3), product.productId );
			product.reviewsRankings.add(reviewRankings);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}

//Edit Product Information.  Name/Description/Department.
public void editProduct(int productId, String name, String description, int department) throws SQLException {
	//create connection
	connection = new ConnectionInfo();

	//create query
	String sql = "UPDATE product SET name=?, description=?, department=? WHERE productId = ?";

	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setString(1, name);
	connection.ps.setString(2, description);
	connection.ps.setInt(3, department);
	connection.ps.setInt(4, productId);
	
	int affectedRows = connection.ps.executeUpdate();
	
    if (affectedRows == 0) {
        throw new SQLException("Creating product seller info failed, no rows affected.");
    }
  }

//Edit Product Seller Information. Price/Shipping Cost.
public void editProductSeller(int productId, int sellerId, double price, double shippingCost) throws SQLException, ClassNotFoundException {
	//create connection
	connection = new ConnectionInfo();

	int productSellerId = doesProductSellerExist(productId, sellerId);
	
	//If Entry already exists, update entry.
	if(productSellerId != -1)
	{
	
		//create query
		String sql = "UPDATE productSeller SET price=?, shippingCost=? WHERE productSellerId = ?";
	
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setDouble(1, price);
		connection.ps.setDouble(2, shippingCost);
		connection.ps.setInt(3, productId);
		
		int affectedRows = connection.ps.executeUpdate();
		
        if (affectedRows == 0) {
            throw new SQLException("Creating product seller info failed, no rows affected.");
        }
	}
	//Else need to insert new row.
	else
	{
		//create query
		String sql = "Insert Into productSeller (sellerId, price, shippingCost, productId) Values (?,?,?,?) ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
		
		//set variable in prepared statement
		connection.ps.setInt(1, sellerId);
		connection.ps.setDouble(2, price);
		connection.ps.setDouble(3, shippingCost);
		connection.ps.setInt(4, productId);
		
        int affectedRows = connection.ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating product seller info failed, no rows affected.");
        }
	}
}

//Returns -1 if entry does not exist, else productSellerId
public int doesProductSellerExist(int productId, int sellerId) throws ClassNotFoundException, SQLException {
	int productSellerId = -1;
	
	//create query
	String sql = "SELECT productSellerId FROM productSeller WHERE sellerId = ? AND productId = ? ";
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, sellerId);
	connection.ps.setInt(2, productId);
	
	connection.executeQuery();

	try {
		if(connection.result.next()) 
		{
			productSellerId = connection.result.getInt(1);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return productSellerId;
	}

  public void reviewProduct(int productId, int ranking, String review) throws SQLException {
		//create connection
		connection = new ConnectionInfo();

		

			//create query
			String sql = "Insert Into reviewsRanking (productId, ranking, review) Values (?,?,?) ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			
			//set variable in prepared statement
			connection.ps.setInt(1, productId);
			connection.ps.setInt(2, ranking);
			connection.ps.setString(3, review);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating product review failed, no rows affected.");
	        }
	  
  }
  
  //returns listItemId if exists otherwise -1
  public int doesItemExistInList( int listId, ProductSeller productSeller) throws SQLException
  {
	  int listItemId = -1;
	  
		//create query
		String sql = "SELECT listItemId FROM listitem l join productSeller p on l.sellerId = p.productSellerId and l.productId = p.productId WHERE listId = ? AND l.sellerId = ? AND l.productId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, listId);
		connection.ps.setInt(2, productSeller.getProductSellerId());
		connection.ps.setInt(3, productSeller.getProductId());
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				listItemId = connection.result.getInt(1);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
			  
	  return listItemId;
  }

//Add Item to object list  
public void addItemToList(List list) throws SQLException
{
	//load items
	list.listItem = new Vector<ListItem>();
	
	//create query
	String sql = "SELECT listItemId, listId, productId, price, quantity, sellerId FROM listItem WHERE listId = ?";
	
	//create connection
	connection = new ConnectionInfo();
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	//set variable in prepared statement
	connection.ps.setInt(1, list.getListId());
	
	connection.executeQuery();
	
	try {
		while(connection.result.next()) 
		{
			ListItem itemx = new ListItem(connection.result.getInt(1),connection.result.getInt(2),connection.result.getInt(3), connection.result.getDouble(4),connection.result.getInt(5),connection.result.getInt(6));
			list.listItem.add(itemx);
		}
	}
	catch (SQLException ex) {
		Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
}
  
  //Add Product to Shopping Cart.  If item already exists update quantity.
  public void addProductShoppingCart(int listId, int productSellerId) throws SQLException {
		//create connection
		connection = new ConnectionInfo();

		ProductSeller productSeller = getProductSeller(productSellerId);
		
		int listItemId = doesItemExistInList(listId, productSeller);
		
		//If Entry already exists, update entry.
		if(listItemId != -1)
		{
		
			//create query
			String sql = "UPDATE listItem SET quantity=quantity+1 WHERE listItemId = ?";
		
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setInt(1, listItemId);
			
			int affectedRows = connection.ps.executeUpdate();
			
	        if (affectedRows == 0) {
	            throw new SQLException("Updating Shopping Cart Failed, no rows affected.");
	        }
		}
		//Else need to insert new row.
		else
		{
			//create query
			String sql = "Insert Into listItem (quantity, sellerId, productId, listId) Values (?,?,?,?) ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			
			//set variable in prepared statement
			connection.ps.setInt(1, 1);
			connection.ps.setInt(2, productSeller.getProductSellerId());
			connection.ps.setInt(3, productSeller.getProductId());
			connection.ps.setInt(4, listId);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Updating shopping Cart failed..");
	        }
		}
  }
  
  //returns productSeller object from productSellerID.
  public ProductSeller getProductSeller(int productSellerId) throws SQLException
  {
	  ProductSeller productSeller = null;
	  
		//create query
		String sql = "SELECT sellerId, price, shippingCost, productId, companyName FROM productSeller p JOIN user u on p.sellerId = u.userId WHERE productSellerId = ?  ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, productSellerId);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				 productSeller= new ProductSeller( productSellerId, connection.result.getInt(1), connection.result.getDouble(2), connection.result.getDouble(3), connection.result.getInt(4),connection.result.getString(5));
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
	  
	  return productSeller;
  }
  
  //returns wish list, else null.
  public List getWishList(int userId) throws SQLException 
  {
	  List wishList = null;
	  /*
		//create query
		String sql = "SELECT listId FROM list WHERE ownerId = ? AND listType = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		connection.ps.setInt(2, ListType.wish.value);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				wishList = new List(connection.result.getInt(1), ListType.wish.value,userId, -1);
				
				//Load items of List
				addItemToList(wishList);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
			*/
	  wishList = getList(userId, ListType.wish.value);
	  
	  
	  return wishList;
  }

  
  //returns shopping Cart, else null.
  public List getShoppingCart(int userId) throws SQLException
  {
	  List shoppingList = null;
	  /*
		//create query
		String sql = "SELECT listId FROM list WHERE ownerId = ? AND listType = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		connection.ps.setInt(2, ListType.shoppingCart.value);
		
		connection.executeQuery();

		try{
			if(connection.result.next())
			{
				shoppingList = new List(connection.result.getInt(1), ListType.shoppingCart.value,userId, -1);
				addItemToList(shoppingList);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
			*/
	  shoppingList = getList(userId, ListType.shoppingCart.value);

	  return shoppingList;
  }
  
  //returns list, else null.
  public List getList(int userId, int listType) throws SQLException
  {
	  List list = null;

		//create query
		String sql = "SELECT listId, orderId FROM list WHERE ownerId = ? AND listType = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		connection.ps.setInt(2, listType);
		
		connection.executeQuery();

		try{
			if(connection.result.next())
			{
				list = new List(connection.result.getInt(1), listType ,userId, connection.result.getInt(2) );
				addItemToList(list);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

	  return list;
  }
  
  //returns list, else null.
  public List getListByListId(int listId) throws SQLException
  {
	  List list = null;

		//create query
		String sql = "SELECT listId, orderId, listType, ownerId FROM list WHERE listId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, listId);
		
		connection.executeQuery();

		try{
			if(connection.result.next())
			{
				list = new List(connection.result.getInt(1), connection.result.getInt(3) ,connection.result.getInt(4), connection.result.getInt(2) );
				addItemToList(list);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

	  return list;
  }
  
  //returns list, else null.
  public List getListByOrderId(int orderId) throws SQLException
  {
	  List list = null;

		//create query
		String sql = "SELECT listId, orderId, listType, ownerId FROM list WHERE orderId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, orderId);
		
		connection.executeQuery();

		try{
			if(connection.result.next())
			{
				list = new List(connection.result.getInt(1), connection.result.getInt(3) ,connection.result.getInt(4), connection.result.getInt(2) );
				addItemToList(list);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

	  return list;
  }
  
//Add Product to Wish List.  If item already exists update quantity.
  public void addProductWishList(int listId, int productSellerId) throws SQLException {
			//create connection
			connection = new ConnectionInfo();

			ProductSeller productSeller = getProductSeller(productSellerId);
			
			int listItemId = doesItemExistInList(listId, productSeller);
			
			//If Entry already exists, update entry.
			if(listItemId != -1)
			{
			
				//create query
				String sql = "UPDATE listItem SET quantity=quantity+1 WHERE listItemId = ?";
			
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql);
				
				//set variable in prepared statement
				connection.ps.setInt(1, listItemId);
				
				int affectedRows = connection.ps.executeUpdate();
				
		        if (affectedRows == 0) {
		            throw new SQLException("Updating Shopping Cart Failed, no rows affected.");
		        }
			}
			//Else need to insert new row.
			else
			{
				//create query
				String sql = "Insert Into listItem (quantity, sellerId, productId, listId) Values (?,?,?,?) ";
				
				//create connection
				connection = new ConnectionInfo();
				//getConnection();
				
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
				
				//set variable in prepared statement
				connection.ps.setInt(1, 1);
				connection.ps.setInt(2, productSeller.getProductSellerId());
				connection.ps.setInt(3, productSeller.getProductId());
				connection.ps.setInt(4, listId);
				
		        int affectedRows = connection.ps.executeUpdate();

		        if (affectedRows == 0) {
		            throw new SQLException("Updating shopping Cart failed..");
		        }
			}
	  
  }

  public void updateOrder(int orderId, String receiverName, String street, String city, String state, String zip) throws SQLException
  {
		//create query
		String sql = "Update orders set receiverName =?, street=?, city=?, state=?, zip=? Where orderId = ? ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setString(1, receiverName);
		connection.ps.setString(2, street);
		connection.ps.setString(3, city);
		connection.ps.setString(4, state);
		connection.ps.setString(5, zip);
		connection.ps.setInt(6, orderId);
		
		connection.ps.executeUpdate();

  }
  
  public int createOrder(int ownerId, double tax, double subTotal, String receiverName, String accountNumber, String street, String city, String state, String zip   ) throws SQLException {

		int orderId = -1;
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(now);
		
		//create query
		String sql = "Insert Into orders (buyerId, receiverName, tax, totalPrice, time, shippingStatus, payPalAcctNumber, street, city, state, zip) Values (?,?,?,?,?,?,?,?,?,?,?) ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
		
		//set variable in prepared statement
		connection.ps.setInt(1, ownerId);
		connection.ps.setString(2, receiverName);
		connection.ps.setDouble(3, tax);
		connection.ps.setDouble(4, subTotal);
		connection.ps.setString(5, currentTime);
		connection.ps.setInt(6, 0);
		connection.ps.setString(7, accountNumber);
		connection.ps.setString(8, street);
		connection.ps.setString(9, city);
		connection.ps.setString(10, state);
		connection.ps.setString(11, zip);
		
      int affectedRows = connection.ps.executeUpdate();

      if (affectedRows == 0) {
          throw new SQLException("Creating order failed, no rows affected.");
      }

      try (ResultSet generatedKeys = connection.ps.getGeneratedKeys()) {
          if (generatedKeys.next()) {
              orderId = generatedKeys.getInt(1);
          }
          else {
              throw new SQLException("Creating order failed, no ID obtained.");
          }
      }

		return orderId;	  
  }
  
  public Orders getBuyerOrder(int userId) throws SQLException {

		//create query
		String sql = "Select  city, state, street, zip, receiverName, tax, totalPrice, time, orderId, shippingStatus FROM orders  WHERE buyerId = ? order by orderId desc ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);

		
		connection.executeQuery();
		
		Orders orders = new Orders();
		orders.orders = new Vector<Order>();
		List list = null;
		
		try {
			while(connection.result.next()) 
			{	 
				String city = connection.result.getString(1);
				String state = connection.result.getString(2);
				String street = connection.result.getString(3);
				String zip = connection.result.getString(4);
				//int listId = connection.result.getInt(5);
				String receiverName = connection.result.getString(5);
				double tax = connection.result.getDouble(6);
				double totalPrice = connection.result.getDouble(7);
				String time = connection.result.getString(8);
				int orderId = connection.result.getInt(9);
				int shippingStatus = connection.result.getInt(10);

				Address shipping = new Address(city, state, street, zip);
				//List list = getListByListId(listId);
				Order order = new Order(receiverName, tax, totalPrice, time, orderId, shippingStatus, shipping, list, userId);
				orders.orders.add(order);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

		return orders;	  
  }
  
  public Orders getSellerOrder(int userId) throws SQLException {

		//create query
		String sql = "Select city, state, street, zip, receiverName, tax, totalPrice, time, orderId, shippingStatus, buyerId FROM orders  WHERE orderId in(select orderId from list where listId in (select listId from listItem where sellerId in (select productSellerId from productSeller where sellerId = ?)))order by orderId desc ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);

		
		connection.executeQuery();
		
		Orders orders = new Orders();
		orders.orders = new Vector<Order>();
		List list = null;
		
		try {
			while(connection.result.next()) 
			{	 
				String city = connection.result.getString(1);
				String state = connection.result.getString(2);
				String street = connection.result.getString(3);
				String zip = connection.result.getString(4);
				//int listId = connection.result.getInt(5);
				String receiverName = connection.result.getString(5);
				double tax = connection.result.getDouble(6);
				double totalPrice = connection.result.getDouble(7);
				String time = connection.result.getString(8);
				int orderId = connection.result.getInt(9);
				int shippingStatus = connection.result.getInt(10);
				int ownerId = connection.result.getInt(11);

				Address shipping = new Address(city, state, street, zip);
				//List list = getListByListId(listId);
				Order order = new Order(receiverName, tax, totalPrice, time, orderId, shippingStatus, shipping, list, ownerId);
				orders.orders.add(order);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

		return orders;	  
  }
  
  public Orders getAdminOrder() throws SQLException {

		//create query
		String sql = "Select  city, state, street, zip,  receiverName, tax, totalPrice, time, orderId, shippingStatus, buyerId FROM orders order by orderId desc ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		connection.executeQuery();
		
		Orders orders = new Orders();
		orders.orders = new Vector<Order>();
		Order order = null;
		List list = null;
		
		try {
			while(connection.result.next()) 
			{	 
				String city = connection.result.getString(1);
				String state = connection.result.getString(2);
				String street = connection.result.getString(3);
				String zip = connection.result.getString(4);
				//int listId = connection.result.getInt(5);
				String receiverName = connection.result.getString(5);
				double tax = connection.result.getDouble(6);
				double totalPrice = connection.result.getDouble(7);
				String time = connection.result.getString(8);
				int orderId = connection.result.getInt(9);
				int shippingStatus = connection.result.getInt(10);
				int ownerId = connection.result.getInt(11);

				Address shipping = new Address(city, state, street, zip);
				//List list = getListByListId(listId);
				order = new Order(receiverName, tax, totalPrice, time, orderId, shippingStatus, shipping, list, ownerId);
				orders.orders.add(order);
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
			}

		return orders;	  
  }
 
  public void shipOrder(int orderId   ) throws SQLException {

		//create query
		String sql = "UPDATE orders SET shippingStatus = ? WHERE orderId = ? ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, Shipped.Shipped.value);
		connection.ps.setInt(2, orderId);


		
     connection.ps.executeUpdate();
  
  }
  
  public void switchShopCartToOrderList(int listId, int orderId   ) throws SQLException {

		//create query
		String sql = "UPDATE list SET listType = ?, orderId = ? WHERE listId = ? ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, ListType.order.value);
		connection.ps.setInt(2, orderId);
		connection.ps.setInt(3, listId);

		
     connection.ps.executeUpdate();
  
  }
  
  public void updateListItemPrices(ListItem item) throws SQLException {

		//create query
		String sql = "UPDATE listItem SET price = ?, shippingPrice = ? WHERE listItemId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setDouble(1, item.getPrice());
		connection.ps.setDouble(2, item.getShippingPrice());
		connection.ps.setInt(3, item.getListItemId());

		
     connection.ps.executeUpdate();
  
  }

  public void removeProductShoppingCart(int listItemId) throws SQLException {
		//create connection
		connection = new ConnectionInfo();
	  
		//create query
		String sql = "DELETE FROM listItem WHERE listItemId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, listItemId);
		
		connection.ps.executeUpdate();

  }

  public void removeProductWishList(int listItemId) throws SQLException {
		//create connection
		connection = new ConnectionInfo();
	  
		//create query
		String sql = "DELETE FROM listItem WHERE listItemId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, listItemId);
		
		connection.ps.executeUpdate();
  }

  public void deleteOrder(int orderId) throws SQLException {
		
	    //create connection
		connection = new ConnectionInfo();
	  
		deleteListItems(orderId);
		deleteList(orderId);

		//create query
		String sql = "DELETE FROM orders Where orderId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, orderId);
		
		connection.ps.executeUpdate();
	  
  }
  
  private void deleteListItems(int orderId) throws SQLException
  {
		//create query
		String sql = "DELETE FROM listItem Where listId in (Select listId From list where orderId = ?) ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, orderId);
		
		connection.ps.executeUpdate();
  }
  
  public void moveListItem(int listItemId, int listId) throws SQLException
  {
	    //create connection
		connection = new ConnectionInfo();
	  
		//create query
		String sql = "Update listItem Set listId=? Where listItemId=? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, listId);
		connection.ps.setInt(2, listItemId);
		
		connection.ps.executeUpdate();
  }
  
  private void deleteList(int orderId) throws SQLException
  {
		//create query
		String sql = "DELETE FROM list WHERE orderId = ? ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, orderId);
		
		connection.ps.executeUpdate();
  }
  
  public void deleteProduct(int productId) throws SQLException {
		//create connection
		connection = new ConnectionInfo();

			//create query
			String sql = "UPDATE product set isDeleted = 1 where productId = ? ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setInt(1, productId);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Deleting Product failed.");
	        }
	        
	     removeProductFromWishShopLists(productId);
  }
  
  public void removeProductFromWishShopLists(int productId) throws SQLException {

			//create query
			String sql = "DELETE FROM listItem WHERE productId = ? AND listId in (SELECT listId FROM list WHERE listType = ? OR listType = ?) ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setInt(1, productId);
			connection.ps.setInt(2, ListType.shoppingCart.value);
			connection.ps.setInt(3, ListType.wish.value);
			
	        int affectedRows = connection.ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Deleting Product from shopping and wish lists failed.");
	        }
	        
	     removeProductFromWishShopLists(productId);
  }


}