CREATE TABLE IF NOT EXISTS schools (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    school_code VARCHAR(50) UNIQUE,
    school_name VARCHAR(255),
    db_name VARCHAR(100),
    db_username VARCHAR(100),
    db_password VARCHAR(255),
    status ENUM('ACTIVE','INACTIVE'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );