-- Chat Database Setup for NextHire
-- Run this script in your MySQL database

USE next_hire;

-- Table to store chat messages
CREATE TABLE IF NOT EXISTS chat_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_username VARCHAR(100) NOT NULL,
    message_text TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    message_type ENUM('text', 'file', 'image') DEFAULT 'text',
    file_path VARCHAR(500) NULL
);

-- Table to store user chat sessions (for tracking who is in the course chat)
CREATE TABLE IF NOT EXISTS chat_sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    session_start DATETIME DEFAULT CURRENT_TIMESTAMP,
    session_end DATETIME NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- Table to store user eligibility for courses (who was referred to courses)
CREATE TABLE IF NOT EXISTS course_eligible_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    job_title VARCHAR(200) NOT NULL,
    company VARCHAR(200) NOT NULL,
    eligibility_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Indexes for better performance
CREATE INDEX idx_chat_messages_timestamp ON chat_messages(timestamp);
CREATE INDEX idx_chat_sessions_username ON chat_sessions(username);
CREATE INDEX idx_course_eligible_users_username ON course_eligible_users(username); 