package dao;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.User;
import model.Address;
import model.Users;

import java.util.ListIterator;

import enums.userType;

public class AuthDAO {
	
	public ConnectionInfo connection;
		
	public AuthDAO()
	{
		
	}
	
	/**
	 * query database to get password for the referencing user name check against
	 * password passed as a parameter
	 * @param userName
	 * @param password
	 * @return If match userId, else -1
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public int checkUserPass(String userName, String password) throws ClassNotFoundException, SQLException  {
		
			int userId = -1;	

			//create query
			String sql = "SELECT userId FROM user WHERE userName = ? and password = ? AND isDeleted = 0 ";
			
			//create connection
			connection = new ConnectionInfo();
			//getConnection();
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setString(1, userName);
			connection.ps.setString(2, password);
			
			connection.executeQuery();

			try {
				if(connection.result.next()) userId = connection.result.getInt(1); //check if next to make sure next is not NULL!
				}
				catch (SQLException ex) {
				Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
				}
				
			return userId;
		}
	
	/**
	 * query database with userId and return User object
	 * @param userId
	 * @return User
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public User getUserById(int userId) throws ClassNotFoundException, SQLException {
		User user = null;
		Address address = null;
		
		//create query
		String sql = "SELECT userId, userName, firstName, lastName, userType, email, phoneNumber, routingNumber, accountNumber, url, companyName, isDeleted, city, state, street, zip, password  FROM user WHERE userId = ?";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();

		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, userId);
		
		connection.executeQuery();

		try {
			if(connection.result.next()) 
			{
				address = new Address(connection.result.getString(13),connection.result.getString(14),connection.result.getString(15),connection.result.getString(16));
			    user = new User(connection.result.getInt(1), connection.result.getString(2), connection.result.getString(3), connection.result.getString(4), connection.result.getInt(5), false, connection.result.getString(6), connection.result.getString(7), connection.result.getInt(8), connection.result.getInt(9), connection.result.getString(10), connection.result.getString(11), connection.result.getInt(12), address, connection.result.getString(13));
			}
			else
			{
				user = new User();
			}
		}
		catch (SQLException ex) {
			Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			return user;
		}
		
	/**
	 * Insert buyer information into user table
	 * @param userId
	 * @return userId if successful, else -1
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public int enterNewUser(String userName, String password, String firstName, String lastName, String email, String phoneNumber, String street, String city, String state, String zip) throws ClassNotFoundException, SQLException {

		int userId = -1;
		
		//create query
		String sql = "Insert Into user (userName, password, firstName, lastName, email, phoneNumber, street, city, state, zip) Values (?,?,?,?,?,?,?,?,?,?) ";
		
		//create connection
		connection = new ConnectionInfo();
		//getConnection();
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
		
		//set variable in prepared statement
		connection.ps.setString(1, userName);
		connection.ps.setString(2, password);
		connection.ps.setString(3, firstName);
		connection.ps.setString(4, lastName);
		connection.ps.setString(5, email);
		connection.ps.setString(6, phoneNumber);
		connection.ps.setString(7, street);
		connection.ps.setString(8, city);
		connection.ps.setString(9, state);
		connection.ps.setString(10, zip);
		
        int affectedRows = connection.ps.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = connection.ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

		return userId;
			
		}
		
		
		/**
		 * Checks if user name is available
		 * @param userId
		 * @return true if available, false otherwise.
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
		 */
		public boolean isUserNameAvailable(String userName) throws ClassNotFoundException, SQLException {
				boolean success = true;
				
				//create query
				String sql = "SELECT userName FROM user WHERE userName = ? ";
				
				//create connection
				connection = new ConnectionInfo();
				//getConnection();
				
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql);
				
				//set variable in prepared statement
				connection.ps.setString(1, userName);
				
				connection.executeQuery();

				try {
					if(connection.result.next()) success = false; //If result returned, user name is not available.
					}
					catch (SQLException ex) {
					Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
					}
					
					return success;
				}
		
		  public void approveSeller(int userId) throws SQLException {
			  String sql="update user set userType =? where userId= ? ";
			  connection= new ConnectionInfo();
			  connection.ps = connection.conn.prepareStatement(sql);
			  connection.ps.setInt(1, userType.seller.value);
			  connection.ps.setInt(2, userId);
			  connection.ps.executeUpdate();
		  }


		  public void closeAccount(int uid) throws SQLException {
			  //geethika
			  String sql="update user set isDeleted=1 where userId= ? ";
			  connection= new ConnectionInfo();
			  connection.ps = connection.conn.prepareStatement(sql);
			  connection.ps.setInt(1,uid);
			  connection.ps.executeUpdate();
		  }
		  
		  public int AccountUpdate(String fn,String ln,String pn,String e,String st, String c, String sta, String zip, String password, int userId, int routingNumber, int accountNumber, String url, String companyName) throws SQLException{
			//create query
				String sql = "Update user set firstName=? , lastName=?, phoneNumber=?, email=?, street=?, city=?, state=?, zip=?, password=?, routingNumber=?, accountNumber=?, url=?, companyName =? where userId=? ";
				
				//create connection
				connection = new ConnectionInfo();
				//getConnection();
				
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql);
				
				//set variable in prepared statement
				connection.ps.setString(1,fn);
				connection.ps.setString(2,ln);
				connection.ps.setString(3,pn);
				connection.ps.setString(4,e);
				connection.ps.setString(5,st);
				connection.ps.setString(6,c);
				connection.ps.setString(7,sta);
				connection.ps.setString(8,zip);
				connection.ps.setString(9,password);
				
				connection.ps.setInt(10,routingNumber);
				connection.ps.setInt(11,accountNumber);
				connection.ps.setString(12,url);
				connection.ps.setString(13,companyName);
				
				
				connection.ps.setInt(14,userId);
			
				return connection.ps.executeUpdate();					
		  }
		  
		  public int UpdateSeller(int userId, int routingNumber, int accountNumber, String url, String companyName) throws SQLException{
			//create query
				String sql = "Update user set  routingNumber=?, accountNumber=?, url=?, companyName =? where userId=? ";
				
				//create connection
				connection = new ConnectionInfo();
				//getConnection();
				
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql);
				
				//set variable in prepared statement
				
				connection.ps.setInt(1,routingNumber);
				connection.ps.setInt(2,accountNumber);
				connection.ps.setString(3,url);
				connection.ps.setString(4,companyName);
				
				
				connection.ps.setInt(5,userId);
			
				return connection.ps.executeUpdate();					
		  }
		  
		//Adds list for user.  If not successful returns -1 else listId.
			public int createList(int userId, int listType, Integer orderId) throws SQLException  {

				int listId = -1;
				
				//create query
				String sql = "Insert Into list (ownerId, listType, orderId) Values (?,?, ?) ";
				
				//create connection
				connection = new ConnectionInfo();
				//getConnection();
				
				//create prepared statement
				connection.ps = connection.conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
				
				//set variable in prepared statement
				connection.ps.setInt(1, userId);
				connection.ps.setInt(2, listType);
				if(orderId != null)
				{
					connection.ps.setInt(3, orderId);
				}
				else{
					connection.ps.setNull(3, java.sql.Types.INTEGER);
				}
				
		        int affectedRows = connection.ps.executeUpdate();

		        if (affectedRows == 0) {
		            throw new SQLException("Creating list failed, no rows affected.");
		        }

		        try (ResultSet generatedKeys = connection.ps.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		                listId = generatedKeys.getInt(1);
		            }
		            else {
		                throw new SQLException("Creating list failed, no ID obtained.");
		            }
		        }

				return listId;
					
				}
			
  public Users getUsers() throws SQLException, ClassNotFoundException
  {
  	Users users = new Users();
	//create connection
	connection = new ConnectionInfo();
	Vector<Integer> userIds = new Vector<Integer>();
  	users.users = new Vector<User>();
	//create query
	String sql = "SELECT userId From user Where isDeleted = 0";
	
	//create prepared statement
	connection.ps = connection.conn.prepareStatement(sql);
	
	connection.executeQuery();

	while(connection.result.next()) 
	{
		userIds.add(connection.result.getInt(1));
		
	}
	
	ListIterator<Integer> userIdIterator = userIds.listIterator();
	int userId = -1;
	while(userIdIterator.hasNext())
	{
		userId = userIdIterator.next();
		User user = getUserById(userId);
		users.users.add(user);
	}

	return users;
  }
  


}
