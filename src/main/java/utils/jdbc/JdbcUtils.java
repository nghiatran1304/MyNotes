package utils.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {

	// Driver database type
	private final String DRIVER_SQLServer = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private final String DRIVER_MySQL = "com.mysql.jdbc.Driver";
	private final String DRIVER_Postgres = "org.postgresql.Driver";
	private String DRIVER = null;

	// User setting
	private String USER_NAME = null;
	private String PASSWORD = null;
	private String URL_CONNECTION = null;
	private Connection conn = null;

	/**
	 * Select database type: SQLServer, MySQL, Postgre...
	 * 
	 * @param dbType       sqlserver | mysql | postgre
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param databaseName
	 * 
	 */
	public JdbcUtils(String dbType, String host, String port, String username, String password, String databaseName) {
		setting(dbType, host, port, username, password, databaseName);
		connectToDatabase();
	}

	private void setting(String dbType, String host, String port, String username, String pass, String dbName) {
		USER_NAME = username;
		PASSWORD = pass;

		String prefix = "";
		if (dbType.equalsIgnoreCase("sqlserver")) {
			DRIVER = DRIVER_SQLServer;
			prefix = "jdbc:sqlserver://";
		} else if (dbType.equalsIgnoreCase("mysql")) {
			DRIVER = DRIVER_MySQL;
			prefix = "jdbc:mysql://";
		} else if (dbType.equalsIgnoreCase("postgre")) {
			DRIVER = DRIVER_Postgres;
			prefix = "jdbc:postgresql://";
		}

		URL_CONNECTION = prefix + host + ":" + port + ";database=" + dbName;

	}

	private Connection connectToDatabase() {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL_CONNECTION, USER_NAME, PASSWORD);
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public PreparedStatement getStmt(String sql, Object... args) throws SQLException {
		conn = connectToDatabase();
		PreparedStatement stmt;
		if (sql.trim().startsWith("{")) {
			stmt = conn.prepareCall(sql);
		} else {
			stmt = conn.prepareStatement(sql);
		}
		for (int i = 0; i < args.length; i++) {
			stmt.setObject(i + 1, args[i]);
		}
		return stmt;
	}

	public void update(String sql, Object... args) {
		try {
			PreparedStatement stmt = getStmt(sql, args);
			try {
				stmt.executeUpdate();
			} finally {
				stmt.getConnection().close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public ResultSet query(String sql, Object... args) throws SQLException {
		PreparedStatement stmt = getStmt(sql, args);
		return stmt.executeQuery();
	}

	public Object value(String sql, Object... args) {
		try {
			ResultSet rs = query(sql, args);
			if (rs.next()) {
				return rs.getObject(1);
			}
			rs.getStatement().getConnection().close();
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
		// String sql = "{call sp_name(?)}";
		try {
			List<Object[]> list = new ArrayList<>();
			ResultSet rs = query(sql, args);
			while (rs.next()) {
				Object[] vals = new Object[cols.length];
				for (int i = 0; i < cols.length; i++) {
					vals[i] = rs.getObject(cols[i]);
				}
				list.add(vals);
			}
			rs.getStatement().getConnection().close();
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
