package command;

import dao.CourseDao;
import dao.StudentDao;
import exceptions.DaoException;
import model.Course;
import model.Student;

import java.util.Scanner;

public class AddStudentToCourseCommand implements Command {
    private static final String DESCRIPTION = "Add a student to the course (from a list).";
    private StudentDao studentDao;
    private CourseDao courseDao;

    public AddStudentToCourseCommand(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void doCommand() throws DaoException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student's id, select from the next list: ");
        studentDao.getAll().stream()
                .map(student -> String.format("%d. %s %s", student.getId(), student.getFirstName(), student.getLastName()))
                .forEach(System.out::println);
        int studentId = scanner.nextInt();
        System.out.println("Enter course's id, select from the next list: ");
        courseDao.getAll().stream()
                .map(course -> String.format("%d. %s", course.getId(), course.getName()))
                .forEach(System.out::println);
        int courseId = scanner.nextInt();
        Student student = studentDao.getById(studentId).get();
        Course course = courseDao.getById(courseId).get();
        courseDao.addStudentToCourse(student, course);
        System.out.println(student.toString() + " was successfully added to the course " + course.getName());
    }
}