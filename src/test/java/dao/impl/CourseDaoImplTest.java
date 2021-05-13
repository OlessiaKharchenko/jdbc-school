package dao.impl;

import dao.CourseDao;
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

class CourseDaoImplTest extends AbstractDaoTest {
    private ConnectionProvider connectionProvider = new DatabaseConnection(new PropertiesCreator());
    private final CourseDao courseDao = new CourseDaoImpl(connectionProvider);
    private final StudentDao studentDao = new StudentDaoImpl(connectionProvider, courseDao);


    @Test
    void add_shouldReturnNewCourse_whenAddNewCourse() throws DaoException {
        Course expected = new Course(4, "Astronomy", "The study of the contents of the entire Universe.");
        Course course = new Course("Astronomy", "The study of the contents of the entire Universe.");
        Course actual = courseDao.add(course);
        assertEquals(expected, actual);
    }

    @Test
    void add_shouldThrowException_whenCoursesNameIsNull() {
        assertThrows(DaoException.class, () -> courseDao.add(new Course(null, "To learn java")));
    }

    @Test
    void add_shouldThrowException_whenCoursesDescriptionIsNull() {
        assertThrows(DaoException.class, () -> courseDao.add(new Course("Java", null)));
    }

    @Test
    void getAll_shouldReturnAllCourses() throws DaoException {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        expected.add(new Course(2, "Biology", "The study of life and living organisms."));
        expected.add(new Course(3, "History", "The study of the past."));
        List<Course> actual = courseDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnCorrectCourseByGivenId() throws DaoException {
        Course expected = new Course(2, "Biology", "The study of life and living organisms.");
        Course actual = courseDao.getById(2).get();
        assertEquals(expected, actual);
    }

    @Test
    void getById_shouldReturnEmpty_whenCourseNotExist() throws DaoException {
        Optional<Course> actual = courseDao.getById(4);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void addStudentToCourse_shouldCorrectlyAddStudentToCourse() throws DaoException {
        Student student = studentDao.getById(1).get();
        Course course = courseDao.getById(3).get();
        courseDao.addStudentToCourse(student, course);
        Set<Course> actual = studentDao.getById(1).get().getCourses();
        Set<Course> expected = new HashSet<>();
        expected.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        expected.add(new Course(2, "Biology", "The study of life and living organisms."));
        expected.add(new Course(3, "History", "The study of the past."));
        assertEquals(expected, actual);
    }

    @Test
    void addStudentToCourse_shouldThrowException_whenAddSameStudent() throws DaoException {
        Student student = studentDao.getById(1).get();
        Course course = courseDao.getById(1).get();
        assertThrows(DaoException.class, () -> courseDao.addStudentToCourse(student, course));
    }

    @Test
    void removeStudentFromCourse_shouldCorrectlyRemoveStudentToCourse() throws DaoException {
        Student student = studentDao.getById(1).get();
        Course course = courseDao.getById(1).get();
        courseDao.removeStudentFromCourse(student, course);
        List<Student> studentsInCourse = studentDao.getByCourseName(course.getName());
        Optional<Student> actual = studentsInCourse.stream().filter(s -> s.getId() == 1).findAny();
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void deleteById_shouldCorrectlyDeleteCourse() throws DaoException {
        courseDao.deleteById(1);
        Optional<Course> actual = courseDao.getById(1);
        Optional<Course> expected = Optional.empty();
        assertEquals(expected, actual);
    }

    @Test
    void addAll_shouldReturnCourses_whenAddedCourses() throws DaoException {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Painting", "The study how to draw."));
        courses.add(new Course("Dancing", "The study how to dance."));
        courseDao.addAll(courses);
        List<Course> expected = new ArrayList<>();
        expected.add(new Course(1, "Math", "The study of quantity, structure, space and change."));
        expected.add(new Course(2, "Biology", "The study of life and living organisms."));
        expected.add(new Course(3, "History", "The study of the past."));
        expected.add(new Course(4, "Painting", "The study how to draw."));
        expected.add(new Course(5, "Dancing", "The study how to dance."));
        List<Course> actual = courseDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldCorrectlyUpdateCourse() throws DaoException {
        Course expected = new Course(3, "Painting", "The study how to draw.");
        courseDao.update(expected);
        Course actual = courseDao.getById(3).get();
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldThrowException_whenCoursesNameIsNull() {
        assertThrows(DaoException.class, () -> courseDao.update(new Course(1, null, "To learn java")));
    }

    @Test
    void update_shouldThrowException_whenCoursesDescriptionIsNull() {
        assertThrows(DaoException.class, () -> courseDao.update(new Course(1, "Java", null)));
    }
}