package util;

import dao.CourseDao;
import dao.GroupDao;
import dao.StudentDao;

import exceptions.DaoException;
import model.Course;
import model.Group;
import model.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DataFillerTest {

    @Mock
    private GroupDao groupDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private StudentDao studentDao;
    @Mock
    private DataGenerator dataGenerator;
    @Mock
    private DataAssigner dataAssigner;
    @InjectMocks
    private DataFiller dataFiller;
    static List<String> coursesList;
    static List<String> firstNames;
    static List<String> lastNames;
    static List<Course> courses;
    static List<Student> students;
    static List<Group> groups;

    @BeforeAll
    public static void createTestData() {
        coursesList = Arrays.asList("History_The study of the past.",
                "Biology_The study of life and living organisms.", "Astronomy_The study of the contents of the entire Universe.",
                "Geometry_The study of the sizes, shapes, positions angles and dimensions of things.");
        firstNames = Arrays.asList("George", "Sarah", "Daniel", "James", "Megan");
        lastNames = Arrays.asList("Anderson", "Green", "Harris", "Cook", "Watson");
        groups = Arrays.asList(new Group(1, "PA-44"), new Group(2, "ED-41"));
        courses = new ArrayList<>();
        courses.add(new Course(1, "History", "The study of the past."));
        courses.add(new Course(2, "Biology", "The study of life and living organisms."));
        courses.add(new Course(3, "Astronomy", "The study of the contents of the entire Universe."));
        courses.add(new Course(4, "Geometry", "The study of the sizes, shapes, positions " +
                "angles and dimensions of things."));
        students = new ArrayList<>();
        students.add(new Student(1, null, "Daniel", "Green"));
        students.add(new Student(2, null, "Sarah", "Cook"));
        students.add(new Student(3, null, "Daniel", "Anderson"));
        students.add(new Student(4, null, "James", "Anderson"));
    }

    @Test
    void fillDataBase_shouldGenerateGroups() throws DaoException {
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(dataGenerator, times(1)).generateGroups();
    }

    @Test
    void fillDataBase_shouldGenerateCourses() throws DaoException {
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(dataGenerator, times(1)).generateCourses(coursesList);
    }

    @Test
    void fillDataBase_shouldGenerateStudents() throws DaoException {
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(dataGenerator, times(1)).generateStudents(firstNames, lastNames);
    }

    @Test
    void fillDataBase_shouldAddAllGroups() throws DaoException {
        when(dataGenerator.generateGroups()).thenReturn(groups);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(groupDao, times(1)).addAll(groups);
    }

    @Test
    void fillDataBase_shouldAddAllCourses() throws DaoException {
        when(dataGenerator.generateCourses(coursesList)).thenReturn(courses);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(courseDao, times(1)).addAll(courses);
    }

    @Test
    void fillDataBase_shouldAddAllStudents() throws DaoException {
        when(dataGenerator.generateStudents(firstNames, lastNames)).thenReturn(students);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(studentDao, times(1)).addAll(students);
    }

    @Test
    void fillDataBase_shouldGetAllStudents() throws DaoException {
        when(studentDao.getAll()).thenReturn(students);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(studentDao, times(1)).getAll();
    }

    @Test
    void fillDataBase_shouldGetAllCourses() throws DaoException {
        when(courseDao.getAll()).thenReturn(courses);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(courseDao, times(1)).getAll();
    }

    @Test
    void fillDataBase_shouldGetAllGroups() throws DaoException {
        when(groupDao.getAll()).thenReturn(groups);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(groupDao, times(1)).getAll();
    }

    @Test
    void fillDataBase_shouldAssignStudentsToGroups() throws DaoException {
        when(studentDao.getAll()).thenReturn(students);
        when(groupDao.getAll()).thenReturn(groups);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(dataAssigner, times(1)).assignStudentsToGroups(students, groups);
    }

    @Test
    void fillDataBase_shouldAssignCoursesToStudents() throws DaoException {
        when(studentDao.getAll()).thenReturn(students);
        when(courseDao.getAll()).thenReturn(courses);
        dataFiller.fillDataBase(coursesList, firstNames, lastNames);
        verify(dataAssigner, times(1)).assignCoursesToStudents(students, courses);
    }
}