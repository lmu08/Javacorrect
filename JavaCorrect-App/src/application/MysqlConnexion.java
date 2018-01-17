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
	
	private static Connection connect = null;
	
	public MysqlConnexion(MysqlPropertiesParser properties) throws ClassNotFoundException {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			connect =
		       DriverManager.getConnection(properties.getUrl(), properties.getUser(), properties.getPassword());
			System.out.println("Le driver s'est connecté à la base de données avec succès");
		    
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}

	}
	
	public static synchronized Connection getInstance(MysqlPropertiesParser properties) throws ClassNotFoundException{
	    if(connect == null){
	      new MysqlConnexion(properties);
	    }
	    return connect;   
	  } 
}
