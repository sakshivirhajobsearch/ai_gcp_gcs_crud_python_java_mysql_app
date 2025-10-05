-- Create dedicated user and grant privileges
CREATE USER IF NOT EXISTS 'gcs_user'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON ai_gcp_gcs.* TO 'gcs_user'@'localhost';
FLUSH PRIVILEGES;

-- Use DB
USE ai_gcp_gcs;

-- Create table
CREATE TABLE IF NOT EXISTS gcs_files (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100),
    updated_at DATETIME,
    anomaly BOOLEAN DEFAULT FALSE,
    UNIQUE KEY (name)
);
