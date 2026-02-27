-- ===============================
-- SCHOOL DATABASE SCHEMA
-- ===============================

-- ---------- SCHOOL PROFILE (MANDATORY) ----------
CREATE TABLE IF NOT EXISTS school_profile (
                                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              school_code VARCHAR(50) NOT NULL,
    school_name VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Insert initial metadata row
INSERT INTO school_profile (school_code, school_name, status)
SELECT 'INIT', 'INIT', 'ACTIVE'
    WHERE NOT EXISTS (SELECT 1 FROM school_profile);

-- ---------- CLASSES TABLE ----------
CREATE TABLE IF NOT EXISTS classes (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       class_name VARCHAR(50) NOT NULL,
    section VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ---------- STUDENTS TABLE ----------
CREATE TABLE IF NOT EXISTS students (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        name VARCHAR(255) NOT NULL,
    father_name VARCHAR(255),
    mother_name VARCHAR(255),
    date_of_birth DATE,
    email VARCHAR(255),
    phone_no VARCHAR(15),
    class_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_students_class
    FOREIGN KEY (class_id)
    REFERENCES classes(id)
    ON DELETE RESTRICT
    );