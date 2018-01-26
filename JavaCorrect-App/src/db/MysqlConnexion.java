package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnexion {
	private static Connection connect = null;
	
	
	/**
	 * Establish database connection through JDBC driver
	 * @param properties the instance of MysqlPropertiesParser that represent all database propertie
	 */
	public MysqlConnexion(final MysqlPropertiesParser properties) {
		if (properties.getUrl() != null && properties.getUser() != null && properties.getPassword() != null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connect = DriverManager.getConnection(properties.getUrl(), properties.getUser(), properties.getPassword());
				System.out.println("Le driver s'est connecté à la base de données avec succès");
			} catch (final SQLException ex) {
				// handle any errors
				System.err.println("SQLException: " + ex.getMessage());
				System.err.println("SQLState: " + ex.getSQLState());
				System.err.println("VendorError: " + ex.getErrorCode());
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * get the instance of the connection
	 * @param properties parsed with MysqlPropertiesParser
	 * @return connection of instance
	 */
	public static synchronized Connection getInstance(final MysqlPropertiesParser properties) {
		if (connect == null) {
			new MysqlConnexion(properties);
		}
		return connect;
	}
}
