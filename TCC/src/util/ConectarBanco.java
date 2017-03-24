package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectarBanco {
	
	//Banco de dados localhost	
	//private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";   SQL SERVER
	//private static String url = "jdbc:sqlserver://localhost:3306;databaseName=TCC;user=root;password=senhatcc"; SQL SERVER
	//private static String username = "root";
	//private static String password = "triala";
		
	private static String driver = "com.mysql.jdbc.Driver"; //MySQL	
	private static String url = "jdbc:mysql://localhost:3306/TCC?user=root&password=senhatcc"; //MySQL
	private static Connection con;
	
	
	public static Connection getConnection() {
	try {
	    Class.forName(driver);
	    try {
	        con = DriverManager.getConnection(url);
	    } catch (SQLException ex) {
	        // log exception
	    	System.out.println("Failed to create the database connection.");
	    	System.out.println(ex.getSQLState() + " " + ex.getErrorCode());
	    }
	} catch (ClassNotFoundException ex) {
	    // log an exception. for example:
		ex.printStackTrace();
		//System.out.println("Driver not found."); 
	}
		return con;
	}
	
}
