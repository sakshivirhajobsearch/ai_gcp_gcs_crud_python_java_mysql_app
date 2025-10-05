package com.ai.gcp.gcs.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai.gcp.gcs.db.DBConnection;

public class GCSFrontend extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel model;

	public GCSFrontend() {
		setTitle("GCS File Viewer with Anomaly Detection");
		setSize(800, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		String[] columns = { "ID", "Name", "Size (bytes)", "Content Type", "Updated At", "Anomaly" };
		model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		add(new JScrollPane(table));

		loadFilesFromDB();
		setVisible(true);
	}

	private void loadFilesFromDB() {
		try (Connection conn = DBConnection.getConnection()) {
			String sql = "SELECT id, name, size, content_type, updated_at, anomaly FROM gcs_files";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				long size = rs.getLong("size");
				String type = rs.getString("content_type");
				Timestamp updatedAt = rs.getTimestamp("updated_at");
				boolean anomaly = rs.getBoolean("anomaly");

				model.addRow(new Object[] { id, name, size, type, updatedAt, anomaly });
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GCSFrontend());
	}
}
