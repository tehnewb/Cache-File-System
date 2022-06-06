package com.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.Driver;

public class SQLLoader {

	private final String username;
	private final String password;
	private final String url;

	public SQLLoader(String url, String username, String password) {
		this.username = username;
		this.password = password;
		this.url = url;
	}

	public Connection connect() throws SQLException {
		DriverManager.registerDriver(new Driver());

		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws SQLException {
		SQLLoader loader = new SQLLoader("jdbc:mysql://localhost:3306/caching", "admin", "Wildking72!");

		loader.connect();
		if (true)
			return;

		/*
		 * PreparedStatement prepared =
		 * conn.prepareStatement("insert into raw(file_name, file_data) values (?, ?) "
		 * ); prepared.setString(1, "0.dat");
		 * 
		 * CacheStore store = new CacheStore(); CacheArchive archive = new
		 * CacheArchive(0); CacheFile file = new CacheFile(archive, 0);
		 * archive.addFile(file); store.addArchive(archive);
		 * 
		 * prepared.setBinaryStream(2, new ByteArrayInputStream(file.getData()),
		 * file.getData().length); prepared.executeUpdate();
		 * 
		 * PreparedStatement statement = conn.prepareStatement("SELECT * from raw");
		 * ResultSet rs = statement.executeQuery(); while (rs.next()) { String name =
		 * rs.getString(1); Object o = rs.getBinaryStream(2); System.out.println(name +
		 * ", " + o); }
		 * 
		 * DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
		 * Result<org.jooq.Record> result = create.select().from("raw").fetch();
		 * System.out.println(result);
		 */
	}
}
