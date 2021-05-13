INSERT INTO courses (course_name, course_description)
VALUES ('Math', 'The study of quantity, structure, space and change.'),
       ('Biology', 'The study of life and living organisms.'),
       ('History', 'The study of the past.');
INSERT INTO groups (group_name)
VALUES ('AB-15'),
       ('BD-22');
INSERT INTO students (group_id, first_name, last_name)
VALUES ('1', 'Adam', 'Jonson'),
       ('1', 'Tina', 'Hamilton'),
       ('2', 'Melinda', 'Doy');
INSERT INTO students_courses (student_id, course_id)
VALUES ('1', '1'),
       ('1', '2'),
       ('2', '1'),
       ('3', '2'),
       ('3', '3');