package dao.impl;

import dao.StudentDao;
import exceptions.DaoException;
import model.Course;
import model.Student;

import org.junit.jupiter.api.Test;
import util.ConnectionProvider;
import util.DatabaseConnection;
import util.PropertiesCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class StudentDaoImplTest extends AbstractDaoTest {
    private ConnectionProvider connectionProvider = new DatabaseConnection(new PropertiesCreator());
    private final StudentDao studentDao = new StudentDaoImpl(connectionProvider,  new CourseDaoImpl(connectionProvider));

    @Test
    void add_shouldReturnNewStudent_whenAddNewStudent() throws DaoException {
        Student expected = new Student(4, null, "Brad", "Jonson");
        Student student = new Student(null, null, "Brad", "Jonson");
        Student actual = studentDao.add(student);
        assertEquals(expected, actual);
    }

    @Test
    void add_shouldThrowException_whenStudentFirstNameIsNull() {
        assertThrows(DaoException.class, () -> studentDao.add(new Student(null, null, null, "Jonson")));
    }

    @Test
    void add_shouldThrowException_whenStudentLastNameIsNull() {
        assertThrows(DaoException.class, () -> studentDao.add(new Student(null, null, "Brad", null)));
    }

    @Test
    void getAll_shouldReturnAllStudents() throws DaoException {
        Set<Course> firstStudentCourses = new HashSet<>();
        firstStudentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        firstStudentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));

        Set<Course> secondStudentCourses = new HashSet<>();
        secondStudentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));

        Set<Course> thirdStudentCourses = new HashSet<>();
        thirdStudentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));
        thirdStudentCourses.add(new Course(3, "History", "The study of the past."));

        List<Student> expected = new ArrayList<>();
        expected.add(new Student(1, 1, "Adam", "Jonson", firstStudentCourses));
        expected.add(new Student(2, 1, "Tina", "Hamilton", secondStudentCourses));
        expected.add(new Student(3, 2, "Melinda", "Doy", thirdStudentCourses));
        assertEquals(expected, studentDao.getAll());
    }

    @Test
    void getById_shouldReturnCorrectStudentByGivenId() throws DaoException {
        Set<Course> studentCourses = new HashSet<>();
        studentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));
        studentCourses.add(new Course(3, "History", "The study of the past."));
        Student expected = new Student(3, 2, "Melinda", "Doy", studentCourses);
        Student actual = studentDao.getById(3).get();
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnEmpty_whenStudentNotExist() throws DaoException {
        Optional<Student> actual = studentDao.getById(4);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void getByCourseName_shouldReturnStudents_whenGivenCourseName() throws DaoException {
        Set<Course> firstStudentCourses = new HashSet<>();
        firstStudentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        firstStudentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));

        Set<Course> secondStudentCourses = new HashSet<>();
        secondStudentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));

        List<Student> expected = new ArrayList<>();
        expected.add(new Student(1, 1, "Adam", "Jonson", firstStudentCourses));
        expected.add(new Student(2, 1, "Tina", "Hamilton", secondStudentCourses));
        List<Student> actual = studentDao.getByCourseName("Math");
        assertEquals(expected, actual);
    }

    @Test
    void getByCourseName_shouldReturnEmptyList_whenCourseNameEmpty() throws DaoException {
        List<Student> expected = new ArrayList<>();
        List<Student> actual = studentDao.getByCourseName("");
        assertEquals(expected, actual);
    }

    @Test
    void deleteById_shouldCorrectlyDeleteStudent() throws DaoException {
        studentDao.deleteById(1);
        Optional<Student> actual = studentDao.getById(1);
        Optional<Student> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void addAll_shouldReturnStudents_whenAddedStudents() throws DaoException {
        List<Student> students = new ArrayList<>();
        students.add(new Student(null, null, "Ivan", "Ivanov"));
        students.add(new Student(null, null, "Petr", "Petrov"));
        studentDao.addAll(students);

        Set<Course> firstStudentCourses = new HashSet<>();
        firstStudentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        firstStudentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));
        Set<Course> secondStudentCourses = new HashSet<>();
        secondStudentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        Set<Course> thirdStudentCourses = new HashSet<>();
        thirdStudentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));
        thirdStudentCourses.add(new Course(3, "History", "The study of the past."));
        Set<Course> emptySet = new HashSet<>();
        emptySet.add(new Course(0, null, null));

        List<Student> expected = new ArrayList<>();
        expected.add(new Student(1, 1, "Adam", "Jonson", firstStudentCourses));
        expected.add(new Student(2, 1, "Tina", "Hamilton", secondStudentCourses));
        expected.add(new Student(3, 2, "Melinda", "Doy", thirdStudentCourses));
        expected.add(new Student(4, 0, "Ivan", "Ivanov", emptySet));
        expected.add(new Student(5, 0, "Petr", "Petrov", emptySet));
        List<Student> actual = studentDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldCorrectlyUpdateStudent() throws DaoException {
        Set<Course> studentCourses = new HashSet<>();
        studentCourses.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        studentCourses.add(new Course(2, "Biology", "The study of life and living organisms."));
        Student expected = new Student(1, 1, "Ivan", "Ivanov", studentCourses);
        studentDao.update(expected);
        Student actual = studentDao.getById(1).get();
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldThrowException_whenStudentFirstNameIsNull() {
        assertThrows(DaoException.class, () -> studentDao.update(new Student(1, 1, null, "Jonson")));
    }

    @Test
    void update_shouldThrowException_whenStudentLastNameIsNull() {
        assertThrows(DaoException.class, () -> studentDao.update(new Student(1, 1, "Brad", null)));
    }
}