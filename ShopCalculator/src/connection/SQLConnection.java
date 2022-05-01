package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


//AZRI AZLAN

public class SQLConnection {
	
	private Connection con = null;
	private Statement st = null;
	
	//private reference to object of class
	private static SQLConnection sqlConn = new SQLConnection();
	
	//private constructor
	private SQLConnection() {
		
	}
	

	//method to return reference of the object of the class
	public static SQLConnection getInstance() {
	
		return sqlConn;
		
	}
	
	
	//method to connect to mySQL database
	public Statement getSQLConnection() {
		
		
		String driver = "com.mysql.cj.jdbc.Driver"; 
		String url = "jdbc:mysql://localhost:3306/"; 
		String dbname = "azriylab";
		String username = "root"; 
		String pwd = "12345678";
		
		try { 
			


			Class.forName(driver);
			con = DriverManager.getConnection(url + dbname, username, pwd); 
			st = con.createStatement();
			System.out.println("Connection succesful");
			
		} 
		catch (Exception e) {
		System.out.println(e); }
		
		return st;
		
	}
	
}
