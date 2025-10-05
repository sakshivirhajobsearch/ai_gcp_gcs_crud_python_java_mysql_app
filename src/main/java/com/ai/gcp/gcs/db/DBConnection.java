package com.ai.gcp.gcs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/ai_gcp_gcs?useSSL=false&allowPublicKeyRetrieval=true";
	private static final String USER = "gcs_user";
	private static final String PASSWORD = "admin";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
