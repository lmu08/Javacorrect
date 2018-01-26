package db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MysqlPropertiesParser {
	private String user;
	private String password;
	private String jdbcDriver;
	private String port;
	private String host;
	private String dbname;
	private String url;
	private static MysqlPropertiesParser instance = null;
	
	/**
	 * Parses db.properties file and store it in different variable.
	 * Then it's possible to get those with getters
	 */
	private MysqlPropertiesParser() {
		final Properties prop = new Properties();
		final File file = new File("resources/db.properties");
		try (InputStream input = new FileInputStream(file);) {
			prop.load(input);
			this.user = prop.getProperty("USER");
			this.password = prop.getProperty("PASSWORD");
			this.jdbcDriver = prop.getProperty("JDBC_DRIVER");
			this.port = prop.getProperty("PORT");
			this.dbname = prop.getProperty("DBNAME");
			this.host = prop.getProperty("HOST");
			this.url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbname;
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getDbname() {
		return dbname;
	}
	
	public String getUrl() {
		return url;
	}

	/**
	 * Singleton pattern
	 * @return instance of this class
	 */
	public static synchronized MysqlPropertiesParser getInstance() {
		if (instance == null) {
			instance = new MysqlPropertiesParser();
		}
		return instance;
	}
	
}
