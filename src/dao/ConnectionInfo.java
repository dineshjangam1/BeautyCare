package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionInfo {

	public Connection conn;
	public PreparedStatement ps;
	public ResultSet result;
	
	//SQL Connection information
	public static final String DB_URL = "jdbc:mysql://localhost:3306/csi518_schema";
	public static final String DB_USER = "dinesh";
	public static final String DB_PW = "password";
	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";



	  public ConnectionInfo() {
		  getConnection();
	  }
	
	public void DB_Close() throws Throwable
	{
		try {
			if(conn!=null) { //c is a variable for a Connection you may have to rename it
				conn.close(); //c is a variable for a Connection you may have to rename it
			}
		}
		catch(SQLException e) {
		System.out.println(e);
		}
	}


	/**
	 * Executes Query.  If there are results, sets results..
	 * @return
	 * @throws SQLException
	 */
	public void executeQuery() throws SQLException 
	{
			
		//execute prepared statement
		result = ps.executeQuery();
	}
	/**
	 * Sets Connection for SQL Server.
	 * @return
	 */
	public void getConnection()  
	{
			//load driver
			try {
				Class.forName(DB_DRIVER);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
			//get connection
			try {
				conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}

}