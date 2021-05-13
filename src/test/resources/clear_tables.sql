DELETE FROM courses;
DELETE FROM students;
DELETE FROM groups;
ALTER SEQUENCE courses_course_id_seq RESTART WITH 1;
ALTER SEQUENCE students_student_id_seq RESTART WITH 1;
ALTER SEQUENCE groups_group_id_seq RESTART WITH 1;