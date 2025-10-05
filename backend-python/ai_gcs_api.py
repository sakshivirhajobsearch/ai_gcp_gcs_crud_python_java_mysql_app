from flask import Flask, jsonify
from db import get_connection
from ai_module import detect_anomalies  # or process_file_anomaly if you're using that

app = Flask(__name__)

@app.route("/")
def index():
    return "✅ GCS CRUD API is running. Visit /files to fetch GCS file data."

@app.route("/files", methods=["GET"])
def get_all_files():
    try:
        conn = get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM gcs_files")
        rows = cursor.fetchall()
        cursor.close()
        conn.close()

        # Run AI anomaly detection
        processed = detect_anomalies(rows)  # or [process_file_anomaly(row) for row in rows]

        return jsonify(processed)

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(debug=True)
