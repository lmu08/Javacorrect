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

	private static MysqlPropertiesParser INSTANCE = null;
	
	public MysqlPropertiesParser() {
		final Properties prop = new Properties();
		InputStream input;
		try {
			final File file = new File("resources/db.properties");
			
			input = new FileInputStream(file);
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
	
	public static synchronized MysqlPropertiesParser getInstance()
	throws ClassNotFoundException {
		if (INSTANCE == null) {
			new MysqlPropertiesParser();
		}
		return INSTANCE;
	}

}
