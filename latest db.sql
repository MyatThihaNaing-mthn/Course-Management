-- Name based on your database/schema name
USE ca_database;
CREATE TABLE IF NOT EXISTS admin
(
	admin_id VARCHAR(10) NOT NULL PRIMARY KEY,
	admin_name VARCHAR(50) NULL,
	admin_pwd VARCHAR(60) NULL
);

DELIMITER //

CREATE TRIGGER generate_adminId
    BEFORE INSERT
    ON admin
    FOR EACH ROW
BEGIN
    DECLARE nextId INT;
    SET nextId = COALESCE((SELECT MAX(SUBSTRING(admin_id, 2) + 1) FROM admin), 1);
    SET NEW.admin_id = CONCAT('A', LPAD(nextId, 8, '0'));
END //

DELIMITER ;

CREATE TABLE IF NOT EXISTS course
(
	course_id INT NOT NULL PRIMARY KEY,
	course_name VARCHAR(100) NULL,
	course_start_date DATE NULL,
	course_end_date DATE NULL,
	course_total_count INT NULL,
	course_size INT NULL,
	course_credits DOUBLE NULL,
	course_description VARCHAR(250) NULL,
	course_schedule VARCHAR(100) NULL
);

CREATE TABLE IF NOT EXISTS lecturer
(
	lecturer_id VARCHAR(10) NOT NULL PRIMARY KEY,
	lecturer_name VARCHAR(50) NULL,
	lecturer_title VARCHAR(50) NULL,
	lecturer_pwd VARCHAR(60) NULL,
	lecturer_email VARCHAR(100) NULL,
	lecturer_dob DATE NULL
);

DELIMITER //

CREATE TRIGGER generate_lecturerId
    BEFORE INSERT
    ON lecturer
    FOR EACH ROW
BEGIN
    DECLARE nextId INT;
    SET nextId = COALESCE((SELECT MAX(SUBSTRING(lecturer_id, 2) + 1) FROM lecturer), 1);
    SET NEW.lecturer_id = CONCAT('L', LPAD(nextId, 8, '0'));
END //

DELIMITER ;

CREATE TABLE IF NOT EXISTS lecturer_course
(
	lecturer_id VARCHAR(10) NOT NULL,
	course_id INT NOT NULL,
	PRIMARY KEY (lecturer_id, course_id)
);

CREATE TABLE IF NOT EXISTS student
(
	stud_id VARCHAR(10) NOT NULL PRIMARY KEY,
	stu_first_name VARCHAR(50) NULL,
	stu_last_name VARCHAR(50) NULL,
	stu_gpa DOUBLE NULL,
	email VARCHAR(100) NULL,
	stu_pwd VARCHAR(60) NULL,
	stu_enroll_date DATE NULL,
	stu_dob DATE NULL
);

DELIMITER //

CREATE TRIGGER generate_studId
    BEFORE INSERT
    ON student
    FOR EACH ROW
BEGIN
    DECLARE nextId INT;
    SET nextId = COALESCE((SELECT MAX(SUBSTRING(stud_id, 2) + 1) FROM student), 1);
    SET NEW.stud_id = CONCAT('S', LPAD(nextId, 8, '0'));
END //

DELIMITER ;

CREATE TABLE IF NOT EXISTS student_course
(
	stud_id VARCHAR(10) NOT NULL,
	course_id INT NOT NULL,
	enroll_status INT NULL,
	score DECIMAL(5,2) NULL,
	enroll_date DATETIME NULL,
	PRIMARY KEY (stud_id, course_id)
);

ALTER TABLE course MODIFY COLUMN course_id INT AUTO_INCREMENT;

-- Populate tables

-- Populate admin table
INSERT INTO admin (admin_name, admin_pwd)
VALUES ('John Smith', LPAD(FLOOR(RAND() * 100000000), 8, '0')),
       ('Jane Doe', LPAD(FLOOR(RAND() * 100000000), 8, '0')),
       ('Michael Johnson', LPAD(FLOOR(RAND() * 100000000), 8, '0')),
       ('Emily Williams', LPAD(FLOOR(RAND() * 100000000), 8, '0'));

-- Populate course table
INSERT INTO course (course_name, course_start_date, course_end_date, course_total_count, course_size, course_credits, course_description, course_schedule)
VALUES 
    ('Python Programming', '2023-01-20', '2023-04-30', 15, 30, 3.5, 'Introduction to Python programming language.', 'Term 1'),
    ('Java Programming', '2023-06-20', '2023-07-30', 15, 30, 4.0, 'Fundamentals of Java programming language.', 'Term 2'),
    ('Web Development', '2023-06-25', '2023-08-05', 10, 20, 3.0, 'Building dynamic websites using HTML, CSS, and JavaScript.', 'Term 2'),
    ('Data Science with R', '2023-07-01', '2023-08-12', 10, 20, 3.5, 'Exploring data analysis and visualization using R programming.', 'Term 1'),
    ('Mobile App Development', '2023-07-05', '2023-08-16', 20, 20, 3.0, 'Creating mobile applications for iOS and Android platforms.', 'Term 2'),
    ('Database Management', '2023-07-10', '2023-08-20', 10, 20, 4.0, 'Designing and managing relational databases using SQL.', 'Term 1');

-- Populate lecturer table
INSERT INTO lecturer (lecturer_name, lecturer_title, lecturer_email, lecturer_dob)
VALUES 
    ('Dr. John Smith', 'Professor', 'john.smith@example.com', '1980-01-01'),
    ('Dr. Emily Johnson', 'Associate Professor', 'emily.johnson@example.com', '1985-05-15'),
    ('Prof. Michael Davis', 'Assistant Professor', 'michael.davis@example.com', '1975-09-10'),
    ('Dr. Sarah Wilson', 'Lecturer', 'sarah.wilson@example.com', '1990-12-20');

-- Update password for John Smith
UPDATE lecturer
SET lecturer_pwd = '$2a$10$YqHrPXYSo6zbAhr99j/cQeU4x/dDPrm6dFpp9HES2raXpSxa6Llz2'
WHERE lecturer_name = 'Dr. John Smith';

-- Populate lecturer_course table
INSERT INTO lecturer_course (lecturer_id, course_id)
VALUES 
    ('L00000001', 1),
    ('L00000002', 1),
    ('L00000002', 2),
    ('L00000001', 3),
    ('L00000002', 4),
    ('L00000003', 5),
    ('L00000004', 6);

-- Populate student table
INSERT INTO student (stu_first_name, stu_last_name, stu_gpa, email, stu_enroll_date, stu_dob)
VALUES 
    ('Jay', 'Tan', 3.7, 'jaytan472@gmail.com', '2023-06-01', '2000-03-15'),
    ('Jane', 'Smith', 3.9, 'jane.smith@example.com', '2023-06-05', '2001-07-20'),
    ('Alex', 'Johnson', 3.5, 'alex.johnson@example.com', '2023-06-10', '1999-09-10'),
    ('Emily', 'Brown', 3.8, 'emily.brown@example.com', '2023-06-15', '2002-12-05'),
    ('James', 'Wilson', 3.6, 'james.wilson@example.com', '2023-06-20', '2003-04-25'),
    ('Sophia', 'Miller', 3.9, 'sophia.miller@example.com', '2023-06-25', '2001-01-08'),
    ('David', 'Davis', 3.3, 'david.davis@example.com', '2023-06-30', '2000-09-12'),
    ('Olivia', 'Taylor', 3.7, 'olivia.taylor@example.com', '2023-07-05', '2002-07-17'),
    ('Liam', 'Anderson', 3.8, 'liam.anderson@example.com', '2023-07-10', '2003-11-30'),
    ('Emma', 'White', 3.4, 'emma.white@example.com', '2023-07-15', '2002-05-23');

-- Populate student_course table
INSERT INTO student_course (stud_id, course_id, enroll_status, score, enroll_date)
VALUES 
    ('S00000001', 1, 0, 60.0, '2023-06-10 09:30:00'),
    ('S00000002', 1, 0, 65.0, '2023-06-12 13:45:00'),
    ('S00000003', 1, 0, 55.5, '2023-06-15 16:20:00'),
    ('S00000004', 2, 0, NULL, '2023-06-18 10:00:00'),
    ('S00000005', 2, 0, NULL, '2023-06-20 14:30:00'),
    ('S00000006', 2, 1, NULL, '2023-06-22 11:45:00'),
    ('S00000007', 3, 0, NULL, '2023-06-25 09:00:00'),
    ('S00000008', 3, 1, NULL, '2023-06-27 13:15:00'),
    ('S00000009', 4, 0, NULL, '2023-06-30 16:45:00'),
    ('S00000010', 5, 1, NULL, '2023-07-03 10:30:00'),
    ('S00000001', 6, 1, NULL, '2023-07-06 14:00:00'),
    ('S00000002', 6, 1, NULL, '2023-07-08 11:15:00');

-- Adding encrypted password
UPDATE student
SET stu_pwd = '$2a$10$YqHrPXYSo6zbAhr99j/cQeU4x/dDPrm6dFpp9HES2raXpSxa6Llz2'
WHERE stud_id = 'S00000001';

-- Adding encrypted password password is password123
UPDATE admin
SET admin_pwd = '$2a$10$UNXtP57S4mpSdTmRVN2BjeYVoWrYskHFcknA7hlX5IA1FLXVLAgv2'
WHERE admin_id = 'A00000001';