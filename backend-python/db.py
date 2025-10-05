# db.py
import mysql.connector
from config import MYSQL_CONFIG

def get_connection():
    """Create and return a MySQL connection."""
    return mysql.connector.connect(**MYSQL_CONFIG)

def create_table():
    """Create gcs_files table if not exists."""
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS gcs_files (
            id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255) NOT NULL UNIQUE,
            size BIGINT NOT NULL,
            content_type VARCHAR(100),
            updated_at DATETIME,
            anomaly BOOLEAN DEFAULT FALSE
        )
    """)
    conn.commit()
    cursor.close()
    conn.close()

def insert_file(file):
    """Insert or update file record in DB."""
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO gcs_files (name, size, content_type, updated_at, anomaly)
        VALUES (%s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
            size=%s,
            content_type=%s,
            updated_at=%s,
            anomaly=%s
    """, (
        file['name'], file['size'], file['content_type'], file['updated_at'], file['anomaly'],
        file['size'], file['content_type'], file['updated_at'], file['anomaly']
    ))
    conn.commit()
    cursor.close()
    conn.close()

def fetch_all_files():
    """Fetch all files from DB."""
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT id, name, size, content_type, updated_at, anomaly FROM gcs_files")
    files = cursor.fetchall()
    cursor.close()
    conn.close()
    return files
