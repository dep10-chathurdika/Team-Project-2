CREATE TABLE IF NOT EXISTS Customer(
                     customer_id VARCHAR(20) PRIMARY KEY ,
                     customer_name VARCHAR(100) NOT NULL ,
                     customer_address VARCHAR(300) NOT NULL
);
CREATE TABLE IF NOT EXISTS Employee(
                                       employee_id VARCHAR(20) PRIMARY KEY ,
                                       employee_name VARCHAR(100) NOT NULL ,
                                       employee_address VARCHAR(300) NOT NULL
);
CREATE TABLE IF NOT EXISTS Teacher(
                                       teacher_id VARCHAR(20) PRIMARY KEY ,
                                       teacher_name VARCHAR(100) NOT NULL ,
                                       teacher_address VARCHAR(300) NOT NULL
);
CREATE TABLE IF NOT EXISTS Student(
                                      student_id VARCHAR(20) PRIMARY KEY ,
                                      student_name VARCHAR(100) NOT NULL ,
                                      student_address VARCHAR(300) NOT NULL
);
