package dao;

import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Messages;
import model.Message;

public class MessageDAO {

    public ConnectionInfo connection;

  public void messageDAO() {
  }
  

  public Messages getMessageByUserId(int receiverId) throws SQLException {

		  	Messages messages = new Messages();
			//create connection
			connection = new ConnectionInfo();
			
		  	messages.messages = new Vector<Message>();
			//create query
			String sql = "SELECT messageId, senderId, message, receiverId, firstName, lastName FROM message m JOIN user u on m.senderId = u.userId WHERE receiverId = ? Order by messageId desc";
			
			//create prepared statement
			connection.ps = connection.conn.prepareStatement(sql);
			
			//set variable in prepared statement
			connection.ps.setInt(1, receiverId);
			
			connection.executeQuery();
			String senderName = "";
			try {
				while(connection.result.next()) 
				{
					senderName = connection.result.getString(5) + " " + connection.result.getString(6);
					Message message = new Message(connection.result.getInt(1),connection.result.getInt(2),connection.result.getString(3),connection.result.getInt(4), senderName);
					messages.messages.add(message);
				}
			}
			catch (SQLException ex) {
				Logger.getLogger(CatalogDAO.class.getName()).log(Level.SEVERE, null, ex);
				}
			
			
			return messages;
	  
  }

  public void sendMessage(int receiverId, int senderId, String message) throws SQLException  {
		//create connection
		connection = new ConnectionInfo();
		
		//create query
		String sql = "Insert Into message (receiverId, senderId, message) Values (?,?,?) ";
		
		//create prepared statement
		connection.ps = connection.conn.prepareStatement(sql);
		
		//set variable in prepared statement
		connection.ps.setInt(1, receiverId);
		connection.ps.setInt(2, senderId);
		connection.ps.setString(3, message);
		
         connection.ps.executeUpdate();


  }

}