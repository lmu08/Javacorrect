package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MysqlConnexion {
	private String user;
	private String password;
	private String jdbcDriver;
	private String port;
	private String host;
	private String dbname;
	private String url;
	private static Connection connect = null;
	
	
	
	// Constructeur privé
	public MysqlConnexion() throws ClassNotFoundException {
		Properties prop = new Properties();
		InputStream input;
		try {
			File file = new File("resources/db.properties");
			
			input = new FileInputStream(file);
			prop.load(input);
			this.user = prop.getProperty("USER");
			this.password = prop.getProperty("PASSWORD");
			this.jdbcDriver = prop.getProperty("JDBC_DRIVER");
			this.port = prop.getProperty("PORT");
			this.dbname = prop.getProperty("DBNAME");
			this.host = prop.getProperty("HOST");
			this.url = "jdbc:mysql://localhost:3306/javacorrectdb";
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(this.host);
		
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			connect =
		       DriverManager.getConnection(this.url, this.user, this.password);

		    
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}

	}
	
	public static Connection getInstance() throws ClassNotFoundException{
	    if(connect == null){
	      new MysqlConnexion();
	    }
	    return connect;   
	  } 
}
