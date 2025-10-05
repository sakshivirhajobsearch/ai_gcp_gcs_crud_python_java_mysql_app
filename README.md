# AI + GCS CRUD Application

## Prerequisites
- Python 3.10+
- Java JDK 17+
- Maven
- MySQL 8+
- pip

## Python Setup
1. Navigate to backend-python
2. Install dependencies:
   pip install -r requirements.txt
3. Place your service_account.json for GCP
4. Run backend:
   python app.py

## MySQL Setup
1. Create database:
   CREATE DATABASE gcs_ai_db;

## Java Setup
1. Navigate to frontend-java
2. Build and run:
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.ai.gcs.UnifiedGUI"

## Usage
- GUI provides buttons for:
  - Create/Delete/List Buckets
  - List Objects
  - AI: Classify, Predict Growth, Detect Anomalies
