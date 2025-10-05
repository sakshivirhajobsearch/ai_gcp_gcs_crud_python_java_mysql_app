package com.ai.gcp.gcs.gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class UnifiedGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel tableModel;
	private JComboBox<String> filterCombo;
	private JButton refreshButton;

	private final String[] COLUMN_NAMES = { "Name", "Size (Bytes)", "Content Type", "Updated At", "Anomaly" };

	public UnifiedGUI() {
		setTitle("GCS Files Viewer with Anomaly Status");
		setSize(900, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Table model
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // make table read-only
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 4)
					return Boolean.class; // anomaly column
				if (columnIndex == 1)
					return Long.class; // size column
				return String.class;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// Top panel for filter and refresh
		JPanel topPanel = new JPanel();
		filterCombo = new JComboBox<>(new String[] { "All", "Normal", "Anomaly" });
		filterCombo.addActionListener(e -> fetchAndUpdateTable());
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> fetchAndUpdateTable());
		topPanel.add(new JLabel("Filter:"));
		topPanel.add(filterCombo);
		topPanel.add(refreshButton);

		add(topPanel, BorderLayout.NORTH);

		// Initial fetch
		fetchAndUpdateTable();
		setVisible(true);
	}

	private void fetchAndUpdateTable() {
		try {
			URL url = new URL("http://127.0.0.1:5000/files");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000); // 3 seconds timeout
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				JOptionPane.showMessageDialog(this, "Failed to fetch data: " + conn.getResponseCode());
				return;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			br.close();

			JSONArray jsonArray = new JSONArray(sb.toString());

			// Clear table
			tableModel.setRowCount(0);

			// Apply filter
			String selectedFilter = filterCombo.getSelectedItem().toString();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				// anomaly might come as 0/1 instead of true/false
				boolean anomaly = obj.get("anomaly").toString().equals("1")
						|| obj.get("anomaly").toString().equalsIgnoreCase("true");

				if ("Normal".equals(selectedFilter) && anomaly)
					continue;
				if ("Anomaly".equals(selectedFilter) && !anomaly)
					continue;

				Object[] row = new Object[] { obj.getString("name"), obj.getLong("size"), obj.getString("content_type"),
						obj.getString("updated_at"), anomaly };
				tableModel.addRow(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"❌ Could not connect to API.\nMake sure Python backend is running at http://127.0.0.1:5000/files");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UnifiedGUI::new);
	}
}
