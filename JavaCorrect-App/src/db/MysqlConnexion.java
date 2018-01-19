package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnexion {
	private static Connection connect = null;
	
	public MysqlConnexion(final MysqlPropertiesParser properties)
	throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(properties.getUrl(), properties.getUser(), properties.getPassword());
			System.out.println("Le driver s'est connecté à la base de données avec succès");
		} catch (final SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static synchronized Connection getInstance(final MysqlPropertiesParser properties)
	throws ClassNotFoundException {
		if (connect == null) {
			new MysqlConnexion(properties);
		}
		return connect;
	}
}
