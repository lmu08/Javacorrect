package db;

import java.sql.Connection;

public class MysqlRequest {
	private static final Connection myqlco = MysqlConnexion.getInstance(MysqlPropertiesParser.getInstance());
}
