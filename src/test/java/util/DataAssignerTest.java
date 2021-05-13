package util;

import dao.CourseDao;
import dao.StudentDao;
import exceptions.DaoException;
import model.Course;
import model.Group;
import model.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Matchers.any;

@ExtendWith(MockitoExtension.class)
class DataAssignerTest {
    @Mock
    private StudentDao studentDao;
    @Mock
    private CourseDao courseDao;
    private Random random = new Random(5);

    @Test
    void assignStudentsToGroups_shouldUpdateStudents_whenGroupSetted() throws DaoException {
        DataAssigner dataAssigner = DataAssigner.builder().minGroupSize(1).maxGroupSize(5).random(random)
                .courseDao(courseDao).studentDao(studentDao).build();
        List<Group> groups = Arrays.asList(new Group(1, "RA-73"), new Group(2, "MY-46"));
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, 0, "George", "Cook"));
        students.add(new Student(2, 0, "Daniel", "Jonson"));
        students.add(new Student(3, 0, "Mark", "Green"));
        students.add(new Student(4, 0, "Sam", "Vatson"));
        doNothing().when(studentDao).update(any(Student.class));

        dataAssigner.assignStudentsToGroups(students, groups);

        verify(studentDao, times(1)).update(new Student(1, 1, "George", "Cook"));
        verify(studentDao, times(1)).update(new Student(2, 1, "Daniel", "Jonson"));
        verify(studentDao, times(1)).update(new Student(3, 1, "Mark", "Green"));
        verifyNoMoreInteractions(studentDao);
    }

    @Test
    void assignCoursesToStudents_shouldAddStudentToCourse() throws DaoException {
        DataAssigner dataAssigner = DataAssigner.builder().random(random).courseDao(courseDao).studentDao(studentDao).build();
        List<Student> students = new ArrayList<>();
        Student firstStudent = new Student(1, 1, "George", "Cook");
        Student secondStudent = new Student(2, 1, "Daniel", "Jonson");
        students.add(firstStudent);
        students.add(secondStudent);
        List<Course> courses = new ArrayList<>();
        Course history = new Course(1, "History", "The study of the past.");
        Course biology = new Course(2, "Biology", "The study of life and living organisms.");
        Course astronomy = new Course(3, "Astronomy", "The study of the contents of the entire Universe.");
        courses.add(history);
        courses.add(biology);
        courses.add(astronomy);
        doNothing().when(courseDao).addStudentToCourse(any(Student.class), any(Course.class));

        dataAssigner.assignCoursesToStudents(students, courses);

        verify(courseDao, times(1)).addStudentToCourse(firstStudent,history);
        verify(courseDao, times(1)).addStudentToCourse(firstStudent, biology);
        verify(courseDao, times(1)).addStudentToCourse(firstStudent, astronomy);
        verify(courseDao, times(1)).addStudentToCourse(secondStudent, history);
        verify(courseDao, times(1)).addStudentToCourse(secondStudent, biology);
        verify(courseDao, times(1)).addStudentToCourse(secondStudent, astronomy);
        verifyNoMoreInteractions(courseDao);
    }
}