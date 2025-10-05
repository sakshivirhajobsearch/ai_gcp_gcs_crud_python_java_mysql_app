package com.ai.gcp.gcs.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ai.gcp.gcs.db.DBConnection;

public class GCSRepository {

	// Fetch all files from MySQL for offline mode
	public JSONArray fetchFilesFromDB() throws Exception {
		JSONArray arr = new JSONArray();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT name, size, content_type, updated_at, anomaly FROM gcs_files");
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				JSONObject obj = new JSONObject();
				obj.put("name", rs.getString("name"));
				obj.put("size", rs.getLong("size"));
				obj.put("content_type", rs.getString("content_type"));
				obj.put("updated_at", rs.getString("updated_at"));
				obj.put("anomaly", rs.getBoolean("anomaly"));
				arr.put(obj);
			}
		}

		return arr;
	}
}
