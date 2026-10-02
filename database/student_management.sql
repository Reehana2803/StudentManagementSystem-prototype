CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    department VARCHAR(50),
    phone VARCHAR(15),
    email VARCHAR(100)
);

INSERT INTO students VALUES
(101,'Reehana shaik',21,'CSE','9876543210','reehana@gmail.com'),
(102,'Asha shaik',20,'ECE','9876543211','asha@gmail.com'),
(103,'Mohith',21,'CSE','9876543212','mohith@gmail.com');
