DROP TABLE IF EXISTS students_courses;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS courses;

CREATE TABLE groups
(
    group_id   SERIAL      NOT NULL PRIMARY KEY,
    group_name VARCHAR(10) NOT NULL
);

CREATE TABLE students
(
    student_id SERIAL      NOT NULL PRIMARY KEY,
    group_id   INTEGER     NULL REFERENCES groups (group_id) ON UPDATE CASCADE ON DELETE CASCADE,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL
);

CREATE TABLE courses
(
    course_id          SERIAL       NOT NULL PRIMARY KEY,
    course_name        VARCHAR(100) NOT NULL,
    course_description VARCHAR(500) NOT NULL
);

CREATE TABLE students_courses
(
    student_id INTEGER REFERENCES students (student_id) ON UPDATE CASCADE ON DELETE CASCADE,
    course_id  INTEGER REFERENCES courses (course_id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (student_id, course_id)
);