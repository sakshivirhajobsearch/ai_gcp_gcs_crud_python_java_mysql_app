import mysql.connector

conn = mysql.connector.connect(
    host="localhost",
    user="gcs_user",
    password="admin",
    database="ai_gcp_gcs",
    auth_plugin="mysql_native_password"
)
print("Connected!")
conn.close()
