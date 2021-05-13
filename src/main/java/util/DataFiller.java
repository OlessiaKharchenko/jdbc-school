package util;

import dao.CourseDao;
import dao.GroupDao;
import dao.StudentDao;
import exceptions.DaoException;
import lombok.AllArgsConstructor;
import model.Course;
import model.Group;
import model.Student;

import java.util.List;

@AllArgsConstructor
public class DataFiller {
    private DataGenerator dataGenerator;
    private DataAssigner dataAssigner;
    private CourseDao courseDao;
    private GroupDao groupDao;
    private StudentDao studentDao;

    public void fillDataBase(List<String> coursesList, List<String> firstNames, List<String> lastNames) throws DaoException {
        List<Group> groups = dataGenerator.generateGroups();
        groupDao.addAll(groups);
        List<Course> courses = dataGenerator.generateCourses(coursesList);
        courseDao.addAll(courses);
        List<Student> students = dataGenerator.generateStudents(firstNames, lastNames);
        studentDao.addAll(students);
        List<Student> studentsWithId = studentDao.getAll();
        dataAssigner.assignStudentsToGroups(studentsWithId, groupDao.getAll());
        dataAssigner.assignCoursesToStudents(studentsWithId, courseDao.getAll());
    }
}