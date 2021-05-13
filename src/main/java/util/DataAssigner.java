package util;

import dao.CourseDao;
import dao.StudentDao;
import exceptions.DaoException;
import lombok.Builder;
import model.Course;
import model.Group;
import model.Student;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

@Builder
public class DataAssigner {
    @Builder.Default
    private int minGroupSize = 10;
    @Builder.Default
    private int maxGroupSize = 30;
    @Builder.Default
    private int minCourseNumber = 1;
    @Builder.Default
    private int maxCourseNumber = 3;
    @Builder.Default
    private Random random = new Random();
    private StudentDao studentDao;
    private CourseDao courseDao;

    public void assignStudentsToGroups(List<Student> students, List<Group> groups) throws DaoException {
        int assignedStudentsIndex = 0;
        for (Group group : groups) {
            int studentIndexToAssign = getRandomNumber(minGroupSize, maxGroupSize) + assignedStudentsIndex;
            if (studentIndexToAssign > students.size()) {
                break;
            }
            for (Student student : students.subList(assignedStudentsIndex, studentIndexToAssign)) {
                student.setGroupId(group.getId());
                studentDao.update(student);
            }
            assignedStudentsIndex = studentIndexToAssign;
        }
    }

    public void assignCoursesToStudents(List<Student> students, List<Course> courses) throws DaoException {
        for (Student student : students) {
            for (Course course : getRandomCourses(courses)) {
                courseDao.addStudentToCourse(student, course);
            }
        }
    }

    private Set<Course> getRandomCourses(List<Course> courses) {
        Set<Course> randomCourses = new HashSet<>();
        random.ints(0, courses.size())
                .distinct()
                .limit(getRandomNumber(minCourseNumber, maxCourseNumber))
                .forEach(id -> randomCourses.add(courses.get(id)));
        return randomCourses;
    }

    private int getRandomNumber(int minNumber, int maxNumber) {
        return random.nextInt(maxNumber - minNumber + 1) + minNumber;
    }
}