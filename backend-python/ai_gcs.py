from datetime import datetime
from db import get_connection, create_table
from ai_module import detect_anomalies

# Create table on startup
create_table()

# Offline sample files (simulate GCS files)
OFFLINE_FILES = [
    {"name": "report1.pdf", "size": 102400, "content_type": "application/pdf", "updated_at": datetime.now()},
    {"name": "video1.mp4", "size": 15_728_640, "content_type": "video/mp4", "updated_at": datetime.now()},
    {"name": "image1.png", "size": 204800, "content_type": "image/png", "updated_at": datetime.now()},
]

def fetch_gcs_files():
    files = []
    conn = get_connection()
    cursor = conn.cursor()
    for blob in OFFLINE_FILES:
        cursor.execute(
            """
            INSERT INTO gcs_files (name, size, content_type, updated_at)
            VALUES (%s, %s, %s, %s)
            ON DUPLICATE KEY UPDATE size=%s, content_type=%s, updated_at=%s
            """,
            (blob["name"], blob["size"], blob["content_type"], blob["updated_at"],
             blob["size"], blob["content_type"], blob["updated_at"])
        )
        files.append(blob)
    conn.commit()
    cursor.close()
    conn.close()

    return detect_anomalies(files)
