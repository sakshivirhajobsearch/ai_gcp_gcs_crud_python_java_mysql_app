# ai_module.py

def process_file_anomaly(file):
    """
    Add 'anomaly' key as boolean: True if file size > 10MB (10 * 1024 * 1024).
    Ensures compatibility with Java's getBoolean() parsing.
    """
    file['anomaly'] = file['size'] > 10 * 1024 * 1024
    return file


def detect_anomalies(file_list):
    """
    Process a list of files and apply anomaly detection.
    """
    return [process_file_anomaly(file) for file in file_list]
